package com.moag.game.server;

import static com.moag.game.server.AuthManager.checkPassword;
import static com.moag.game.server.AuthManager.isPlayerRegistered;
import static com.moag.game.server.AuthManager.registerPlayer;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.moag.game.networking.LoginMessage;
import com.moag.game.networking.Message;
import com.moag.game.networking.MessageFromServer;
import com.moag.game.networking.MessageType;
import com.moag.game.networking.RegisterMessage;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

/** Thread for processing messages from clients */
public class MessageHandler extends Thread
{
	private Queue<MessageTask> messages = new ConcurrentLinkedQueue<>();
	
	@Override
	public void run()
	{
		Executor executor = Executors.newFixedThreadPool(4);
		
		while(true)
		{
			executor.execute(() ->
			{
				try
				{
					processMessage(messages.poll());
				}
				catch(IOException e)
				{
					System.out.println(e.getMessage());
				}
			});
		}
	}
	
	void handleMessage(MessageTask task)
	{
		messages.add(task);
	}
	
	private void processMessage(MessageTask task) throws IOException
	{
		if(task == null) return;
		
		ServerConnection connectionWithClient = task.getMessageOwner();			
		Message message = task.getMessage();
		MessageType content = message.getMessageType();
		
		switch(content)
		{			
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
					
				}
			break;
				
			case ATTACK:
				
				if(connectionWithClient.isLogedIn())
				{
					
				}
			break;
				
			default:
				connectionWithClient.sendMessageToClient(new MessageFromServer(false, "Unknown command"));
		}
	}
	
	private void processRegister(ServerConnection connectionWithClient, RegisterMessage message) throws IOException
	{		
		connectionWithClient.stopCommunication();
		
		String login = message.getLogin();
		char[] password = message.getPassword().toCharArray();
		Argon2 argon2 = Argon2Factory.create();
		String hashedPassword = argon2.hash(10, 65536, 1, password);
			
		if(isPlayerRegistered(login))
		{
			connectionWithClient.sendMessageToClient(new MessageFromServer(false, "User with such login already exist!"));	
		}
		else
		{
			registerPlayer(login, hashedPassword);
			connectionWithClient.sendMessageToClient(new MessageFromServer(true, "Successfuly registered!"));	
		}
	}
	
	private void processLogin(ServerConnection connectionWithClient, LoginMessage message) throws IOException
	{
		connectionWithClient.stopCommunication();
		String login = message.getLogin();
		String password = message.getPassword();
			
		if(!isPlayerRegistered(login))
		{
			connectionWithClient.sendMessageToClient(new MessageFromServer(false, "User with such login does not exist!"));
		}
		else
		{
			if(checkPassword(login, password))
			{
				connectionWithClient.login();
				connectionWithClient.sendMessageToClient(new MessageFromServer(true, "Successfuly logged in!"));
			}
			else connectionWithClient.sendMessageToClient(new MessageFromServer(false, "Given password is wrong!"));
		}
	}
}
