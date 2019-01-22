package trade;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import entities.Item;
import entities.ItemType;

public class OfferTest {

	@Test
	public void test() 
	{
		String login = "login";
		Item item = new Item(15, 21, 4, ItemType.SWORD);
		
		String login2 = "login2";
		Item item2 = new Item(3, 2, 24, ItemType.GRIMOIRE);
		
		Offer offer = new Offer(login, item);
		Offer offer2 = new Offer(login2, item2);
		
		assertNotNull(offer);
		assertNotNull(offer.getLogin());
		assertNotNull(offer.getTraderItem());
		
		assertNotNull(offer2);
		assertNotNull(offer2.getLogin());
		assertNotNull(offer2.getTraderItem());
		
		assertEquals(offer.getLogin(), login);
		assertEquals(offer.getTraderItem(), item);
		
		assertEquals(offer2.getLogin(), login2);
		assertEquals(offer2.getTraderItem(), item2);
		
		assertNotEquals(offer, offer2);
		assertNotEquals(offer.getLogin(), offer2.getLogin());
		assertNotEquals(offer.getTraderItem(), offer2.getTraderItem());
	}

}
