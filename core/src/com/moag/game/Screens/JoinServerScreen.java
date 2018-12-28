package com.moag.game.Screens;

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
import com.moag.game.Client;
import com.moag.game.SyndicateOfAdventurers;
import com.moag.game.utils.GdxUtils;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class JoinServerScreen implements Screen
{
	private SyndicateOfAdventurers game;
	private Stage stage;
	
	private Skin skin;
	private TextField playerNameField, passwordField;
	private TextButton loginButton, registerButton, backButton;
	
	public JoinServerScreen(SyndicateOfAdventurers game, Client client)
	{	
		this.game = game;
		SyndicateOfAdventurers.setClient(client);
		
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
		
		loginButton.addListener(new ClickListener()
	    {
			@Override
			public void clicked(InputEvent event, float x, float y)
			{

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

		registerButton.addListener(new ClickListener()
	    {
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				String login = playerNameField.getText();
				char[] password = passwordField.getText().toCharArray();
				
				Argon2 argon2 = Argon2Factory.create();
				try
				{
				    String hashedPassword = argon2.hash(10, 65536, 1, password);

				    if (argon2.verify(hashedPassword, password))
				    {
				    	SyndicateOfAdventurers.getClient().register(login, hashedPassword);
				    }
				    else
				    {
				    	/** ADD POPUP? STH WITH PASSWORD? */
				    }
				}
				finally
				{
				    argon2.wipeArray(password);
				}
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
