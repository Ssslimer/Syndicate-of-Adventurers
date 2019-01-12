package server;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.math.Vector3;

import entities.Entity;
import entities.World;
import networking.messages.fromserver.UpdateEntityMessage;
import util.ServerProperties;
import util.Timer;

public class Server
{	
	private final ServerProperties serverProperties;	
	private ConnectionManager connectionManager;
	private AuthManager authManager;
	private static World world;
	private static List<String> chat = Collections.synchronizedList(new ArrayList<>());
	
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
				if(connection.isLogedIn()) 
				{
					connection.sendMessageToClient(new UpdateEntityMessage(id, position, velocity, chat));
				}
			}
			catch(IOException e)
			{
				System.out.println(e);
			}
		}
		chat.clear();
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
	
	public static void addChatMessage(String chatMessageString)
	{
		chat.add(chatMessageString);
	}
}