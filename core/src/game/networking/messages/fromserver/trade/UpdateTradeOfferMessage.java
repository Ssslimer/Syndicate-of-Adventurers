package networking.messages.fromserver.trade;

import entities.Item;
import networking.MessageType;
import networking.messages.Message;

public class UpdateTradeOfferMessage extends Message
{
	private static final long serialVersionUID = 6266859747843737975L;
	
	private String sellerLogin;
	private String buyerLogin;
	private Item sellerItem;
	private Item buyerItem;

	public UpdateTradeOfferMessage(String sellerLogin, String buyerLogin, Item buyerItem, Item sellerItem) 
	{
		super(MessageType.UPDATE_TRADE_OFFER);
		
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

	public Item getSellerItem() {
		return sellerItem;
	}

	public Item getBuyerItem() {
		return buyerItem;
	}
	
	
}
