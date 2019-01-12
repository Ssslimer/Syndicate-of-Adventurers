package screens;

import java.nio.file.Paths;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import client.ClientConnection;
import client.MyGame;
import util.GdxUtils;
import util.ServerAddressValidator;

public class MainMenuScreen implements Screen
{
	private MyGame game;	
	private Stage stage;
	
	private Skin skin;
	private TextField serverIPField, serverPortField;
	private TextButton joinServerButton, quitButton;
	
	private final Texture background;
	private final SpriteBatch spriteBatch = new SpriteBatch();
		
	private Sound CLANG;
	
	public MainMenuScreen(MyGame game)
	{
		this.game = game;
		stage = new Stage(new ScreenViewport());
		
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		background = MyGame.getResources().getTexture("GUI_BACKGROUND");
		CLANG = Gdx.audio.newSound(Gdx.files.getFileHandle(Paths.get("assets", "sounds", "clangberserk.wav").toString(), FileType.Internal));
		
		setupServerIPField();
		setupServerPortField();
		setupJoinServerButton();
		setupQuitButton();
	}

	@Override
	public void show() {}
	
	@Override
	public void resize(int width, int height) {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void render(float delta) 
	{
		GdxUtils.clearScreen(Color.WHITE);
		spriteBatch.begin();
		spriteBatch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		spriteBatch.end();
		stage.act();
		stage.draw();
		Gdx.input.setInputProcessor(stage);
	}

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
		float posY = (Gdx.graphics.getHeight()-serverIPField.getHeight()) / 2f + 100;
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
		float posY = (Gdx.graphics.getHeight()-serverPortField.getHeight()) / 2f;
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
		float posY = (Gdx.graphics.getHeight()-joinServerButton.getHeight()) / 2f - 100;
		joinServerButton.setPosition(posX, posY);
		
		joinServerButton.addListener(new ClickListener()
	    {
		    @Override
		    public void clicked(InputEvent event, float x, float y)
		    {
		    	CLANG.play(1.0f);
		    	String ip = serverIPField.getText();
		    	if(!ServerAddressValidator.isIPAddressCorrect(ip))
		    	{
					Dialog dialog = new Dialog("Wrong IP address", skin, "dialog");	
					dialog.button("OK");
					dialog.key(Keys.ENTER, null);
					dialog.setMovable(false);
					dialog.text("Incorrect IP address format"); /** TODO add translations? */
					dialog.show(stage);
					
		    		return;
		    	}
		    	
		    	if(!ServerAddressValidator.isPortCorrect(serverPortField.getText()))
		    	{
					Dialog dialog = new Dialog("Wrong port", skin, "dialog");	
					dialog.button("OK");
					dialog.key(Keys.ENTER, null);
					dialog.setMovable(false);
					dialog.text("Port must be a number from range 0 to 65535"); /** TODO add translations? */
					dialog.show(stage);
					return;
		    	}
		    	
		    	int port = Integer.parseInt(serverPortField.getText());	
		    	
		    	ClientConnection connection = new ClientConnection(ip, port);
		    	if(!connection.isServerOnline())
		    	{
					Dialog dialog = new Dialog("Connection error", skin, "dialog");	
					dialog.button("OK");
					dialog.key(Keys.ENTER, null);
					dialog.setMovable(false);
					dialog.text("Server offline"); /** TODO add translations? */
					dialog.show(stage);
					return;
		    	}
	    	
		    	game.setScreen(new JoinServerScreen(game, ip, port));
		    	dispose();
		    }
	    });
		stage.addActor(joinServerButton);
	}
	
	private void setupQuitButton()
	{
		quitButton = new TextButton("Quit", skin, "default");
		quitButton.setWidth(200);
		quitButton.setHeight(50);
		float posX = (Gdx.graphics.getWidth()-quitButton.getWidth()) / 2f;
		float posY = (Gdx.graphics.getHeight()-quitButton.getHeight()) / 2f - 200;
		quitButton.setPosition(posX, posY);
		
		quitButton.addListener(new ClickListener()
	    {
		    @Override
		    public void clicked(InputEvent event, float x, float y)
		    {
		    	Gdx.app.exit();
		    }
	    });
		
		stage.addActor(quitButton);
	}
}
