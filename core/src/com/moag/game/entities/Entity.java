package com.moag.game.entities;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector3;

public class Entity implements Serializable
{
	private static final long serialVersionUID = 9193370594513812803L;
	
	private static long lastId = 0;
	protected final long id;
	
	protected Vector3 position;
	protected Vector3 velocity = new Vector3();
	protected float rotation;

	public Entity(Vector3 position)
	{
		this.id = lastId;
		lastId++;
		
		this.position = position;
	}
	
	public void update() {}
	
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
	
	public long getId() 
	{
		return id;
	}
}
