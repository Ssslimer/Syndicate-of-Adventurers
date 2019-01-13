package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.math.Vector3;

import client.MyGame;
import networking.MoveDirection;
import networking.messages.fromserver.SpawnEntityMessage;
import server.ConnectionToClient;
import server.Server;

public class World implements Serializable
{	
	private static final long serialVersionUID = -787730389424356815L;
	
	private Map<Long, Entity> entities = new HashMap<>();
	private Map<String, EntityPlayer> players = new HashMap<>();
	private List<TerrainTile> terrain = new ArrayList<>();
	
	private static boolean isLocal = false;
	
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
			System.out.println(Server.getConnectionManager().getAllConnections().size());
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
		
		entity.position = position;
		entity.velocity = velocity;
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

	public void update()
	{
		for(Entity e : entities.values())
		{
			e.update();
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
					((EntityEnemy)e).getDamage(damageAttack);
				}
			}
			else if(attackDirection == MoveDirection.UP_AND_LEFT)
			{
				if(e.getPosition().y == attackerPosition.y + 1 && e.getPosition().x == attackerPosition.x - 1 && e instanceof EntityEnemy)
				{
					((EntityEnemy)e).getDamage(damageAttack);
				}
			}
			else if(attackDirection == MoveDirection.DOWN_AND_RIGHT)
			{
				if(e.getPosition().y == attackerPosition.y - 1 && e.getPosition().x == attackerPosition.x + 1 && e instanceof EntityEnemy)
				{
					((EntityEnemy)e).getDamage(damageAttack);
				}
			}
			else if(attackDirection == MoveDirection.DOWN_AND_LEFT)
			{
				if(e.getPosition().y == attackerPosition.y - 1 && e.getPosition().x == attackerPosition.x - 1 && e instanceof EntityEnemy)
				{
					((EntityEnemy)e).getDamage(damageAttack);
				}
			}
			else if(attackDirection == MoveDirection.UP)
			{
				if(e.getPosition().y == attackerPosition.y + 1 && e instanceof EntityEnemy)
				{
					((EntityEnemy)e).getDamage(damageAttack);
				}
			}
			else if(attackDirection == MoveDirection.DOWN)
			{
				if(e.getPosition().y == attackerPosition.y - 1 && e instanceof EntityEnemy)
				{
					((EntityEnemy)e).getDamage(damageAttack);
				}
			}
			else if(attackDirection == MoveDirection.RIGHT)
			{
				if(e.getPosition().y == attackerPosition.x + 1 && e instanceof EntityEnemy)
				{
					((EntityEnemy)e).getDamage(damageAttack);
				}
			}
			else if(attackDirection == MoveDirection.LEFT)
			{
				if(e.getPosition().y == attackerPosition.x - 1 && e instanceof EntityEnemy)
				{
					((EntityEnemy)e).getDamage(damageAttack);
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
					((EntityPlayer)e).getDamage(damageAttack);
				}
			}
			else if(attackDirection == MoveDirection.UP_AND_LEFT)
			{
				if(e.getPosition().y == attackerPosition.y + 1 && e.getPosition().x == attackerPosition.x - 1 && e instanceof EntityPlayer)
				{
					((EntityPlayer)e).getDamage(damageAttack);
				}
			}
			else if(attackDirection == MoveDirection.DOWN_AND_RIGHT)
			{
				if(e.getPosition().y == attackerPosition.y - 1 && e.getPosition().x == attackerPosition.x + 1 && e instanceof EntityPlayer)
				{
					((EntityPlayer)e).getDamage(damageAttack);
				}
			}
			else if(attackDirection == MoveDirection.DOWN_AND_LEFT)
			{
				if(e.getPosition().y == attackerPosition.y - 1 && e.getPosition().x == attackerPosition.x - 1 && e instanceof EntityPlayer)
				{
					((EntityPlayer)e).getDamage(damageAttack);
				}
			}
			else if(attackDirection == MoveDirection.UP)
			{
				if(e.getPosition().y == attackerPosition.y + 1 && e instanceof EntityPlayer)
				{
					((EntityPlayer)e).getDamage(damageAttack);
				}
			}
			else if(attackDirection == MoveDirection.DOWN)
			{
				if(e.getPosition().y == attackerPosition.y - 1 && e instanceof EntityPlayer)
				{
					((EntityPlayer)e).getDamage(damageAttack);
				}
			}
			else if(attackDirection == MoveDirection.RIGHT)
			{
				if(e.getPosition().y == attackerPosition.x + 1 && e instanceof EntityPlayer)
				{
					((EntityPlayer)e).getDamage(damageAttack);
				}
			}
			else if(attackDirection == MoveDirection.LEFT)
			{
				if(e.getPosition().y == attackerPosition.x - 1 && e instanceof EntityPlayer)
				{
					((EntityPlayer)e).getDamage(damageAttack);
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
}
