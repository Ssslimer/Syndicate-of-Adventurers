package com.moag.game.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector3;

public class Map implements Serializable
{	
	private static final long serialVersionUID = -787730389424356815L;
	
	private List<Entity> entities = new ArrayList<>();
	private List<TerrainTile> terrain = new ArrayList<>();
	
	public Map()
	{
		generateMap();
	}
	
	public void spawnEntity(Entity entity)
	{
		entities.add(entity);
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

	public List<Entity> getEntities()
	{
		return entities;
	}

	public List<TerrainTile> getTerrain()
	{
		return terrain;
	}
}
