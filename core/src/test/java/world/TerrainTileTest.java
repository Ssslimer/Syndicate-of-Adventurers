package world;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector3;

class TerrainTileTest {

	@Test
	void test() {
		
		Vector3 pos = new Vector3(4, 10, 55);
		int terrainType = 4;
		
		Vector3 pos2 = new Vector3(66, 7, 15);
		int terrainType2 = 2;
		
		TerrainTile tile = new TerrainTile(pos, terrainType);
		TerrainTile tile2 = new TerrainTile(pos2, terrainType2);
		
		assertNotNull(tile);
		assertNotNull(tile.getPosition());
		
		assertNotNull(tile2);
		assertNotNull(tile2.getPosition());
		
		assertNotEquals(tile, tile2);
		assertNotEquals(tile.getPosition(), tile2.getPosition());
		
		assertEquals(tile.getPosition(), pos);
		assertEquals(tile2.getPosition(), pos2);
		
		assertTrue(tile.getTerrainType() == 4);
		assertTrue(tile2.getTerrainType() == 2);
	}

}
