package server;

import java.io.IOException;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.badlogic.gdx.math.Vector3;

import entities.EntityPlayer;
import networking.MessageStatus;
import networking.MessageType;
import networking.MoveDirection;
import networking.messages.AttackMessage;
import networking.messages.Message;
import networking.messages.MoveMessage;
import networking.messages.fromclient.ChatMessage;
import networking.messages.fromclient.LoginMessage;
import networking.messages.fromclient.RegisterMessage;
import networking.messages.fromserver.AuthLoginMessage;
import networking.messages.fromserver.AuthRegisterMessage;
import networking.messages.fromserver.SendMapMessage;
import networking.messages.fromserver.UpdateChatMessage;

/** Thread for processing messages from clients */
public class MessageHandler extends Thread
{
	private final Server server;
	
	private Queue<MessageTask> messages = new ConcurrentLinkedQueue<>();
	private boolean shouldWait = true;
	
	private Random rand;
	
	public MessageHandler(Server server)
	{
		this.server = server;
		rand = new Random();
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
		synchronized(this){notify();}
	}
	
	private void processMessage(MessageTask task) throws IOException
	{	
		ServerConnection connectionWithClient = task.getMessageOwner();			
		Message message = task.getMessage();
		MessageType content = message.getMessageType();
		
		System.out.println("NEW MESSAGE: " + message.getMessageType().toString());
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
					((EntityPlayer)Server.getMap().getEntities().get(((AttackMessage)task.getMessage()).getSessionId())).attack();
				}
			break;
			
			case CHAT_MESSAGE:				
				if(connectionWithClient.isLogedIn())
				{
					ChatMessage chatMessage = (ChatMessage) message;
					String nick = Server.getLogin(chatMessage.getSessionId());
					
					for(ServerConnection connectionToClient : server.getConnectionManager().getAllConnections())
					{
						connectionToClient.sendMessageToClient(new UpdateChatMessage(nick + ": " + chatMessage.getText()));
					}
					Server.addChatMessage(nick + ": " + chatMessage.getText());
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
			connectionWithClient.sendMessageToClient(new AuthRegisterMessage(MessageStatus.GIVEN_LOGIN_EXISTS, -1));	
		}
		else
		{
			server.getAuthManager().registerPlayerIfNotRegistered(login, hashedPassword);
			long sessionID = generateSessionID();
			Server.addClient(sessionID, login);
			connectionWithClient.login();
			connectionWithClient.sendMessageToClient(new AuthRegisterMessage(MessageStatus.OK, sessionID));
			Server.getMap().spawnEntity(new EntityPlayer(new Vector3(0, 5, 0), login));
			connectionWithClient.sendMessageToClient(new SendMapMessage(Server.getMap()));
		}
	}
	
	private void processLogin(ServerConnection connectionWithClient, LoginMessage message) throws IOException
	{
		String login = message.getLogin();
		String password = message.getPassword();
			
		if(!server.getAuthManager().isPlayerRegistered(login))
		{
			connectionWithClient.sendMessageToClient(new AuthLoginMessage(MessageStatus.NOT_REGISTRED, -1));
		}
		else
		{
			if(server.getAuthManager().checkPassword(login, password))
			{
				long sessionID = generateSessionID();
				Server.addClient(sessionID, login);
				connectionWithClient.login();
				connectionWithClient.sendMessageToClient(new AuthLoginMessage(MessageStatus.OK, sessionID));
				Server.getMap().spawnEntity(new EntityPlayer(new Vector3(0, 5, 0), login));
				connectionWithClient.sendMessageToClient(new SendMapMessage(Server.getMap()));
			}
			else connectionWithClient.sendMessageToClient(new AuthLoginMessage(MessageStatus.WRONG_PASSWORD, -1));
		}
	}
	
	private long generateSessionID() 
	{
		long sessionID;
		
		do
		{
			sessionID = rand.nextLong();
			
		}while(!Server.IdNotOccupied(sessionID));
		
		return sessionID;
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
