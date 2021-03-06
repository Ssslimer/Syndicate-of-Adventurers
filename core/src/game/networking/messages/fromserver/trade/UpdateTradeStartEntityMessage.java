package networking.messages.fromserver.trade;

import entities.Item;
import networking.MessageType;
import networking.messages.Message;

public class UpdateTradeStartEntityMessage extends Message
{
	private static final long serialVersionUID = -3663697575718857975L;
	private final String login;
	private final Item item;

	public UpdateTradeStartEntityMessage(String login, Item item) 
	{
		super(MessageType.UPDATE_TRADE_START);
		this.login = login;		
		this.item = item;
	}
	
	public String getLogin()
	{
		return login;
	}
	
	public Item getItem()
	{
		return this.item;
	}
}