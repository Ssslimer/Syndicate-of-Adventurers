package trade;

import entities.Item;

public class Offer 
{
	protected long sessionId;
	protected Item item;
	
	public Offer(long sessionId, Item item)
	{
		this.sessionId = sessionId;
		this.item = item;
	}
	
	public long getTraderId()
	{
		return sessionId;
	}
	
	public Item getTraderItem()
	{
		return this.item;
	}
}
