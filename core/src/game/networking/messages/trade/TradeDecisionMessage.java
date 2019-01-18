package networking.messages.trade;

import networking.MessageType;
import networking.messages.fromclient.ClientMessage;

public class TradeDecisionMessage extends ClientMessage
{
	private static final long serialVersionUID = 6718610010753779173L;
	
	private boolean offerAccepted;
	private long otherTraderId;
	
	public TradeDecisionMessage(long sessionId, long otherTraderId, boolean offerAccepted) 
	{
		super(MessageType.TRADE_DECISION, sessionId);
		
		this.otherTraderId = otherTraderId;
		this.offerAccepted = offerAccepted;
	}
	
	public long getOtherTraderId()
	{
		return this.otherTraderId;
	}

	public boolean getOfferAccepted()
	{
		return this.offerAccepted;
	}
}
