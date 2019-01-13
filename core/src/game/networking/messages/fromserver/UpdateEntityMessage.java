package networking.messages.fromserver;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.badlogic.gdx.math.Vector3;

import networking.MessageType;
import networking.messages.Message;

public class UpdateEntityMessage extends Message
{
	private static final long serialVersionUID = 7059769221328707957L;
	
	private final long entityId;
	private final Vector3 velocity, position;
	
	public UpdateEntityMessage(long entityId, Vector3 velocity, Vector3 position)
	{
		super(MessageType.UPDATE_ENTITY);
		
		this.entityId = entityId;
		this.velocity = velocity;
		this.position = position;
		System.out.println("Constructor: " + position);
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

	@Override
	public String toString()
	{
		return new ToStringBuilder(this)
			.appendSuper(super.toString())
			.append("entityId", entityId)
			.append("velocity", velocity)
			.append("position", position)
			.toString();
	}
}
