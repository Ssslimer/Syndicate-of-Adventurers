package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.math.Vector3;

import networking.MoveDirection;

public class World implements Serializable
{	
	private static final long serialVersionUID = -787730389424356815L;
	
	private Map<Long, Entity> entities = new HashMap<>();
	private List<TerrainTile> terrain = new ArrayList<>();
	
	public World()
	{
		generateMap();
	}
	
	public void spawnEntity(Entity entity)
	{
		entities.put(entity.getId(), entity);
	}
	
	public Entity getEntity(long id)
	{
		return entities.get(id);
	}
	
	public void updateEntityPos(long id, Vector3 position, Vector3 velocity)
	{
		Entity entity = entities.get(id);
		entity.position = position;
		entity.velocity = velocity;
	}
	
	private void generateMap()
	{
		for(int x = -10; x < 10; x++)
		{
			for(int y = -10; y < 10; y++)
			{
				Vector3 position = new Vector3(x, y, 0);
				terrain.add(new TerrainTile(position, 1));
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
}
