package server;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import entities.World;
import util.ServerProperties;
import util.Timer;

public class Server
{	
	private final ServerProperties serverProperties;	
	private ConnectionManager connectionManager;
	private AuthManager authManager;
	private static World world;
	private static List<String> chat = Collections.synchronizedList(new ArrayList<>());
	private static Map<Long, String> clientIDs = Collections.synchronizedMap(new HashMap<>());
	
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
		world.update();
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
	
	public ConnectionManager getConnectionManager()
	{
		return connectionManager;
	}
	
	public static String getLogin(long sessionID)
	{
		return clientIDs.get(sessionID);
	}
	
	public static void addClient(long sessionID, String login)
	{
		clientIDs.put(sessionID, login);
	}
	
	public static boolean IdNotOccupied(long sessionID)
	{
		return !clientIDs.containsKey(sessionID);
	}
}