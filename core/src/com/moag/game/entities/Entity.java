package com.moag.game.entities;

import com.badlogic.gdx.math.Vector3;

public class Entity
{
	private Vector3 position;
	private float rotation;

	public Entity(Vector3 position)
	{
		this.position = position;
	}
	
	public Vector3 getPosition()
	{
		return position;
	}

	public void setPosition(Vector3 position)
	{
		this.position = position;
	}

	public float getRotation()
	{
		return rotation;
	}

	public void setRotation(float rotation)
	{
		this.rotation = rotation;
	}
}
