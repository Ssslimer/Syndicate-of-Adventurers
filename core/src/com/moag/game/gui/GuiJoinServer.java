package com.moag.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.moag.game.Game;

public class GuiJoinServer extends Gui
{
	private Skin skin;

	private TextField playerNameField, passwordField;
	private TextButton loginButton, registerButton, backButton;
	
	public GuiJoinServer(Stage previousStage)
	{
		super(previousStage);
		
		this.skin = new Skin(Gdx.files.internal("uiskin.json"));

		setupPlayerNameTextField();
		setupPasswordTextField();
		
		setupLoginButton();
		setupRegisterButton();
		setupBackButton();
	}
	
	private void setupPlayerNameTextField()
	{
		TextFieldStyle style = new TextFieldStyle();
		style.fontColor = Color.RED;
		style.font = new BitmapFont();
		
		playerNameField = new TextField("", style);
		playerNameField.setText("Player name");
		playerNameField.setWidth(200);
		playerNameField.setHeight(50);
		float posX = (Gdx.graphics.getWidth()-playerNameField.getWidth()) / 2f;
		float posY = (Gdx.graphics.getHeight()-playerNameField.getHeight()) / 2f + 100;
		playerNameField.setPosition(posX, posY);
		stage.addActor(playerNameField);
	}

	private void setupPasswordTextField()
	{
		TextFieldStyle style = new TextFieldStyle();
		style.fontColor = Color.RED;
		style.font = new BitmapFont();
		
		passwordField = new TextField("", style);
		passwordField.setText("Server IP");
		passwordField.setWidth(200);
		passwordField.setHeight(50);
		float posX = (Gdx.graphics.getWidth()-passwordField.getWidth()) / 2f;
		float posY = (Gdx.graphics.getHeight()-passwordField.getHeight()) / 2f;
		passwordField.setPosition(posX, posY);
		stage.addActor(passwordField);
	}

	private void setupLoginButton()
	{				
		loginButton = new TextButton("Login", skin, "default");
		loginButton.setWidth(200);
		loginButton.setHeight(50);
		float posX = (Gdx.graphics.getWidth()-loginButton.getWidth()) / 2f;
		float posY = (Gdx.graphics.getHeight()-loginButton.getHeight()) / 2f - 200;
		loginButton.setPosition(posX, posY);
		
		loginButton.addListener(new EventListener()
	    {
			@Override
			public boolean handle(Event event)
			{
				return true;
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
		float posY = (Gdx.graphics.getHeight()-registerButton.getHeight()) / 2f - 200;
		registerButton.setPosition(posX, posY);

		registerButton.addListener(new EventListener()
	    {
			@Override
			public boolean handle(Event event)
			{
				return true;
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
		    	Game.openGui(new GuiMainMenu());
		    }
	    });

		stage.addActor(backButton);
	}
}
