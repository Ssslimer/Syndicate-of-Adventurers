package com.moag.game.networking.messages;

import com.moag.game.networking.MessageType;
import com.moag.game.networking.messages.fromclient.ClientMessage;

public class QuitMessage extends ClientMessage
{
	private static final long serialVersionUID = -7549316832854835104L;
	
	public QuitMessage(long sessionId)
	{
		super(MessageType.QUIT, sessionId);
	}
}