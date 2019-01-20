package networking.messages.fromserver;

import org.apache.commons.lang3.builder.ToStringBuilder;

import entities.EntityPlayer;
import networking.MessageType;
import networking.messages.Message;
import world.World;

public class SendMapMessage extends Message
{
	private static final long serialVersionUID = -687309963999434123L;

	private final World map;
	private EntityPlayer player;
	
	public SendMapMessage(World map, EntityPlayer e)
	{
		super(MessageType.LOAD_MAP);
		this.map = map;
		player = e;
	}
	
	public World getMap()
	{
		return map;
	}
	
	public EntityPlayer getPlayer()
	{
		return player;
	}
	@Override
	public String toString()
	{
		return new ToStringBuilder(this)
			.appendSuper(super.toString())
			.append("map", map)
			.toString();
	}
}