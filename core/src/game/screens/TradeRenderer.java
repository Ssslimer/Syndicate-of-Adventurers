package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class TradeRenderer 
{
	public static final boolean SELLING = true;
	public static final boolean BUYING = false;
	
	private Stage stage;
	private Texture tradeWindowTexture;
	private SpriteBatch batch;
	private Skin skin;
	
	private TextButton startTradeBtn;
	private TextButton offerBtn;
	private TextButton acceptOffertBtn;
	private TextButton declineOffertBtn;
	private TextButton endTradeBtn;
	
	private Boolean sellingOrBuying;
		
	public TradeRenderer()
	{
		stage = new Stage();
		batch = new SpriteBatch();	
		sellingOrBuying = null;
		
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		setupStartTradeButton();
		setupOfferButton();
		setupAcceptOffertButton();
		setupDeclineOffertButton();
		setupEndTradeButton();
	}
	
	public void render()
	{
		if(sellingOrBuying != null)
		{
			if(sellingOrBuying = SELLING) renderSelling();
			else renderBuying();	
		}
	}
	
	private void renderSelling()
	{
		
	}
	
	private void renderBuying()
	{
		
	}
	
	public Stage getStage()
	{
		return stage;
	}
	
	public void setSellingOrBuying(Boolean sellingOrBuying)
	{
		this.sellingOrBuying = sellingOrBuying;
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
				if(sellingOrBuying == SELLING)
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
				if(sellingOrBuying == BUYING)
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
				if(sellingOrBuying == SELLING)
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
				if(sellingOrBuying == SELLING)
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
				if(sellingOrBuying == SELLING)
				{
					//finish trade stop selling
				}
				else
				{
					//close trade window maybe only if you didn't put any offer?
				}
			}
		});
		
		stage.addActor(endTradeBtn);
	}
}
