package com.moag.game.networking;

public class AttackMessage extends Message
{
	private static final long serialVersionUID = -1202842837792095679L;

	public AttackMessage(String clientID, long sessionID)
	{
		this.messageType = MessageType.ATTACK;
		this.clientID = clientID;
		this.sessionID = sessionID;
	}
}
