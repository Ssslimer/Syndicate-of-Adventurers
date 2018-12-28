package com.moag.game;

public class MainClient
{
	public static void main(String[] args)
	{
		String ip = "192.168.2.59";
		int port = 4444;
		
		Client client = new Client(ip, port);
		client.register("Maciej", "");
		//client.login(userName, password);
	}
}
