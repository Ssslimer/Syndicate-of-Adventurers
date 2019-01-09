package com.moag.game.networking.messages;

import com.badlogic.gdx.math.Vector3;
import com.moag.game.networking.MessageType;
import com.moag.game.networking.messages.fromclient.ClientMessage;

public class MoveMessage extends ClientMessage
{
	private static final long serialVersionUID = 1621250670013266868L;
	
	private Vector3 translation;
	
	public MoveMessage(Vector3 translation, long sessionId)
	{
		super(MessageType.MOVE, sessionId);
		this.translation = translation;
	}

	public Vector3 getTranslation()
	{
		return translation;
	}
}