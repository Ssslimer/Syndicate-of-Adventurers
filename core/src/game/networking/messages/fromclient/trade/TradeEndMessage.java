package networking.messages.fromclient.trade;

import entities.Item;
import networking.MessageType;
import networking.messages.fromclient.ClientMessage;

public class TradeEndMessage extends ClientMessage
{
	private static final long serialVersionUID = 8812329717884815272L;

	private final String login;
	private final Item item;
	
	public TradeEndMessage(long sessionId, String login, Item item) 
	{
		super(MessageType.TRADE_END, sessionId);
		
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