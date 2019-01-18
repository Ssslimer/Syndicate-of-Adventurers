package client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

import entities.Entity;
import entities.EntityPlayer;
import world.TerrainTile;

public class WorldRenderer
{
    private ModelBuilder modelBuilder = new ModelBuilder();
    private Environment environment = new Environment();
    private ModelBatch modelBatch = new ModelBatch();
	private OrthographicCamera cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    
    private List<ModelInstance> terrainModels = new ArrayList<>();	
    private Map<Entity, ModelInstance> entityInstances = new HashMap<>();
        
    public WorldRenderer()
    {
    	environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
    	environment.add(new DirectionalLight().set(1, 1, 1, 0, -1, 0));
    	
    	cam.position.set(-10, 10, 0);
    	cam.lookAt(0, 0, 0);
    	cam.near = 1f;
    	cam.far = 300f;
    	cam.zoom = 0.02f;
    	cam.update();
    }
    
    public ModelInstance initEntity(Entity entity)
    {
		ModelInstance testBox = new ModelInstance(modelBuilder.createBox(1, 1, 1, new Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Position));
		testBox.transform.setTranslation(entity.getPosition());
		
		entityInstances.put(entity, testBox);
		
		return testBox;
    }
    
	public void initTerrain()
	{
        List<TerrainTile> terrain = MyGame.getGameMap().getTerrain();
        for(TerrainTile tile : terrain)
        {
        	ModelInstance modelInstance = new ModelInstance(createTerrainTile(MyGame.getResources().getTerrainMaterial(tile.getTerrainType())));
        	modelInstance.transform.setFromEulerAngles(0, -90, 0);       	
        	modelInstance.transform.translate(tile.getPosition());
        	modelInstance.transform.scale(2.5f, 2.5f, 2.5f);
        	
        	terrainModels.add(modelInstance);
        }
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

	public void render()
	{
		modelBatch.begin(cam);
		
		followPlayer();
		renderEntities();
		renderMap();
		
		modelBatch.end();
	}
	
	private void renderEntities()
	{
		for(Entity entity : MyGame.getGameMap().getEntities().values())
		{
			ModelInstance instance = entityInstances.get(entity);
			if(instance == null) instance = MyGame.getRenderer().initEntity(entity);
			
			
			instance.transform.setTranslation(entity.getPosition());
	        modelBatch.render(instance, environment);
		}
	}	

	private void renderMap()
	{	
        modelBatch.render(terrainModels, environment);       
        MyGame.getRenderer().renderEntities();    
	}
	
	public void clear()
	{
		modelBatch.dispose();
	}
	
	private void followPlayer()
	{
//		EntityPlayer player = MyGame.getGameMap().getPlayer(MyGame.getClient().getLogin());
//    	cam.position.set(-10, 10, 0).add(player.getPosition());
//    	cam.lookAt(player.getPosition());
//    	cam.update();
	}
}
