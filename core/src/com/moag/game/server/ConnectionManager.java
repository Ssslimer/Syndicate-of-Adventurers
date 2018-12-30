package com.moag.game.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLServerSocketFactory;

public class ConnectionManager extends Thread
{
	private final InetAddress address;
	private final int port;

	public ConnectionManager(String ip, int port) throws UnknownHostException
	{
		this.port = port;
		this.address = InetAddress.getByName(ip);
		
		System.setProperty("javax.net.ssl.keyStore", "za.store");
		System.setProperty("javax.net.ssl.keyStorePassword", "qazwsx123");
	}

	@Override
	public void run()
	{
		MessageHandler messageHander = new MessageHandler();
		messageHander.start();
		
		SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		try(ServerSocket serverSocket = sslserversocketfactory.createServerSocket(port, 0, address))
		{
			System.out.println("Server was setup successfully");
			System.out.println("ip: " + serverSocket.getInetAddress().getHostAddress());
			System.out.println("port: " + serverSocket.getLocalPort());
			
			while(true)
			{
				ServerConnection connectionToClient = new ServerConnection(serverSocket.accept(), messageHander);			     			
				connectionToClient.start();
        	}
        }
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
