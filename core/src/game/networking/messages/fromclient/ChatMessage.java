package networking.messages.fromclient;

import org.apache.commons.lang3.builder.ToStringBuilder;

import networking.MessageType;

public class ChatMessage extends ClientMessage
{
	private static final long serialVersionUID = -7376195247283240087L;

	private final String text;
	
	public ChatMessage(long sessionId, String chatMessageString) 
	{
		super(MessageType.CHAT_MESSAGE, sessionId);
		this.text = chatMessageString;
	}
	
	public String getText()
	{
		return text;
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this)
				.appendSuper(super.toString())
				.append("text", text)
				.toString();
	}
}
