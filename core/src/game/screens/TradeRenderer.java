package screens;

import java.nio.file.Paths;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import client.MyGame;
import entities.EntityPlayer;
import entities.Item;
import entities.ItemButton;
import trade.TradeState;
import util.ConfigConstants;

public class TradeRenderer 
{
	private final float TRADE_SCREEN_WIDTH;
	private final float TRADE_SCREEN_HEIGHT;
	
	private Stage sellingStage;
	private Stage offerStage;
	private Stage decisionStage;
	private Texture tradeWindowTexture;
	private SpriteBatch batch;
	
	private Skin skin;
	private BitmapFont tradeFont;
	
	private TextButton startTradeBtn;
	private TextButton offerBtn;
	private TextButton acceptOffertBtn;
	private TextButton declineOffertBtn;
	private TextButton endTradeBtn;
	private TextButton endOfferBtn;

	private TextButton sellItemBtn;
	private Item tradingItem;
	
	private EntityPlayer trader;
	
	private boolean wasStartTradeMessageSent = false;
		
	public TradeRenderer(float width, float height, Stage sellingStage, Stage offerStage, Stage decisionStage)
	{
		TRADE_SCREEN_WIDTH = width;
		TRADE_SCREEN_HEIGHT = height;

		this.sellingStage = sellingStage;
		this.offerStage = offerStage;
		this.decisionStage = decisionStage;
		
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
		setupEndOfferButton();
		setupSellItemButton();
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
		
		sellingStage.act();
		sellingStage.draw();	
		//stage.clear();
	}
	
	private void renderBuying()
	{
		String traderName = ((EntityPlayer)MyGame.getGameMap().getEntity(trader.getTradingWithId())).getLogin();
		
		batch.begin();
		batch.draw(tradeWindowTexture, 0, 0, TRADE_SCREEN_WIDTH, TRADE_SCREEN_HEIGHT);
		tradeFont.draw(batch, "Trading with " + traderName, TRADE_SCREEN_WIDTH/10f, (TRADE_SCREEN_HEIGHT * 9f)/10f);
		batch.end();
		
		offerStage.act();
		offerStage.draw();
	}
	
	private void renderOffer()
	{		
		
		decisionStage.act();
		decisionStage.draw();
	}
	
	public Stage getSellingStage()
	{
		return sellingStage;
	}
	
	public Stage getOfferStage()
	{
		return offerStage;
	}
	
	public Stage getDecisionStage()
	{
		return decisionStage;
	}
	
