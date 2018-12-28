package com.moag.game.networking;

import java.io.Serializable;

import com.moag.game.networking.NetworkingEnums.MessageContent;

public class Message implements Serializable
{
	private static final long serialVersionUID = -5885736425999483646L;
	
	private final MessageContent content;
	private final Object data;	

	public Message(MessageContent activity, Object data)
	{
		this.content = activity;
		this.data = data;
	}

	public MessageContent getActivity()
	{
		return content;
	}
	
	public Object getData()
	{
		return data;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Activity: ");
		builder.append(content.toString());
		builder.append(" Data: ");
		builder.append(data.toString());
		
		return builder.toString();
	}
}
