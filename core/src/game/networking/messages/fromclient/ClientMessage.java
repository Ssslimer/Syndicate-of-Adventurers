package networking.messages.fromclient;

import org.apache.commons.lang3.builder.ToStringBuilder;

import networking.MessageType;
import networking.messages.Message;

public class ClientMessage extends Message
{
	private static final long serialVersionUID = -8860359814688077896L;

	protected final long sessionId;
	
	public ClientMessage(MessageType messageType, long sessionId)
	{
		super(messageType);
		this.sessionId = sessionId;
	}
	
	public long getSessionId()
	{
		return sessionId;
	}
	
	@Override
	public String toString()
	{
		return new ToStringBuilder(this)
				.appendSuper(super.toString())
				.append("sessionId", sessionId)
				.toString();
	}
}
