package client;

import java.util.Random;

import com.badlogic.gdx.Game;

import entities.EntityPlayer;
import screens.MainMenuScreen;
import trade.TradeManager;
import world.World;

public class MyGame extends Game
{	 
	public static final Random random = new Random();
	private static ClientConnection client;
	private static WorldRenderer renderer;
	private static final Resources resources = new Resources();
	private static World gameMap;
	private static EntityPlayer player;
	
	private static final TradeManager tradeManager = new TradeManager();

	@Override
	public void create()
	{		
		resources.loadAll();
		setScreen(new MainMenuScreen(this));
		renderer = new WorldRenderer();
	}
	
	@Override
	public void dispose() 
	{ 
		resources.unload();
	}
	
	public static ClientConnection getClient()
	{
		return client;
	}
	
	public static void setClient(ClientConnection client)
	{
		MyGame.client = client;
	}

	public static Resources getResources()
	{
		return resources;
	}
	
	public static World getGameMap()
	{
		return gameMap;
	}
	
	public static void setGameMap(World map)
	{
		gameMap = map;
	}

	public static WorldRenderer getRenderer()
	{
		return renderer;
	}
	
	public static void setupPlayer(EntityPlayer e)
	{
		player = e;
	}
	
	public static EntityPlayer getPlayer()
	{
		return player;
	}

	public static TradeManager getTradeManager()
	{
		return tradeManager;
	}
}