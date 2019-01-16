package server;

import java.net.UnknownHostException;

public class MainServer
{
	public static void main(String[] args)
	{
        Server server = new Server();
        try
		{
			server.start();
		}
		catch(UnknownHostException e)
		{
			server.stop();
			e.printStackTrace();
		}
        
        server.loop();
        server.stop(); 
	}
}
