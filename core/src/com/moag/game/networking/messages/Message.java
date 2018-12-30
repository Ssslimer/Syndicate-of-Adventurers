package com.moag.game.networking.messages;

import java.io.Serializable;

import com.moag.game.networking.MessageType;

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
}
