package server;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import trade.TradeManager;

import java.util.HashMap;

import util.Timer;
import world.World;

public class Server
{	
	private final ServerProperties serverProperties;
	private final List<String> admins;
	private AuthManager authManager;
	
	public static final Random random = new Random();
	private static World world;
	private static ConnectionManager connectionManager;
	private MessageHandler messageHandler;
	private static TradeManager tradeManager = new TradeManager();
	
	private static List<String> chat = Collections.synchronizedList(new ArrayList<>());
	private static Map<Long, String> clientIDs = Collections.synchronizedMap(new HashMap<>());
	
	public Server()
	{
		ServerPropertiesLoader propertiesLoader = new ServerPropertiesLoader();
		this.serverProperties = propertiesLoader.load();
		
		AdminsLoader adminsLoader = new AdminsLoader();
		this.admins = adminsLoader.load();
	}
	
	public void start() throws UnknownHostException
	{
		Thread.currentThread().setPriority(10);
		Timer.setLogicFrequency(serverProperties.getTPS());
		
		authManager = new AuthManager();
		
		messageHandler = new MessageHandler(this);
		messageHandler.start();
		
		connectionManager = new ConnectionManager(this, serverProperties.getIP(), serverProperties.getPortNumber());
		connectionManager.start();
		
		world = new World();
	}

	public void loop()
	{		
		while(true)
		{
			long timeBefore = Timer.getTime();
			
			update(Timer.getLogicDeltaTime());
			
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
	
	private void update(float delta)
	{	
		world.update(delta);
	}

	public void stop()
	{
		
	}

	public static void addChatMessage(String chatMessageString)
	{
		chat.add(chatMessageString);
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
	
	public static long getSessionIDByLogin(String login)
	{
		for(Map.Entry<Long, String> entry : clientIDs.entrySet())
		{
			if(entry.getValue().equals(login)) return entry.getKey();
		}
		
		return -1;
	}
	
	public boolean isAdmin(String login)
	{
		return admins.contains(login);
	}
	
	public MessageHandler getMessageHandler()
	{
		return messageHandler;
	}
	
	public static World getMap()
	{
		return world;
	}

	public AuthManager getAuthManager()
	{
		return authManager;
	}
		
	public static ConnectionManager getConnectionManager()
	{
		return connectionManager;
	}
	
	public static TradeManager getTradeManager()
	{
		return tradeManager;
	}
}