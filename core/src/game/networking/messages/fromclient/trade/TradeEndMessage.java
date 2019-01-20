package networking.messages.fromclient.trade;

import networking.MessageType;
import networking.messages.fromclient.ClientMessage;

public class TradeEndMessage extends ClientMessage
{
	private static final long serialVersionUID = 8812329717884815272L;

	public TradeEndMessage(long sessionId) 
	{
		super(MessageType.TRADE_END, sessionId);
		// TODO Auto-generated constructor stub
	}
}
