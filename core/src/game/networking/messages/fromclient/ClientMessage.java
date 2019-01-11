package networking.messages.fromclient;

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
	
//	@Override
//	public String toString()
//	{
//		ToStringBuilder builder = new ToStringBuilder(this);
//		builder.append(sessionId);
//		
//		return builder.build();
//	}
}
