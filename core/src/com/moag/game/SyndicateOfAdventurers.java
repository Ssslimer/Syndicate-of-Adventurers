package com.moag.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.moag.game.entities.World;
import com.moag.game.screens.MainMenuScreen;

public class SyndicateOfAdventurers extends Game
{	 
	private AssetManager assetManager;
	private static ClientConnection client;
	private static Resources resources;
	private static World gameMap;

	@Override
	public void create()
	{
		assetManager = new AssetManager();
		resources = new Resources();
		setScreen(new MainMenuScreen(this));
	}
	
	@Override
	public void dispose() 
	{ 
		assetManager.dispose(); 
	}
	
	public AssetManager getAssetManager() 
	{
		return assetManager; 
	}

	public static ClientConnection getClient()
	{
		return client;
	}
	
	public static void setClient(ClientConnection client)
	{
		SyndicateOfAdventurers.client = client;
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
}
