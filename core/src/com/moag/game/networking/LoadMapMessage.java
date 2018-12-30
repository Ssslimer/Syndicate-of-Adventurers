package com.moag.game.networking;

public class LoadMapMessage extends Message
{
	private static final long serialVersionUID = 152353815402371323L;

	public LoadMapMessage(String login, long sessionId)
	{
		this.login = login;
		this.sessionId = sessionId;
		this.messageType = MessageType.LOAD_MAP;
	}
	
}
