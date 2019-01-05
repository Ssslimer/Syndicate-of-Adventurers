package com.moag.game.server;

import java.net.UnknownHostException;

import com.moag.game.entities.Map;
import com.moag.game.util.ServerProperties;
import com.moag.game.util.Timer;

public class Server
{	
	private final ServerProperties serverProperties;	
	private ConnectionManager connectionManager;
	private AuthManager authManager;
	private static Map map;
	
	public Server(ServerProperties serverProperties)
	{
		this.serverProperties = serverProperties;
	}
	
	public void start() throws UnknownHostException
	{
		Thread.currentThread().setPriority(10);
		Timer.setLogicFrequency(serverProperties.getTPS());
		
		map = new Map();
		authManager = new AuthManager();
		
		connectionManager = new ConnectionManager(this, serverProperties.getIP(), serverProperties.getPortNumber());
		connectionManager.start();	
	}

	public void loop()
	{		
		while(true)
		{
			long timeBefore = Timer.getTime();
			
			update();
			
			long timeLeft = (long)Timer.getLogicDeltaTime() + timeBefore - Timer.getTime();	
			Timer.updateTPS();
			
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
	
	public static Map getMap()
	{
		return map;
	}

	public AuthManager getAuthManager()
	{
		return authManager;
	}
}