package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import javax.net.ssl.SSLHandshakeException;

import networking.messages.Message;

public class ConnectionToClient extends Thread
{	
	private ObjectInputStream streamFromClient;
	private ObjectOutputStream streamToClient;
	
	private boolean hasLogedIn, isRunning=true;
	
	private String login;

	private final ConnectionManager connectionManager;
	private final Socket clientSocket;
	private final MessageHandler messageHandler;
	
	public ConnectionToClient(ConnectionManager connectionManager, Socket clientSocket, MessageHandler messageHandler)
	{
		super("ClientConnectionThread");
		
		this.connectionManager = connectionManager;
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
				messageHandler.handleMessage(this, message);
			}
			
			/** TODO send player a disconnect info? */
			connectionManager.removeConnection(this);
	    }
	    catch(SSLHandshakeException e)
	    {
	    	System.out.println(e.getMessage());
	    	connectionManager.removeConnection(this);
	    }
	    catch(SocketException e)
	    {
	    	System.out.println(e.getMessage());
	    	System.out.println("Lost connection with client");
	    	connectionManager.removeConnection(this);
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
			connectionManager.removeConnection(this);
			return;
		}
    }
	
	/** TODO REMOVE IF CHECK after adding better client list handling */
	public void sendMessageToClient(Message message) throws IOException
	{
		System.out.println("Sending: " + message.getMessageType());
		if(streamToClient != null) streamToClient.writeObject(message);
	}
	
	private Message getMessageFromClient() throws ClassNotFoundException, IOException
	{
		Message message = (Message) streamFromClient.readObject();
		System.out.printf("Message from: %s Message content: %s \n", login, message.getMessageType());
		
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
	
	public boolean isClosed()
	{
		return clientSocket.isOutputShutdown();
	}
}
