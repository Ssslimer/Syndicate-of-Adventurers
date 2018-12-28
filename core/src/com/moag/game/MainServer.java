package com.moag.game;

import com.moag.game.utils.ServerProperties;
import com.moag.game.utils.ServerPropertiesLoader;

public class MainServer
{
	public static void main(String[] args)
	{
		ServerProperties serverProperties = new ServerProperties(new ServerPropertiesLoader());
				
        Server server = new Server(serverProperties);
        server.init();
        server.loop();
        server.stop(); 
	}
}
