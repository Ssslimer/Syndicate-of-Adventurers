package trade;

import java.util.LinkedList;
import java.util.List;

import entities.EntityPlayer;
import entities.Item;

public class Auction
{
	private static final long lifespan = 50*15; // in tps*seconds
	
	private final EntityPlayer owner;
	private final Item item;
	private final int minimalPrice;
	
	private List<EntityPlayer> participants = new LinkedList<>();
	private EntityPlayer bestParticipant;

	private int currentPrice;
	private long age;
	private boolean isOpen = true;
	
	private static long lastAuctionId;
	private final long auctionId;
	
	public Auction(EntityPlayer owner, Item item, int minimalPrice)
	{
		this.owner = owner;
		this.owner.removeItem(item);
		this.item = item;
		this.minimalPrice = minimalPrice;
		
		synchronized(this)
		{
			auctionId = lastAuctionId;
			lastAuctionId++;
		}
	}

	public List<EntityPlayer> getParticipants()
	{
		return participants;
	}
	
	public EntityPlayer getOwner()
	{
		return owner;
	}

	public int getMinimalPrice()
	{
		return minimalPrice;
	}
	
	public void update()
	{
		if(!isOpen) return;
		
		age++;
		if(age >= lifespan)
		{
			isOpen = false;
			endAuction();
		}
	}
	
	public synchronized void offerPrice(EntityPlayer offerer, int price)
	{
		if(!isOpen) return;
		
		synchronized(offerer)
		{
			if(!offerer.canAfford(price)) return;
				
			if(price >= currentPrice)
			{
				offerer.changeGold(-price);
				synchronized(offerer)
				{
					bestParticipant.changeGold(currentPrice);				
				}
				currentPrice = price;
			}
		}
	}
	
	private synchronized void endAuction()
	{
		synchronized(owner)
		{
			owner.getItems().remove(item);
		}
	}

	public long getAuctionId()
	{
		return auctionId;
	}
}
