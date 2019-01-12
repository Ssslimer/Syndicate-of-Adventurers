package client;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;

public class Resources
{
	private AssetManager assetManager = new AssetManager();
	private List<Material> terrainMaterials = new ArrayList<>();
	
	public static final Map<String, AssetDescriptor<Texture>> textures = new HashMap<>();
	
	public void loadAll()
	{
		generateTerrainMaterials();
		addTexture("GUI_BACKGROUND", Paths.get("assets", "textures", "gui", "menu.png"));
		
		assetManager.finishLoading();
		System.out.println(terrainMaterials.size());
	}
	
	private void generateTerrainMaterials()
	{
		final int grassTextures = 8;
		for(int i = 1; i <= grassTextures; i++)
		{
			String name = "TERRAIN_GRASS_" + i;
			addTexture(name, Paths.get("assets", "textures", "terrain", "grass_"+i+".png"));
			assetManager.finishLoading();
			Texture texture = getTexture(name);
			texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
			terrainMaterials.add(new Material(TextureAttribute.createDiffuse(texture)));
		}
	}
	
	public void addTexture(String name, Path path)
	{
		AssetDescriptor<Texture> ad = new AssetDescriptor<>(path.toString(), Texture.class);
		textures.put(name, ad);
		assetManager.load(ad);
	}
	
	public Texture getTexture(String textureName)
	{
		return assetManager.get(textures.get(textureName));
	}
	
	public Material getTerrainMaterial(int i)
	{
		return terrainMaterials.get(i);
	}
	
	public void unload()
	{
		assetManager.dispose();
	}
}
