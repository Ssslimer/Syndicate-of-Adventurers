package com.moag.game.server;


import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.moag.game.entities.EntityPlayer;
import com.moag.game.networking.MessageStatus;
import com.moag.game.networking.MessageType;
import com.moag.game.networking.MoveDirection;
import com.moag.game.networking.messages.Message;
import com.moag.game.networking.messages.MoveMessage;
import com.moag.game.networking.messages.fromclient.LoginMessage;
import com.moag.game.networking.messages.fromclient.RegisterMessage;
import com.moag.game.networking.messages.fromserver.MessageFromServer;
import com.moag.game.networking.messages.fromserver.SendMapMessage;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

/** Thread for processing messages from clients */
public class MessageHandler extends Thread
{
	private final Server server;
	
	private Queue<MessageTask> messages = new ConcurrentLinkedQueue<>();
	private boolean shouldWait = true;
	
	public MessageHandler(Server server)
	{
		this.server = server;
	}
	
	@Override
	public void run()
	{
		Executor executor = Executors.newFixedThreadPool(1);
		
		while(true)
		{
			if(shouldWait)
			{
				synchronized(this)
				{
					try{wait();}
					catch(InterruptedException e) {e.printStackTrace();}
				}
			}
			
			executor.execute(() ->
			{
				try
				{
					MessageTask task = messages.poll();
					if(task == null) shouldWait = true;
					else processMessage(task);
				}
				catch(IOException e)
				{
					System.out.println(e.getMessage());
				}
			});
		}
	}
	
	void handleMessage(ServerConnection source, Message message)
	{
		messages.add(new MessageTask(source, message));
		shouldWait = false;
		
		synchronized(this)
		{
			notify();
		}
	}
	
	private void processMessage(MessageTask task) throws IOException
	{	
		ServerConnection connectionWithClient = task.getMessageOwner();			
		Message message = task.getMessage();
		MessageType content = message.getMessageType();
		
		switch(content)
		{			
			case PING:
				connectionWithClient.sendMessageToClient(message);
			break;
			
			case REGISTER:
				processRegister(connectionWithClient, (RegisterMessage) message);
			break;
			
			case LOGIN: 
				processLogin(connectionWithClient, (LoginMessage) message);			
			break;

			case QUIT:
				/** TODO check whether this is enough */
				connectionWithClient.stopCommunication();
			break;
			
			case MOVE:
				
				if(connectionWithClient.isLogedIn())
				{
					MoveDirection direction = ((MoveMessage)task.getMessage()).getDirection();
					boolean ifStop = ((MoveMessage)task.getMessage()).getIfToStop();
					((EntityPlayer)Server.getMap().getEntities().get(((MoveMessage)task.getMessage()).getSessionId())).move(direction, ifStop);
				}
			break;
				
			case ATTACK:
				
				if(connectionWithClient.isLogedIn())
				{
					((EntityPlayer)Server.getMap().getEntities().get(((MoveMessage)task.getMessage()).getSessionId())).attack();
				}
			break;
				
			default:/** TODO maybe delete message to client ? */
				System.out.println("Client send wrong command!!");
		}
	}
	
	private void processRegister(ServerConnection connectionWithClient, RegisterMessage message) throws IOException
	{		
		connectionWithClient.stopCommunication();
		
		String login = message.getLogin();
		char[] password = message.getPassword().toCharArray();
		String hashedPassword = server.getAuthManager().hashPassword(password);
			
		if(server.getAuthManager().isPlayerRegistered(login))
		{
			connectionWithClient.sendMessageToClient(new MessageFromServer(MessageStatus.GIVEN_LOGIN_EXISTS));	
		}
		else
		{
			server.getAuthManager().registerPlayerIfNotRegistered(login, hashedPassword);
			connectionWithClient.sendMessageToClient(new MessageFromServer(MessageStatus.OK));	
		}
	}
	
	private void processLogin(ServerConnection connectionWithClient, LoginMessage message) throws IOException
	{
		String login = message.getLogin();
		String password = message.getPassword();
			
		if(!server.getAuthManager().isPlayerRegistered(login))
		{
			connectionWithClient.sendMessageToClient(new MessageFromServer(MessageStatus.NOT_REGISTRED));
		}
		else
		{
			if(server.getAuthManager().checkPassword(login, password))
			{
				connectionWithClient.login();
				connectionWithClient.sendMessageToClient(new MessageFromServer(MessageStatus.OK));
				connectionWithClient.sendMessageToClient(new SendMapMessage(Server.getMap()));
			}
			else connectionWithClient.sendMessageToClient(new MessageFromServer(MessageStatus.WRONG_PASSWORD));
		}
	}
	
	private class MessageTask
	{
		private final ServerConnection messageOwner;
		private final Message message;
		
		private MessageTask(ServerConnection messageOwner, Message message)
		{
			this.messageOwner = messageOwner;
			this.message = message;
		}

		private ServerConnection getMessageOwner()
		{
			return messageOwner;
		}

		private Message getMessage()
		{
			return message;
		}
	}
}
