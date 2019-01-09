package com.moag.game.networking.messages.fromclient;

import com.moag.game.networking.MessageType;
import com.moag.game.networking.messages.Message;

public class ClientMessage extends Message
{
	private static final long serialVersionUID = -8860359814688077896L;

	protected final long sessionId;
	
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
