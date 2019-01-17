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
	
	private float speed = 5f;
	private int HP;
	private int attackPower;
	private int defencePower;
	
	private Loot loot;
	
	public EntityEnemy(Vector3 position)
	{
		super(position);
		
		rand = new Random();
		
		HP = BASE_ENEMY_HP;
		attackPower = BASE_ENEMY_ATTACK;
		defencePower = BASE_ENEMY_DEFENCE;		
		
		loot = new Loot();
	}
	
	@Override
	public void update(float delta)
	{
		move(delta);
		
		double attackProbability = rand.nextDouble();
		if(attackProbability <= 0.5d) attack();	
	}
	
	private void move(float delta)
	{
		Vector3 closestPlayer = Server.getMap().getClosestPlayerPosition(this.position);
		
		if(closestPlayer != null)
		{
			this.moveDirection = this.position.sub(closestPlayer).nor(); // check if this is correct
			
			position.add(moveDirection.cpy().scl(speed / delta));
		}
		else
		{
			float x = rand.nextFloat();
			float y = 0; // y has to be 0 so that enemy is not flying
			float z = rand.nextFloat();
			
			Vector3 newDirection = new Vector3(x, y, z);
			
			position.add(moveDirection.cpy().scl(speed / delta));
			
		}
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
	
	public Loot getLoot()
	{
		return loot;
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
