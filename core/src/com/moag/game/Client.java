package com.moag.game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import com.moag.game.networking.Message;
import com.moag.game.networking.NetworkingEnums.MessageContent;

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
	    	
	    	List<String> data = new ArrayList<>(2);
	    	data.add(login);
	    	data.add(password);
	    	sendToServer(new Message(MessageContent.REGISTER, data));
	    	
	    	Message fromServer = (Message) streamFromServer.readObject();	
	    	System.out.println("Message from server: " + fromServer.toString());
	    		
	    	Message toClient = handleMessage(fromServer);
	    	
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
	
	private Message handleMessage(Message message)
	{
		MessageContent content = message.getActivity();
		
		switch(content)
		{	
			case REGISTER:				
				//List<String> data = (List<String>) message.getData();
				System.out.println(message.getData());
				
			default:
				return new Message(MessageContent.STRING, "Unknown command");
		}
	}
	
	public boolean login(String userName, String password)
	{
		try
    	{	
			System.setProperty("javax.net.ssl.trustStore", "za.store");
			SocketFactory sslsocketfactory = SSLSocketFactory.getDefault();
			clientSocket = sslsocketfactory.createSocket(ip, port);
			System.out.println("Connected to server IP: " + ip + " Port: " + clientSocket.getPort());
	
    		streamToServer = new ObjectOutputStream(clientSocket.getOutputStream());
	    	streamFromServer = new ObjectInputStream(clientSocket.getInputStream());
	    	return true;
    	}
    	catch(IOException e)
    	{  		
    		System.out.println(e.getMessage());
    		
    		try
    		{
    			clientSocket.close();
			}
    		catch(IOException e1) {}
    		return false;
    	}	
	}

	private void handleCallback(Message serverCallback)
	{
		switch(serverCallback.getActivity())
		{
			default:
				System.out.println("Server have send unknown message");
		}
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
