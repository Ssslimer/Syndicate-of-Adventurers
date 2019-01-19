package util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector3;

public class MathTest
{
	@Test
	public void testVectorAngle()
	{
		Vector3 dir = new Vector3(0, -1, 0);
		assertEquals(0, MathHelper.getRotation(dir));
		
		dir = new Vector3(1, 0, 0);
		assertEquals(2, MathHelper.getRotation(dir));
		
		dir = new Vector3(0, 1, 0);	
		assertEquals(4, MathHelper.getRotation(dir));
	}
}