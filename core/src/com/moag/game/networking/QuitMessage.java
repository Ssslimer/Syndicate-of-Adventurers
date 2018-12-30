package com.moag.game.networking;

public class QuitMessage extends Message
{
	private static final long serialVersionUID = -7549316832854835104L;
	
	public QuitMessage(String clientID, long sessionID)
	{
		this.messageType = MessageType.QUIT;
		this.login = clientID;
		this.sessionId = sessionID;
	}

}
