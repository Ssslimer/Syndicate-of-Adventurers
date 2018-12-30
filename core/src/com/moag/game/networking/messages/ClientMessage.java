package com.moag.game.networking.messages;

import com.moag.game.networking.MessageType;

public class ClientMessage extends Message
{
	private static final long serialVersionUID = -8860359814688077896L;

	protected long sessionId;
	
	public ClientMessage(MessageType messageType, long sessionId)
	{
		super(messageType);
		this.sessionId = sessionId;
	}
	
	public long getSessionId()
	{
		return sessionId;
	}
}
