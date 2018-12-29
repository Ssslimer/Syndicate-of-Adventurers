package com.moag.game;

import com.moag.game.util.ServerProperties;
import com.moag.game.util.Timer;

public class Server
{	
	private final ServerProperties serverProperties;	
	private ConnectionManager connectionManager;

	public Server(ServerProperties serverProperties)
	{
		this.serverProperties = serverProperties;
	}
	
	public void init()
	{
		connectionManager = new ConnectionManager(serverProperties.getIP(), serverProperties.getPortNumber());
		connectionManager.start();
		
		Thread.currentThread().setPriority(10);
		Timer.setLogicFrequency(serverProperties.getTPS());
	}

	public void loop()
	{		
		long currentTickTime = 0;		
		
		while(true)
		{
			currentTickTime = Timer.getTime();
			
			update();
			
			long timeLeft = (long)Timer.getLogicDeltaTime() + currentTickTime - Timer.getTime();	
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
}