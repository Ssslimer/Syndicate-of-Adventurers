package entities;

import java.util.Random;

import com.badlogic.gdx.math.Vector3;

import networking.MoveDirection;
import server.Server;

public class EntityEnemy extends Entity
{
	private static final long serialVersionUID = -8463316271178712082L;
	
	private static final int BASE_ENEMY_ATTACK = 5;
	private static final int BASE_ENEMY_DEFENCE = 5;
	private static final int BASE_ENEMY_HP = 50;
	
	private Random rand;
	
	private int HP;
	private int attackPower;
	private int defencePower;
	
	public EntityEnemy(Vector3 position)
	{
		super(position);
		
		rand = new Random();
		
		HP = BASE_ENEMY_HP;
		attackPower = BASE_ENEMY_ATTACK;
		defencePower = BASE_ENEMY_DEFENCE;		
	}
	
	@Override
	public void update(float delta)
	{
		double attackProbability = rand.nextDouble();
		if(attackProbability <= 0.5d) attack();	
	}
	
	public int getHP()
	{
		return HP;
	}
	
	public int getAttack()
	{
		return attackPower;
	}
	
	public int getDefence()
	{
		return defencePower;
	}

	public void getDamage(int damageAttack)
	{
		damageAttack -= defencePower;
		
		if(damageAttack > 0)
		{
			HP -= damageAttack;
		}
	}
	
	private void attack()
	{
		//Server.getMap().attackIfPlayerInFront(attackPower, position, moveDirection);
	}
	
}
