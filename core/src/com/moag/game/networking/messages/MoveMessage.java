package com.moag.game.networking.messages;

import com.badlogic.gdx.math.Vector3;
import com.moag.game.networking.MessageType;
import com.moag.game.networking.MoveDirection;

public class MoveMessage extends ClientMessage
{
	private static final long serialVersionUID = 1621250670013266868L;
	
	private MoveDirection direction;
	private boolean toStop;
	
	public MoveMessage(long sessionId, MoveDirection direction, boolean toStop)
	{
		super(MessageType.MOVE, sessionId);
		this.direction = direction;
		this.toStop = toStop;
	}
	
	public MoveDirection getDirection()
	{
		return direction;
	}

	public boolean getIfToStop()
	{
		return toStop;
	}
	
}