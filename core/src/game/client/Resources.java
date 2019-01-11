package client;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;

public class Resources
{
	private List<Material> terrainMaterials = new ArrayList<>();
	
	public Resources()
	{
		generateTerrainMaterials();
	}
	
	private void generateTerrainMaterials()
	{
		final int grassTextures = 8;
		for(int i = 1; i <= grassTextures; i++)
		{
			Texture texTile = new Texture(Paths.get("assets", "textures", "terrain", "grass_"+i+".png").toString());
			texTile.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
			terrainMaterials.add(new Material(TextureAttribute.createDiffuse(texTile)));
		}
	}
	
	public Material getTerrainMaterial(int i)
	{
		return terrainMaterials.get(i);
	}
}
