package trade;

import entities.Item;

public class Deal 
{
	private Offer sellerOffer;
	private BuyerOffer buyerOffer;
	
	public Deal(long sellerId, long buyerId, Item sellingItem, int offeredGold)
	{
		sellerOffer = new Offer(sellerId, sellingItem);
		buyerOffer = new BuyerOffer(buyerId, offeredGold);
	}

	public Deal(long sellerId, long buyerId, Item sellingItem, Item item)
	{
		sellerOffer = new Offer(sellerId, sellingItem);
		buyerOffer = new BuyerOffer(buyerId, item);
	}
	
	public Deal(long sellerId, long buyerId, Item sellingItem, int offeredGold, Item item)
	{
		sellerOffer = new Offer(sellerId, sellingItem);
		buyerOffer = new BuyerOffer(buyerId, item, offeredGold);
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
