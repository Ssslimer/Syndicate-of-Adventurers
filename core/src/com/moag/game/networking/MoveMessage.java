package com.moag.game.networking;

import com.badlogic.gdx.math.Vector3;

public class MoveMessage extends Message
{
	private static final long serialVersionUID = 1621250670013266868L;
	
	private Vector3 translation;
	
	public MoveMessage(Vector3 translation, String clientID, long sessionID)
	{
		this.messageType = MessageType.MOVE;
		
		this.clientID = clientID;
		this.sessionID = sessionID;
		
		this.translation = translation;
	}

	public Vector3 getTranslation()
	{
		return translation;
	}
}
