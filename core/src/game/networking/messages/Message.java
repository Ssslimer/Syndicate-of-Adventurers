package networking.messages;

import java.io.Serializable;

import networking.MessageType;

public abstract class Message implements Serializable
{
	private static final long serialVersionUID = -5885736425999483646L;
	
	protected MessageType messageType;
	
	public Message(MessageType messageType)
	{
		this.messageType = messageType;
	}
	
	public MessageType getMessageType()
	{
		return messageType;
	}
	
//	@Override
//	public abstract String toString();
}
