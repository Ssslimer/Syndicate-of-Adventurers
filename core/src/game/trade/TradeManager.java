package trade;

import java.util.LinkedList;
import java.util.List;

import entities.EntityPlayer;
import entities.Item;

public class TradeManager
{
	private List<Auction> auctions = new LinkedList<>();
	
	public void openAuction(EntityPlayer owner, Item item, int minimalPrice)
	{
		Auction auction = new Auction(owner, item, minimalPrice);
		auctions.add(auction);
	}
	
	public synchronized void update()
	{
		for(Auction a : auctions) a.update();
	}

	public List<Auction> getAuctions()
	{
		return auctions;
	}
	
	public synchronized void setAuctions(List<Auction> auctions)
	{
		this.auctions = auctions;
	}
	
	public void resetAuctions()
	{
		auctions.clear();
	}
}
