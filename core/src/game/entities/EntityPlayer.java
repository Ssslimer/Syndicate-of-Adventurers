package entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector3;

import networking.MoveDirection;
import networking.messages.fromserver.UpdateEntityMessage;
import server.ConnectionToClient;
import server.Server;
import world.World;

public class EntityPlayer extends Entity implements Damageable
{
	private static final long serialVersionUID = 4071973552148454031L;
	
	private static final int BASE_PLAYER_ATTACK = 10;
	private static final int BASE_PLAYER_DEFENCE = 5;
	private static final int BASE_PLAYER_HP = 100;
	
	public static final boolean SELLING = true;
	public static final boolean BUYING = false;

	private String login;
	
	private float speed = 5f;
	private int health;
	private int attackPower;
	private int defencePower;
	private int gold;
	
	private List<Item> eqList = new ArrayList<>();
	private Item sellingItem;
	
	boolean moveUp, moveDown, moveLeft, moveRight;
	
	private Boolean sellingOrBuying;
	private Boolean hasOffer;
	
	public EntityPlayer(Vector3 position, String login) 
	{
		super(position);
		
		this.login = login;
		
		health = BASE_PLAYER_HP;
		attackPower = BASE_PLAYER_ATTACK;
		defencePower = BASE_PLAYER_DEFENCE;
		gold = 0;
		
		sellingOrBuying = null;
		setHasOffer(false);
	}
	
	public EntityPlayer(EntityPlayer player)
	{
		super(player.getPosition());
		
		eqList = player.getItems();
		determinePlayerStats();
		
		sellingOrBuying = null;
	}
	
	@Override
	public void update(float delta)
	{	
		position.add(moveDirection.cpy().scl(speed / delta));
		
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
		
		health += item.getHPBonus();
		attackPower += item.getAttack();
		defencePower += item.getDefence();
	}
	
	public void removeItem(Item item)
	{
		if(eqList.contains(item))
		{
			eqList.remove(item);
			
			health -= item.getHPBonus();
			attackPower -= item.getAttack();
			defencePower -= item.getDefence();
		}
	}

	private void determinePlayerStats()
	{
		for(Item item : eqList)
		{
			health += item.getHPBonus();
			attackPower += item.getAttack();
			defencePower += item.getDefence();
		}
	}

	public void dealDamage(int damageAttack)
	{
		damageAttack -= defencePower;
		
		if(damageAttack > 0)
		{
			health -= damageAttack;
		}
	}
	
	public void setMoveDirection(MoveDirection direction, boolean ifToStop)
	{
		if(direction == MoveDirection.UP)			moveUp = !ifToStop;
		else if(direction == MoveDirection.DOWN)	moveDown = !ifToStop;
		else if(direction == MoveDirection.LEFT)	moveLeft = !ifToStop;
		else if(direction == MoveDirection.RIGHT) 	moveRight = !ifToStop;
		
		if(moveUp && moveRight)			moveDirection = MoveDirection.UP_AND_RIGHT.getDirection();
		else if(moveUp && moveLeft)		moveDirection = MoveDirection.UP_AND_LEFT.getDirection();
		else if(moveDown && moveRight)	moveDirection = MoveDirection.DOWN_AND_RIGHT.getDirection();
		else if(moveDown && moveLeft)	moveDirection = MoveDirection.DOWN_AND_LEFT.getDirection();
		else if(moveUp) 				moveDirection = MoveDirection.UP.getDirection();
		else if(moveDown) 				moveDirection = MoveDirection.DOWN.getDirection();
		else if(moveRight)				moveDirection = MoveDirection.RIGHT.getDirection();
		else if(moveLeft) 				moveDirection = MoveDirection.LEFT.getDirection();
		else 							moveDirection = MoveDirection.NONE.getDirection();
	}
	
	public void attack()
	{
		//Server.getMap().attackIfEnemyInFront(attackPower, position, moveDirection);
	}
	
	public void addGold(int goldAmount)
	{
		gold += goldAmount;
	}

	public boolean canAfford(int expense)
	{
		return gold >= expense;
	}
	
	public String getLogin()
	{
		return login;
	}

	public int getHealth()
	{
		return health;
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
	
	public Boolean getSellingOrBuying()
	{
		return sellingOrBuying;
	}
	
	public void setSellingOrBuying(Boolean sellingOrBuying)
	{
		this.sellingOrBuying = sellingOrBuying;
	}
	
	public Boolean getHasOffer() 
	{
		return hasOffer;
	}

	public void setHasOffer(Boolean hasOffer) 
	{
		this.hasOffer = hasOffer;
	}

	public void die()
	{
		alive = false;
	}
}
