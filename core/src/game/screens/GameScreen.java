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
import entities.EntityPlayer;
import networking.MoveDirection;
import trade.TradeState;
import util.ConfigConstants;
import util.GdxUtils;

public class GameScreen implements Screen, InputProcessor 
{
	private final float CHAT_WIDTH;
	private final float CHAT_HEIGHT;
	
	private MyGame game;
	
	private InputMultiplexer inputMultiplexer;
	private Stage stage;
	private Stage tradeStage;
	      
    private SpriteBatch spriteBatch;
    private Texture chatTexture;
    
    private Skin skin;
    private TextField chatText;
    private TextButton chatSendText;
    private BitmapFont chatFont;
    
    private boolean usingChat;
    private boolean displayingChat;
    
    private boolean isTrading = false;
    
    private TradeRenderer tradeRenderer;
    
    private Sound CLANG;

    private float timer; // for ping
	
	public GameScreen(MyGame game)
	{	
		MyGame.loadPlayer();
		this.game = game;
		
		stage = new Stage();
		tradeStage = new Stage();
		inputMultiplexer = new InputMultiplexer();	
    	
    	spriteBatch = new SpriteBatch();
    	chatTexture = new Texture(Gdx.files.getFileHandle(Paths.get("assets", "textures", "gui", "chatbackground.png").toString(), FileType.Internal));
    	
    	CHAT_WIDTH = ConfigConstants.WIDTH/4;
    	CHAT_HEIGHT = ConfigConstants.HEIGHT/2;
    	
    	chatFont = new BitmapFont();
    	
    	skin = new Skin(Gdx.files.internal("uiskin.json"));
    	setupChatTextField();
    	setupChatSendTextButton();
    	
    	CLANG = Gdx.audio.newSound(Gdx.files.getFileHandle(Paths.get("assets", "sounds", "clangberserk.wav").toString(), FileType.Internal));
				
		inputMultiplexer.addProcessor(this);
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(tradeStage);
		//inputMultiplexer.addProcessor(tradeRenderer.getStage());
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
		GdxUtils.clearScreen();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
      
        //pingServer(delta);
        MyGame.getRenderer().render();
        if(displayingChat) renderChat();
        
        if(isTrading) tradeRenderer.render();     
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
		
		spriteBatch.draw(chatTexture, ConfigConstants.WIDTH - CHAT_WIDTH, 0, CHAT_WIDTH, CHAT_HEIGHT);
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
				
				case Input.Keys.C: displayingChat = !displayingChat; break;
				
				case Input.Keys.T: 
					tradeRenderer = new TradeRenderer(CHAT_WIDTH, CHAT_HEIGHT, tradeStage); 
					inputMultiplexer.addProcessor(tradeRenderer.getStage());
					Gdx.input.setInputProcessor(inputMultiplexer);
					MyGame.getPlayer().setTrateState(TradeState.SELLING);
					isTrading = true;
				break;
				
				case Input.Keys.Y:
					if(isTrading)
					{
						isTrading = false;
					}
					else
					{
						tradeRenderer = new TradeRenderer(CHAT_WIDTH, CHAT_HEIGHT, tradeStage); 
						inputMultiplexer.addProcessor(tradeRenderer.getStage());
						Gdx.input.setInputProcessor(inputMultiplexer);
						
						EntityPlayer seller = MyGame.getGameMap().findClosestTradingEntity(MyGame.getPlayer().getPosition());
						if(seller != null)
						{
							MyGame.getPlayer().setTradingWithId(seller.getId());
							MyGame.getPlayer().setTrateState(TradeState.BUYING);
							isTrading = true;
						}
					}
							
				break;
			}
		}
		
		if(usingChat)
		{
			if(keycode == Input.Keys.ENTER)
			{
				if(chatText.getText() != null && chatText.getText().compareTo("Type message...") != 0 && chatText.getText().compareTo("") != 0)
				{
					MyGame.getClient().sentChatMessage(chatText.getText());
					chatText.setText("");
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
		if(displayingChat && screenX >= ConfigConstants.WIDTH - CHAT_WIDTH &&
				screenY >= ConfigConstants.HEIGHT - CHAT_HEIGHT)
		{
			usingChat = true;
			stage.setKeyboardFocus(null);
		}
		else if(isTrading && screenX <= CHAT_WIDTH &&
				screenY >= ConfigConstants.HEIGHT - CHAT_HEIGHT)
		{
			tradeStage.touchDown(screenX, screenY, pointer, button);
			return true;
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
		if(isTrading && screenX <= CHAT_WIDTH &&
				screenY >= ConfigConstants.HEIGHT - CHAT_HEIGHT)
		{
			tradeStage.touchUp(screenX, screenY, pointer, button);
			return true;
		}
		
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
		
		chatText.setWidth((CHAT_WIDTH/10f)*8f);
		chatText.setHeight(CHAT_WIDTH / 10f);
		
		float posX = ConfigConstants.WIDTH - CHAT_WIDTH + 10f;
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
		chatSendText.setWidth(CHAT_WIDTH/8f);
		chatSendText.setHeight(chatText.getHeight());

		float posX = ConfigConstants.WIDTH - chatSendText.getWidth();	
		float posY = 10f;
		
		chatSendText.setPosition(posX, posY);
		
		chatSendText.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				if(chatText.getText() != null && chatText.getText().compareTo("Type message...") != 0 && chatText.getText().compareTo("") != 0)
				{
					MyGame.getClient().sentChatMessage(chatText.getText());
					chatText.setText("");
				}
			}
		});
		
		stage.addActor(chatSendText);
	}

}
