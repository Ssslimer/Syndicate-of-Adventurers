package entities;

import java.util.Random;

import com.badlogic.gdx.math.Vector3;

import networking.MoveDirection;
import server.Server;

public class EntityEnemy extends Entity
{
	private static final long serialVersionUID = -8463316271178712082L;
	
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
	
	@Override
	public void update()
	{
		double xAndYTranslationProbability = rand.nextDouble();
		double xTranslationProbability = rand.nextDouble();
		double yTranslationProbability = rand.nextDouble();
		double attackProbability = rand.nextDouble();
		
		if(xAndYTranslationProbability <= 0.5d)
		{
			if(xTranslationProbability <= 0.5d && yTranslationProbability <= 0.5d)
			{
				position.x++;
				position.y++;
				this.direction = MoveDirection.UP_AND_RIGHT;
			}
			else if(xTranslationProbability > 0.5d && yTranslationProbability <= 0.5d)
			{
				position.x--;
				position.y++;
				this.direction = MoveDirection.UP_AND_LEFT;
			}
			else if(xTranslationProbability <= 0.5d && yTranslationProbability > 0.5d)
			{
				position.x++;
				position.y--;
				this.direction = MoveDirection.DOWN_AND_LEFT;
			}
			else if(xTranslationProbability > 0.5d && yTranslationProbability > 0.5d)
			{
				position.x--;
				position.y--;
				this.direction = MoveDirection.DOWN_AND_RIGHT;
			}
		}		
		else
		{
			if(xTranslationProbability <= 0.33d)
			{
				position.x++;
				this.direction = MoveDirection.RIGHT;
			}
			else if(xTranslationProbability <= 0.67d)
			{
				position.x--;
				this.direction = MoveDirection.LEFT;
			}
			
			if(yTranslationProbability <= 0.33d)
			{
				position.y++;
				this.direction = MoveDirection.UP;
			}
			else if(yTranslationProbability <= 0.67d)
			{
				position.y--;
				this.direction = MoveDirection.DOWN;
			}
		}

		if(attackProbability <= 0.5d)
		{
			attack();
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
	
	private void attack()
	{
		Server.getMap().attackIfPlayerInFront(attackPower, position, direction);
	}
	
}
