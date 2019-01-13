package entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector3;

import client.MyGame;
import networking.MoveDirection;
import networking.messages.fromserver.UpdateEntityMessage;
import server.ConnectionToClient;
import server.Server;

public class EntityPlayer extends Entity
{
	private static final long serialVersionUID = 4071973552148454031L;
	
	private static final int BASE_PLAYER_ATTACK = 10;
	private static final int BASE_PLAYER_DEFENCE = 5;
	private static final int BASE_PLAYER_HP = 100;

	private String login;
	
	private int HP;
	private int attackPower;
	private int defencePower;
	
	private List<Item> eqList = new ArrayList<>();
	
	boolean moveUp;
	boolean moveDown;
	boolean moveLeft;
	boolean moveRight;
	
	public EntityPlayer(Vector3 position, String login) 
	{
		super(position);
		
		this.login = login;
		
		HP = BASE_PLAYER_HP;
		attackPower = BASE_PLAYER_ATTACK;
		defencePower = BASE_PLAYER_DEFENCE;
	}
	
	public EntityPlayer(EntityPlayer player)
	{
		super(player.getPosition());
		
		eqList = player.getEqList();
		determinePlayerStats();
	}
	
	@Override
	public void update()
	{
		super.update();
				
		if(moveUp && moveRight)
		{
			position.x++;
			position.y++;
			direction = MoveDirection.UP_AND_RIGHT;
		}
		else if(moveUp && moveLeft)
		{
			position.x--;
			position.y++;
			direction = MoveDirection.UP_AND_LEFT;
		}
		else if(moveDown && moveRight)
		{
			position.x++;
			position.y--;
			direction = MoveDirection.DOWN_AND_RIGHT;
		}
		else if(moveDown && moveLeft)
		{
			position.x--;
			position.y--;
			direction = MoveDirection.DOWN_AND_LEFT;
		}
		else
		{
			if(moveUp)
			{
				position.y++;
				direction = MoveDirection.UP;
			}
			
			if(moveDown)
			{
				position.y--;
				direction = MoveDirection.DOWN;
			}
			
			if(moveRight)
			{
				position.x++;
				direction = MoveDirection.RIGHT;
			}
			
			if(moveLeft)
			{
				position.x--;
				direction = MoveDirection.LEFT;
			}
		}
	
		if(!World.isLocal() && (moveUp || moveDown || moveRight || moveLeft))
		{
			for(ConnectionToClient connectionToClient : Server.getConnectionManager().getAllConnections())
			{
				connectionToClient.sendMessageToClient(new UpdateEntityMessage(id, velocity, position));
			}
		}
	}
	
	public String getLogin()
	{
		return login;
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
	
	public void move(MoveDirection direction, boolean ifToStop)
	{
		if(direction == MoveDirection.UP)
		{
			moveUp = !ifToStop;
		}
		else if(direction == MoveDirection.DOWN)
		{
			moveDown = !ifToStop;
		}
		else if(direction == MoveDirection.LEFT)
		{
			moveLeft = !ifToStop;
		}
		else if(direction == MoveDirection.RIGHT)
		{
			moveRight = !ifToStop;
		}
	}
	
	public void attack()
	{
		Server.getMap().attackIfEnemyInFront(attackPower, position, direction);
	}
}
