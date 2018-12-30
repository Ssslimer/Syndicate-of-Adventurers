package com.moag.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.moag.game.entities.Map;
import com.moag.game.entities.TerrainTile;

public class TestCamera implements ApplicationListener
{
    public PerspectiveCamera cam;
    public ModelBatch modelBatch;
    public Model model;
    public ModelInstance box;
    private List<ModelInstance> terrainModels = new ArrayList<>();
    public Environment environment;
    private ModelBuilder modelBuilder;

    @Override
    public void create() 
    {
    	environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(1, 1, 1, 0, -1, 0));
    	
    	modelBatch = new ModelBatch();
    	modelBuilder = new ModelBuilder();
    	
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(-10f, 10f, -10f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        model = modelBuilder.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Position | Usage.Normal);
        box = new ModelInstance(model);
        
        Resources r = new Resources();
        Map map = new Map();
        List<TerrainTile> terrain = map.getTerrain();
        for(TerrainTile tile : terrain)
        {
        	ModelInstance modelInstance = new ModelInstance(createTerrainTile(r.getTerrainMaterial(tile.getTerrainType())));
        	modelInstance.transform.setFromEulerAngles(0, -90, 0);       	
        	modelInstance.transform.translate(tile.getPosition());
        	modelInstance.transform.scale(2.5f, 2.5f, 2.5f);
        	terrainModels.add(modelInstance);
        }
    }

	@Override
	public void resize(int width, int height) {}

	@Override
	public void render() 
	{
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);
        modelBatch.render(terrainModels, environment);
        modelBatch.end();
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose()
	{
        modelBatch.dispose();
        model.dispose();
	}
	

	
	private Model createTerrainTile(Material material)
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

}