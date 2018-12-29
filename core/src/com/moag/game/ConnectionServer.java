package com.moag.game;

import java.io.EOFException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.moag.game.networking.Message;
import com.moag.game.networking.NetworkingEnums.LoginStatus;
import com.moag.game.networking.NetworkingEnums.MessageContent;
import com.moag.game.networking.NetworkingEnums.RegisterStatus;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

class ConnectionServer extends Thread
{	
	private Socket clientSocket;
	private ObjectInputStream streamFromClient;
	private ObjectOutputStream streamToClient;
	
	private boolean hasLogedIn = false, terminateConnection = false;

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
			
			while(!terminateConnection)
			{
				Message fromClient = (Message) streamFromClient.readObject();	
		    	System.out.println("Message from: " + fromClient.toString());
		    		
		    	Message toClient = handleMessage(fromClient);
		    		
		    	streamToClient.writeObject(toClient);
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

	private Message handleMessage(Message message)
	{
		MessageContent content = message.getActivity();
		
		switch(content)
		{	
			case REGISTER:
				try
				{			
					terminateConnection = true;
					List<String> data = (List<String>) message.getData();
					String login = data.get(0);
					char[] password = data.get(1).toCharArray();
					Argon2 argon2 = Argon2Factory.create();
					String hashedPassword = argon2.hash(10, 65536, 1, password);
					
					if(isPlayerRegistered(login)) return new Message(MessageContent.REGISTER, RegisterStatus.OCCUPIED);
					
					if(registerPlayer(login, hashedPassword)) new Message(MessageContent.REGISTER, RegisterStatus.OK);
					else new Message(MessageContent.REGISTER, RegisterStatus.ERROR);
									
					return new Message(MessageContent.REGISTER, RegisterStatus.OK);
				}
				catch(Exception e)
				{
					return new Message(MessageContent.REGISTER, RegisterStatus.ERROR);	
				}
			case LOGIN:
				try
				{
					terminateConnection = true;
					List<String> data = (List<String>) message.getData();
					String login = data.get(0);
					String password = data.get(1);
					
					if(!isPlayerRegistered(login)) return new Message(MessageContent.LOGIN, LoginStatus.NOT_REGISTERED);
					
					if(checkPassword(login, password))
					{
						hasLogedIn = true;
						terminateConnection = false;
						return new Message(MessageContent.LOGIN, LoginStatus.OK);
					}
					
					return new Message(MessageContent.LOGIN, LoginStatus.WRONG_PASSWORD);
				}
				catch(Exception e)
				{
					return new Message(MessageContent.REGISTER, RegisterStatus.ERROR);
				}	
			default:
				return new Message(MessageContent.STRING, "Unknown command");
		}
	}

	private boolean registerPlayer(String login, String password)
	{
		Path pathToFolder = Paths.get("save", "auth");
		Path pathToFile = Paths.get("save", "auth", login+".txt");
		
		try
		{
			Files.createDirectories(pathToFolder);
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
			return false;
		}			
	
		try(PrintWriter writer = new PrintWriter(new FileWriter(pathToFile.toFile())))
		{
			writer.print(password);			
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
			return false;
		}
		
	    return true;
	}
	
	private boolean isPlayerRegistered(String login)
	{
		return Files.exists(Paths.get("save", "auth", login+".txt"));
	}
	
	private boolean checkPassword(String login, String password) throws IOException
	{
		Path pathToFile = Paths.get("save", "auth", login+".txt");
		List<String> lines = Files.readAllLines(pathToFile);
		if(lines.size() != 1) return false;
		
		Argon2 argon2 = Argon2Factory.create();
		
		return argon2.verify(lines.get(0), password);
	}

}
