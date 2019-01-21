package networking.messages.fromclient.trade;

import networking.MessageType;
import networking.messages.fromclient.ClientMessage;

public class AuctionOfferPriceMessage extends ClientMessage
{
	private static final long serialVersionUID = 7258156904743397899L;

	private final long auctionId;
	private final int price;
	
	public AuctionOfferPriceMessage(long sessionId, long auctionId, int price)
	{
		super(MessageType.AUCTION_OFFER_PRICE, sessionId);
		this.auctionId = auctionId;
		this.price = price;
	}

	public long getAuctionId()
	{
		return auctionId;
	}

	public int getPrice()
	{
		return price;
	}
}