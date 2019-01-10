package test.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector3;
import com.moag.game.entities.Entity;

class EntityTest {

	@Test
	void entityTest() 
	{
		Entity e1 = new Entity(new Vector3(0, 0, 0));
	}

}
