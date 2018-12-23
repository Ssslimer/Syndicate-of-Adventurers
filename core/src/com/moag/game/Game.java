package com.moag.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.moag.game.gui.GuiMainMenu;

public class Game extends ApplicationAdapter
{
	private Stage stage;

	@Override
	public void create()
	{
		GuiMainMenu mainMenu = new GuiMainMenu();
		stage = mainMenu.getStage();
	    Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render()
	{
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}
}
