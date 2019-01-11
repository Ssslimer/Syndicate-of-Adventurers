package networking.messages;

import org.apache.commons.lang3.builder.ToStringBuilder;

import networking.MessageType;
import networking.messages.fromclient.ClientMessage;

public class QuitMessage extends ClientMessage
{
	private static final long serialVersionUID = -7549316832854835104L;
	
	public QuitMessage(long sessionId)
	{
		super(MessageType.QUIT, sessionId);
	}
	
	@Override
	public String toString()
	{
		return new ToStringBuilder(this).appendSuper(super.toString()).toString();
	}
}