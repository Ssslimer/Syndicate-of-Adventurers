package com.moag.game.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.SSLServerSocketFactory;

public class ConnectionManager extends Thread
{
	private final String ip;
	private final int port;
	
	private List<ConnectionServer> connectionsToClients = new LinkedList<>();
	
	public ConnectionManager(String ip, int port)
	{
		this.ip = ip;
		this.port = port;
	}

	@Override
	public void run()
	{
		System.setProperty("javax.net.ssl.keyStore", "za.store");
		System.setProperty("javax.net.ssl.keyStorePassword", "qazwsx123");
		
		InetAddress address = null;
		try
		{
			address = InetAddress.getByName(ip);
		}
		catch(UnknownHostException e)
		{
			e.printStackTrace();
		}
		
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
				connectionsToClients.add(connectionToClient);
        	}
        }
		catch(IOException e)
		{
			e.printStackTrace();
		}

	}
}
