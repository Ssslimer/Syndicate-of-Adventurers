package com.moag.game.Screens;

import java.util.regex.Pattern;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.moag.game.Client;
import com.moag.game.SyndicateOfAdventurers;
import com.moag.game.utils.GdxUtils;

public class MainMenuScreen implements Screen
{
	private SyndicateOfAdventurers game;	
	private Stage stage;
	
	private Skin skin;
	private TextField serverIPField, serverPortField;
	private TextButton joinServerButton;

	public MainMenuScreen(SyndicateOfAdventurers game)
	{
		this.game = game;
		stage = new Stage(new ScreenViewport());
		
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		setupServerIPField();
		setupServerPortField();
		setupJoinServerButton();
	}


	@Override
	public void show() {}

	@Override
	public void render(float delta) 
	{
		GdxUtils.clearScreen(Color.WHITE);
		stage.draw();
		Gdx.input.setInputProcessor(stage);		
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
		skin.dispose();
		stage.dispose();	
	}
	
	private void setupServerIPField()
	{
		TextFieldStyle style = new TextFieldStyle();
		style.fontColor = Color.GREEN;
		style.font = new BitmapFont();
		
		serverIPField = new TextField("", style);
		serverIPField.setText("Server IP...");
		serverIPField.setWidth(200);
		serverIPField.setHeight(50);
		float posX = (Gdx.graphics.getWidth()-serverIPField.getWidth()) / 2f;
		float posY = (Gdx.graphics.getHeight()-serverIPField.getHeight()) / 2f;
		serverIPField.setPosition(posX, posY);
		
		Pixmap background = new Pixmap((int)posX, (int)posY, Pixmap.Format.RGB888);
		background.setColor(Color.BLUE);
		background.fill();
		
		serverIPField.getStyle().background = new Image(new Texture(background)).getDrawable();
		
		serverIPField.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
		    {
				serverIPField.setText("");
		    }
		});
		
		stage.addActor(serverIPField);
	}
	
	private void setupServerPortField()
	{
		TextFieldStyle style = new TextFieldStyle();
		style.fontColor = Color.GREEN;
		style.font = new BitmapFont();
		
		serverPortField = new TextField("", style);
		serverPortField.setText("Port...");
		serverPortField.setWidth(200);
		serverPortField.setHeight(50);
		float posX = (Gdx.graphics.getWidth()-serverPortField.getWidth()) / 2f;
		float posY = (Gdx.graphics.getHeight()-serverPortField.getHeight()) / 2f - 100;
		serverPortField.setPosition(posX, posY);
		
		Pixmap background = new Pixmap((int)posX, (int)posY, Pixmap.Format.RGB888);
		background.setColor(Color.BLUE);
		background.fill();
		
		serverPortField.getStyle().background = new Image(new Texture(background)).getDrawable();
		
		serverPortField.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
		    {
				serverPortField.setText("");
		    }
		});
		
		stage.addActor(serverPortField);
	}
	
	private void setupJoinServerButton()
	{
		joinServerButton = new TextButton("Join server", skin, "default");
		joinServerButton.setWidth(200);
		joinServerButton.setHeight(50);
		float posX = (Gdx.graphics.getWidth()-joinServerButton.getWidth()) / 2f;
		float posY = (Gdx.graphics.getHeight()-joinServerButton.getHeight()) / 2f - 200;
		joinServerButton.setPosition(posX, posY);
		
		joinServerButton.addListener(new ClickListener()
	    {
			/** TODO add popup messages when strings are incorrect */
		    @Override
		    public void clicked(InputEvent event, float x, float y)
		    {
		    	String ip = serverIPField.getText();
		    	/** ADD POPUP */
		    	if(!isIPValid(ip)) return;
		    	
		    	int port = -1;		    	
				try
				{
					port = getPort(serverPortField.getText());
				}
				catch(NumberFormatException e)
				{
					/** ADD POPUP */
					return;
				}
				catch(PortFormatException e)
				{
					/** ADD POPUP */
					return;
				}
		    	
		    	Client client = new Client(ip, port);
		    	game.setScreen(new JoinServerScreen(game, client));
		    }

		    // https://www.mkyong.com/regular-expressions/how-to-validate-ip-address-with-regular-expression/
		    private static final String IPADDRESS_PATTERN = 
		    		"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		    		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		    		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		    		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
		    
		    private boolean isIPValid(String ip)
		    {
		    	return Pattern.matches(IPADDRESS_PATTERN, ip);
		    }
		    
		    private int getPort(String s) throws NumberFormatException, PortFormatException
		    {
		    	int port = Integer.parseInt(s);
		    	
		    	if(port < 0 || port > 65535) throw new PortFormatException();
		    	
		    	return port;
		    }
	    });
		stage.addActor(joinServerButton);
	}
}
