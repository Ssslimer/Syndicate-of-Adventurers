package com.moag.game.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.math.Vector3;

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
}
