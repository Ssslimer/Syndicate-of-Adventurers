package trade;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import entities.Item;
import entities.ItemType;

class BuyerOfferTest 
{

	@Test
	void test() 
	{
		String login = "login";
		Item item = new Item(20, 6, 9, ItemType.SWORD);
		int gold = 99;
		
		String login2 = "login2";
		Item item2 = new Item(1, 10, 5, ItemType.BOOTS);
		
		String login3 = "login3";
		int gold3 = 67;
		
		BuyerOffer offer = new BuyerOffer(login, item, gold);
		BuyerOffer offer2 = new BuyerOffer(login2, item2);
		BuyerOffer offer3 = new BuyerOffer(login3, gold3);
		
		assertNotNull(offer);
		assertNotNull(offer2);
		assertNotNull(offer3);
		
		assertNotNull(offer.getLogin());
		assertNotNull(offer2.getLogin());
		assertNotNull(offer3.getLogin());
		
		assertNotNull(offer.getTraderItem());
		assertNotNull(offer2.getTraderItem());
		
		assertNull(offer3.getTraderItem());
		
		assertTrue(offer.getGoldAmount() == gold);
		assertTrue(offer2.getGoldAmount() == 0);
		assertTrue(offer3.getGoldAmount() == gold3);
	}

}
