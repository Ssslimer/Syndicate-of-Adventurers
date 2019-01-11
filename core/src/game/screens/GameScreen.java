package screens;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.audio.Sound;
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

import entities.TerrainTile;
import entities.World;
import networking.MoveDirection;
import other.Resources;
import other.SyndicateOfAdventurers;

public class GameScreen implements Screen, InputProcessor 
{
	private SyndicateOfAdventurers game;
	
	private PerspectiveCamera cam;
    private ModelBatch modelBatch;
    private List<ModelInstance> terrainModels = new ArrayList<>();
    private Environment environment;
    private ModelBuilder modelBuilder;
    
    private Sound CLANG;
	
	public GameScreen(SyndicateOfAdventurers game)
	{	
		this.game = game;
		
    	this.environment = new Environment();
    	this.environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
    	this.environment.add(new DirectionalLight().set(1, 1, 1, 0, -1, 0));
    	
    	this.modelBatch = new ModelBatch();
    	this.modelBuilder = new ModelBuilder();
    	
    	this.cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	this.cam.position.set(-10f, 10f, -10f);
    	this.cam.lookAt(0, 0, 0);
    	this.cam.near = 1f;
    	this.cam.far = 300f;
    	this.cam.update();
    	
    	CLANG = Gdx.audio.newSound(Gdx.files.getFileHandle(Paths.get("assets", "sounds", "clangberserk.wav").toString(), FileType.Internal));
        
        Resources r = new Resources();
        World map = new World();
        List<TerrainTile> terrain = map.getTerrain();
        for(TerrainTile tile : terrain)
        {
        	ModelInstance modelInstance = new ModelInstance(createTerrainTile(r.getTerrainMaterial(tile.getTerrainType())));
        	modelInstance.transform.setFromEulerAngles(0, -90, 0);       	
        	modelInstance.transform.translate(tile.getPosition());
        	modelInstance.transform.scale(2.5f, 2.5f, 2.5f);
        	this.terrainModels.add(modelInstance);
        }

		try{Thread.sleep(5000);}
		catch(InterruptedException e){e.printStackTrace();}
		
		while(true)
		{
			SyndicateOfAdventurers.getClient().pingServer();
			try{Thread.sleep(1000);}
			catch(InterruptedException e){e.printStackTrace();}
		}
	}
	
	@Override
	public void show() {}

	@Override
	public void render(float delta)
	{
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(cam);
        modelBatch.render(terrainModels, environment);
        modelBatch.end();        
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose()
	{
        modelBatch.dispose();
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

	@Override
	public boolean keyDown(int keycode) 
	{ 
		switch(keycode)
		{
		case Input.Keys.W:
			SyndicateOfAdventurers.getClient().move(MoveDirection.UP, false);
		break;
		
		case Input.Keys.S:
			SyndicateOfAdventurers.getClient().move(MoveDirection.DOWN, false);
		break;
			
		case Input.Keys.A:
			SyndicateOfAdventurers.getClient().move(MoveDirection.LEFT, false);
		break;
			
		case Input.Keys.D:
			SyndicateOfAdventurers.getClient().move(MoveDirection.RIGHT, false);
		break;
		
		}
		return false; 
	}

	@Override
	public boolean keyUp(int keycode) 
	{ 
		switch(keycode)
		{
		case Input.Keys.W:
			SyndicateOfAdventurers.getClient().move(MoveDirection.UP, true);
		break;
		
		case Input.Keys.S:
			SyndicateOfAdventurers.getClient().move(MoveDirection.DOWN, true);
		break;
			
		case Input.Keys.A:
			SyndicateOfAdventurers.getClient().move(MoveDirection.LEFT, true);
		break;
			
		case Input.Keys.D:
			SyndicateOfAdventurers.getClient().move(MoveDirection.RIGHT, true);
		break;
		
		}
		return false; 
	}

	@Override
	public boolean keyTyped(char character) { return false; }

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) 
	{
		CLANG.play(1.0f);
		SyndicateOfAdventurers.getClient().attack();	
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }

	@Override
	public boolean mouseMoved(int screenX, int screenY) { return false; }

	@Override
	public boolean scrolled(int amount) { return false; }
}
