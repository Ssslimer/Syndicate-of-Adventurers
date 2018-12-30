package com.moag.game.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.net.ssl.SSLServerSocketFactory;

import com.moag.game.networking.Message;

public class ConnectionManager extends Thread
{
	private final String ip;
	private final int port;
	
	Random rand;
	
	//private List<ConnectionServer> connectionsToClients = new LinkedList<>();
	private static Map<Long, ConnectionServer> connectionsToClients = new HashMap<>();
	private static Queue<Message> messageQueue = new ConcurrentLinkedQueue<>();
	
	public ConnectionManager(String ip, int port)
	{
		this.ip = ip;
		this.port = port;
		
		rand = new Random();
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
			
			MessageManager messageManager = new MessageManager();
			messageManager.start();
		
			while(true)
			{
				Long newClientSessionID = generateNewClientSessionID();
				ConnectionServer connectionToClient = new ConnectionServer(serverSocket.accept(), newClientSessionID);
				System.out.println("New connection is open");       			
				connectionToClient.start();
				connectionsToClients.put(newClientSessionID, connectionToClient);
				//connectionsToClients.add(newClientSessionID, connectionToClient);
        	}
        }
		catch(IOException e)
		{
			e.printStackTrace();
		}

	}
	
	public Long generateNewClientSessionID()
	{
		return rand.nextLong();
	}
	
	public static boolean queueIsNotEmpty()
	{
		return !messageQueue.isEmpty();
	}
	
	public static Message getHeadMessage()
	{
		return messageQueue.poll();
	}
	
	public static void addMessageToQueue(Message message)
	{
		messageQueue.offer(message);
	}
	
	public static Map<Long, ConnectionServer> getConnectionsToClients()
	{
		return connectionsToClients;
	}
}
