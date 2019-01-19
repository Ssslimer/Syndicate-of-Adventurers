package world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.math.Vector3;

import entities.Entity;
import entities.EntityEnemy;
import entities.EntityPlayer;
import networking.MoveDirection;
import networking.messages.fromserver.SpawnEntityMessage;
import server.ConnectionToClient;
import server.Server;
import trade.TradeState;
import util.Timer;

public class World implements Serializable
{	
	private static final long serialVersionUID = -787730389424356815L;
	
	private Map<Long, Entity> entities = new HashMap<>();
	private Map<String, EntityPlayer> players = new HashMap<>();
	private List<TerrainTile> terrain = new ArrayList<>();
	
	private static boolean isLocal = false;
	
	private Vector3 spawnPoint = new Vector3();
	
	public World()
	{
		generateMap();
	}

	public void spawnEntity(Entity entity)
	{		
		entities.put(entity.getId(), entity);
		if(entity instanceof EntityPlayer) players.put(((EntityPlayer) entity).getLogin(), (EntityPlayer) entity);
		
		if(!isLocal)
		{
			for(ConnectionToClient connectionToClient : Server.getConnectionManager().getAllConnections())
			{
				connectionToClient.sendMessageToClient(new SpawnEntityMessage(entity));
			}
		}
	}
	
	public void updateEntityPos(long id, Vector3 position, Vector3 velocity)
	{
		Entity entity = entities.get(id);
		if(entity == null) return;
		
		entity.setPosition(position);
		entity.setVelocity(velocity);
	}
	
	/** TODO tmp map for testing */
	private void generateMap()
	{
		for(int x = -10; x < 10; x++)
		{
			for(int y = -10; y < 10; y++)
			{
				Vector3 position = new Vector3(2.5f * x, 2.5f * y, 0);
				terrain.add(new TerrainTile(position, new Random().nextInt(8)));
			}
		}
	}

	public void update(float delta)
	{
		for(Entity e : entities.values())
		{
			e.update(delta);
		}
		
		if(!isLocal && Timer.getTickCount() % 500 == 0)
		{
			int posX = -5 + Server.random.nextInt(10);
			int posY = -5 + Server.random.nextInt(10);
			spawnEntity(new EntityEnemy(new Vector3(posX, posY, 0)));
		}
	}
	
	public void attackIfEnemyInFront(int damageAttack, Vector3 attackerPosition, MoveDirection attackDirection)
	{
		for(Entity e : entities.values())
		{
			if(attackDirection == MoveDirection.UP_AND_RIGHT)
			{
				if(e.getPosition().y == attackerPosition.y + 1 && e.getPosition().x == attackerPosition.x + 1 && e instanceof EntityEnemy)
				{
					((EntityEnemy)e).dealDamage(damageAttack);
				}
			}
			else if(attackDirection == MoveDirection.UP_AND_LEFT)
			{
				if(e.getPosition().y == attackerPosition.y + 1 && e.getPosition().x == attackerPosition.x - 1 && e instanceof EntityEnemy)
				{
					((EntityEnemy)e).dealDamage(damageAttack);
				}
			}
			else if(attackDirection == MoveDirection.DOWN_AND_RIGHT)
			{
				if(e.getPosition().y == attackerPosition.y - 1 && e.getPosition().x == attackerPosition.x + 1 && e instanceof EntityEnemy)
				{
					((EntityEnemy)e).dealDamage(damageAttack);
				}
			}
			else if(attackDirection == MoveDirection.DOWN_AND_LEFT)
			{
				if(e.getPosition().y == attackerPosition.y - 1 && e.getPosition().x == attackerPosition.x - 1 && e instanceof EntityEnemy)
				{
					((EntityEnemy)e).dealDamage(damageAttack);
				}
			}
			else if(attackDirection == MoveDirection.UP)
			{
				if(e.getPosition().y == attackerPosition.y + 1 && e instanceof EntityEnemy)
				{
					((EntityEnemy)e).dealDamage(damageAttack);
				}
			}
			else if(attackDirection == MoveDirection.DOWN)
			{
				if(e.getPosition().y == attackerPosition.y - 1 && e instanceof EntityEnemy)
				{
					((EntityEnemy)e).dealDamage(damageAttack);
				}
			}
			else if(attackDirection == MoveDirection.RIGHT)
			{
				if(e.getPosition().y == attackerPosition.x + 1 && e instanceof EntityEnemy)
				{
					((EntityEnemy)e).dealDamage(damageAttack);
				}
			}
			else if(attackDirection == MoveDirection.LEFT)
			{
				if(e.getPosition().y == attackerPosition.x - 1 && e instanceof EntityEnemy)
				{
					((EntityEnemy)e).dealDamage(damageAttack);
				}
			}
		}
	}
	
