package screens;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
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
	private final float CHAT_WIDTH, CHAT_HEIGHT;
	
	private InputMultiplexer inputMultiplexer;
	private Stage stage, tradeStage, tradeOfferStage, tradeDecisionStage;
	      
	private Dialog respawnDialog;
	
    private SpriteBatch spriteBatch;
    private Texture chatTexture;
    
    private Skin skin;
    private TextField chatText;
    private TextButton chatSendText;
    private BitmapFont chatFont;
    
    private boolean usingChat, displayingChat;
    
    public static boolean isTrading = false;
    
    private TradeRenderer tradeRenderer;
    
    private float timer; // for ping
    
    private MyGame game;
	
	public GameScreen(MyGame game)
	{	
		this.game = game;
		stage = new Stage();
		tradeStage = new Stage();
		tradeOfferStage = new Stage();
		tradeDecisionStage = new Stage();
		inputMultiplexer = new InputMultiplexer();	
    	
    	spriteBatch = new SpriteBatch();
    	chatTexture = MyGame.getResources().getTexture("GUI_CHAT_WINDOW");
    	
    	CHAT_WIDTH = ConfigConstants.WIDTH/4;
    	CHAT_HEIGHT = ConfigConstants.HEIGHT/2;
    	
    	chatFont = new BitmapFont();
    	
    	skin = new Skin(Gdx.files.internal("uiskin.json"));
    	setupChatTextField();
    	setupChatSendTextButton();
    	setupRespawnDialog();
    	
		inputMultiplexer.addProcessor(this);
		inputMultiplexer.addProcessor(stage);
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
      
        MyGame.getRenderer().render();
        if(displayingChat) renderChat();
        
        if(isTrading) tradeRenderer.render();
               
        if(MyGame.getPlayer() != null && !MyGame.getPlayer().isAlive())
        {
    		respawnDialog.show(stage);
    		MyGame.setupPlayer(null);
        }
        
		stage.act();
		stage.draw();
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
	
	private void setupRespawnDialog()
	{	
		respawnDialog = new Dialog("You have been killed", skin, "dialog")
		{
			protected void result(Object object)
			{
				switch((RespawnChoice) object)
				{
					case RESPAWN:
						
					break;
					
					case QUIT_SERVER:
						MyGame.getClient().stopConnection();
						game.setScreen(new MainMenuScreen(game));
			    		dispose();
					break;
				}
	        }
		};
		
		respawnDialog.button("Respawn", RespawnChoice.RESPAWN);
		respawnDialog.button("Quit game", RespawnChoice.QUIT_SERVER);
		respawnDialog.setMovable(false);
	}
	
	private enum RespawnChoice
	{
		RESPAWN, QUIT_SERVER
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
					if(tradeRenderer == null) tradeRenderer = new TradeRenderer(CHAT_WIDTH, CHAT_HEIGHT, tradeStage, tradeOfferStage, tradeDecisionStage); 
					MyGame.getPlayer().setTradeState(TradeState.STARTING_SELLING);
					isTrading = true;
				break;
				
				case Input.Keys.Y:

						if(tradeRenderer == null) tradeRenderer = new TradeRenderer(CHAT_WIDTH, CHAT_HEIGHT, tradeStage, tradeOfferStage, tradeDecisionStage); 
						
						EntityPlayer seller = MyGame.getGameMap().findClosestTradingEntity(MyGame.getPlayer().getPosition());
						if(seller != null)
						{
							MyGame.getPlayer().setTradingWithId(seller.getId());
							MyGame.getPlayer().setTradeState(TradeState.BUYING);
							isTrading = true;
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
			if(MyGame.getPlayer().getTradeState() == TradeState.BUYING)
			{
				tradeOfferStage.touchDown(screenX, screenY, pointer, button);
				return true;
			}
			
			tradeStage.touchDown(screenX, screenY, pointer, button);
			return true;
		}
		else
		{
			usingChat = false;
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
			if(MyGame.getPlayer().getTradeState() == TradeState.BUYING)
			{
				tradeOfferStage.touchUp(screenX, screenY, pointer, button);
				return true;
			}
			
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
