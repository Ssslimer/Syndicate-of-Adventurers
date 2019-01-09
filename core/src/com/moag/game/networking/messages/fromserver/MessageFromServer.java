package com.moag.game.networking.messages.fromserver;

import com.moag.game.networking.MessageStatus;
import com.moag.game.networking.MessageType;
import com.moag.game.networking.messages.Message;

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
		return "MessageFromServer: " + status.toString();
	}
}
