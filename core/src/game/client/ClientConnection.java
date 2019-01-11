package client;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

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
import other.SyndicateOfAdventurers;

public class ClientConnection extends Thread
{
	private Socket clientSocket;
	private ObjectOutputStream streamToServer;
	private ObjectInputStream streamFromServer;
	
	private final String ip;
	private final int port;
	
	private boolean isLogedIn;
	private String login;
	private long sessionId;

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
		try 
		{
			sendToServer(new MoveMessage(sessionId, direction, toStop));

		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public void attack()
	{
		try 
		{
			sendToServer(new AttackMessage(sessionId));

		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void sentChatMessage(String chatMessageString)
	{
		try 
		{
			sendToServer(new ChatMessage(chatMessageString));

		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public MessageStatus register(String login, String password)
	{
		try
    	{
			System.setProperty("javax.net.ssl.trustStore", "za.store");
			SocketFactory sslsocketfactory = SSLSocketFactory.getDefault();
			clientSocket = sslsocketfactory.createSocket(ip, port);
			System.out.println("Connected to server IP: " + ip + " Port: " + clientSocket.getPort());
	
    		streamToServer = new ObjectOutputStream(clientSocket.getOutputStream());
	    	streamFromServer = new ObjectInputStream(clientSocket.getInputStream());
	    	
	    	sendToServer(new RegisterMessage(login, password));	    	
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
			System.out.println("Connected to server IP: " + ip + " Port: " + clientSocket.getPort());
	
    		streamToServer = new ObjectOutputStream(clientSocket.getOutputStream());
	    	streamFromServer = new ObjectInputStream(clientSocket.getInputStream());
    	
	    	sendToServer(new LoginMessage(login, password));
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
				System.out.println(message);
				SyndicateOfAdventurers.getGameMap().updateEntityPos(message.getEntityId(), message.getPosition(), message.getVelocity());
			break;
			
			default:
				System.out.println("Unknown command");
		}
	}
	
	public void sendToServer(Message message) throws IOException
	{
		streamToServer.writeObject(message);
	}
	
	private Message getDataFromServer() throws ClassNotFoundException, IOException, EOFException
	{
		return (Message) streamFromServer.readObject();	
	}

	public void pingServer()
	{
		try
		{
			sendToServer(new PingMessage(sessionId, System.currentTimeMillis()));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void stopConnection()
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
}