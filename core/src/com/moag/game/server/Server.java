package com.moag.game.server;

import java.net.ServerSocket;

public class Server
{	
	public Server(int port)
	{
		try(ServerSocket echoServer = new ServerSocket(port))
		{
			System.out.println("Server setup on port: " + echoServer.getLocalPort());
		
			while(true)
			{
//				ConnectionServer connectionToClient = new ConnectionServer(echoServer.accept());
//				System.out.println("New connection is open");       			
//				connectionToClient.start();
        	}
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            try {Thread.sleep(2000);}
			catch(InterruptedException e) {e.printStackTrace();}
        }
	}
}