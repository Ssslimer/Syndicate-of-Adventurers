package entities;

import java.util.List;

import com.badlogic.gdx.math.Vector3;

import networking.messages.fromserver.DamageEntityMessage;
import networking.messages.fromserver.UpdateEntityMessage;
import server.Server;
import util.Timer;
import world.World;

public class EntityEnemy extends Entity implements Damageable
{
	private static final long serialVersionUID = -8463316271178712082L;
	
	private static final int BASE_ATTACK = 5;
	private static final int BASE_DEFENCE = 5;
	private static final int BASE_HEALTH = 50;
	private static final float ATTACK_RANGE = 1f;	
	private static final float WALK_SPEED = 1f;
	private static final int FOLLOW_RANGE = 20;
	
	private int health, attack, defence;	
	
	private Loot loot;

	public EntityEnemy(Vector3 position)
	{
		super(position);
		
		health = BASE_HEALTH;
		attack = BASE_ATTACK;
		defence = BASE_DEFENCE;		
		
		loot = new Loot();
	}
	
	@Override
	public void update(float delta)
	{
		if(Timer.getTickCount() % 10 == 0) changeTarget();
		move(delta);
		
		if(Timer.getTickCount() % 50 == 0) attack();
	}
	
	private void changeTarget()
	{
		EntityPlayer player = Server.getMap().getClosestPlayerInRange(this, FOLLOW_RANGE);
		
		if(player != null) 	moveDirection = player.getPosition().cpy().sub(position).nor();
		else 				moveDirection = new Vector3(Server.random.nextFloat()*2 - 1, Server.random.nextFloat()*2 - 1, 0);
	}
	
	private void move(float delta)
	{	
		velocity = moveDirection.cpy().scl(WALK_SPEED);
		position.add(velocity.cpy().scl(1f/delta));
		
		if(!World.isLocal() && velocity.len() != 0)
		{
			Server.getConnectionManager().sendToAll(new UpdateEntityMessage(id, velocity, position));
		}
	}

	public int getHP()
	{
		return health;
	}
	
	public int getAttack()
	{
		return attack;
	}
	
	public int getDefence()
	{
		return defence;
	}
	
	public Loot getLoot()
	{
		return loot;
	}

	public void dealDamage(int damage, DamageSource source)
	{
		damage -= defence;		
		if(damage > 0) health -= damage;
		Server.getConnectionManager().sendToAll(new DamageEntityMessage(this, damage, source));
	}
	
	/** TODO implement AI */
	private void attack()
	{
		List<EntityPlayer> players = Server.getMap().getPlayersInRange(position, ATTACK_RANGE);
		for(Entity player : players)
		{
			if(player instanceof Damageable && player != this)
			{				
				((Damageable) player).dealDamage(attack, DamageSource.NORMAL);
			}
		}
	}	
	
	public Loot die()
	{
		alive = false;
		return loot;
	}
}
