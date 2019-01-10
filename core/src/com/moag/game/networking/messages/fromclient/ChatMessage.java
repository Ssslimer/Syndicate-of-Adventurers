package com.moag.game.networking.messages.fromclient;

import com.moag.game.networking.MessageType;
import com.moag.game.networking.messages.Message;

public class ChatMessage extends Message
{
	private static final long serialVersionUID = -7376195247283240087L;

	private String chatMessageString;
	
	public ChatMessage(String chatMessageString) 
	{
		super(MessageType.CHAT_MESSAGE);
		this.chatMessageString = chatMessageString;
	}
	
	public String getMessageString()
	{
		return this.chatMessageString;
	}
}
