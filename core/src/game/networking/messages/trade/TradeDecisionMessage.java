package networking.messages.trade;

import entities.Item;
import networking.MessageType;
import networking.messages.fromclient.ClientMessage;

public class TradeDecisionMessage extends ClientMessage
{
	private static final long serialVersionUID = 6718610010753779173L;
	
	private boolean offerAccepted;
	private long otherTraderId;
	private TradeOfferMessage offer;
	private Item sellingItem;
	
	public TradeDecisionMessage(long sessionId, long otherTraderId, boolean offerAccepted, TradeOfferMessage offer, Item sellingItem) 
	{
		super(MessageType.TRADE_DECISION, sessionId);
		
		this.otherTraderId = otherTraderId;
		this.offerAccepted = offerAccepted;
		this.offer = offer;
		this.sellingItem = sellingItem;
	}
	
	public long getOtherTraderId()
	{
		return this.otherTraderId;
	}

	public boolean getOfferAccepted()
	{
		return this.offerAccepted;
	}
	
	public TradeOfferMessage getOffer()
	{
		return offer;
	}
	
	public Item getSellingItem()
	{
		return sellingItem;
	}
}
