package networking.messages.trade;

import entities.Item;
import networking.MessageType;
import networking.messages.fromclient.ClientMessage;
import trade.Offer;

public class TradeDecisionMessage extends ClientMessage
{
	private static final long serialVersionUID = 6718610010753779173L;
	
	private boolean offerAccepted;
	private Offer offer;
	
	
	public TradeDecisionMessage(long sessionId, long otherTraderId, boolean offerAccepted, Item sellingItem, int goldAmount) 
	{
		super(MessageType.TRADE_DECISION, sessionId);
		
		this.offerAccepted = offerAccepted;
		this.offer = new Offer(sessionId, otherTraderId, sellingItem, goldAmount);
	}
	
	public TradeDecisionMessage(long sessionId, long otherTraderId, boolean offerAccepted, Item sellingItem, Item item) 
	{
		super(MessageType.TRADE_DECISION, sessionId);
		
		this.offerAccepted = offerAccepted;
		this.offer = new Offer(sessionId, otherTraderId, sellingItem, item);
	}
	
	public TradeDecisionMessage(long sessionId, long otherTraderId, boolean offerAccepted, Item sellingItem, int goldAmount, Item item) 
	{
		super(MessageType.TRADE_DECISION, sessionId);
		
		this.offerAccepted = offerAccepted;
		this.offer = new Offer(sessionId, otherTraderId, sellingItem, goldAmount, item);
	}
	
	public TradeDecisionMessage(boolean offerAccepted, Offer offer) 
	{
		super(MessageType.TRADE_DECISION, offer.getSellerId());
		
		this.offerAccepted = offerAccepted;
		this.offer = offer;
	}

	public boolean getOfferAccepted()
	{
		return this.offerAccepted;
	}
	
	public Offer getOffer()
	{
		return this.offer;
	}
}
