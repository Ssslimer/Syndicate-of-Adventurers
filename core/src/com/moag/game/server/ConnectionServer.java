package com.moag.game.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.moag.game.networking.LoginMessage;
import com.moag.game.networking.Message;
import com.moag.game.networking.MessageFromServer;
import com.moag.game.networking.MessageType;
import com.moag.game.networking.RegisterMessage;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import static com.moag.game.server.AuthManager.*;

public class ConnectionServer extends Thread
{	
	private Socket clientSocket;
	private ObjectInputStream streamFromClient;
	private ObjectOutputStream streamToClient;
	
	private boolean hasLogedIn; 
	private boolean terminateConnection;
	
	private boolean canHandleMessage;
	private long sessionID;

	public ConnectionServer(Socket clientSocket, long sessionID)
	{
		this.clientSocket = clientSocket;
		this.sessionID = sessionID;
	}
	
	@Override
	public void run()
	{		
	    try
	    {
			streamToClient = new ObjectOutputStream(clientSocket.getOutputStream());
			streamFromClient = new ObjectInputStream(clientSocket.getInputStream());
			
			while(!terminateConnection)
			{
				Message fromClient = (Message) streamFromClient.readObject();	
		    	System.out.println("Message from: " + fromClient.toString());
		    		
		    	waitForHandlingPermission();
		    	Message toClient = handleMessage(fromClient);
		    		
		    	streamToClient.writeObject(toClient);
		    	
		    	canHandleMessage = false; // restoring original value;
		    	MessageManager.messageHandled();
			}    	
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
	
	private void waitForHandlingPermission()
	{
		while(!canHandleMessage) {}
	}

	private Message handleMessage(Message message)
	{
		MessageType content = message.getMessageType();
		
		switch(content)
		{	
		
		case REGISTER:
			try
			{			
				terminateConnection = true;
				String login = ((RegisterMessage)message).getLogin();
				char[] password = ((RegisterMessage)message).getPassword().toCharArray();
				Argon2 argon2 = Argon2Factory.create();
				String hashedPassword = argon2.hash(10, 65536, 1, password);
				
				if(isPlayerRegistered(login)) return new MessageFromServer(false, "User with such login already exist!");
				
				if(registerPlayer(login, hashedPassword)) new MessageFromServer(true, "Successfuly registered!");
				else new MessageFromServer(false, "Something went wrong!");
			}
			catch(Exception e)
			{
				return new MessageFromServer(false, "Something went wrong!");	
			}

		case LOGIN:
			try
			{
				terminateConnection = true;
				String login = ((LoginMessage)message).getLogin();
				String password = ((LoginMessage)message).getPassword();
				
				if(!isPlayerRegistered(login)) return new MessageFromServer(false, "User with such login does not exist!");
				
				if(checkPassword(login, password))
				{
					hasLogedIn = true;
					terminateConnection = false;
					return new MessageFromServer(true, "Successfuly logged in!");
				}
				return new MessageFromServer(false, "Given password is wrong!");
			}
			catch(Exception e)
			{
				return new MessageFromServer(false, "Something went wrong!");
			}	
		
		case QUIT:
			//check whether this is enough
			terminateConnection = true;
			return new MessageFromServer(true, "Server disconnected");
		
		case MOVE:
			
			if(hasLogedIn)
			{
				
			}
			
		case ATTACK:
			
			if(hasLogedIn)
			{
				
			}
			
		default:
			return new MessageFromServer(false, "Unknown command");
		}
	}

	public void canHandleMessage()
	{
		this.canHandleMessage = true;
	}
}
