package trade;

import entities.Item;

public class BuyerOffer extends Offer
{
	private static final long serialVersionUID = -3741444199214577822L;
	private final int goldAmount;
	
	public BuyerOffer(String login, int goldAmount) 
	{
		super(login, null);
		this.goldAmount = goldAmount;
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