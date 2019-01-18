package networking;

import com.badlogic.gdx.math.Vector3;

public enum MoveDirection
{
	NONE (new Vector3( 0, 0, 0)),
	UP   (new Vector3( 0, 1, 0)), 
	DOWN (new Vector3( 0,-1, 0)), 
	LEFT (new Vector3(-1, 0, 0)), 
	RIGHT(new Vector3( 1, 0, 0)),
	
	UP_AND_RIGHT  (new Vector3( 1, 1, 0).nor()),
	UP_AND_LEFT   (new Vector3(-1, 1, 0).nor()),
	DOWN_AND_RIGHT(new Vector3( 1,-1, 0).nor()),
	DOWN_AND_LEFT (new Vector3(-1,-1, 0).nor());
	
	private final Vector3 direction;
	
	private MoveDirection(Vector3 direction)
	{
		this.direction = direction;
	}
	
	public Vector3 getDirection()
	{
		return direction;
	}
}
