package com.moag.game.networking;

public class PingMessage extends Message
{
	private static final long serialVersionUID = -4705966453759418019L;

	private final long time;
	
	public PingMessage(String login, long sessionId, long time)
	{
		this.login = login;
		this.sessionId = sessionId;
		this.messageType = MessageType.PING;
		this.time = time;
	}
	
	public long getTime()
	{
		return time;
	}
}
