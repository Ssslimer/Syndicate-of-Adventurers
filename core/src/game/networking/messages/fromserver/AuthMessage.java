package networking.messages.fromserver;

import org.apache.commons.lang3.builder.ToStringBuilder;

import networking.MessageStatus;
import networking.MessageType;
import networking.messages.Message;

public class AuthMessage extends Message
{
	private static final long serialVersionUID = 2431956245233772488L;

	private MessageStatus status;
	private long sessionID;
	
	public AuthMessage(MessageStatus status, long sessionID)
	{
		super(MessageType.FROM_SERVER);

		this.status = status;
		this.sessionID = sessionID;
	}

	public MessageStatus getMessageStatus()
	{
		return status;
	}
	
	public long getSessionID()
	{
		return sessionID;
	}
	
	@Override
	public String toString()
	{
		return new ToStringBuilder(this)
				.appendSuper(super.toString())
				.append("status", status)
				.toString();
	}
}
