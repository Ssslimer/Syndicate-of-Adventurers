package entities;

import java.util.Random;

public class Loot 
{
	private Random rand;
	
	private int goldLoot;
	private Item item;
	
	public Loot()
	{
		goldLoot = rand.nextInt(50);	
		item = generateItem();
	}
	
	public Loot(int goldLoot, Item item)
	{
		this.goldLoot = goldLoot;
		this.item = item;
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
		if(item == null) return false;
		else return true;
	}
	
	private Item generateItem()
	{
		double itemLootProbability = rand.nextDouble();
		
		if(itemLootProbability < 0.5)
		{
			int attack = rand.nextInt(10);
			int defence = rand.nextInt(10);
			int health = rand.nextInt(10);
			
			Item newItem = new Item(attack, defence, health, ItemType.SWORD);
					
			return newItem;
		}
		else return null;
	}
}
