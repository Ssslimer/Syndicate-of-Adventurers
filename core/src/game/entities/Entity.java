package entities;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector3;

public abstract class Entity implements Serializable
{
	private static final long serialVersionUID = 9193370594513812803L;
	
	private static long lastId = 0;
	protected final long id;
	
	protected Vector3 position;
	protected Vector3 velocity = new Vector3();
	protected Vector3 moveDirection = new Vector3();

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
	
	public Vector3 getMoveDirection()
	{
		return moveDirection;
	}
	
	public long getId() 
	{
		return id;
	}
}
