package screens;

import java.nio.file.Paths;
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
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import client.Chat;
import client.MyGame;
import entities.Entity;
import networking.MoveDirection;
import util.ConfigConstants;

public class GameScreen implements Screen, InputProcessor 
{
	private MyGame game;
	
	private InputMultiplexer inputMultiplexer;
	private Stage stage;
	      
    private SpriteBatch spriteBatch;
    private Texture chatTexture;
    
    private Skin skin;
    private TextField chatText;
    private TextButton chatSendText;
    private BitmapFont chatFont;
    private boolean usingChat;
    
    private TradeRenderer tradeRenderer;
    private boolean usingTrade;
    
    private Sound CLANG;

    private float timer; // for ping
	
	public GameScreen(MyGame game)
	{	
		this.game = game;
		
		stage = new Stage();
		inputMultiplexer = new InputMultiplexer();	
    	
    	spriteBatch = new SpriteBatch();
    	chatTexture = new Texture(Gdx.files.getFileHandle(Paths.get("assets", "textures", "gui", "chatbackground.png").toString(), FileType.Internal));
    	chatFont = new BitmapFont();
    	
    	skin = new Skin(Gdx.files.internal("uiskin.json"));
    	setupChatTextField();
    	setupChatSendTextButton();
    	
    	tradeRenderer = new TradeRenderer();
    	
    	CLANG = Gdx.audio.newSound(Gdx.files.getFileHandle(Paths.get("assets", "sounds", "clangberserk.wav").toString(), FileType.Internal));
				
		inputMultiplexer.addProcessor(this);
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(tradeRenderer.getStage());
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		MyGame.getRenderer().initTerrain();
		initEntities();
	}
	
	private void initEntities()
	{    	
		for(Entity entity : MyGame.getGameMap().getEntities().values())
		{
			MyGame.getRenderer().initEntity(entity);						
		}   	
	}

	@Override
	public void show() {}

	@Override
	public void render(float delta)
	{
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
      
        //pingServer(delta);
        MyGame.getRenderer().render();
        renderChat();
	}
	
	private void pingServer(float delta)
	{
        timer += delta;
		if(timer > 5)
		{
			timer = 0;
			MyGame.getClient().pingServer();
		}	
	}	
	
	private void renderChat()
	{
		spriteBatch.begin();
		
		spriteBatch.draw(chatTexture, ConfigConstants.WIDTH - chatTexture.getWidth(), 0);
		List<String> newChatMessages = Chat.getChatMessages();
		
		if(!newChatMessages.isEmpty())
		{
			for(int index = newChatMessages.size()-1, i = 0; index >= 0; index--, i++)
			{
				String str = newChatMessages.get(index);
				chatFont.setColor(Color.GOLD);
				chatFont.draw(spriteBatch, str, chatText.getX(), chatText.getY() + 80f + i*20f);
			}
		}
		
		spriteBatch.end();
		
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
	public void dispose()
	{
		spriteBatch.dispose();
		MyGame.getRenderer().clear();
	}
	
	@Override
	public boolean keyDown(int keycode) 
	{ 
		if(!usingChat)
		{
			switch(keycode)
			{
				case Input.Keys.W: MyGame.getClient().move(MoveDirection.UP,    false);	break;
				case Input.Keys.S: MyGame.getClient().move(MoveDirection.DOWN,  false);	break;		
				case Input.Keys.A: MyGame.getClient().move(MoveDirection.LEFT,  false);	break;
				case Input.Keys.D: MyGame.getClient().move(MoveDirection.RIGHT, false);	break;			
			}
		}
		
		if(usingChat)
		{
			if(keycode == Input.Keys.ENTER)
			{
				if(chatText.getText() != null && chatText.getText().compareTo("Type message...") != 0)
				{
					MyGame.getClient().sentChatMessage(chatText.getText());
					//chatText.setText("Type message...");
				}
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
				case Input.Keys.W: MyGame.getClient().move(MoveDirection.UP,    true); 	break;		
				case Input.Keys.S: MyGame.getClient().move(MoveDirection.DOWN,  true);	break;			
				case Input.Keys.A: MyGame.getClient().move(MoveDirection.LEFT,  true);	break;
				case Input.Keys.D: MyGame.getClient().move(MoveDirection.RIGHT, true);	break;			
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
			MyGame.getClient().attack();	
		}
		
		return !usingChat;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{ 
		return !usingChat; 
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) 
	{ 
		return !usingChat; 
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) 
	{
		return !usingChat; 
	}

	@Override
	public boolean scrolled(int amount)
	{
		return !usingChat; 
	}
	
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
					MyGame.getClient().sentChatMessage(chatText.getText());
					//chatText.setText("Type message...");
				}
			}
		});
		
		stage.addActor(chatSendText);
	}

}