	public void attackIfPlayerInFront(int damageAttack, Vector3 attackerPosition, MoveDirection attackDirection)
	{
		for(Entity e : entities.values())
		{
			if(attackDirection == MoveDirection.UP_AND_RIGHT)
			{
				if(e.getPosition().y == attackerPosition.y + 1 && e.getPosition().x == attackerPosition.x + 1 && e instanceof EntityPlayer)
				{
					((EntityPlayer)e).dealDamage(damageAttack);
				}
			}
			else if(attackDirection == MoveDirection.UP_AND_LEFT)
			{
				if(e.getPosition().y == attackerPosition.y + 1 && e.getPosition().x == attackerPosition.x - 1 && e instanceof EntityPlayer)
				{
					((EntityPlayer)e).dealDamage(damageAttack);
				}
			}
			else if(attackDirection == MoveDirection.DOWN_AND_RIGHT)
			{
				if(e.getPosition().y == attackerPosition.y - 1 && e.getPosition().x == attackerPosition.x + 1 && e instanceof EntityPlayer)
				{
					((EntityPlayer)e).dealDamage(damageAttack);
				}
			}
			else if(attackDirection == MoveDirection.DOWN_AND_LEFT)
			{
				if(e.getPosition().y == attackerPosition.y - 1 && e.getPosition().x == attackerPosition.x - 1 && e instanceof EntityPlayer)
				{
					((EntityPlayer)e).dealDamage(damageAttack);
				}
			}
			else if(attackDirection == MoveDirection.UP)
			{
				if(e.getPosition().y == attackerPosition.y + 1 && e instanceof EntityPlayer)
				{
					((EntityPlayer)e).dealDamage(damageAttack);
				}
			}
			else if(attackDirection == MoveDirection.DOWN)
			{
				if(e.getPosition().y == attackerPosition.y - 1 && e instanceof EntityPlayer)
				{
					((EntityPlayer)e).dealDamage(damageAttack);
				}
			}
			else if(attackDirection == MoveDirection.RIGHT)
			{
				if(e.getPosition().y == attackerPosition.x + 1 && e instanceof EntityPlayer)
				{
					((EntityPlayer)e).dealDamage(damageAttack);
				}
			}
			else if(attackDirection == MoveDirection.LEFT)
			{
				if(e.getPosition().y == attackerPosition.x - 1 && e instanceof EntityPlayer)
				{
					((EntityPlayer)e).dealDamage(damageAttack);
				}
			}
		}
	}

	public Map<Long, Entity> getEntities()
	{
		return entities;
	}

	public List<TerrainTile> getTerrain()
	{
		return terrain;
	}
	
	public Map<String, EntityPlayer> getPlayers()
	{
		return players;
	}
	
	public Entity getEntity(long id)
	{
		return entities.get(id);
	}
	
	public EntityPlayer getPlayer(String login)
	{
		return players.get(login);
	}

	public static boolean isLocal()
	{
		return isLocal;
	}
	
	public static void setLocal(boolean b)
	{
		isLocal = b;
	}

	public Vector3 getSpawnPoint()
	{
		return spawnPoint;
	}

	public void setSpawnPoint(Vector3 spawnPoint)
	{
		this.spawnPoint = spawnPoint;
	}
	
	public EntityPlayer findClosestTradingEntity(Vector3 pos)
	{
		EntityPlayer player = null;
		float maxDistance = 10;
		float currentDistance = 10;
		
		for(Entity e : entities.values())
		{
			if(e instanceof EntityPlayer)
			{
				if(((EntityPlayer)e).getTradeState() == TradeState.SELLING)
				{
					if(e.getPosition().dst(pos) < maxDistance && e.getPosition().dst(pos) < currentDistance)
					{
						currentDistance = e.getPosition().dst(pos);
						player = (EntityPlayer)e;
					}
				}
			}
		}
		
		if(player == null) return null;
		else return player;
	}
}
