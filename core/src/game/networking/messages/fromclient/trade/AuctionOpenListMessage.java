package networking.messages.fromclient.trade;

import networking.MessageType;
import networking.messages.fromclient.ClientMessage;

public class AuctionOpenListMessage extends ClientMessage
{
	private static final long serialVersionUID = -2416388048370096974L;

	public AuctionOpenListMessage(long sessionId)
	{
		super(MessageType.AUCTION_OPEN_LIST, sessionId);
	}
}