package networking.messages.fromserver;

import org.apache.commons.lang3.builder.ToStringBuilder;

import networking.MessageType;
import networking.messages.Message;

public class UpdateChatMessage extends Message
{
	private static final long serialVersionUID = 4573116319903517279L;

	private final String text;
	
	public UpdateChatMessage(String text)
	{
		super(MessageType.UPDATE_CHAT);
		this.text = text;
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