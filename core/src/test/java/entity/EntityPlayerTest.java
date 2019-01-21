package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.UnknownHostException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector3;

import entities.DamageSource;
import entities.EntityPlayer;
import entities.Item;
import entities.ItemType;
import networking.MoveDirection;
import server.Server;
import util.Clamp;

public class EntityPlayerTest 
{
	private static final int BASE_ATTACK = 10;
	private static final int BASE_DEFENCE = 0;
	private static final int BASE_HEALTH = 100;
	
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
		
		e.addItem(item);
		assertTrue(e.getItems().contains(item));
		assertTrue(e.getAttack() == BASE_ATTACK + itemAttackPower);
		assertTrue(e.getDefence() == BASE_DEFENCE + itemDefencePower);
		assertTrue(e.getHealth() == BASE_HEALTH + itemHPPower);
		
		e.removeItem(item);
		assertTrue(!e.getItems().contains(item));
		assertTrue(e.getAttack() == BASE_ATTACK);
		assertTrue(e.getDefence() == BASE_DEFENCE);
		assertTrue(e.getHealth() == BASE_HEALTH);
		
	}

}
