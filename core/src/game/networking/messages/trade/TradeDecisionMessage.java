package networking.messages.trade;

import entities.Item;
import networking.MessageType;
import networking.messages.fromclient.ClientMessage;
import trade.Deal;

public class TradeDecisionMessage extends ClientMessage
{
	private static final long serialVersionUID = 6718610010753779173L;
	
	private boolean offerAccepted;
	private Deal deal;
	
	
	public TradeDecisionMessage(long sessionId, long otherTraderId, boolean offerAccepted, Item sellingItem, int goldAmount) 
	{
		super(MessageType.TRADE_DECISION, sessionId);
		
		this.offerAccepted = offerAccepted;
		this.deal = new Deal(sessionId, otherTraderId, sellingItem, goldAmount);
	}
	
	public TradeDecisionMessage(long sessionId, long otherTraderId, boolean offerAccepted, Item sellingItem, Item item) 
	{
		super(MessageType.TRADE_DECISION, sessionId);
		
		this.offerAccepted = offerAccepted;
		this.deal = new Deal(sessionId, otherTraderId, sellingItem, item);
	}
	
	public TradeDecisionMessage(long sessionId, long otherTraderId, boolean offerAccepted, Item sellingItem, int goldAmount, Item item) 
	{
		super(MessageType.TRADE_DECISION, sessionId);
		
		this.offerAccepted = offerAccepted;
		this.deal = new Deal(sessionId, otherTraderId, sellingItem, goldAmount, item);
	}
	
	public TradeDecisionMessage(boolean offerAccepted, Deal offer) 
	{
		super(MessageType.TRADE_DECISION, offer.getSellerOffer().getTraderId());
		
		this.offerAccepted = offerAccepted;
		this.deal = offer;
	}

	public boolean getOfferAccepted()
	{
		return this.offerAccepted;
	}
	
	public Deal getDeal()
	{
		return this.deal;
	}
}
