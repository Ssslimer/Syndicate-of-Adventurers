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
		super("Message Handler");		
		this.server = server;
		rand = new Random();
	}
	
	@Override
	public void run()
	{
		setPriority(10);
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
				processMove(connectionWithClient, (MoveMessage)message);
			break;
				
			case ATTACK:
				processAttack(connectionWithClient, (AttackMessage) message);
			break;
			
			case CHAT_MESSAGE:	
				processChat(connectionWithClient, (ChatMessage) message);
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
	
	private void processMove(ServerConnection connectionWithClient, MoveMessage message)
	{
		if(connectionWithClient.isLogedIn())
		{
			MoveDirection direction = message.getDirection();
			boolean ifStop = message.getIfToStop();		
			String login = Server.getLogin(message.getSessionId());
			
			Server.getMap().getPlayer(login).move(direction, ifStop);
		}
	}
	
	private void processAttack(ServerConnection connectionWithClient, AttackMessage message)
	{
		if(connectionWithClient.isLogedIn())
		{
			String login = Server.getLogin(message.getSessionId());
			Server.getMap().getPlayer(login).attack();
		}
	}
	
	private void processChat(ServerConnection connectionWithClient, ChatMessage message) throws IOException
	{
		if(connectionWithClient.isLogedIn())
		{
			String nick = Server.getLogin(message.getSessionId());
			
			for(ServerConnection connectionToClient : server.getConnectionManager().getAllConnections())
			{
				connectionToClient.sendMessageToClient(new UpdateChatMessage(nick + ": " + message.getText()));
			}
			Server.addChatMessage(nick + ": " + message.getText());
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
