package entities;

public class Item 
{
	private int attack;
	private int defence;
	private int HPBonus;
	
	public Item(int attack, int defence, int HPBonus)
	{
		this.attack = attack;
		this.defence = defence;
		this.HPBonus = HPBonus;
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
