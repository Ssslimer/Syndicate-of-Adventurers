package com.moag.game.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.moag.game.Game;

public class DesktopLauncher
{
	public static void main(String[] arg)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.resizable = true;
		config.vSyncEnabled = true;
		config.title = "Syndicate of Adventurers";
		config.initialBackgroundColor = Color.BLACK;
		config.addIcon("icon128x.png", FileType.Internal);
		config.addIcon("icon32x.png", FileType.Internal);
		config.addIcon("icon16x.png", FileType.Internal);
		new LwjglApplication(new Game(), config);
	}
}
