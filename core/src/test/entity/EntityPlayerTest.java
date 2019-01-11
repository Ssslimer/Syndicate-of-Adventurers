
package test.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector3;
import com.moag.game.entities.EntityPlayer;
import com.moag.game.entities.Item;
import com.moag.game.networking.MoveDirection;
import com.moag.game.util.Clamp;

class EntityPlayerTest 
{
	private static final int BASE_PLAYER_ATTACK = 10;
	private static final int BASE_PLAYER_DEFENCE = 5;
	private static final int BASE_PLAYER_HP = 100;
	
	@Test
	void test() 
	{
		int x1 = 0;
		int y1 = 0;
		int z1 = 0;
		
		int x2 = 10;
		int y2 = 5;
		int z2 = 1;
		
		Vector3 v1 = new Vector3(x1, y1, z1);
		Vector3 v2 = new Vector3(x2, y2, z2);
		
		EntityPlayer e1 = new EntityPlayer(v1);
		EntityPlayer e2 = new EntityPlayer(v2);
		
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
		
		assertTrue(e1.getAttackPower() == BASE_PLAYER_ATTACK);
		assertTrue(e1.getDefencePower() == BASE_PLAYER_DEFENCE);
		assertTrue(e1.getHP() == BASE_PLAYER_HP);
	}
	
	@Test
	void entityPlayerMoveTest()
	{
		EntityPlayer e = new EntityPlayer(new Vector3(5, 5, 5));
		e.move(MoveDirection.UP, false);	
		e.update();
		
		assertTrue(e.getPosition().x == 5);
		assertTrue(e.getPosition().y == 6); // after update it should move in y axis by 1
		assertTrue(e.getPosition().z == 5);
		
		e.move(MoveDirection.RIGHT, false);	
		e.update();
		
		assertTrue(e.getPosition().x == 6); // after update it should move in x axis by 1
		assertTrue(e.getPosition().y == 7); // after update it should move in y axis by 1
		assertTrue(e.getPosition().z == 5);
		
		// stopping movement UP and RIGHT
		e.move(MoveDirection.UP, true);
		e.move(MoveDirection.RIGHT, true);
		e.update();
		
		// we expect that it won't move after update
		assertTrue(e.getPosition().x == 6);
		assertTrue(e.getPosition().y == 7);
		assertTrue(e.getPosition().z == 5);
	}

	@Test
	void getStatsAndDamageTest()
	{
		int itemAttackPower = 25;
		int itemDefencePower = 10;
		int itemHPPower = 3;
		
		int damage = 25;
		
		EntityPlayer e = new EntityPlayer(new Vector3(0, 0, 0));
		Item item = new Item(itemAttackPower, itemDefencePower, itemHPPower);
		
		e.addItem(item);
		
		assertTrue(e.getAttackPower() == BASE_PLAYER_ATTACK + itemAttackPower);
		assertTrue(e.getDefencePower() == BASE_PLAYER_DEFENCE + itemDefencePower);
		assertTrue(e.getHP() == BASE_PLAYER_HP + itemHPPower);
		
		int hpBeforeDamage = e.getHP();
		e.getDamage(damage);
		
		int realDamage = Clamp.clampInt(damage - e.getDefencePower(), 0, damage);
		
		assertTrue(e.getHP() == hpBeforeDamage - realDamage);
		
		e.removeItem(item);
		
		assertTrue(e.getAttackPower() == BASE_PLAYER_ATTACK);
		assertTrue(e.getDefencePower() == BASE_PLAYER_DEFENCE);
		assertTrue(e.getHP() == BASE_PLAYER_HP - realDamage);
	}

}
