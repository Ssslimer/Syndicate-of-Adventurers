package com.moag.game.entities;

import java.util.Random;

import com.badlogic.gdx.math.Vector3;

public class EntityEnemy extends Entity
{
	private static final int BASE_ENEMY_ATTACK = 5;
	private static final int BASE_ENEMY_DEFENCE = 5;
	private static final int BASE_ENEMY_HP = 50;
	
	private Random rand;
	
	private int HP;
	private int attackPower;
	private int defencePower;
	
	public EntityEnemy(Vector3 position)
	{
		super(position);
		
		rand = new Random();
		
		HP = BASE_ENEMY_HP;
		attackPower = BASE_ENEMY_ATTACK;
		defencePower = BASE_ENEMY_DEFENCE;		
	}
	
	public void update()
	{
		double xTranslationProbability = rand.nextDouble();
		double yTranslationProbability = rand.nextDouble();
		
		if(xTranslationProbability <= 0.33d)
		{
			position.x++;
		}
		else if(xTranslationProbability <= 0.67d)
		{
			position.x--;
		}
		
		if(yTranslationProbability <= 0.33d)
		{
			position.y++;
		}
		else if(yTranslationProbability <= 0.67d)
		{
			position.y--;
		}
	}
	
	public int getHP()
	{
		return HP;
	}
	
	public int getAttack()
	{
		return attackPower;
	}
	
	public int getDefence()
	{
		return defencePower;
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
