package entity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.Test;

import entities.Item;
import entities.ItemType;

public class ItemTest 
{
	Random rand = new Random();
	
	@Test
	public void test() 
	{
		int atk = rand.nextInt(50);
		int def = rand.nextInt(50);
		int hp = rand.nextInt(50);
		
		Item item = new Item(atk, def, hp, ItemType.ARMOR);
		
		assertNotNull(item);
		assertTrue(item.getAttack() == atk);
		assertTrue(item.getDefence() == def);
		assertTrue(item.getHPBonus() == hp);
	}
	
	@Test
	public void itemTypesTest() 
	{
		int atk = rand.nextInt(50);
		int def = rand.nextInt(50);
		int hp = rand.nextInt(50);
		
		ItemType t1 = ItemType.SWORD;
		ItemType t2 = ItemType.SHIELD;
		ItemType t3 = ItemType.ARMOR;
		ItemType t4 = ItemType.HELMET;
		ItemType t5 = ItemType.BOOTS;
		ItemType t6 = ItemType.GRIMOIRE;
		
		Item i1 = new Item(atk, def, hp, t1);
		Item i2 = new Item(atk, def, hp, t2);
		Item i3 = new Item(atk, def, hp, t3);
		Item i4 = new Item(atk, def, hp, t4);
		Item i5 = new Item(atk, def, hp, t5);
		Item i6 = new Item(atk, def, hp, t6);
		
		assertTrue(i1.getType() == t1);
		assertTrue(i2.getType() == t2);
		assertTrue(i3.getType() == t3);
		assertTrue(i4.getType() == t4);
		assertTrue(i5.getType() == t5);
		assertTrue(i6.getType() == t6);	
	}
	

}
