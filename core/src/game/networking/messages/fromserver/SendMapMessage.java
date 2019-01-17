package networking.messages.fromserver;

import org.apache.commons.lang3.builder.ToStringBuilder;

import networking.MessageType;
import networking.messages.Message;
import world.World;

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

	@Override
	public String toString()
	{
		return new ToStringBuilder(this)
			.appendSuper(super.toString())
			.append("map", map)
			.toString();
	}
}