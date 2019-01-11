package networking.messages.fromclient;

import org.apache.commons.lang3.builder.ToStringBuilder;

import networking.MessageType;
import networking.messages.Message;

public class ChatMessage extends Message
{
	private static final long serialVersionUID = -7376195247283240087L;

	private String text;
	
	public ChatMessage(String chatMessageString) 
	{
		super(MessageType.CHAT_MESSAGE);
		this.text = chatMessageString;
	}
	
	public String getMessageString()
	{
		return this.text;
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
