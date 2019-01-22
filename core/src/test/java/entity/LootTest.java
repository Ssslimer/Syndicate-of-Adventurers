package entity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.Test;

import entities.Item;
import entities.ItemType;
import entities.Loot;

public class LootTest 
{
	Random rand = new Random();
	
	@Test
	public void test() 
	{
		int itemAtk = 0;
		int itemDef = 0;
		int itemHp = 0;
		
		int goldAmount = 0;
		Item item = new Item(itemAtk, itemDef, itemHp, ItemType.SWORD);
		
		Loot loot1 = new Loot();
		Loot loot2 = new Loot(goldAmount, item);
		
		assertNotNull(loot1);
		assertNotNull(loot2);
		
		assertNotEquals(loot1, loot2);
		assertNotEquals(loot1.getItem(), loot2.getItem());
		
		assertNotNull(loot2.getItem());
	}
	
	@Test
	public void itemAndGoldTest()
	{
		int itemAtk = rand.nextInt(30);
		int itemDef = rand.nextInt(23);
		int itemHp = rand.nextInt(15);
		
		int goldAmount = rand.nextInt(25);
		Item item = new Item(itemAtk, itemDef, itemHp, ItemType.SWORD);
		
		Loot loot = new Loot(goldAmount, item);
		
		assertEquals(loot.getItem(), item);
		assertTrue(loot.getItem().getAttack() == itemAtk);
		assertTrue(loot.getItem().getDefence() == itemDef);
		assertTrue(loot.getItem().getHPBonus() == itemHp);
		assertTrue(loot.getGold() == goldAmount);
		assertTrue(loot.hasItem());
	}

}
