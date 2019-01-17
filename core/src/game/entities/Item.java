package entities;

public class Item 
{
	private int attack;
	private int defence;
	private int HPBonus;
	
	public final ItemType type;
	
	public Item(int attack, int defence, int HPBonus, ItemType type)
	{
		this.attack = attack;
		this.defence = defence;
		this.HPBonus = HPBonus;
		
		this.type = type;
	}
	
	public int getAttack()
	{
		return attack;
	}
	
	public int getDefence()
	{
		return defence;
	}
	
	public int getHPBonus()
	{
		return HPBonus;
	}
}
