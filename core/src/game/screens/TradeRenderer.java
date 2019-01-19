package screens;

import java.nio.file.Paths;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import client.MyGame;
import entities.EntityPlayer;
import trade.TradeState;

public class TradeRenderer 
{
	private final float TRADE_SCREEN_WIDTH;
	private final float TRADE_SCREEN_HEIGHT;
	
	private Stage stage;
	private Texture tradeWindowTexture;
	private SpriteBatch batch;
	
	private Skin skin;
	private BitmapFont tradeFont;
	
	private TextButton startTradeBtn;
	private TextButton offerBtn;
	private TextButton acceptOffertBtn;
	private TextButton declineOffertBtn;
	private TextButton endTradeBtn;
	
	private EntityPlayer trader;
		
	public TradeRenderer(float width, float height)
	{
		TRADE_SCREEN_WIDTH = width;
		TRADE_SCREEN_HEIGHT = height;
		
		stage = new Stage();
		tradeWindowTexture = new Texture(Gdx.files.getFileHandle(Paths.get("assets", "textures", "gui", "chatbackground.png").toString(), FileType.Internal));
		batch = new SpriteBatch();
				
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		tradeFont = new BitmapFont();
		
		trader = MyGame.getPlayer();
		
		setupStartTradeButton();
		setupOfferButton();
		setupAcceptOffertButton();
		setupDeclineOffertButton();
		setupEndTradeButton();
	}
	
	public void render()
	{
		if(trader.getTradeState() != TradeState.NOT_TRADING)
		{
			if(trader.getTradeState() == TradeState.SELLING) renderSelling();
			else if(trader.getTradeState() == TradeState.BUYING) renderBuying();	
		}
		
		if(trader.getHasOffer())
		{
			renderOffer();
		}
	}
	
	private void renderSelling()
	{
		batch.begin();
		batch.draw(tradeWindowTexture, 0, 0, TRADE_SCREEN_WIDTH, TRADE_SCREEN_HEIGHT);
		batch.end();
		
		stage.addActor(startTradeBtn);
		stage.addActor(endTradeBtn);
		
		stage.act();
		stage.draw();	
		stage.clear();
	}
	
	private void renderBuying()
	{
		String traderName = ((EntityPlayer)MyGame.getGameMap().getEntity(trader.getTradingWithId())).getLogin();
		
		batch.begin();
		batch.draw(tradeWindowTexture, 0, 0, TRADE_SCREEN_WIDTH, TRADE_SCREEN_HEIGHT);
		tradeFont.draw(batch, "Trading with " + traderName, TRADE_SCREEN_WIDTH/10f, (TRADE_SCREEN_HEIGHT * 9f)/10f);
		batch.end();
		
		stage.addActor(offerBtn);
		stage.addActor(endTradeBtn);
		
		stage.act();
		stage.draw();	
		stage.clear();
	}
	
	private void renderOffer()
	{
		stage.addActor(acceptOffertBtn);
		stage.addActor(declineOffertBtn);
		
		stage.act();
		stage.draw();	
		stage.clear();
	}
	
	public Stage getStage()
	{
		return stage;
	}
	
	private void setupStartTradeButton()
	{
		startTradeBtn = new TextButton("Start", skin);
		//setup pos and size
		
		startTradeBtn.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				if(trader.getTradeState() == TradeState.SELLING)
				{
					//send start trade;
				}
			}
		});
		
		stage.addActor(startTradeBtn);
	}
	
	private void setupOfferButton()
	{
		offerBtn = new TextButton("Offer", skin);
		
		offerBtn.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				if(trader.getTradeState() == TradeState.BUYING)
				{
					//check for correctness and send offer message
				}
			}
		});
		
		stage.addActor(offerBtn);
	}
	
	private void setupAcceptOffertButton()
	{
		acceptOffertBtn = new TextButton("Accept", skin);
		
		acceptOffertBtn.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				if(trader.getTradeState() == TradeState.SELLING)
				{
					//send accept message
				}
			}
		});
		
		stage.addActor(acceptOffertBtn);
	}
	
	private void setupDeclineOffertButton()
	{
		declineOffertBtn = new TextButton("Decline", skin);
		
		declineOffertBtn.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				if(trader.getTradeState() == TradeState.SELLING)
				{
					//send decline message
				}
			}
		});
		
		stage.addActor(declineOffertBtn);
	}
	
	private void setupEndTradeButton()
	{
		endTradeBtn = new TextButton("Exit", skin);
		
		startTradeBtn.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				if(trader.getTradeState() == TradeState.SELLING)
				{
					//finish trade stop selling
				}
				else if(trader.getTradeState() == TradeState.BUYING)
				{
					//close trade window maybe only if you didn't put any offer?
				}
			}
		});
		
		stage.addActor(endTradeBtn);
	}
}
