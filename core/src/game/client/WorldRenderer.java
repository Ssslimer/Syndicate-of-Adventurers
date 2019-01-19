package client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import entities.Entity;
import entities.EntityPlayer;
import world.TerrainTile;

public class WorldRenderer
{
    private Environment environment = new Environment();
    private ModelBatch modelBatch = new ModelBatch();
	private OrthographicCamera cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    
    private List<ModelInstance> terrainModels = new ArrayList<>();	
    private Map<Entity, ModelInstance[]> entityInstances = new HashMap<>();
    
    public static final int MOVE_DIRECTIONS = 8;
        
    public WorldRenderer()
    {
    	environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
    	environment.add(new DirectionalLight().set(1, 1, 1, 0, -1, 0));
    	
    	cam.position.set(0, -5, 5);
    	cam.lookAt(0, 0, 0);
    	cam.near = 1f;
    	cam.far = 300f;
    	cam.zoom = 0.005f;
    	cam.update();
    }
    
    public ModelInstance[] initEntity(Entity entity)
    {
    	ModelInstance[] instances = new ModelInstance[MOVE_DIRECTIONS];
    	for(int i = 0; i < MOVE_DIRECTIONS; i++)
    	{
    		ModelInstance instance = new ModelInstance(MyGame.getResources().getModel("PLAYER_" + i));
    		instance.transform.setTranslation(entity.getPosition());
    		instances[i] = instance;
    	}
    	
		entityInstances.put(entity, instances);
		
		return instances;
    }
    
	public void initTerrain()
	{
        List<TerrainTile> terrain = MyGame.getGameMap().getTerrain();
        for(TerrainTile tile : terrain)
        {
        	ModelInstance modelInstance = new ModelInstance(MyGame.getResources().getModel("TERRAIN_GRASS_" + tile.getTerrainType()));      	
        	modelInstance.transform.translate(tile.getPosition());
        	modelInstance.transform.scale(2.5f, 2.5f, 2.5f);
        	
        	terrainModels.add(modelInstance);
        }
	}

	public void render()
	{
		modelBatch.begin(cam);
		
		renderMap();
		followPlayer();
		renderEntities();
		
		modelBatch.end();
	}
	
	private void renderEntities()
	{
		for(Entity entity : MyGame.getGameMap().getEntities().values())
		{
			ModelInstance[] instances = entityInstances.get(entity);
			if(instances == null) instances = MyGame.getRenderer().initEntity(entity);
			
			ModelInstance instance = instances[getRotation(entity.getVelocity())];
			
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
		EntityPlayer player = MyGame.getGameMap().getPlayer(MyGame.getClient().getLogin());
		if(player == null) return;
		
    	cam.position.set(0, -5, 5).add(player.getPosition());
    	cam.lookAt(player.getPosition());
    	cam.update();
	}
	
	/** Returns int 0-7 coresponding to angle of rotation. Used by entities for texturing */
	public static int getRotation(Vector3 vector)
	{
		Vector2 ref = new Vector2(0, -1);
		Vector2 dir = new Vector2(vector.x, vector.y);
		
		int angle = ((int)ref.angle(dir));
		return angle / (360/8);
	}
}
