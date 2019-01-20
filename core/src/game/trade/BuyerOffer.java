package trade;

import entities.Item;

public class BuyerOffer extends Offer
{
	private int goldAmount;
	
	public BuyerOffer(long sessionId, int goldAmount) 
	{
		super(sessionId, null);
		this.goldAmount = 0;
	}
	
	public BuyerOffer(long sessionId, Item item) 
	{
		super(sessionId, item);
		this.goldAmount = 0;
	}
	
	public BuyerOffer(long sessionId, Item item, int goldAmount) 
	{
		super(sessionId, item);
		this.goldAmount = goldAmount;
	}
	
	public int getGoldAmount()
	{
		return goldAmount;
	}
	
}
