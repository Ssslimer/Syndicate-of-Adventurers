package entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector3;

import networking.messages.fromserver.UpdateEntityMessage;
import server.ConnectionToClient;
import server.Server;
import util.Timer;
import world.World;

public class EntityEnemy extends Entity implements Damageable
{
	private static final long serialVersionUID = -8463316271178712082L;
	
	private static final int BASE_ENEMY_ATTACK = 5;
	private static final int BASE_ENEMY_DEFENCE = 5;
	private static final int BASE_ENEMY_HP = 50;
	
	private float walk_speed = 1f;
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
		if(Timer.getTickCount() % 50 == 0) changeTarget();
		move(delta);
		
		//double attackProbability = Server.random.nextDouble();
		//if(attackProbability <= 0.5d) attack();	
	}
	
	private void changeTarget()
	{
		EntityPlayer player = getClosestPlayerInRange();
		
		if(player != null)
		{
			moveDirection = player.getPosition().cpy().sub(position).nor(); /** TODO check if this is correct */
		}
		else
		{
			moveDirection = new Vector3(Server.random.nextFloat()*2 - 1, Server.random.nextFloat()*2 - 1, 0);
		}
	}
	
	private void move(float delta)
	{	
		velocity = moveDirection.cpy().scl(walk_speed);
		position.add(velocity.cpy().scl(1f/delta));
		
		if(!World.isLocal() && velocity.len() != 0)
		{
			for(ConnectionToClient connectionToClient : Server.getConnectionManager().getAllConnections())
			{
				if(connectionToClient.isLogedIn()) connectionToClient.sendMessageToClient(new UpdateEntityMessage(id, velocity, position));
			}
		}
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
			float distance = player.getPosition().dst(position);
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
