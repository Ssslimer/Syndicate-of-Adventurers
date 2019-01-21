package networking.messages.fromclient.trade;

import entities.Item;
import networking.MessageType;
import networking.messages.fromclient.ClientMessage;

public class AuctionOpenMessage extends ClientMessage
{
	private static final long serialVersionUID = -270147337578781161L;

	private final Item item;
	private final int minimalPrice;
	
	public AuctionOpenMessage(long sessionId, Item item, int minimalPrice)
	{
		super(MessageType.AUCTION_OPEN, sessionId);
		this.item = item;
		this.minimalPrice = minimalPrice;
	}

	public int getMinimalPrice()
	{
		return minimalPrice;
	}

	public Item getItem()
	{
		return item;
	}
}