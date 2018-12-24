package com.moag.game;

import com.moag.game.utils.ServerPropertiesLoader;

public class MainServer
{
	public static void main(String[] args)
	{
//		ServerPropertiesLoader serverProperties = new ServerPropertiesLoader();
//		String ip = serverProperties.getIP();
//		int port = serverProperties.getPortNumber();
				
        Server server = new Server("192.168.2.59", 4444); 
        server.start();
	}
}
