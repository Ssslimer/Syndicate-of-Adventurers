package com.moag.game;

import java.util.ArrayList;
import java.util.List;

import com.moag.game.entities.Entity;

public class Map
{
	List<Entity> entities = new ArrayList<>();
	
	public void spawnEntity(Entity entity)
	{
		entities.add(entity);
	}
}
