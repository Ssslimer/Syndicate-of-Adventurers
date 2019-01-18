package networking.messages.trade;

import entities.Item;
import entities.Offer;
import networking.MessageType;
import networking.messages.fromclient.ClientMessage;

public class TradeOfferMessage extends ClientMessage
{
	private static final long serialVersionUID = -4513990275032853847L;
	private Offer offer;
	
	public TradeOfferMessage(long sessionId, long sellerId, int goldAmount, Item sellingItem) 
	{
		super(MessageType.TRADE_OFFER, sessionId);	
		offer = new Offer(sessionId, sellerId, sellingItem, goldAmount);
	}
	
	public TradeOfferMessage(long sessionId, long sellerId, Item item, Item sellingItem) 
	{
		super(MessageType.TRADE_OFFER, sessionId);
		offer = new Offer(sessionId, sellerId, sellingItem, item);
	}
	
	public TradeOfferMessage(long sessionId, long sellerId, int goldAmount, Item item, Item sellingItem) 
	{
		super(MessageType.TRADE_OFFER, sessionId);
		offer = new Offer(sessionId, sellerId, sellingItem, goldAmount, item);
	}
	
	public Offer getOffer()
	{
		return this.offer;
	}
}
