package networking.messages.trade;

import networking.MessageType;
import networking.messages.fromclient.ClientMessage;

public class TradeDecisionMessage extends ClientMessage
{
	private static final long serialVersionUID = 6718610010753779173L;
	private boolean offerAccepted;
	
	public TradeDecisionMessage(long sessionId, boolean offerAccepted) 
	{
		super(MessageType.TRADE_DECISION, sessionId);
		this.offerAccepted = offerAccepted;
	}

	public boolean getOfferAccepted()
	{
		return this.offerAccepted;
	}
}
