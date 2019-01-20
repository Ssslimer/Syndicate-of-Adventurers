package trade;

import entities.Item;

public class BuyerOffer extends Offer
{
	private int goldAmount;
	
	public BuyerOffer(String login, int goldAmount) 
	{
		super(login, null);
		this.goldAmount = 0;
	}
	
	public BuyerOffer(String login, Item item) 
	{
		super(login, item);
		this.goldAmount = 0;
	}
	
	public BuyerOffer(String login, Item item, int goldAmount) 
	{
		super(login, item);
		this.goldAmount = goldAmount;
	}
	
	public int getGoldAmount()
	{
		return goldAmount;
	}
	
}
