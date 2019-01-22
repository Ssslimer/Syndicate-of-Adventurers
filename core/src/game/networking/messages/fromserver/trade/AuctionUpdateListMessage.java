package networking.messages.fromserver.trade;

import java.util.List;

import networking.MessageType;
import networking.messages.Message;
import trade.Auction;

public class AuctionUpdateListMessage extends Message
{
	private static final long serialVersionUID = 1921911456829574556L;

	private final List<Auction> auctions;
	
	public AuctionUpdateListMessage(List<Auction> auctions)
	{
		super(MessageType.AUCTION_UPDATE_LIST);
		this.auctions = auctions;
	}

	public List<Auction> getAuctions()
	{
		return auctions;
	}
}