package com.moag.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.moag.game.Screens.MainMenuScreen;

public class SyndicateOfAdventurers extends Game
{	 
	private AssetManager assetManager;

	@Override
	public void create()
	{
		assetManager = new AssetManager();
		setScreen(new MainMenuScreen(this));
	}
	
	@Override
	public void dispose() { assetManager.dispose(); }
	
	public AssetManager getAssetManager() { return assetManager; }
}
