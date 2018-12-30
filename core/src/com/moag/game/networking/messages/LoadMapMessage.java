package com.moag.game.networking.messages;

import com.moag.game.networking.MessageType;

public class LoadMapMessage extends ClientMessage
{
	private static final long serialVersionUID = 152353815402371323L;

	public LoadMapMessage(long sessionId)
	{
		super(MessageType.LOAD_MAP, sessionId);
	}
	
}
