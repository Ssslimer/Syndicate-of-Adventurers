package desktop;

import java.nio.file.Paths;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;

import client.MyGame;
import util.ConfigConstants;

public class DesktopLauncher
{
	public static void main(String[] arg)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.resizable = true;
		config.fullscreen = false;
		config.vSyncEnabled = true;
		config.title = "Syndicate of Adventurers";
		config.initialBackgroundColor = Color.BLACK;
		config.addIcon(Paths.get("assets", "textures", "icons", "icon128x.png").toString(), FileType.Internal);
		config.addIcon(Paths.get("assets", "textures", "icons", "icon32x.png").toString(), FileType.Internal);
		config.addIcon(Paths.get("assets", "textures", "icons", "icon16x.png").toString(), FileType.Internal);
		config.width = ConfigConstants.WIDTH;
		config.height = ConfigConstants.HEIGHT;
		new LwjglApplication(new MyGame(), config);
	}
}
