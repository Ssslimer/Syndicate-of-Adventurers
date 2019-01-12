package client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import client.chat.Chat;
import networking.MessageStatus;
import networking.MoveDirection;
import networking.messages.AttackMessage;
import networking.messages.Message;
import networking.messages.MoveMessage;
import networking.messages.fromclient.ChatMessage;
import networking.messages.fromclient.LoginMessage;
import networking.messages.fromclient.PingMessage;
import networking.messages.fromclient.RegisterMessage;
import networking.messages.fromserver.MessageFromServer;
import networking.messages.fromserver.SendMapMessage;
import networking.messages.fromserver.UpdateEntityMessage;

public class ClientConnection extends Thread
{
	private Socket clientSocket;
	//private ObjectOutputStream streamToServer;
	private ObjectInputStream streamFromServer;
	
	private final String ip;
	private final int port;
	
	private boolean isLogedIn;
	private String login;
	private long sessionId;
	
	private MessageSender sender;

	public ClientConnection(String ip, int port)
	{
		this.ip = ip;
		this.port = port;
	}
	
	@Override
	public void run()
	{	
		while(true)
		{
//			sender.start();
			
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
		sender.addMessage(new MoveMessage(sessionId, direction, toStop));
	}

	public void attack()
	{
		sender.addMessage(new AttackMessage(sessionId));
	}
	
	public void sentChatMessage(String chatMessageString)
	{
		sender.addMessage(new ChatMessage(login + ":" + " " + chatMessageString));
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
	
    		//streamToServer = new ObjectOutputStream(clientSocket.getOutputStream());
	    	streamFromServer = new ObjectInputStream(clientSocket.getInputStream());
	    	
	    	//sendToServer(new RegisterMessage(login, password));	
	    	sender.addMessage(new RegisterMessage(login, password));
	    	
	    	MessageFromServer fromServer = (MessageFromServer) getDataFromServer();

	    	System.out.println("Message from server: " + fromServer.toString());

	    	MessageStatus status = fromServer.getMessageStatus();
	    	System.out.println(status);
	    	
	    	return status;
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
			
    		//streamToServer = new ObjectOutputStream(clientSocket.getOutputStream());
	    	streamFromServer = new ObjectInputStream(clientSocket.getInputStream());
    	
	    	//sendToServer(new LoginMessage(login, password));
	    	sender.addMessage(new LoginMessage(login, password));
	    	
	    	MessageFromServer fromServer = (MessageFromServer) getDataFromServer();
	    	if(fromServer.getMessageStatus() == MessageStatus.OK) start();
	    	
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
			
			case LOGIN:
				/** TODO add missing code, maybe pop-ups */
				switch(((MessageFromServer) serverCallback).getMessageStatus())
				{
					case OK: isLogedIn = true; break;
					case WRONG_PASSWORD: break;
					case NOT_REGISTRED: break;
				}
			break;
			
			case LOAD_MAP:
				SyndicateOfAdventurers.setGameMap(((SendMapMessage) serverCallback).getMap());
			break;

			case UPDATE_ENTITY:
				UpdateEntityMessage message = (UpdateEntityMessage) serverCallback;
				SyndicateOfAdventurers.getGameMap().updateEntityPos(message.getEntityId(), message.getPosition(), message.getVelocity());
				Chat.updateChat(message.getChat());
			break;
			
			default:
				System.out.println("Unknown command");
		}
	}
	
//	public void sendToServer(Message message) throws IOException
//	{
//		streamToServer.writeObject(message);
//	}
	
	private Message getDataFromServer() throws ClassNotFoundException, IOException, EOFException
	{
		return (Message) streamFromServer.readObject();	
	}

	public void pingServer()
	{
		sender.addMessage(new PingMessage(sessionId, System.currentTimeMillis()));
//		try
//		{
//			sendToServer(new PingMessage(sessionId, System.currentTimeMillis()));
//		}
//		catch(IOException e)
//		{
//			e.printStackTrace();
//		}
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
		try
		{
			clientSocket = sslsocketfactory.createSocket(ip, port);
		}
		catch(Exception e)
		{
			return false;
		}
		
		return true;
	}
}