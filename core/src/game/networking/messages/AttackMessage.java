package networking.messages;

import networking.MessageType;
import networking.messages.fromclient.ClientMessage;

public class AttackMessage extends ClientMessage
{
	private static final long serialVersionUID = -1202842837792095679L;

	public AttackMessage(long sessionId)
	{
		super(MessageType.ATTACK, sessionId);
	}
}
