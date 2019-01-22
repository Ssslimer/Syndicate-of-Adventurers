package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector3;

import entities.EntityPlayer;
import entities.Item;
import entities.ItemType;

public class EntityPlayerTest 
{	
	@Test
	public void test() 
	{
		int x1 = 0;
		int y1 = 0;
		int z1 = 0;
		
		int x2 = 10;
		int y2 = 5;
		int z2 = 1;
		
		Vector3 v1 = new Vector3(x1, y1, z1);
		Vector3 v2 = new Vector3(x2, y2, z2);
		
		EntityPlayer e1 = new EntityPlayer(v1, "test1");
		EntityPlayer e2 = new EntityPlayer(v2, "test2");
		
		assertNotNull(e1);
		assertNotNull(e2);
		
		assertNotEquals(e1, e2);
		
		assertTrue(e1.getPosition().x == x1);
		assertTrue(e1.getPosition().y == y1);
		assertTrue(e1.getPosition().z == z1);
		
		assertTrue(e2.getPosition().x == x2);
		assertTrue(e2.getPosition().y == y2);
		assertTrue(e2.getPosition().z == z2);
		
		e1.setPosition(new Vector3(1,2,3));
		
		assertTrue(e1.getPosition().x == 1);
		assertTrue(e1.getPosition().y == 2);
		assertTrue(e1.getPosition().z == 3);
		
		assertEquals(e2.getPosition(), v2);
	}

	@Test
	public void playerItemsTest()
	{
		int itemAttackPower = 25;
		int itemDefencePower = 10;
		int itemHPPower = 3;
		
		EntityPlayer e = new EntityPlayer(new Vector3(0, 0, 0), "test");
		Item item = new Item(itemAttackPower, itemDefencePower, itemHPPower, ItemType.SWORD);	
		
		int originalAtk = e.getAttack();
		int originalDef = e.getDefence();
		int originalHp = e.getHealth();
		
		e.addItem(item);
		assertTrue(e.getItems().contains(item));
		assertTrue(e.getAttack() == originalAtk + itemAttackPower);
		assertTrue(e.getDefence() == originalDef + itemDefencePower);
		assertTrue(e.getHealth() == originalHp + itemHPPower);
		
		e.removeItem(item);
		assertTrue(!e.getItems().contains(item));
		assertTrue(e.getAttack() == originalAtk);
		assertTrue(e.getDefence() == originalDef);
		assertTrue(e.getHealth() == originalHp);
		
	}

}
