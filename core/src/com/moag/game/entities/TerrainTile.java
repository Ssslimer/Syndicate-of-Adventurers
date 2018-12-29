package com.moag.game.entities;

import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.math.Vector3;

public class TerrainTile
{
	private final Vector3 position;
	private final Material material;
	
	public TerrainTile(Vector3 position, Material material)
	{
		this.position = position;
		this.material = material;
	}

	public Vector3 getPosition()
	{
		return position;
	}

	public Material getMaterial()
	{
		return material;
	}
}
