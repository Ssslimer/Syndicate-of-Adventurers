package com.moag.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.moag.game.screens.MainMenuScreen;

public class SyndicateOfAdventurers extends Game
{	 
	private AssetManager assetManager;
	private static Client client;
	private static Resources resources;

	@Override
	public void create()
	{
		assetManager = new AssetManager();
		resources = new Resources();
		setScreen(new MainMenuScreen(this));
	}
	
	@Override
	public void dispose() { assetManager.dispose(); }
	
	public AssetManager getAssetManager() { return assetManager; }

	public static Client getClient()
	{
		return client;
	}
	
	public static void setClient(Client client)
	{
		SyndicateOfAdventurers.client = client;
	}

	public static Resources getResources()
	{
		return resources;
	}
}
