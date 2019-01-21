package networking.messages.fromserver.trade;

import networking.MessageType;
import networking.messages.Message;

public class AuctionUpdateMessage extends Message
{
	private static final long serialVersionUID = 2718717697456312325L;

	private final long auctionID;
	private final String bestOfferer;
	private final int currentPrice;
	
	public AuctionUpdateMessage(long auctionID, String bestOfferer, int currentPrice)
	{
		super(MessageType.AUCTION_UPDATE);
		this.auctionID = auctionID;
		this.bestOfferer = bestOfferer;
		this.currentPrice = currentPrice;
	}

	public long getAuctionID()
	{
		return auctionID;
	}

	public String getBestOfferer()
	{
		return bestOfferer;
	}

	public int getCurrentPrice()
	{
		return currentPrice;
	}
}