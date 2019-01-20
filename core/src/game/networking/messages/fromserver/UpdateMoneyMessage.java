package networking.messages.fromserver;

import networking.MessageType;
import networking.messages.Message;

public class UpdateMoneyMessage extends Message
{
	private static final long serialVersionUID = -9076138802334712920L;

	private final int money;
	
	public UpdateMoneyMessage(int money)
	{
		super(MessageType. UPDATE_MONEY);
		this.money = money;
	}

	public int getMoney()
	{
		return money;
	}
}