package com.moag.game.networking.messages.fromserver;

import com.badlogic.gdx.math.Vector3;
import com.moag.game.networking.MessageType;
import com.moag.game.networking.messages.Message;

public class UpdateEntityMessage extends Message
{
	private static final long serialVersionUID = 6354465579235220216L;

	private final long entityId;
	private final Vector3 velocity, position;
	
	public UpdateEntityMessage(long entityId, Vector3 velocity, Vector3 position)
	{
		super(MessageType.UPDATE_ENTITY);
		
		this.entityId = entityId;
		this.velocity = velocity;
		this.position = position;
	}

	public Vector3 getVelocity()
	{
		return velocity;
	}

	public Vector3 getPosition()
	{
		return position;
	}

	public long getEntityId()
	{
		return entityId;
	}
}
