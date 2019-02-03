package client;

import entities.EntityPlayer;
import entities.Item;
import screens.TradeRenderer;
import trade.BuyerOffer;
import trade.Offer;
import trade.TradeState;

public class ClientTrade
{
	public static void setEntityTradeStart(String login, Item item)
	{
		EntityPlayer entity = MyGame.getGameMap().getPlayer(login);	
		if(entity == null) return;
		
		entity.setSellingOffer(new Offer(login, item));
		entity.setTradeState(TradeState.SELLING);
	}
	
	public static void updateTradeOffer(String sellerLogin, String buyerLogin, Item buyerItem)
	{
		EntityPlayer seller = MyGame.getGameMap().getPlayer(sellerLogin);
		seller.setHasOffer(true);
		seller.setBuyingOffer(new BuyerOffer(buyerLogin, buyerItem));
		
		EntityPlayer buyer = MyGame.getGameMap().getPlayer(buyerLogin);
		buyer.setTradeState(TradeState.BUYING);
	}
	
	public static void updateTradeEnd(String login)
	{
		EntityPlayer player = MyGame.getGameMap().getPlayer(login);
		player.setTradeState(TradeState.NOT_TRADING);
	}
	
	public static void setEntityTradeStateBuying(String login, Item item)
	{
		EntityPlayer entity = MyGame.getGameMap().getPlayer(login);
		entity.setBuyingOffer(new BuyerOffer(login, item));
		entity.setTradeState(TradeState.BUYING);
	}
	
	public static void setEntityTradeEnd(String login)
	{
		EntityPlayer entity = MyGame.getGameMap().getPlayer(login);
		entity.setSellingOffer(null);
		entity.setBuyingOffer(null);
		entity.setTradeState(TradeState.NOT_TRADING);
	}
	
	public static void updateTradeDecision(boolean offerAccepted, String sellerLogin, String buyerLogin, Item sellerItem, Item buyerItem)
	{	
		if(offerAccepted)
		{
			EntityPlayer seller = MyGame.getGameMap().getPlayer(sellerLogin);
			EntityPlayer buyer = MyGame.getGameMap().getPlayer(buyerLogin);
			
			seller.removeItem(sellerItem.getType(), sellerItem.getAttack(), sellerItem.getDefence(), sellerItem.getHPBonus());
			buyer.addItem(sellerItem);
			
			buyer.removeItem(buyerItem.getType(), buyerItem.getAttack(), buyerItem.getDefence(), buyerItem.getHPBonus());
			seller.addItem(buyerItem);
			
			seller.setTradeState(TradeState.NOT_TRADING);
			buyer.setTradeState(TradeState.NOT_TRADING);
			
			seller.setHasOffer(false);
			
			if(MyGame.getPlayer() == buyer)
			{
				TradeRenderer.response = TradeResponse.ACCEPTED;
				TradeRenderer.displayResponse = true;
			}
		}
		else
		{
			EntityPlayer seller = MyGame.getGameMap().getPlayer(sellerLogin);
			seller.setHasOffer(false);
			
			EntityPlayer buyer = MyGame.getGameMap().getPlayer(buyerLogin);
			
			if(MyGame.getPlayer() == buyer)
			{
				TradeRenderer.response = TradeResponse.NOT_ACCEPTED;
				TradeRenderer.displayResponse = true;
			}
		}
	}
}