package screens;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import client.MyGame;
import entities.EntityPlayer;
import entities.Item;
import entities.ItemButton;
import trade.TradeState;

public class TradeRenderer 
{
	private final float TRADE_SCREEN_WIDTH;
	private final float TRADE_SCREEN_HEIGHT;
	
	private Stage sellingStage;
	private Stage offerStage;
	private Stage decisionStage;
	private Texture tradeWindowTexture;
	private SpriteBatch batch;
	
	private Dialog offerDialog;
	private boolean isDialogNotSetup = true;
	
	private Skin skin;
	private BitmapFont tradeFont;
	
	private TextButton startTradeBtn;
	private TextButton offerBtn;
	private TextButton endTradeBtn;
	private TextButton endOfferBtn;

	private TextButton sellItemBtn;
	private Item tradingItem;
	
	private TextButton toBuyItemBtn; //btn with item from seller
	private TextButton offerItemBtn;
	
	private Item sellerItem;
	private long sellerId;
	
	private Item buyerItem;
	
	public static EntityPlayer trader;
	
	private boolean wasStartTradeMessageSent = false;
	
	//offer variables
	private EntityPlayer seller;
	private boolean hasFoundSomeoneToTrade = false;
		
	public TradeRenderer(float width, float height, Stage sellingStage, Stage offerStage, Stage decisionStage)
	{
		TRADE_SCREEN_WIDTH = width;
		TRADE_SCREEN_HEIGHT = height;

		this.sellingStage = sellingStage;
		this.offerStage = offerStage;
		this.decisionStage = decisionStage;
		
		tradeWindowTexture = MyGame.getResources().getTexture("GUI_TRADE_WINDOW");
		batch = new SpriteBatch();
				
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		tradeFont = new BitmapFont();
		
		trader = MyGame.getPlayer();
		
		setupStartTradeButton();
		setupOfferButton();
		setupToBuyItemButton();
		setupOfferItemButton();
		
		setupEndTradeButton();
		setupEndOfferButton();
		setupSellItemButton();
	}
	
	public void render()
	{
		if(trader.getTradeState() != TradeState.NOT_TRADING)
		{
			if(trader.getTradeState() == TradeState.SELLING || trader.getTradeState() == TradeState.STARTING_SELLING) renderSelling();
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
		if(!hasFoundSomeoneToTrade)
		{
			seller = (EntityPlayer)MyGame.getGameMap().getEntity(trader.getTradingWithId());
			
			hasFoundSomeoneToTrade = true;
		}
		//String traderName = ((EntityPlayer)MyGame.getGameMap().getEntity(trader.getTradingWithId())).getLogin();
		
		String traderName = seller.getLogin();
		Item item = seller.getSellingOffer().getTraderItem();
		toBuyItemBtn.setText(item.getType().toString() + "(" + item.getAttack() + "," + item.getDefence() + "," + item.getHPBonus() + ")");
		
		batch.begin();
		batch.draw(tradeWindowTexture, 0, 0, TRADE_SCREEN_WIDTH, TRADE_SCREEN_HEIGHT);
		tradeFont.draw(batch, "Trading with " + traderName, TRADE_SCREEN_WIDTH/10f, (TRADE_SCREEN_HEIGHT * 9f)/10f);
		batch.end();
		
		offerStage.act();
		offerStage.draw();
	}
	
	private void renderOffer()
	{		
		if(isDialogNotSetup)
		{
			setupTradeOffer();
			//isDialogNotSetup = false;
		}
		
		offerDialog.show(decisionStage);
		
		decisionStage.act();
		decisionStage.draw();
	}
	
	private void setupTradeOffer() 
	{
		String buyerLogin = trader.getBuyingOffer().getLogin();
		Item buyerItem = trader.getBuyingOffer().getTraderItem();
		
		int attack = buyerItem.getAttack();
		int def = buyerItem.getDefence();
		int hp = buyerItem.getHPBonus();
		
		offerDialog = new Dialog("Player " + buyerLogin + " is offering you " + buyerItem.getType().toString() + "(" + attack + "," + def + "," + hp + ")", skin, "dialog")
		{
			protected void result(Object object)
			{
				System.out.println("INSIDE DIALOGGGGGG");
				switch((Decision) object)
				{
					case ACCEPT:
						System.out.println("ACCEPTED FOR FUCK SAKE");
						MyGame.getClient().sentTradeDecisionMessage(true, trader.getLogin(), trader.getBuyingOffer().getLogin(), trader.getBuyingOffer().getTraderItem(), tradingItem);
					break;
					
					case DECLINE:

					break;
				}
			}
		};
		
		
		
		offerDialog.button("Accept", Decision.ACCEPT);
		offerDialog.button("Decline", Decision.DECLINE);
		offerDialog.setMovable(false);		
	}
	
	private enum Decision
	{
		ACCEPT, DECLINE
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
				if(trader.getTradeState() == TradeState.STARTING_SELLING && tradingItem != null)
				{
					//trader.setTradeState(TradeState.SELLING); 
					MyGame.getClient().sentTradeStartMessage(tradingItem);
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
				if(trader.getTradeState() == TradeState.BUYING && seller.getSellingOffer().getTraderItem() != null && buyerItem != null)
				{
					if(hasFoundSomeoneToTrade)
					{
						String login = seller.getLogin();
						Item sellerItem = seller.getSellingOffer().getTraderItem();
						MyGame.getClient().sentTradeOfferMessage(trader.getLogin(), login, buyerItem, sellerItem);
					}
				}
			}
		});
		
		offerStage.addActor(offerBtn);
	}
	
	private void setupToBuyItemButton()
	{
		toBuyItemBtn = new TextButton("Item...", skin);
		toBuyItemBtn.setWidth(startTradeBtn.getWidth() * 2);
		toBuyItemBtn.setHeight(startTradeBtn.getHeight());
		
		float posX = 5 * TRADE_SCREEN_WIDTH / 14;
		float posY = TRADE_SCREEN_HEIGHT / 2;
		
		toBuyItemBtn.setPosition(posX, posY);
		
		offerStage.addActor(toBuyItemBtn);	
	}
	
	private void setupOfferItemButton()
	{
		offerItemBtn = new TextButton("Item...", skin);
		offerItemBtn.setWidth(startTradeBtn.getWidth() * 2);
		offerItemBtn.setHeight(startTradeBtn.getHeight());
		
		float posX = 0;
		float posY = TRADE_SCREEN_HEIGHT / 2;
		
		offerItemBtn.setPosition(posX, posY);
		
		offerItemBtn.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				setupOfferItemButtons();
			}
		});
		
		offerStage.addActor(offerItemBtn);
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
				trader.setTradeState(TradeState.NOT_TRADING);
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
				trader.setTradeState(TradeState.NOT_TRADING);
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
	
	private void setupOfferItemButtons()
	{
		clearStageFromOfferItemButtons();
		
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
					buyerItem = button.getItem();
					offerItemBtn.setText(buyerItem.getType().toString() + "(" + buyerItem.getAttack() + "," + buyerItem.getDefence() + "," + buyerItem.getHPBonus() + ")");
				}
			});
			
			offerStage.addActor(button);
			index++;
		}
	}
	
	private void clearStageFromOfferItemButtons() 
	{
		for(Actor actor : offerStage.getActors())
		{
			if(actor instanceof ItemButton)
			{
				actor.remove();
			}
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
