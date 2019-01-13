package networking.messages.fromserver;

import entities.Entity;
import networking.MessageType;
import networking.messages.Message;

public class SpawnEntityMessage extends Message
{
	private static final long serialVersionUID = 7119995592023265484L;

	private final Entity entity;
	
	public SpawnEntityMessage(Entity entity)
	{
		super(MessageType.SPAWN_ENTITY);
		this.entity = entity;
	}

	public Entity getEntity()
	{
		return entity;
	}
}