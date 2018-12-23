package com.moag.game;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.moag.game.networking.Message;
import com.moag.game.networking.MessageContent;

class ConnectionServer extends Thread
{	
	private Socket clientSocket;
	private ObjectInputStream streamFromClient;
	private ObjectOutputStream streamToClient;

	public ConnectionServer(Socket clientSocket)
	{
		this.clientSocket = clientSocket;
	}
	
	@Override
	public void run()
	{		
	    try
	    {
			streamToClient = new ObjectOutputStream(clientSocket.getOutputStream());
			streamFromClient = new ObjectInputStream(clientSocket.getInputStream());
			
	    	Message fromClient = (Message) streamFromClient.readObject();	
	    	System.out.println("Message from: " + fromClient.toString());
	    		
	    	Message toClient = handleMessage(fromClient);
	    		
	    	streamToClient.writeObject(toClient);
	    }
	    catch(SocketException e)
	    {
	    	System.out.println(e.getMessage());
	    	System.out.println("Lost connection with client");
	    	return;
	    }
	    catch(EOFException e)
	    {	 
	    	try {wait(1);}
	    	catch(InterruptedException e1) {}
	    }
	    catch(ClassNotFoundException | IOException e)
	    {
			e.printStackTrace();
			return;
		}
    }

	private Message handleMessage(Message message)
	{
		MessageContent content = message.getActivity();
		
		switch(content)
		{						
			default:
				return new Message(MessageContent.STRING, "Unknown command");
		}
	}
}
