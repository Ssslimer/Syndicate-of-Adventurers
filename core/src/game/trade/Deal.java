package trade;

import java.io.Serializable;

import entities.Item;

public class Deal implements Serializable
{
	private static final long serialVersionUID = -6977324891878925183L;
	private final Offer sellerOffer;
	private final BuyerOffer buyerOffer;
	
	public Deal(String sellerLogin, String buyerLogin, Item sellingItem, int offeredGold)
	{
		sellerOffer = new Offer(sellerLogin, sellingItem);
		buyerOffer = new BuyerOffer(buyerLogin, offeredGold);
	}

	public Deal(String sellerLogin, String buyerLogin, Item sellingItem, Item item)
	{
		sellerOffer = new Offer(sellerLogin, sellingItem);
		buyerOffer = new BuyerOffer(buyerLogin, item);
	}
	
	public Deal(String sellerLogin, String buyerLogin, Item sellingItem, int offeredGold, Item item)
	{
		sellerOffer = new Offer(sellerLogin, sellingItem);
		buyerOffer = new BuyerOffer(buyerLogin, item, offeredGold);
	}
	
	public Offer getSellerOffer()
	{
		return this.sellerOffer;
	}
	
	public BuyerOffer getBuyerOffer()
	{
		return this.buyerOffer;
	}
}
