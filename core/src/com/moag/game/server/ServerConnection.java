package com.moag.game.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.moag.game.networking.Message;

public class ServerConnection extends Thread
{	
	private Socket clientSocket;
	private ObjectInputStream streamFromClient;
	private ObjectOutputStream streamToClient;
	
	private final MessageHandler messageHandler;
	
	private boolean hasLogedIn, isRunning=true;	

	public ServerConnection(Socket clientSocket, MessageHandler messageHandler)
	{
		super("ClientConnectionThread");
		this.clientSocket = clientSocket;
		this.messageHandler = messageHandler;
	}
	
	@Override
	public void run()
	{
		System.out.println("New connection is open");
		
	    try
	    {
			streamToClient = new ObjectOutputStream(clientSocket.getOutputStream());
			streamFromClient = new ObjectInputStream(clientSocket.getInputStream());

			while(isRunning)
			{
				Message message = getMessageFromClient();
				messageHandler.handleMessage(new MessageTask(this, message));
			}
			
			/** TODO send player a disconnect info? */
	    }
	    catch(SocketException e)
	    {
	    	System.out.println(e.getMessage());
	    	System.out.println("Lost connection with client");
	    	return;
	    }
	    catch(EOFException e)
	    {	 
	    	try {sleep(1);}
	    	catch(InterruptedException e1) {}
	    }
	    catch(ClassNotFoundException | IOException e)
	    {
			e.printStackTrace();
			return;
		}
    }
	
	public void sendMessageToClient(Message message) throws IOException
	{
		streamToClient.writeObject(message);
	}
	
	private Message getMessageFromClient() throws ClassNotFoundException, IOException
	{
		Message message = (Message) streamFromClient.readObject();
		System.out.println("Message from: " + message.toString());
		
		return message;
	}
	
	void stopCommunication()
	{
		isRunning = false;
	}
	
	void login()
	{
		hasLogedIn = true;
	}

	public boolean isLogedIn()
	{
		return hasLogedIn;
	}
}
