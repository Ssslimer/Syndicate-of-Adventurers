package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector3;

import entities.EntityEnemy;
import util.Clamp;

class EntityEnemyTest 
{
	private static final int BASE_ENEMY_ATTACK = 5;
	private static final int BASE_ENEMY_DEFENCE = 5;
	private static final int BASE_ENEMY_HP = 50;
	
	@Test
	void entityEnemyConstructurTest() 
	{
		int x1 = 0;
		int y1 = 0;
		int z1 = 0;
		
		int x2 = 10;
		int y2 = 5;
		int z2 = 1;
		
		Vector3 v1 = new Vector3(x1, y1, z1);
		Vector3 v2 = new Vector3(x2, y2, z2);
		
		EntityEnemy e1 = new EntityEnemy(v1);
		EntityEnemy e2 = new EntityEnemy(v2);
		
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
		
		assertTrue(e1.getAttack() == BASE_ENEMY_ATTACK);
		assertTrue(e1.getDefence() == BASE_ENEMY_DEFENCE);
		assertTrue(e1.getHP() == BASE_ENEMY_HP);
	}
	
	@Test
	void entityGetDamage()
	{
		EntityEnemy e = new EntityEnemy(new Vector3(5, 1, 3));
		int damage = 20;
		
		int realDamage = Clamp.clampInt(damage - e.getDefence(), 0, damage);
		e.getDamage(damage);
		
		assertTrue(e.getHP() != BASE_ENEMY_HP);
		assertTrue(e.getHP() == BASE_ENEMY_HP - realDamage);
	}

}
