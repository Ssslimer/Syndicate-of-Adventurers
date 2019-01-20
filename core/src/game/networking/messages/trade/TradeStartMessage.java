package networking.messages.trade;

import entities.Item;
import networking.MessageType;
import networking.messages.fromclient.ClientMessage;

public class TradeStartMessage extends ClientMessage
{
	private static final long serialVersionUID = -7364912797737097249L;
	private Item item; // check if this is necessary
	private String login;

	public TradeStartMessage(long sessionId, String login, Item item) 
	{
		super(MessageType.TRADE_START, sessionId);
		this.item = item;	
	}
	
	public Item getItem()
	{
		return this.item;
	}
	
	public String getLogin()
	{
		return this.login;
	}
}
