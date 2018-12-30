package com.moag.game.entities;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector3;

public class TerrainTile implements Serializable
{
	private static final long serialVersionUID = -8892348832832660314L;
	
	private final Vector3 position;
	private final int terrainType;
	
	public TerrainTile(Vector3 position, int terrainType)
	{
		this.position = position;
		this.terrainType = terrainType;
	}

	public Vector3 getPosition()
	{
		return position;
	}

	public int getTerrainType()
	{
		return terrainType;
	}
}
