package server;

public class ServerProperties 
{
	private final String ip;
	private final int port;
	private final int tps;
	
	public ServerProperties()
	{	
		this.ip = "127.0.0.1";
		this.port = 4444;
		this.tps = 50;
	}
	
	public ServerProperties(String ip, int port, int tps)
	{	
		this.ip = ip;
		this.port = port;
		this.tps = tps;
	}
	
	public String getIP()
	{	
		return ip;
	}
	
	public int getPortNumber()
	{
		return port;
	}
	
	public int getTPS()
	{
		return tps;
	}
}