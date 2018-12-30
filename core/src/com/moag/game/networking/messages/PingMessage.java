package com.moag.game.networking.messages;

import com.moag.game.networking.MessageType;

public class PingMessage extends ClientMessage
{
	private static final long serialVersionUID = -4705966453759418019L;

	private final long time;
	
	public PingMessage(long sessionId, long time)
	{
		super(MessageType.PING, sessionId);
		this.time = time;
	}
	
	public long getTime()
	{
		return time;
	}
}