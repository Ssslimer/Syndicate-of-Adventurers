package networking.messages.fromserver;

import org.apache.commons.lang3.builder.ToStringBuilder;

import networking.MessageStatus;
import networking.MessageType;
import networking.messages.Message;

public class MessageFromServer extends Message
{
	private static final long serialVersionUID = 2431956245233772488L;

	private MessageStatus status;
	
	public MessageFromServer(MessageStatus status)
	{
		super(MessageType.FROM_SERVER);

		this.status = status;
	}

	public MessageStatus getMessageStatus()
	{
		return status;
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