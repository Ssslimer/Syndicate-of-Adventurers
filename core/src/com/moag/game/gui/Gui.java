package com.moag.game.gui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Gui
{
	protected final Stage stage, previousStage;
	
	public Gui(Stage previousStage)
	{
		this.stage = new Stage(new ScreenViewport());
		this.previousStage = previousStage;
	}
	
	public Stage getStage()
	{
		return stage;
	}
	
	public Stage getPreviousStage()
	{
		return previousStage;
	}

}
