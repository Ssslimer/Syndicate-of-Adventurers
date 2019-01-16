package entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector3;

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
	
	private float speed = 5f;
	private int helth;
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
		
		helth = BASE_PLAYER_HP;
		attackPower = BASE_PLAYER_ATTACK;
		defencePower = BASE_PLAYER_DEFENCE;
	}
	
	public EntityPlayer(EntityPlayer player)
	{
		super(player.getPosition());
		
		eqList = player.getItems();
		determinePlayerStats();
	}
	
	@Override
	public void update(float delta)
	{	
		position.add(moveDirection.getDirection().cpy().scl(1 / delta));
		
		if(!World.isLocal() && (moveUp || moveDown || moveRight || moveLeft))
		{
			for(ConnectionToClient connectionToClient : Server.getConnectionManager().getAllConnections())
			{
				connectionToClient.sendMessageToClient(new UpdateEntityMessage(id, velocity, position));
			}
		}
	}
	
	public void addItem(Item item)
	{
		eqList.add(item);
		
		helth += item.getHPBonus();
		attackPower += item.getAttack();
		defencePower += item.getDefence();
	}
	
	public void removeItem(Item item)
	{
		if(eqList.contains(item))
		{
			eqList.remove(item);
			
			helth -= item.getHPBonus();
			attackPower -= item.getAttack();
			defencePower -= item.getDefence();
		}
	}
	
	private void determinePlayerStats()
	{
		for(Item item : eqList)
		{
			helth += item.getHPBonus();
			attackPower += item.getAttack();
			defencePower += item.getDefence();
		}
	}

	public void getDamage(int damageAttack)
	{
		damageAttack -= defencePower;
		
		if(damageAttack > 0)
		{
			helth -= damageAttack;
		}
	}
	
	public void setMoveDirection(MoveDirection direction, boolean ifToStop)
	{
		if(direction == MoveDirection.UP)			moveUp = !ifToStop;
		else if(direction == MoveDirection.DOWN)	moveDown = !ifToStop;
		else if(direction == MoveDirection.LEFT)	moveLeft = !ifToStop;
		else if(direction == MoveDirection.RIGHT) 	moveRight = !ifToStop;
		
		if(moveUp && moveRight)			moveDirection = MoveDirection.UP_AND_RIGHT;
		else if(moveUp && moveLeft)		moveDirection = MoveDirection.UP_AND_LEFT;
		else if(moveDown && moveRight)	moveDirection = MoveDirection.DOWN_AND_RIGHT;
		else if(moveDown && moveLeft)	moveDirection = MoveDirection.DOWN_AND_LEFT;
		else if(moveUp) 				moveDirection = MoveDirection.UP;
		else if(moveDown) 				moveDirection = MoveDirection.DOWN;
		else if(moveRight)				moveDirection = MoveDirection.RIGHT;
		else if(moveLeft) 				moveDirection = MoveDirection.LEFT;
		else 							moveDirection = MoveDirection.NONE;
	}
	
	public void attack()
	{
		Server.getMap().attackIfEnemyInFront(attackPower, position, moveDirection);
	}
	
	public String getLogin()
	{
		return login;
	}

	public int getHealth()
	{
		return helth;
	}
	
	public int getAttack()
	{
		return attackPower;
	}
	
	public int getDefence()
	{
		return defencePower;
	}
	
	public List<Item> getItems()
	{
		return eqList;
	}
}
