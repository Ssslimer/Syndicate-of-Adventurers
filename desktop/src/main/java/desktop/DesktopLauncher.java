package desktop;

import java.nio.file.Paths;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;

import other.SyndicateOfAdventurers;
import util.ConfigConstants;

public class DesktopLauncher
{
	public static void main(String[] arg)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.resizable = true;
		config.vSyncEnabled = true;
		config.title = "Syndicate of Adventurers";
		config.initialBackgroundColor = Color.BLACK;
		config.addIcon(Paths.get("assets", "icon128x.png").toString(), FileType.Internal);
		config.addIcon(Paths.get("assets", "icon32x.png").toString(), FileType.Internal);
		config.addIcon(Paths.get("assets", "icon16x.png").toString(), FileType.Internal);
		config.width = ConfigConstants.WIDTH;
		config.height = ConfigConstants.HEIGHT;
		new LwjglApplication(new SyndicateOfAdventurers(), config);
	}
}
