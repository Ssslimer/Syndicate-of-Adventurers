package trade;

import java.io.Serializable;

import entities.Item;

public class Offer implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7098452146246928216L;
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
