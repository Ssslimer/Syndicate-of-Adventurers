package networking.messages.trade;

import entities.Item;
import networking.MessageType;
import networking.messages.fromclient.ClientMessage;
import trade.Deal;

public class TradeOfferMessage extends ClientMessage
{
	private static final long serialVersionUID = -4513990275032853847L;
	private Deal deal;
	
	public TradeOfferMessage(long sessionId, String sellerLogin, String buyerLogin, int goldAmount, Item sellingItem) 
	{
		super(MessageType.TRADE_OFFER, sessionId);	
		deal = new Deal(sellerLogin, buyerLogin, sellingItem, goldAmount);
	}
	
	public TradeOfferMessage(long sessionId, String sellerLogin, String buyerLogin, Item item, Item sellingItem) 
	{
		super(MessageType.TRADE_OFFER, sessionId);
		deal = new Deal(sellerLogin, buyerLogin, sellingItem, item);
	}
	
	public TradeOfferMessage(long sessionId, String sellerLogin, String buyerLogin, int goldAmount, Item item, Item sellingItem) 
	{
		super(MessageType.TRADE_OFFER, sessionId);
		deal = new Deal(sellerLogin, buyerLogin, sellingItem, goldAmount, item);
	}
	
	public Deal getDeal()
	{
		return this.deal;
	}
}
