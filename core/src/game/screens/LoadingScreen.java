package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

import client.SyndicateOfAdventurers;
import util.GdxUtils;

public class LoadingScreen implements Screen
{
	private SyndicateOfAdventurers game;
	private Stage stage;
	
	private final SpriteBatch spriteBatch = new SpriteBatch();
	private Texture background;
	private Label label;
	
	public LoadingScreen(SyndicateOfAdventurers game)
	{
		this.game = game;
		stage = new Stage();
		
		background = SyndicateOfAdventurers.getResources().getTexture("GUI_BACKGROUND");
		setupLabel();
	}

	@Override
	public void show() {}

	@Override
	public void render(float delta) 
	{	
		GdxUtils.clearScreen(Color.WHITE);
		spriteBatch.begin();
		spriteBatch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		spriteBatch.end();
		
		stage.act();
		stage.draw();
		
		if(finishedLoading())
		{
			game.setScreen(new GameScreen(game));
			this.dispose();
			return;
		}
	}

	private boolean finishedLoading() 
	{
		return SyndicateOfAdventurers.getGameMap() != null;
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
	public void dispose() { stage.dispose(); }

	private void setupLabel()
	{
		LabelStyle style = new LabelStyle();
		style.fontColor = Color.WHITE;
		style.font = new BitmapFont();
		style.font.getData().setScale(2f);
		
		Label label = new Label("", style);
		label.setText("Loading game, please wait...");
		label.setWidth(400);
		label.setHeight(100);

		float x = (Gdx.graphics.getWidth() - label.getWidth()) / 2f + 20f;
		float y = (Gdx.graphics.getHeight()-label.getHeight()) / 2f - 100f;
		label.setPosition(x, y);
		
		stage.addActor(label);
	}
}
