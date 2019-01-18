package entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector3;

import server.Server;

public class EntityEnemy extends Entity implements Damageable
{
	private static final long serialVersionUID = -8463316271178712082L;
	
	private static final int BASE_ENEMY_ATTACK = 5;
	private static final int BASE_ENEMY_DEFENCE = 5;
	private static final int BASE_ENEMY_HP = 50;
	
	private float speed = 5f;
	private int HP;
	private int attackPower;
	private int defencePower;
	
	private Loot loot;
	
	private static final int FOLLOW_RANGE = 20;

	public EntityEnemy(Vector3 position)
	{
		super(position);
		
		HP = BASE_ENEMY_HP;
		attackPower = BASE_ENEMY_ATTACK;
		defencePower = BASE_ENEMY_DEFENCE;		
		
		loot = new Loot();
	}
	
	@Override
	public void update(float delta)
	{
		changeTarget();
		move(delta);
		
		double attackProbability = Server.random.nextDouble();
		if(attackProbability <= 0.5d) attack();	
	}
	
	private void changeTarget()
	{
		EntityPlayer player = getClosestPlayerInRange();
		
		if(player != null)
		{
			moveDirection = position.sub(player.getPosition()).nor(); /** TODO check if this is correct */
		}
		else
		{
			moveDirection = new Vector3(Server.random.nextFloat(), 0, Server.random.nextFloat());
		}
	}
	
	private void move(float delta)
	{	
		position.add(moveDirection.cpy().scl(speed / delta));	
	}
	
	private List<EntityPlayer> findPlayersInRange() 
	{	
		List<EntityPlayer> playersInRange = new ArrayList<>();
		
		for(EntityPlayer player : Server.getMap().getPlayers().values())
		{
			if(player.getPosition().sub(getPosition()).len() <= FOLLOW_RANGE)
			{
				playersInRange.add(player);
			}
		}
		
		return playersInRange;
	}
	
	private EntityPlayer getClosestPlayerInRange()
	{
		EntityPlayer closest = null;
		float smallestDistance = Float.MAX_VALUE;
		
		for(EntityPlayer player : Server.getMap().getPlayers().values())
		{
			float distance = player.getPosition().dst(getPosition());
			if(distance<= FOLLOW_RANGE && distance < smallestDistance)
			{
				closest = player;
				smallestDistance = distance;
			}
		}
		
		return closest;
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

	public void dealDamage(int damageAttack)
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
	
	public Loot die()
	{
		alive = false;
		return loot;
	}
}
