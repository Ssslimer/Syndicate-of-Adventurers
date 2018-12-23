package com.moag.game;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class Server
{	
	private final String ip;
	private final int port;
	
	public Server(String ip, int port)
	{
		this.ip = ip;
		this.port = port;
	}
	
	public void start()
	{
		InetAddress address = null;
		try
		{
			address = InetAddress.getByName(this.ip);
		}
		catch(UnknownHostException e)
		{
			e.printStackTrace();
		}
		
		try(ServerSocket echoServer = new ServerSocket(port, 0, address))
		{
			System.out.println("Server was setup successfully");
			System.out.println("ip: " + echoServer.getInetAddress().getHostAddress());
			System.out.println("port: " + echoServer.getLocalPort());
		
			while(true)
			{
				ConnectionServer connectionToClient = new ConnectionServer(echoServer.accept());
				System.out.println("New connection is open");       			
				connectionToClient.start();
        	}
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
	}
}