package com.moag.game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import com.moag.game.networking.LoginMessage;
import com.moag.game.networking.Message;
import com.moag.game.networking.MessageFromServer;
import com.moag.game.networking.RegisterMessage;

public class Client
{
	private Socket clientSocket;
	private ObjectOutputStream streamToServer;
	private ObjectInputStream streamFromServer;
	
	private final String ip;
	private final int port;

	public Client(String ip, int port)
	{
		this.ip = ip;
		this.port = port;
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

	    	sendToServer(new LoginMessage(login, password));
	    	
	    	Message fromServer = getDataFromServer();
	    	handleCallback(fromServer);
	    	System.out.println("Message from server: " + fromServer.toString());
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

	private void handleCallback(Message serverCallback)
	{
//		switch(serverCallback.getMessageType())
//		{
//			case REGISTER:
//				
//			break;
//			
//			case LOGIN:
//				
//			break;
//			
//			default:
//				System.out.println("Server have send unknown message");
//		}
		
		String messageFromServer = ((MessageFromServer)serverCallback).getMessageString();
		System.out.println(messageFromServer);
	}
	
	public void sendToServer(Message message) throws IOException
	{
		streamToServer.writeObject(message);
	}
	
	private Message getDataFromServer() throws ClassNotFoundException, IOException
	{
		return (Message) streamFromServer.readObject();	
	}

}
