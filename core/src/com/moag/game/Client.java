package com.moag.game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import com.moag.game.networking.Message;

class Client
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
	
	public void connectToServer()
	{
		try
    	{	
			System.setProperty("javax.net.ssl.trustStore", "za.store");
			SocketFactory sslsocketfactory = SSLSocketFactory.getDefault();
			clientSocket = sslsocketfactory.createSocket(ip, port);
			System.out.println("Connected to server IP: " + ip + " Port: " + clientSocket.getPort());
	
    		streamToServer = new ObjectOutputStream(clientSocket.getOutputStream());
	    	streamFromServer = new ObjectInputStream(clientSocket.getInputStream());					
    	}
    	catch(IOException e)
    	{  		
    		System.out.println(e.getMessage());
    		e.printStackTrace();
    		
    		try{clientSocket.close();
			}catch(IOException e1){}
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
