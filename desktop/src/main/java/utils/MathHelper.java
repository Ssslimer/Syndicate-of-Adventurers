package utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class MathHelper
{
	/** Returns int 0-7 coresponding to angle of rotation. Used by entities for texturing */
	public static int getRotation(Vector3 vector)
	{
		Vector2 ref = new Vector2(0, 1);
		Vector2 dir = new Vector2(vector.x, vector.y);
		int angle = ((int)ref.angle(dir)) + 180;
		return angle / (360/8);
	}
}
