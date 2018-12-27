package com.moag.game;

import com.moag.game.utils.ServerProperties;

public class MainServer
{
	public static void main(String[] args)
	{
		ServerProperties serverProperties = new ServerProperties();
				
        Server server = new Server(serverProperties);
        server.init();
        server.loop();
        server.stop();
	}
}
