package trade;

import entities.Item;

public class Offer 
{
	protected String login;
	protected Item item;
	
	public Offer(String login, Item item)
	{
		this.login= login;
		this.item = item;
	}
	
	public String getLogin()
	{
		return login;
	}
	
	public Item getTraderItem()
	{
		return this.item;
	}
}
