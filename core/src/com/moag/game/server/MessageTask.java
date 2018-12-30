package com.moag.game.server;

import com.moag.game.networking.Message;

/** Wrapper class for processing the clients messages. Links connection with the send request */
public class MessageTask
{
	private final ServerConnection messageOwner;
	private final Message message;
	
	public MessageTask(ServerConnection messageOwner, Message message)
	{
		this.messageOwner = messageOwner;
		this.message = message;
	}

	public ServerConnection getMessageOwner()
	{
		return messageOwner;
	}

	public Message getMessage()
	{
		return message;
	}
}
