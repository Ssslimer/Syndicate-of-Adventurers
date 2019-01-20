package networking.messages.trade;

import entities.Item;
import networking.MessageType;
import networking.messages.fromclient.ClientMessage;
import trade.Deal;

public class TradeOfferMessage extends ClientMessage
{
	private static final long serialVersionUID = -4513990275032853847L;
	private Deal deal;
	
	public TradeOfferMessage(long sessionId, long sellerId, int goldAmount, Item sellingItem) 
	{
		super(MessageType.TRADE_OFFER, sessionId);	
		deal = new Deal(sessionId, sellerId, sellingItem, goldAmount);
	}
	
	public TradeOfferMessage(long sessionId, long sellerId, Item item, Item sellingItem) 
	{
		super(MessageType.TRADE_OFFER, sessionId);
		deal = new Deal(sessionId, sellerId, sellingItem, item);
	}
	
	public TradeOfferMessage(long sessionId, long sellerId, int goldAmount, Item item, Item sellingItem) 
	{
		super(MessageType.TRADE_OFFER, sessionId);
		deal = new Deal(sessionId, sellerId, sellingItem, goldAmount, item);
	}
	
	public Deal getDeal()
	{
		return this.deal;
	}
}
