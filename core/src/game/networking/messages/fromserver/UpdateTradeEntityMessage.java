package networking.messages.fromserver;

import networking.MessageType;
import networking.messages.Message;
import trade.BuyerOffer;
import trade.Offer;

public class UpdateTradeEntityMessage extends Message
{
	private final long entityId;
	private Offer sellerOffer;
	private BuyerOffer buyerOffer;
	
	public UpdateTradeEntityMessage(long entityId, Offer sellerOffer) 
	{
		super(MessageType.UPDATE_TRADE);
		this.entityId = entityId;
		this.sellerOffer = sellerOffer;
		buyerOffer = null;
	}
	
	public UpdateTradeEntityMessage(long entityId, BuyerOffer buyerOffer) 
	{
		super(MessageType.UPDATE_TRADE);
		this.entityId = entityId;
		this.sellerOffer = null;
		this.buyerOffer = buyerOffer;
	}
	
	public Offer getSellerOffer() {
		return sellerOffer;
	}

	public BuyerOffer getBuyerOffer() {
		return buyerOffer;
	}

	public long getEntityId() {
		return entityId;
	}
	
	

}
