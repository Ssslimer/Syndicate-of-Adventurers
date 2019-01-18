package client;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class Resources
{
    private ModelBuilder modelBuilder = new ModelBuilder();
	private AssetManager assetManager = new AssetManager();
	
	private static final Map<String, AssetDescriptor<Texture>> textures = new HashMap<>();
	private static final Map<String, Model> models = new HashMap<>();
	
	public void loadAll()
	{
		addTexture("GUI_BACKGROUND", Paths.get("assets", "textures", "gui", "menu.png"));
		
		loadTerrainTextures();
		assetManager.finishLoading();
		createTerrainModels();

		loadPlayerTextures();
		assetManager.finishLoading();
		createPlayerModels();
		
		assetManager.finishLoading();
	}

	private void loadTerrainTextures()
	{
		for(int i = 0; i < 8; i++)
		{
			String name = "TERRAIN_GRASS_" + i;
			addTexture(name, Paths.get("assets", "textures", "terrain", "grass_"+i+".png"));
		}	
	}

	private void createTerrainModels()
	{
		for(int i = 0; i < 8; i++)
		{
			String name = "TERRAIN_GRASS_" + i;
			Texture texture = getTexture(name);
			texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		
			Model model = createModel(new Material(TextureAttribute.createDiffuse(texture)));			
			models.put(name, model);
		}
	}
	
	public void addTexture(String name, Path path)
	{
		AssetDescriptor<Texture> ad = new AssetDescriptor<>(path.toString(), Texture.class);
		textures.put(name, ad);
		assetManager.load(ad);
	}
	
	private void loadPlayerTextures()
	{
		for(int i = 0; i < 8; i++)
		{
			String name = "PLAYER_" + i;
			addTexture(name, Paths.get("assets", "textures", "entities", "player", "player_"+i+".png"));
		}
	}
	
	private void createPlayerModels()
	{
		for(int i = 0; i < 8; i++)
		{
			String name = "PLAYER_" + i;
			Texture texture = getTexture(name);
			texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		
			Model model = createModel(new Material(TextureAttribute.createDiffuse(texture)));			
			models.put(name, model);
		}
	}
	
	private Model createModel(Material material)
	{	
		final float size = 1;
		
		modelBuilder.begin();
		MeshPartBuilder bPartBuilder = modelBuilder.part("rect", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.TextureCoordinates, material);
		bPartBuilder.setUVRange(0, 0, 1, 1);
	    bPartBuilder.rect(-(size*0.5f), -(size*0.5f), 0, 
	                	   (size*0.5f), -(size*0.5f), 0, 
	                	   (size*0.5f),  (size*0.5f), 0, 
	                	  -(size*0.5f),  (size*0.5f), 0,
	                	   0, 0, -1);
	
	    return (modelBuilder.end());
    }
	
	public Texture getTexture(String textureName)
	{
		return assetManager.get(textures.get(textureName));
	}
	
	public void unload()
	{
		assetManager.dispose();
	}
	
	public Model getModel(String name)
	{
		return models.get(name);
	}
}
