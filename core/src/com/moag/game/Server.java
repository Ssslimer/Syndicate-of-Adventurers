package com.moag.game;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLServerSocketFactory;

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
		
		System.setProperty("javax.net.ssl.keyStore", "za.store");
		System.setProperty("javax.net.ssl.keyStorePassword", "qazwsx123");
		SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		try(ServerSocket serverSocket = sslserversocketfactory.createServerSocket(port, 0, address))
		{
			System.out.println("Server was setup successfully");
			System.out.println("ip: " + serverSocket.getInetAddress().getHostAddress());
			System.out.println("port: " + serverSocket.getLocalPort());
		
			while(true)
			{
				ConnectionServer connectionToClient = new ConnectionServer(serverSocket.accept());
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