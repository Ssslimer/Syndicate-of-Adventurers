package client;

import java.util.Random;

import com.badlogic.gdx.Game;

import entities.World;
import screens.MainMenuScreen;

public class MyGame extends Game
{	 
	public static final Random random = new Random();
	private static ClientConnection client;
	private static WorldRenderer renderer;
	private static final Resources resources = new Resources();
	private static World gameMap;

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
}
