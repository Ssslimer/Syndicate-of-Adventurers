package com.moag.game.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector3;
import com.moag.game.Resources;

public class Map
{
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
		Resources r = new Resources();
		for(int x = -10; x < 10; x++)
		{
			for(int y = -10; y < 10; y++)
			{
				Vector3 position = new Vector3(x, y, 0);
				terrain.add(new TerrainTile(position, r.getTerrainMaterial(1)));
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
