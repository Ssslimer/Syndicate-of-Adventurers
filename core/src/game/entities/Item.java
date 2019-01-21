package entities;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Item implements Serializable
{
	private static final long serialVersionUID = -5446185183517083909L;
	
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
	
	public String getName()
	{
		return type.toString() + "(" + attack + "," + defence + "," + HPBonus + ")";
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
	
