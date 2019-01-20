package networking.messages.fromclient.trade;

import entities.Item;
import networking.MessageType;
import networking.messages.fromclient.ClientMessage;

public class TradeStartMessage extends ClientMessage
{
	private static final long serialVersionUID = -7364912797737097249L;
	private final Item item; // check if this is necessary
	private final String login;

	public TradeStartMessage(long sessionId, String login, Item item) 
	{
		super(MessageType.TRADE_START, sessionId);
		this.item = item;
		this.login = login;
	}
	
	public Item getItem()
	{
		return item;
	}
	
	public String getLogin()
	{
		return login;
	}
}