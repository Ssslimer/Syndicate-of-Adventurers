package entities;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Item 
{
	private int attack;
	private int defence;
	private int HPBonus;
	
	private final ItemType type;
	
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
	
	public ItemType getType()
	{
		return type;
	}
	
	@Override
	public String toString()
	{		
		return new ToStringBuilder(this)
				.appendSuper(super.toString())
				.append("type", type)
				.append("attack", attack)
				.append("defence", defence)
				.toString();
	}
}
	
