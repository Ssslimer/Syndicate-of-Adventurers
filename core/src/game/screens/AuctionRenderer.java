package screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import client.MyGame;
import entities.Item;
import entities.ItemType;
import trade.Auction;
import util.ConfigConstants;

public class AuctionRenderer 
{	
	private final Texture tradeWindowTexture = MyGame.getResources().getTexture("GUI_TRADE_WINDOW");
	private final SpriteBatch batch = new SpriteBatch();
	private final Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
	private final BitmapFont tradeFont = new BitmapFont();
	
	private float PANEL_WIDTH, PANEL_HEIGHT;
	private float LIST_WIDTH, LIST_HEIGHT;
	
	private Stage stage;
	private Table auctionList;
	private Button exit, openList, closeList, exitAuction;
	
	private Auction displayedAuction;
	
	private boolean listNeedsRefresh;
	private boolean isListVisible;
	
	public AuctionRenderer(Stage stage)
	{
		this.stage = stage;
		
    	PANEL_WIDTH = ConfigConstants.WIDTH/4;
    	PANEL_HEIGHT = ConfigConstants.HEIGHT/2;
    	LIST_WIDTH = ConfigConstants.WIDTH * 0.4f;
    	LIST_HEIGHT = 400;
    	
    	setupOpenListButton(0, 30, 150, 20);
    	setupCloseListButton(150, 30, 150, 20);
    	setupExitButton(125, 10, 50, 20);
	}

	public void render()
	{
		batch.begin();

		if(listNeedsRefresh && !isListVisible && !MyGame.getTradeManager().getAuctions().isEmpty())
		{
			listNeedsRefresh = false;
			isListVisible = true;
			setupAuctionList();
		}
		
		batch.draw(tradeWindowTexture, 0, 0, PANEL_WIDTH, PANEL_HEIGHT);	
		
		if(displayedAuction != null) renderAuction();
		
		batch.end();		
		stage.act();
		stage.draw();
	}
	
	private void renderAuction()
	{
		batch.draw(tradeWindowTexture, 0, PANEL_HEIGHT, PANEL_WIDTH, PANEL_HEIGHT/2);
		
		String data = "Owner: " + displayedAuction.getOwner().getLogin()+" Item: " + displayedAuction.getItem();
		float dataPosX = 0;
		float dataPosY = 600;
		tradeFont.draw(batch, data, dataPosX, dataPosY);

		String price = "Current price: " + String.valueOf(displayedAuction.getCurrentPrice()) + "$";	
		float pricePosX = 0;
		float pricePosY = 570;
		tradeFont.draw(batch, price, pricePosX, pricePosY);
	}

	private void setupExitButton(float posX, float posY, float width, float height)
	{
		exit = new TextButton("Exit", skin);
		exit.setWidth(width);
		exit.setHeight(height);
		exit.setPosition(posX, posY);
		exit.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				GameScreen.isInAuction = false;
			}
		});
		
		stage.addActor(exit);
	}
	
	private void setupCloseListButton(float posX, float posY, float width, float height)
	{
		closeList = new TextButton("Close auctions", skin);
		closeList.setWidth(width);
		closeList.setHeight(height);		
		closeList.setPosition(posX, posY);		
		closeList.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				listNeedsRefresh = false;
				isListVisible = false;
				
				for(Actor actor : stage.getActors())
				{
			        if(actor == auctionList)
			        {
			        	actor.addAction(Actions.removeActor());
			        	MyGame.getTradeManager().resetAuctions();
			        }
			    }
			}
		});
		
		stage.addActor(closeList);	
	}

	private void setupOpenListButton(float posX, float posY, float width, float height)
	{
		openList = new TextButton("Show auctions", skin);
		openList.setWidth(width);
		openList.setHeight(height);		
		openList.setPosition(posX, posY);		
		openList.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				if(!listNeedsRefresh) /** TODO */
				{
			        List<Auction> auctions = new ArrayList<>();
			        auctions.add(new Auction(MyGame.getPlayer(), new Item(1, 2, 0, ItemType.SWORD), 100));
			        MyGame.getTradeManager().setAuctions(auctions);
					//MyGame.getClient().sendMessage(new AuctionOpenListMessage(MyGame.getClient().getSessionID()));
					listNeedsRefresh = true;
				}			
			}
		});
		
		stage.addActor(openList);		
	}
	
	private void setupExitAuctionButton(float posX, float posY, float width, float height)
	{
		exitAuction = new TextButton("Exit auction", skin);
		exitAuction.setWidth(width);
		exitAuction.setHeight(height);		
		exitAuction.setPosition(posX, posY);		
		exitAuction.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				displayedAuction = null;
				
				for(Actor actor : stage.getActors())
				{
			        if(actor == exitAuction)
			        {
			        	actor.addAction(Actions.removeActor());
			        	return;
			        }
			    }				
			}
		});
		
		stage.addActor(exitAuction);	
	}

	/** TODO call when open list button is pressed */
	private void setupAuctionList()
	{
		listNeedsRefresh = true;
        auctionList = new Table();
        Table table = new Table();
        ScrollPane pane = new ScrollPane(table, skin);
        auctionList.add(pane).width(LIST_WIDTH).height(LIST_HEIGHT);
        auctionList.row();
        auctionList.setBounds(PANEL_WIDTH, 0, LIST_WIDTH, LIST_HEIGHT);
        stage.addActor(auctionList);
        
        List<Auction> auctions = MyGame.getTradeManager().getAuctions();
        auctions.add(new Auction(MyGame.getPlayer(), new Item(1, 2, 0, ItemType.SWORD), 100));
        for(Auction auction : auctions)
        {
            TextButton panelElement = new TextButton(auction.toString(), skin);
            panelElement.addListener(new ClickListener()
    		{
    			@Override
    			public void clicked(InputEvent event, float x, float y)
    			{
    				long auctioID = getAuctionNumberFromText(panelElement.getText());
    				for(Auction auction : auctions)
    				{
    					if(auction.getAuctionId() == auctioID)
    					{
    						displayedAuction = auction;
    						setupExitAuctionButton(125, 400, 50, 20);  						
    						
    						return;
    					}
    				}
    			}
    		});
            table.add(panelElement);
            table.row();
        }
	}
	
	private long getAuctionNumberFromText(CharSequence cs)
	{
		String text = cs.toString();
		String IDfragment = text.split(" ")[0];
		
		return Long.valueOf(IDfragment);
	}
	
	
}
