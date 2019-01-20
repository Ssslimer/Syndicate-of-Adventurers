package networking.messages.trade;

import entities.Item;
import networking.MessageType;
import networking.messages.fromclient.ClientMessage;

public class TradeDecisionMessage extends ClientMessage
{
	private static final long serialVersionUID = 6718610010753779173L;
	
	private boolean offerAccepted;
	
	private String sellerLogin;
	private String buyerLogin;
	private Item sellerItem;
	private Item buyerItem;
	
	public TradeDecisionMessage(long sessionId, boolean offerAccepted, String sellerLogin, String buyerLogin, Item buyerItem, Item sellerItem) 
	{
		super(MessageType.TRADE_DECISION, sessionId);
		
		this.offerAccepted = offerAccepted;
		
		this.sellerLogin = sellerLogin;
		this.buyerLogin = buyerLogin;
		this.sellerItem = sellerItem;
		this.buyerItem = buyerItem;
	}
	

	public boolean getOfferAccepted()
	{
		return this.offerAccepted;
	}


	public String getSellerLogin() {
		return sellerLogin;
	}


	public String getBuyerLogin() {
		return buyerLogin;
	}


	public Item getSellerItem() {
		return sellerItem;
	}


	public Item getBuyerItem() {
		return buyerItem;
	}
	
	
	
	
}
