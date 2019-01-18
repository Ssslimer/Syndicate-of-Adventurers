package networking.messages;

import entities.Item;
import networking.MessageType;
import networking.messages.fromclient.ClientMessage;

public class TradeOfferMessage extends ClientMessage
{
	private static final long serialVersionUID = -4513990275032853847L;
	
	private int goldAmount;
	private Item item;

	public TradeOfferMessage(long sessionId, int goldAmount) 
	{
		super(MessageType.TRADE_OFFER, sessionId);
		
		this.goldAmount = goldAmount;
		this.item = null;
	}
	
	public TradeOfferMessage(long sessionId, Item item) 
	{
		super(MessageType.TRADE_OFFER, sessionId);
		
		this.goldAmount = 0;
		this.item = item;
	}
	
	public TradeOfferMessage(long sessionId, int goldAmount, Item item) 
	{
		super(MessageType.TRADE_OFFER, sessionId);
		
		this.goldAmount = goldAmount;
		this.item = item;
	}

	public int getGoldAmount()
	{
		return this.goldAmount;
	}
	
	public Item getItem()
	{
		return this.item;
	}
}
