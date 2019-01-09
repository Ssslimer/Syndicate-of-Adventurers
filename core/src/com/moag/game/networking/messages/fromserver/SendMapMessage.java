package com.moag.game.networking.messages.fromserver;

import com.moag.game.entities.World;
import com.moag.game.networking.MessageType;
import com.moag.game.networking.messages.Message;

public class SendMapMessage extends Message
{
	private static final long serialVersionUID = -687309963999434123L;

	private final World map;
	
	public SendMapMessage(World map)
	{
		super(MessageType.LOAD_MAP);
		this.map = map;
	}
	
	public World getMap()
	{
		return map;
	}
}