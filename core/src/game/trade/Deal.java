package trade;

import entities.Item;

public class Deal 
{
	private Offer sellerOffer;
	private BuyerOffer buyerOffer;
	
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
