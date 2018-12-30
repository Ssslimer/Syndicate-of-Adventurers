package com.moag.game.networking;

import java.io.Serializable;

public abstract class Message implements Serializable
{
	private static final long serialVersionUID = -5885736425999483646L;
	
	protected MessageType messageType;

	protected String login;
	protected long sessionId;

	public MessageType getMessageType()
	{
		return messageType;
	}
	
	public String getClientLogin()
	{
		return login;
	}
	
	public long getSessionId()
	{
		return sessionId;
	}
	
	@Override
	public String toString()
	{		
		return messageType.toString();
	}
}
