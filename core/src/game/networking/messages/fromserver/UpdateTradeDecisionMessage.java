package networking.messages.fromserver;

import entities.Item;
import networking.MessageType;
import networking.messages.Message;

public class UpdateTradeDecisionMessage extends Message
{
	private static final long serialVersionUID = -570744921751856358L;

	private boolean offerAccepted;
	
	private String sellerLogin;
	private String buyerLogin;
	private Item sellerItem;
	private Item buyerItem;
	
	public UpdateTradeDecisionMessage(boolean offerAccepted, String sellerLogin, String buyerLogin, Item sellerItem, Item buyerItem) 
	{
		super(MessageType.UPDATE_TRADE_DECISION);
		
		this.offerAccepted = offerAccepted;
		this.sellerLogin = sellerLogin;
		this.buyerLogin = buyerLogin;
		this.sellerItem = sellerItem;
		this.buyerItem = buyerItem;
	}

	public boolean isOfferAccepted() {
		return offerAccepted;
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
