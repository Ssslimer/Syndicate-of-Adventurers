package networking.messages.ingame;

import org.apache.commons.lang3.builder.ToStringBuilder;

import networking.MessageType;
import networking.messages.fromclient.ClientMessage;

public class AttackMessage extends ClientMessage
{
	private static final long serialVersionUID = -1202842837792095679L;

	public AttackMessage(long sessionId)
	{
		super(MessageType.ATTACK, sessionId);
	}
	
	@Override
	public String toString()
	{
		return new ToStringBuilder(this).appendSuper(super.toString()).toString();
	}
}
