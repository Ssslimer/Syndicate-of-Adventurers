package com.moag.game.utils;

public class ServerProperties 
{
	private final String ip;
	private final int port;
	private final int tps;
	
	public ServerProperties(ServerPropertiesLoader loader)
	{	
		this.ip = loader.getIP();
		this.port = loader.getPortNumber();
		this.tps = loader.getTPS();
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