	private void setupStartTradeButton()
	{
		startTradeBtn = new TextButton("Start", skin);
		startTradeBtn.setWidth(TRADE_SCREEN_WIDTH/7f);
		startTradeBtn.setHeight(TRADE_SCREEN_HEIGHT/10f);
		
		float posX = 4 * TRADE_SCREEN_WIDTH / 7f;	
		float posY = TRADE_SCREEN_HEIGHT/10f;
		
		startTradeBtn.setPosition(posX, posY);
		
		startTradeBtn.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				if(trader.getTradeState() == TradeState.SELLING)
				{
					/** TODO send start trade message*/
				}
			}
		});
		
		sellingStage.addActor(startTradeBtn);
	}
	
	private void setupOfferButton()
	{
		offerBtn = new TextButton("Offer", skin);
		offerBtn.setWidth(startTradeBtn.getWidth());
		offerBtn.setHeight(startTradeBtn.getHeight());
		
		float posX = startTradeBtn.getX();	
		float posY = startTradeBtn.getY();
		
		offerBtn.setPosition(posX, posY);
		
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
		
		offerStage.addActor(offerBtn);
	}
	
	private void setupAcceptOffertButton()
	{
		acceptOffertBtn = new TextButton("Accept", skin);
		acceptOffertBtn.setWidth(startTradeBtn.getWidth());
		acceptOffertBtn.setHeight(startTradeBtn.getHeight());
		
		float posX = startTradeBtn.getX();	
		float posY = startTradeBtn.getY();
		
		acceptOffertBtn.setPosition(posX, posY);
		
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
		
		decisionStage.addActor(acceptOffertBtn);
	}
	
	private void setupDeclineOffertButton()
	{
		declineOffertBtn = new TextButton("Decline", skin);
		declineOffertBtn.setWidth(startTradeBtn.getWidth());
		declineOffertBtn.setHeight(startTradeBtn.getHeight());
		
		float posX = 2 * TRADE_SCREEN_WIDTH / 7f;	
		float posY = startTradeBtn.getY();
		
		declineOffertBtn.setPosition(posX, posY);
		
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
		
		decisionStage.addActor(declineOffertBtn);
	}
	
	private void setupEndTradeButton()
	{
		endTradeBtn = new TextButton("Exit", skin);
		endTradeBtn.setWidth(startTradeBtn.getWidth());
		endTradeBtn.setHeight(startTradeBtn.getHeight());
		
		float posX = 2 * TRADE_SCREEN_WIDTH / 7f;
		float posY = startTradeBtn.getY();
		
		endTradeBtn.setPosition(posX, posY);
		
		endTradeBtn.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				/** TODO send stop trade message*/
				if(wasStartTradeMessageSent)
				{
					
				}
				GameScreen.isTrading = false;
				trader.setTrateState(TradeState.NOT_TRADING);
			}
		});
		
		sellingStage.addActor(endTradeBtn);
	}
	
	private void setupEndOfferButton()
	{
		endOfferBtn = new TextButton("Exit", skin);
		endOfferBtn.setWidth(startTradeBtn.getWidth());
		endOfferBtn.setHeight(startTradeBtn.getHeight());
		
		float posX = 2 * TRADE_SCREEN_WIDTH / 7f;
		float posY = startTradeBtn.getY();
		
		endOfferBtn.setPosition(posX, posY);
		
		endOfferBtn.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				GameScreen.isTrading = false;
				trader.setTrateState(TradeState.NOT_TRADING);
			}
		});
		
		offerStage.addActor(endOfferBtn);
	}
	
	private void setupSellItemButton()
	{
		sellItemBtn = new TextButton("Item...", skin);
		sellItemBtn.setWidth(startTradeBtn.getWidth() * 2);
		sellItemBtn.setHeight(startTradeBtn.getHeight());
		
		float posX = 5 * TRADE_SCREEN_WIDTH / 14;
		float posY = TRADE_SCREEN_HEIGHT / 2;
		
		sellItemBtn.setPosition(posX, posY);
		
		sellItemBtn.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				setupItemButtons();
			}
		});
		
		sellingStage.addActor(sellItemBtn);
	}
	
	private void setupItemButtons()
	{
		clearStageFromItemButtons();
			
		List<Item> items = trader.getItems();	
		
		int index = 1;
		for(Item item : items)
		{
			ItemButton button = new ItemButton(skin, item);
			
			button.setWidth(startTradeBtn.getWidth() * 2);
			button.setHeight(startTradeBtn.getHeight());
			
			float posX = 5 * TRADE_SCREEN_WIDTH / 7f;
			float posY = TRADE_SCREEN_HEIGHT - (index * button.getHeight());
			button.setPosition(posX, posY);
			
			button.addListener(new ClickListener()
			{
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					tradingItem = button.getItem();
					sellItemBtn.setText(tradingItem.getType().toString() + "(" + tradingItem.getAttack() + "," + tradingItem.getDefence() + "," + tradingItem.getHPBonus() + ")");
				}
			});
			
			sellingStage.addActor(button);
			index++;
		}
	}
	
	private void clearStageFromItemButtons()
	{
		for(Actor actor : sellingStage.getActors())
		{
			if(actor instanceof ItemButton)
			{
				actor.remove();
			}
		}
	}
}
