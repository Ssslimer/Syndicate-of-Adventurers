package screens;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import client.SyndicateOfAdventurers;
import client.chat.Chat;
import client.chat.ChatLabelGenerator;
import entities.TerrainTile;
import networking.MoveDirection;
import util.ConfigConstants;

public class GameScreen implements Screen, InputProcessor 
{
	private SyndicateOfAdventurers game;
	
	private InputMultiplexer inputMultiplexer;
	private Stage stage;
	
	private PerspectiveCamera cam;
    private ModelBatch modelBatch;
    
    private List<ModelInstance> terrainModels = new ArrayList<>();
    private Environment environment;
    private ModelBuilder modelBuilder;
    
    private SpriteBatch spriteBatch;
    private Texture chatTexture;
    
    private Skin skin;
    private TextField chatText;
    private TextButton chatSendText;
    
    private Sound CLANG;
    
    private boolean usingChat;
	
	public GameScreen(SyndicateOfAdventurers game)
	{	
		this.game = game;
		
		stage = new Stage();
		inputMultiplexer = new InputMultiplexer();
		
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
    	
    	spriteBatch = new SpriteBatch();
    	chatTexture = new Texture(Gdx.files.getFileHandle(Paths.get("assets", "textures", "gui", "chatbackground.png").toString(), FileType.Internal));
    	
    	skin = new Skin(Gdx.files.internal("uiskin.json"));
    	setupChatTextField();
    	setupChatSendTextButton();
    	
    	CLANG = Gdx.audio.newSound(Gdx.files.getFileHandle(Paths.get("assets", "sounds", "clangberserk.wav").toString(), FileType.Internal));
        
        List<TerrainTile> terrain = SyndicateOfAdventurers.getGameMap().getTerrain();
        for(TerrainTile tile : terrain)
        {
        	ModelInstance modelInstance = new ModelInstance(createTerrainTile(SyndicateOfAdventurers.getResources().getTerrainMaterial(tile.getTerrainType())));
        	modelInstance.transform.setFromEulerAngles(0, -90, 0);       	
        	modelInstance.transform.translate(tile.getPosition());
        	modelInstance.transform.scale(2.5f, 2.5f, 2.5f);
        	this.terrainModels.add(modelInstance);
        }

		try{Thread.sleep(5000);}
		catch(InterruptedException e){e.printStackTrace();}
				
		inputMultiplexer.addProcessor(this);
		inputMultiplexer.addProcessor(stage);
		Gdx.input.setInputProcessor(inputMultiplexer);
				
//		while(true)
//		{
//			SyndicateOfAdventurers.getClient().pingServer();
//			try{Thread.sleep(1000);}
//			catch(InterruptedException e){e.printStackTrace();}
//		}
	}

	@Override
	public void show() {}

	@Override
	public void render(float delta)
	{
//        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        renderGameplay();
        renderChat();
	}
	
	private void renderGameplay()
	{
		modelBatch.begin(cam);
        modelBatch.render(terrainModels, environment);
        modelBatch.end();
	}
	
	private void renderChat()
	{
		spriteBatch.begin();
		spriteBatch.draw(chatTexture, ConfigConstants.WIDTH - chatTexture.getWidth(), 0);
		spriteBatch.end();
		
		List<String> newChatMessages = Chat.getChatMessages();
		
		for(int index = newChatMessages.size()-1, i = 0; index >= 0; index--, i++)
		{
			String labelText = newChatMessages.get(index);
			Label label = ChatLabelGenerator.generateLabel(chatText.getX(), chatText.getY() + i*20f , chatText.getWidth(), chatText.getHeight(), labelText, skin);
			
			stage.addActor(label);
			
		}
		
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) 
	{
		Gdx.gl.glViewport(0, 0, width, height);
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose(){ modelBatch.dispose(); }
	
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
		if(!usingChat)
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
		}

		return !usingChat; 
	}

	@Override
	public boolean keyUp(int keycode) 
	{ 
		if(!usingChat)
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
		}
		
		return !usingChat; 
	}

	@Override
	public boolean keyTyped(char character) { return !usingChat; }

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) 
	{
		if(screenX >= ConfigConstants.WIDTH - chatTexture.getWidth() &&
				screenY >= ConfigConstants.HEIGHT - chatTexture.getHeight())
		{
			usingChat = true;
			stage.setKeyboardFocus(null);
		}		
		else
		{
			usingChat = false;
			
			CLANG.play(1.0f);
			SyndicateOfAdventurers.getClient().attack();	
		}
		
		return !usingChat;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) { return !usingChat; }

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) { return !usingChat; }

	@Override
	public boolean mouseMoved(int screenX, int screenY) { return !usingChat; }

	@Override
	public boolean scrolled(int amount) { return !usingChat; }
	
	private void setupChatTextField() 
	{
		TextFieldStyle style = new TextFieldStyle();
		style.fontColor = Color.BLACK;
		style.font = new BitmapFont();
		
		chatText = new TextField("", style);
		chatText.setText("Type message...");
		
		chatText.setWidth((chatTexture.getWidth()/10f)*8f);
		chatText.setHeight(chatTexture.getWidth() / 10f);
		
		float posX = ConfigConstants.WIDTH - chatTexture.getWidth() + 10f;
		float posY = 10f;
		
		chatText.setPosition(posX, posY);
		
		Pixmap background = new Pixmap((int)posX, (int)posY, Pixmap.Format.RGB888);
		background.setColor(Color.GRAY);
		background.fill();
		
		chatText.getStyle().background = new Image(new Texture(background)).getDrawable();

		chatText.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
		    {
				chatText.setText("");
		    }
		});
		
		stage.addActor(chatText);
	}
	
	private void setupChatSendTextButton() 
	{
		chatSendText = new TextButton("Send", skin);
		chatSendText.setWidth(chatTexture.getWidth()/10f);
		chatSendText.setHeight(chatText.getHeight());

		float posX = ConfigConstants.WIDTH - 50f;	
		float posY = 10f;
		
		chatSendText.setPosition(posX, posY);
		
		chatSendText.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				if(chatText.getText() != null && chatText.getText().compareTo("Type message...") != 0)
				{
					SyndicateOfAdventurers.getClient().sentChatMessage(chatText.getText());
				}
			}
		});
		
		stage.addActor(chatSendText);
	}

}
