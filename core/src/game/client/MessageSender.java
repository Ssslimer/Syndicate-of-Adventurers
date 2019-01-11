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
	
	private Socket clientSocket;
	private ObjectOutputStream streamToServer;
	
	public MessageSender(Socket clientSocket)
	{
		this.clientSocket = clientSocket;
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			Message message = messages.poll();
			
			if(message != null)
			{
				try { sendToServer(message);} 
				catch (IOException e) {e.printStackTrace();}
			}
		}
	}
	
	public void addMessage(Message message)
	{
		messages.add(message);
	}
	
	private void sendToServer(Message message) throws IOException
	{
		if(!clientSocket.isClosed())
		{
			streamToServer = new ObjectOutputStream(clientSocket.getOutputStream());
			streamToServer.writeObject(message);
		}
	}
}
