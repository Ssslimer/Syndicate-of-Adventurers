package networking.messages.fromserver.trade;

import entities.Item;
import networking.MessageType;
import networking.messages.Message;

public class UpdateTradeEndMessage extends Message
{
	private static final long serialVersionUID = -8822996915555269535L;
	
	private String login;
	private Item item;

	public UpdateTradeEndMessage(String login, Item item) 
	{
		super(MessageType.UPDATE_TRADE_END);
		
		this.login = login;
		this.item = item;
	}

	public String getLogin() 
	{
		return login;
	}

	public Item getItem() 
	{
		return item;
	}
}
