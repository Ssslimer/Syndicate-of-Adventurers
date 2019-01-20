package networking.messages.fromserver;

import networking.MessageType;
import networking.messages.Message;

public class DeathEntityMessage extends Message
{
	private static final long serialVersionUID = -4183325479371362469L;

	private final long entityId;
	
	public DeathEntityMessage(long entityId)
	{
		super(MessageType.ENTITY_DEATH);
		this.entityId = entityId;
	}

	public long getEntityId()
	{
		return entityId;
	}
}