package com.moag.game;

import java.io.IOException;
import java.net.Socket;

public class MainClient
{
	public static void main(String[] args)
	{
		String ip = "192.168.2.59";
		int port = 4444;
		
		try
		{
			Socket socket = new Socket(ip, port);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
