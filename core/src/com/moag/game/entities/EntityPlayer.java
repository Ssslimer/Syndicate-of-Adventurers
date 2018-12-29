package com.moag.game.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector3;

public class EntityPlayer extends Entity
{
	private static final int BASE_PLAYER_ATTACK = 10;
	private static final int BASE_PLAYER_DEFENCE = 5;
	private static final int BASE_PLAYER_HP = 100;
	
	private long playerID; // equal to sessionID ?
	
	private int HP;
	private int attackPower;
	private int defencePower;
	
	private List<Item> eqList = new ArrayList<>(); 
	
	public EntityPlayer(Vector3 position) {
		super(position);
		
		HP = BASE_PLAYER_HP;
		attackPower = BASE_PLAYER_ATTACK;
		defencePower = BASE_PLAYER_DEFENCE;
	}
	
	public EntityPlayer(EntityPlayer player)
	{
		super(player.getPosition());
		
		playerID = player.getPlayerID();
		
		eqList = player.getEqList();
		determinePlayerStats();
	}
	
	public long getPlayerID()
	{
		return playerID;
	}
	
	public int getHP()
	{
		return HP;
	}
	
	public int getAttackPower()
	{
		return attackPower;
	}
	
	public int getDefencePower()
	{
		return defencePower;
	}
	
	public List<Item> getEqList()
	{
		return eqList;
	}
	
	public void addItem(Item item)
	{
		eqList.add(item);
		
		HP += item.getHPBonus();
		attackPower += item.getAttack();
		defencePower += item.getDefence();
	}
	
	public void removeItem(Item item)
	{
		if(eqList.contains(item))
		{
			eqList.remove(item);
			
			HP -= item.getHPBonus();
			attackPower -= item.getAttack();
			defencePower -= item.getDefence();
		}
	}
	
	private void determinePlayerStats()
	{
		for(Item item : eqList)
		{
			HP += item.getHPBonus();
			attackPower += item.getAttack();
			defencePower += item.getDefence();
		}
	}

	public void getDamage(int damageAttack)
	{
		damageAttack -= defencePower;
		
		if(damageAttack > 0)
		{
			HP -= damageAttack;
		}
	}
}
