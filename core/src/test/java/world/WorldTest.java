package world;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector3;

public class WorldTest 
{
	@Test
	public void test() 
	{
		World world = new World();
		
		assertNotNull(world);
		
		assertNotNull(world.getEntities());
		assertNotNull(world.getPlayers());
		assertNotNull(world.getTerrain());
	
		assertTrue(world.getEntities().isEmpty());
		assertTrue(world.getPlayers().isEmpty());
		assertTrue(!world.getTerrain().isEmpty());
		
		Vector3 v = new Vector3(1, 2, 3);
		world.setSpawnPoint(v);
		
		assertEquals(world.getSpawnPoint(), v);
		
		World.setLocal(true);
		assertTrue(World.isLocal() == true);
		
		World.setLocal(false);
		assertTrue(World.isLocal() == false);
	}

}
