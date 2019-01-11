package server;

import java.net.UnknownHostException;

import util.ServerProperties;
import util.ServerPropertiesLoader;

public class MainServer
{
	public static void main(String[] args)
	{
		ServerProperties serverProperties = new ServerProperties(new ServerPropertiesLoader());
				
        Server server = new Server(serverProperties);
        try
		{
			server.start();
		}
		catch(UnknownHostException e)
		{
			System.out.println(e.getMessage());
			server.stop();
			e.printStackTrace();
		}
        
        server.loop();
        server.stop(); 
	}
}
