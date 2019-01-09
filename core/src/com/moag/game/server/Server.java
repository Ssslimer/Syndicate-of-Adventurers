package com.moag.game.server;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.math.Vector3;
import com.moag.game.entities.Entity;
import com.moag.game.entities.World;
import com.moag.game.networking.messages.fromserver.UpdateEntityMessage;
import com.moag.game.util.ServerProperties;
import com.moag.game.util.Timer;

public class Server
{	
	private final ServerProperties serverProperties;	
	private ConnectionManager connectionManager;
	private AuthManager authManager;
	private static World world;
	
	public Server(ServerProperties serverProperties)
	{
		this.serverProperties = serverProperties;
	}
	
	public void start() throws UnknownHostException
	{
		Thread.currentThread().setPriority(10);
		Timer.setLogicFrequency(serverProperties.getTPS());
		
		world = new World();
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
			catch(IllegalArgumentException e)
			{
				System.out.println(e.getLocalizedMessage());
			}
			catch(InterruptedException e)
			{
				System.out.println(e.getLocalizedMessage());
			}
		}
	}
	
	private void update()
	{	
		long id = -1;
		world.spawnEntity(new Entity(new Vector3()));
		Map<Long, Entity> entities = world.getEntities();
		for(Entity entity : entities.values())
		{
			id = entity.getId();
			break;
		}
		
		Random random = new Random();
<<<<<<< HEAD
		Vector3 position = new Vector3(random.nextInt(10), 0, random.nextInt(10));
		Vector3 velocity = new Vector3(new Vector3(random.nextInt(10), 0, random.nextInt(10)));
		world.updateEntityPos(id, position, velocity);
		
		try
		{
			Thread.sleep(5000);
		}
		catch(InterruptedException e1)
		{
			e1.printStackTrace();
		}
		
		for(ServerConnection connection : connectionManager.getAllConnections())
		{
			try
			{
				if(connection.isLogedIn()) connection.sendMessageToClient(new UpdateEntityMessage(id, position, velocity));
			}
			catch(IOException e)
			{
				System.out.println(e);
			}
		}
=======
		world.updateEntityPos(id, new Vector3(random.nextInt(10), 0, random.nextInt(10)), new Vector3(random.nextInt(10), 0, random.nextInt(10)));
		world.update();
>>>>>>> branch 'master' of https://github.com/Ssslimer/Syndicate-of-Adventurers.git
	}

	public void stop()
	{
		
	}
	
	public static World getMap()
	{
		return world;
	}

	public AuthManager getAuthManager()
	{
		return authManager;
	}
}