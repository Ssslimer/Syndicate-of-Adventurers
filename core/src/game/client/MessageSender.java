package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import networking.messages.Message;

public class MessageSender extends Thread
{
	private Queue<Message> messages = new ConcurrentLinkedQueue<>();
	private boolean shouldWait = true;
	
	private Socket clientSocket;
	private ObjectOutputStream streamToServer;
	
	public MessageSender(Socket clientSocket)
	{
		super("Output to server");
		setDaemon(true);
		this.clientSocket = clientSocket;
	}
	
	@Override
	public void run()
	{
		try 
		{
			streamToServer = new ObjectOutputStream(clientSocket.getOutputStream());
		} 
		catch (IOException e1) { e1.printStackTrace(); }
		
		while(true)
		{
			if(shouldWait)
			{
				synchronized(this)
				{
					try{wait();}
					catch(InterruptedException e) {e.printStackTrace();}
				}
			}
			
			Message message = messages.poll();
			if(messages.isEmpty()) shouldWait = true;
			try { sendToServer(message);} 
			catch (IOException e) {e.printStackTrace();}
		}
	}
	
	public void addMessage(Message message)
	{
		messages.add(message);
		shouldWait = false;
		synchronized(this){notify();}
	}
	
	private void sendToServer(Message message) throws IOException
	{
		if(!clientSocket.isClosed() && !clientSocket.isOutputShutdown())
		{
			System.out.println("NEW MESSAGE: " + message.getMessageType().toString());
			streamToServer.writeObject(message);
			streamToServer.reset();
			streamToServer.flush();
		}
	}
}
