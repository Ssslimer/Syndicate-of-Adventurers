package networking.messages.fromserver;

import entities.World;
import networking.MessageType;
import networking.messages.Message;

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

//	@Override
//	public String toString()
//	{
//		ToStringBuilder builder = new ToStringBuilder(this);
//		builder.append(map);
//		
//		return builder.build();
//	}
}