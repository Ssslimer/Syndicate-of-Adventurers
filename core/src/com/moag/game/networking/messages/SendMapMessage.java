package com.moag.game.networking.messages;

import com.moag.game.entities.Map;
import com.moag.game.networking.MessageType;

public class SendMapMessage extends Message
{
	private static final long serialVersionUID = -687309963999434123L;

	private final Map map;
	
	public SendMapMessage(Map map)
	{
		super(MessageType.LOAD_MAP);
		this.map = map;
	}
	
	public Map getMap()
	{
		return map;
	}
}