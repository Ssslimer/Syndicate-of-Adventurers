package client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import networking.MessageStatus;
import networking.MoveDirection;
import networking.messages.Message;
import networking.messages.fromclient.ChatMessage;
import networking.messages.fromclient.LoginMessage;
import networking.messages.fromclient.PingMessage;
import networking.messages.fromclient.RegisterMessage;
import networking.messages.fromserver.AuthLoginMessage;
import networking.messages.fromserver.AuthRegisterMessage;
import networking.messages.fromserver.SendMapMessage;
import networking.messages.fromserver.SpawnEntityMessage;
import networking.messages.fromserver.UpdateChatMessage;
import networking.messages.fromserver.UpdateEntityMessage;
import networking.messages.ingame.AttackMessage;
import networking.messages.ingame.MoveMessage;
import world.World;

public class ClientConnection extends Thread
{
	private Socket clientSocket;
	private ObjectInputStream streamFromServer;
	
	private final String ip;
	private final int port;
	
	private boolean isLogedIn;
	private String login;
	private long sessionId;
	
	private MessageSender sender;

	public ClientConnection(String ip, int port)
	{
		super("Input from server");
		this.ip = ip;
		this.port = port;
	}
	
	@Override
	public void run()
	{	
		while(true)
		{
	    	Message fromServer = null;
			try
			{
				fromServer = getDataFromServer();
			}
			catch(SocketException e)
			{
				e.printStackTrace();
			}			
			catch(ClassNotFoundException e)
			{
				e.printStackTrace();
				continue;
			}
			catch(IOException e)
			{
				e.printStackTrace();
				continue;
			}
			
	    	handleCallback(fromServer);
		}
	}
	
	public void move(MoveDirection direction, boolean toStop)
	{
		if(isLogedIn) sender.addMessage(new MoveMessage(sessionId, direction, toStop));
	}

	public void attack()
	{
		if(isLogedIn) sender.addMessage(new AttackMessage(sessionId));
	}
	
	public void sentChatMessage(String chatMessageString)
	{
		if(isLogedIn) sender.addMessage(new ChatMessage(sessionId, chatMessageString));
	}
	
	public MessageStatus register(String login, String password)
	{
		try
    	{
			System.setProperty("javax.net.ssl.trustStore", "za.store");
			SocketFactory sslsocketfactory = SSLSocketFactory.getDefault();
			clientSocket = sslsocketfactory.createSocket(ip, port);
			System.out.println("Connected to server IP: " + ip + " Port: " + clientSocket.getPort());
			
			sender = new MessageSender(clientSocket);
			sender.start();
	
	    	streamFromServer = new ObjectInputStream(clientSocket.getInputStream());
	    	
	    	sender.addMessage(new RegisterMessage(login, password));
	    	
	    	AuthRegisterMessage fromServer = (AuthRegisterMessage) getDataFromServer();

	    	System.out.println("Message from server: " + fromServer.toString());
	    	if(fromServer.getMessageStatus() == MessageStatus.OK) 
	    	{
	    		isLogedIn = true;
	    		this.sessionId = fromServer.getSessionID(); 
	    		
	    		start();
	    	}	    	
	    	
	    	return fromServer.getMessageStatus();
    	}
    	catch(IOException | ClassNotFoundException e)
    	{  		
    		System.out.println(e.getMessage());
    		
    		stopConnection();
    		return MessageStatus.ERROR;
    	}
	}
	
	public MessageStatus login(String login, String password)
	{
		try
    	{	
			System.setProperty("javax.net.ssl.trustStore", "za.store");
			SocketFactory sslsocketfactory = SSLSocketFactory.getDefault();
			clientSocket = sslsocketfactory.createSocket(ip, port);
			System.out.println("Connected to server IP: " + ip + " Port: " + port);
	
			sender = new MessageSender(clientSocket);
			sender.start();
			
	    	streamFromServer = new ObjectInputStream(clientSocket.getInputStream());
    	
	    	sender.addMessage(new LoginMessage(login, password));
	    	
	    	AuthLoginMessage fromServer = (AuthLoginMessage) getDataFromServer();
	    	if(fromServer.getMessageStatus() == MessageStatus.OK) 
	    	{
	    		isLogedIn = true;
	    		this.sessionId = fromServer.getSessionID(); 
	    		this.login = login;
	    		
	    		start();
	    	}
	    	
	    	return fromServer.getMessageStatus();
    	}
    	catch(Exception e)
    	{  		
    		System.out.println(e.getMessage());
    		
    		stopConnection();
    		return MessageStatus.ERROR;
    	}
	}

	private void handleCallback(Message serverCallback)
	{	
		System.out.println(serverCallback);
		
		switch(serverCallback.getMessageType())
		{
			case PING:
				long ping = System.currentTimeMillis() - ((PingMessage) serverCallback).getTime();
				System.out.println("PING " + ping);
			break;

			case LOAD_MAP:
				World world = ((SendMapMessage) serverCallback).getMap();
				world.setLocal(true);
				MyGame.setGameMap(world);
			break;

			case UPDATE_ENTITY:
				UpdateEntityMessage message = (UpdateEntityMessage) serverCallback;
				MyGame.getGameMap().updateEntityPos(message.getEntityId(), message.getPosition(), message.getVelocity());
			break;
			
			case UPDATE_CHAT:
				UpdateChatMessage updateChat = (UpdateChatMessage) serverCallback;
				Chat.updateChat(updateChat.getText());
			break;
			
			case SPAWN_ENTITY:
				SpawnEntityMessage spawnMessage = (SpawnEntityMessage) serverCallback;
				MyGame.getGameMap().spawnEntity(spawnMessage.getEntity());
			break;
			
			default:
				System.out.println("Unknown command");
		}
	}
	
	private Message getDataFromServer() throws ClassNotFoundException, IOException, EOFException
	{
		return (Message) streamFromServer.readObject();	
	}

	public void pingServer()
	{
		sender.addMessage(new PingMessage(sessionId, System.currentTimeMillis()));
	}
	
	public void stopConnection()
	{
		try
		{
			clientSocket.close();
		}
		catch(IOException e) 
		{
			System.out.println(e.getMessage());
		}
	}
	
	public boolean isServerOnline()
	{
		System.setProperty("javax.net.ssl.trustStore", "za.store");
		SocketFactory sslsocketfactory = SSLSocketFactory.getDefault();
		
		try(Socket socket = sslsocketfactory.createSocket(ip, port))
		{
			socket.close();
		}
		catch(Exception e)
		{
			return false;
		}

		return true;
	}
	
	public String getLogin()
	{
		return login;
	}
}