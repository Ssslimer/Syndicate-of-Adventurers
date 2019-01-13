package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.SSLServerSocketFactory;

public class ConnectionManager extends Thread
{
	private final Server server;
	private final InetAddress address;
	private final int port;
	
	private List<ConnectionToClient> connections = new LinkedList<>();

	public ConnectionManager(Server server, String ip, int port) throws UnknownHostException
	{
		this.server = server;
		this.port = port;
		this.address = InetAddress.getByName(ip);
		
		System.setProperty("javax.net.ssl.keyStore", "za.store");
		System.setProperty("javax.net.ssl.keyStorePassword", "qazwsx123");
	}

	@Override
	public void run()
	{
		MessageHandler messageHander = new MessageHandler(server);
		messageHander.start();
		
		SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		try(ServerSocket serverSocket = sslserversocketfactory.createServerSocket(port, 0, address))
		{
			System.out.println("Server was setup successfully");
			System.out.println("ip: " + serverSocket.getInetAddress().getHostAddress());
			System.out.println("port: " + serverSocket.getLocalPort());
			
			while(true)
			{
				ConnectionToClient connectionToClient = new ConnectionToClient(this, serverSocket.accept(), messageHander);			     			
				connectionToClient.start();
				connections.add(connectionToClient);
        	}
        }
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public List<ConnectionToClient> getAllConnections()
	{
		return connections;
	}
	
	void removeConnection(ConnectionToClient connection)
	{
		synchronized(connections)
		{
			connections.remove(connection);
		}
	}
}
