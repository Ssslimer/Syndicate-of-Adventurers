package com.moag.game;


public class MainServer
{
	public static void main(String[] args)
	{
		String ip = "192.168.2.59";
		int port = 4444;
        
        Server server = new Server(ip, port); 
        server.start();
	}
}
