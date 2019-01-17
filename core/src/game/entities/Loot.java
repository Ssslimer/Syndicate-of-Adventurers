package entities;

import server.Server;

public class Loot 
{
	private final int goldLoot;
	private final Item item;
	
	/** TODO remove random loot to generator */
	public Loot()
	{
		goldLoot = Server.random.nextInt(50);	
		item = generateItem();
	}
	
	public Loot(int goldLoot, Item item)
	{
		this.goldLoot = goldLoot;
		this.item = item;
	}
	
	private Item generateItem()
	{
		double itemLootProbability = Server.random.nextDouble();
		
		if(itemLootProbability < 0.5)
		{
			int attack = Server.random.nextInt(10);
			int defence = Server.random.nextInt(10);
			int health = Server.random.nextInt(10);

			return new Item(attack, defence, health, ItemType.SWORD);
		}
		
		return null;
	}
	
	public int getGold()
	{
		return goldLoot;
	}
	
	public Item getItem()
	{
		return item;
	}
	
	public boolean hasItem()
	{
		return item != null;
	}
}