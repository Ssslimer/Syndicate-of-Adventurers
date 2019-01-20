package networking.messages.fromclient.trade;

import entities.Item;
import networking.MessageType;
import networking.messages.fromclient.ClientMessage;
import trade.Deal;

public class TradeOfferMessage extends ClientMessage
{
	private static final long serialVersionUID = -4513990275032853847L;
	
	private String sellerLogin;
	private String buyerLogin;
	private Item buyerItem;
	private Item sellerItem;
	
	public TradeOfferMessage(long sessionId, String sellerLogin, String buyerLogin, Item buyerItem, Item sellerItem) 
	{
		super(MessageType.TRADE_OFFER, sessionId);
		
		this.sellerLogin = sellerLogin;
		this.buyerLogin = buyerLogin;
		this.sellerItem = sellerItem;
		this.buyerItem = buyerItem;
	}

	public String getSellerLogin() {
		return sellerLogin;
	}

	public String getBuyerLogin() {
		return buyerLogin;
	}

	public Item getBuyerItem() {
		return buyerItem;
	}

	public Item getSellerItem() {
		return sellerItem;
	}
	
	
}
