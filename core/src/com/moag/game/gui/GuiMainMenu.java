package com.moag.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.moag.game.Client;
import com.moag.game.Game;

public class GuiMainMenu extends Gui
{
	private Skin skin;
	private TextField serverIPField, serverPortField;
	private TextButton joinServerButton;

	public GuiMainMenu()
	{
		super(null);
		skin = new Skin(Gdx.files.internal("uiskin.json"));

		setupServerIPTextField();
		setupServerPortTextField();
		setupJoinServerButton();
	}

	private void setupServerIPTextField()
	{
		TextFieldStyle style = new TextFieldStyle();
		style.fontColor = Color.RED;
		style.font = new BitmapFont();
		
		serverIPField = new TextField("", style);
		serverIPField.setText("Server IP");
		serverIPField.setWidth(200);
		serverIPField.setHeight(50);
		float posX = (Gdx.graphics.getWidth()-serverIPField.getWidth()) / 2f;
		float posY = (Gdx.graphics.getHeight()-serverIPField.getHeight()) / 2f;
		serverIPField.setPosition(posX, posY);
		stage.addActor(serverIPField);
	}
	
	private void setupServerPortTextField()
	{
		TextFieldStyle style = new TextFieldStyle();
		style.fontColor = Color.RED;
		style.font = new BitmapFont();
		
		serverPortField = new TextField("", style);
		serverPortField.setText("Port");
		serverPortField.setWidth(200);
		serverPortField.setHeight(50);
		float posX = (Gdx.graphics.getWidth()-serverPortField.getWidth()) / 2f;
		float posY = (Gdx.graphics.getHeight()-serverPortField.getHeight()) / 2f - 100;
		serverPortField.setPosition(posX, posY);
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
		    	Game.client = new Client(serverIPField.getText(), Integer.parseInt(serverPortField.getText()));
		    	Game.openGui(new GuiJoinServer(stage));
		    }
	    });
		
		stage.addActor(joinServerButton);
	}
		
	public Stage getStage()
	{
		return stage;
	}
}
