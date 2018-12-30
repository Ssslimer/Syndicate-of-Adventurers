package com.moag.game.entities;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector3;

public class Entity implements Serializable
{
	private static final long serialVersionUID = -2948009979518301886L;
	
	protected Vector3 position;
	protected float rotation;

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
