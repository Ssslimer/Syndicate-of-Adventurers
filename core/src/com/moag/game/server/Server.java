package com.moag.game.server;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.moag.game.networking.Message;
import com.moag.game.util.ServerProperties;
import com.moag.game.util.Timer;

public class Server
{	
	private final ServerProperties serverProperties;	
	private ConnectionManager connectionManager;

	private static Queue<Message> messageQueue = new ConcurrentLinkedQueue<>();
	
	public Server(ServerProperties serverProperties)
	{
		this.serverProperties = serverProperties;
	}
	
	public void start()
	{
		connectionManager = new ConnectionManager(serverProperties.getIP(), serverProperties.getPortNumber());
		connectionManager.start();
		
		Thread.currentThread().setPriority(10);
		Timer.setLogicFrequency(serverProperties.getTPS());
	}

	public void loop()
	{		
		while(true)
		{
			long timeBefore = Timer.getTime();
			
			update();
			
			long timeLeft = (long)Timer.getLogicDeltaTime() + timeBefore - Timer.getTime();	
			Timer.updateTPS(timeLeft);
			
			try
			{
				Thread.sleep(timeLeft);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private void update()
	{	
		
	}

	public void stop()
	{
		
	}
	
	public Message getHeadMessage()
	{
		return messageQueue.poll();
	}
	
	public void addMessageToQueue(Message message)
	{
		messageQueue.offer(message);
	}
}