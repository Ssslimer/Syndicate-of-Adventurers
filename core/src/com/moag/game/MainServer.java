package com.moag.game;

import com.moag.game.util.ServerProperties;
import com.moag.game.util.ServerPropertiesLoader;

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
