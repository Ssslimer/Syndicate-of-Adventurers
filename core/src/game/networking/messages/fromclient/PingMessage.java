package networking.messages.fromclient;

import org.apache.commons.lang3.builder.ToStringBuilder;

import networking.MessageType;

public class PingMessage extends ClientMessage
{
	private static final long serialVersionUID = -4705966453759418019L;

	private final long time;
	
	public PingMessage(long sessionId, long time)
	{
		super(MessageType.PING, sessionId);
		this.time = time;
	}
	
	public long getTime()
	{
		return time;
	}
	
	@Override
	public String toString()
	{
		return new ToStringBuilder(this)
				.appendSuper(super.toString())
				.append("time", time)
				.toString();
	}
}