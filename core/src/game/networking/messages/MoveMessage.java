package networking.messages;

import org.apache.commons.lang3.builder.ToStringBuilder;

import networking.MessageType;
import networking.MoveDirection;
import networking.messages.fromclient.ClientMessage;

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
	
	@Override
	public String toString()
	{
		return new ToStringBuilder(this)
				.appendSuper(super.toString())
				.append("direction", direction)
				.append("toStop", toStop)
				.toString();
	}	
}