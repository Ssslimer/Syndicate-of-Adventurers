package trade;

import entities.Item;

public class Offer 
{
	private long sellerId;
	private long buyerId;
	
	private Item offeredItem;
	private int offeredGold;
	
	private Item sellingItem;
	
	public Offer(long sellerId, long buyerId, Item sellingItem, int offeredGold)
	{
		this.sellerId = sellerId;
		this.buyerId = buyerId;
		this.offeredGold = offeredGold;
		this.offeredItem = null;
		this.sellingItem = sellingItem;
	}

	public Offer(long sellerId, long buyerId, Item sellingItem, Item item)
	{
		this.sellerId = sellerId;
		this.buyerId = buyerId;
		this.offeredGold = 0;
		this.offeredItem = item;
		this.sellingItem = sellingItem;
	}
	
	public Offer(long sellerId, long buyerId, Item sellingItem, int offeredGold, Item item)
	{
		this.sellerId = sellerId;
		this.buyerId = buyerId;
		this.offeredGold = offeredGold;
		this.offeredItem = item;
		this.sellingItem = sellingItem;
	}
	
	public long getSellerId() 
	{
		return sellerId;
	}

	public long getBuyerId() 
	{
		return buyerId;
	}

	public Item getOfferedItem() 
	{
		return offeredItem;
	}

	public int getOfferedGold() 
	{
		return offeredGold;
	}
	
	public Item getSellingItem()
	{
		return this.sellingItem;
	}
}
