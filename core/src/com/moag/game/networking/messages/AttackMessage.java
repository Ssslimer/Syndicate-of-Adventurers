package com.moag.game.networking.messages;

import com.moag.game.networking.MessageType;

public class AttackMessage extends ClientMessage
{
	private static final long serialVersionUID = -1202842837792095679L;

	public AttackMessage(long sessionId)
	{
		super(MessageType.ATTACK, sessionId);
	}
}
