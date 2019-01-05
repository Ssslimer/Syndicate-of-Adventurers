package com.moag.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.moag.game.ClientConnection;
import com.moag.game.SyndicateOfAdventurers;
import com.moag.game.networking.MessageStatus;
import com.moag.game.util.GdxUtils;

public class JoinServerScreen implements Screen
{
	private SyndicateOfAdventurers game;
	private Stage stage;
	
	private Skin skin;
	private TextField playerNameField, passwordField;
	private TextButton loginButton, registerButton, backButton;
	
	private final String ip;
	private final int port;
	
	public JoinServerScreen(SyndicateOfAdventurers game, String ip, int port)
	{	
		this.game = game;
		this.ip = ip;
		this.port = port;
		
		stage = new Stage();
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		setupPlayerNameTextField();
		setupPasswordTextField();
		
		setupLoginButton();
		setupRegisterButton();
		setupBackButton();
	}
	
	@Override
	public void show() {}

	@Override
	public void render(float delta) 
	{
		GdxUtils.clearScreen(Color.WHITE);
		stage.act();
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
	
	private void setupPlayerNameTextField()
	{
		TextFieldStyle style = new TextFieldStyle();
		style.fontColor = Color.GREEN;
		style.font = new BitmapFont();
		
		playerNameField = new TextField("", style);
		playerNameField.setText("Login...");
		playerNameField.setWidth(200);
		playerNameField.setHeight(50);
		float posX = (Gdx.graphics.getWidth()-playerNameField.getWidth()) / 2f;
		float posY = (Gdx.graphics.getHeight()-playerNameField.getHeight()) / 2f + 100;
		playerNameField.setPosition(posX, posY);
		
		Pixmap background = new Pixmap((int)posX, (int)posY, Pixmap.Format.RGB888);
		background.setColor(Color.BLUE);
		background.fill();
		
		playerNameField.getStyle().background = new Image(new Texture(background)).getDrawable();
		
		playerNameField.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
		    {
				playerNameField.setText("");
		    }
		});
		
		stage.addActor(playerNameField);
	}

	private void setupPasswordTextField()
	{
		TextFieldStyle style = new TextFieldStyle();
		style.fontColor = Color.GREEN;
		style.font = new BitmapFont();
		
		passwordField = new TextField("", style);
		passwordField.setText("Password...");
		passwordField.setPasswordCharacter('*');
		passwordField.setWidth(200);
		passwordField.setHeight(50);
		float posX = (Gdx.graphics.getWidth()-passwordField.getWidth()) / 2f;
		float posY = (Gdx.graphics.getHeight()-passwordField.getHeight()) / 2f + 30;
		passwordField.setPosition(posX, posY);
		
		Pixmap background = new Pixmap((int)posX, (int)posY, Pixmap.Format.RGB888);
		background.setColor(Color.BLUE);
		background.fill();
		
		passwordField.getStyle().background = new Image(new Texture(background)).getDrawable();

		passwordField.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
		    {
				passwordField.setText("");
		    }
		});

		passwordField.setTextFieldListener(new TextFieldListener()
		{
		    @Override
		    public void keyTyped(TextField textField, char key)
		    {
		    	passwordField.setPasswordMode(true);
		    }
		});
		
		stage.addActor(passwordField);
	}

	private void setupLoginButton()
	{				
		loginButton = new TextButton("Login", skin, "default");
		loginButton.setWidth(200);
		loginButton.setHeight(50);
		float posX = (Gdx.graphics.getWidth()-loginButton.getWidth()) / 2f;
		float posY = (Gdx.graphics.getHeight()-loginButton.getHeight()) / 2f - 60;
		loginButton.setPosition(posX, posY);
		
		Dialog dialog = new Dialog("Login error", skin, "dialog");	
		dialog.button("OK");
		dialog.key(Keys.ENTER, null);
		dialog.setMovable(false);
		
		loginButton.addListener(new ClickListener()
	    {
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				String login = playerNameField.getText();
				String password = passwordField.getText();
				
				SyndicateOfAdventurers.setClient(new ClientConnection(ip, port));
				MessageStatus status = SyndicateOfAdventurers.getClient().login(login, password);
				if(status == MessageStatus.OK)
				{
					game.setScreen(new GameScreen(game));	
				}
				else
				{
					dialog.text(status.name()); /** TODO add translations? */
					dialog.show(stage);
				}			
			}
	    });
		
		stage.addActor(loginButton);
	}
	
	private void setupRegisterButton()
	{				
		registerButton = new TextButton("Register", skin, "default");
		registerButton.setWidth(200);
		registerButton.setHeight(50);
		float posX = (Gdx.graphics.getWidth()-registerButton.getWidth()) / 2f;
		float posY = (Gdx.graphics.getHeight()-registerButton.getHeight()) / 2f - 130;
		registerButton.setPosition(posX, posY);
		
		Dialog dialog = new Dialog("Registration error", skin, "dialog");	
		dialog.button("OK");
		dialog.key(Keys.ENTER, null);
		dialog.setMovable(false);
		
		registerButton.addListener(new ClickListener()
	    {
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				String login = playerNameField.getText();
				String password = passwordField.getText();
	
				SyndicateOfAdventurers.setClient(new ClientConnection(ip, port));
				MessageStatus status = SyndicateOfAdventurers.getClient().register(login, password);
				if(status == MessageStatus.OK) return;
				
				dialog.text(status.name()); /** TODO add translations? */
				dialog.show(stage);
			}
	    });
		
		stage.addActor(registerButton);		
	}
	
	private void setupBackButton()
	{
		backButton = new TextButton("Back to main menu", skin, "default");
		backButton.setWidth(200);
		backButton.setHeight(50);
		float posX = (Gdx.graphics.getWidth()-backButton.getWidth()) / 2f;
		float posY = (Gdx.graphics.getHeight()-backButton.getHeight()) / 2f - 200;
		backButton.setPosition(posX, posY);
		
		backButton.addListener(new ClickListener()
	    {
			@Override
		    public void clicked(InputEvent event, float x, float y)
		    {
		    	game.setScreen(new MainMenuScreen(game));
		    }
	    });

		stage.addActor(backButton);
	}
}
