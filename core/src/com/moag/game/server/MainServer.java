package com.moag.game.server;

import com.moag.game.util.ServerProperties;
import com.moag.game.util.ServerPropertiesLoader;

public class MainServer
{
	public static void main(String[] args)
	{
		ServerProperties serverProperties = new ServerProperties(new ServerPropertiesLoader());
				
        Server server = new Server(serverProperties);
        server.start();
        server.loop();
        server.stop(); 
	}
}
