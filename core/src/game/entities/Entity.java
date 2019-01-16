package entities;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector3;

import networking.MoveDirection;

public abstract class Entity implements Serializable
{
	private static final long serialVersionUID = 9193370594513812803L;
	
	private static long lastId = 0;
	protected final long id;
	
	protected Vector3 position;
	protected Vector3 velocity = new Vector3();
	protected MoveDirection moveDirection = MoveDirection.NONE;

	public Entity(Vector3 position)
	{
		this.id = lastId;
		lastId++;
		
		this.position = position;
	}

	public abstract void update(float delta);
	
	public Vector3 getPosition()
	{
		return position;
	}

	public void setPosition(Vector3 position)
	{
		this.position = position;
	}
	
	public MoveDirection getDirection()
	{
		return moveDirection;
	}
	
	public void setDirection(MoveDirection direction)
	{
		this.moveDirection = direction;
	}
	
	public long getId() 
	{
		return id;
	}
}
