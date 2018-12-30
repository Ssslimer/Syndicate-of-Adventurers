package com.moag.game;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import com.badlogic.gdx.math.Vector3;
import com.moag.game.networking.AttackMessage;
import com.moag.game.networking.LoadMapMessage;
import com.moag.game.networking.LoginMessage;
import com.moag.game.networking.Message;
import com.moag.game.networking.MessageFromServer;
import com.moag.game.networking.MoveMessage;
import com.moag.game.networking.PingMessage;
import com.moag.game.networking.RegisterMessage;

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
	
	public boolean register(String login, String password)
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
	    	
	    	Message fromServer = getDataFromServer();
	    	handleCallback(fromServer);
	    	System.out.println("Message from server: " + fromServer.toString());

	    	return true;
    	}
    	catch(IOException e)
    	{  		
    		System.out.println(e.getMessage());   		   		
    	}
		finally
		{
    		try
			{
				clientSocket.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
    		return false;
		}
	}
	
	public boolean login(String login, String password)
	{
		try
    	{	
			System.setProperty("javax.net.ssl.trustStore", "za.store");
			SocketFactory sslsocketfactory = SSLSocketFactory.getDefault();
			clientSocket = sslsocketfactory.createSocket(ip, port);
			System.out.println("Connected to server IP: " + ip + " Port: " + clientSocket.getPort());
	
    		streamToServer = new ObjectOutputStream(clientSocket.getOutputStream());
	    	streamFromServer = new ObjectInputStream(clientSocket.getInputStream());

	    	start();
	    	sendToServer(new LoginMessage(login, password));
    	}
    	catch(Exception e)
    	{  		
    		System.out.println(e.getMessage());   	

			try
			{
				clientSocket.close();
			}
			catch(IOException e2)
			{
				System.out.println(e2.getMessage());
			}

			return false;
    	}
		
    	return true;
	}
	
	public void loadMap()
	{
	    try
		{
			sendToServer(new LoadMapMessage(login, sessionId));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/** TODO REMAKE */
	public boolean move(Vector3 translation)
	{
		if(isLogedIn)
		{
			try 
			{
				sendToServer(new MoveMessage(translation, login, sessionId));
				
				Message fromServer = getDataFromServer();
				handleCallback(fromServer);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			} 
			catch (ClassNotFoundException e) 
			{
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	/** TODO REMAKE */
	public boolean attack()
	{
		if(isLogedIn)
		{
			try 
			{
				sendToServer(new AttackMessage(login, sessionId));
				
				Message fromServer = getDataFromServer();
				handleCallback(fromServer);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			} 
			catch (ClassNotFoundException e) 
			{
				e.printStackTrace();
			}
			
		}
		return false;
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
				if(((MessageFromServer) serverCallback).getWasSuccessful()) isLogedIn = true;
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
			sendToServer(new PingMessage(login, sessionId, System.currentTimeMillis()));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

}
