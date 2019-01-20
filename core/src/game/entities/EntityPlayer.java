package entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector3;

import networking.MoveDirection;
import networking.messages.fromserver.EntityHitMessage;
import networking.messages.fromserver.UpdateEntityMessage;
import server.Server;
import trade.TradeState;
import world.World;

public class EntityPlayer extends Entity implements Damageable
{
	private static final long serialVersionUID = 4071973552148454031L;
	
	private static final int BASE_PLAYER_ATTACK = 10;
	private static final int BASE_PLAYER_DEFENCE = 5;
	private static final int BASE_PLAYER_HP = 100;

	private String login;
	
	private static final float WALK_SPEED = 5f;
	private static final float ATTACK_RANGE = 50f;
	private int health;
	private int attackPower;
	private int defencePower;
	private int gold;
	
	private List<Item> eqList = new ArrayList<>();
	
	boolean moveUp, moveDown, moveLeft, moveRight;
	
	private Boolean hasOffer;
	private TradeState tradeState;
	private long tradingWithId = -1;
	
	public EntityPlayer(Vector3 position, String login) 
	{
		super(position);
		
		this.login = login;
		
		health = BASE_PLAYER_HP;
		attackPower = BASE_PLAYER_ATTACK;
		defencePower = BASE_PLAYER_DEFENCE;
		gold = 0;
		
		addItem(new Item(1, 2, 0, ItemType.SWORD));
		addItem(new Item(2, 15, 3, ItemType.SHIELD));
		addItem(new Item(10, 5, 8, ItemType.SWORD));
		
		tradeState = TradeState.NOT_TRADING;
		hasOffer = new Boolean(false);
	}
	
	public EntityPlayer(EntityPlayer player)
	{
		super(player.getPosition());
		
		eqList = player.getItems();
		determinePlayerStats();
		
		addItem(new Item(1, 2, 0, ItemType.SWORD));
		addItem(new Item(2, 15, 3, ItemType.SHIELD));
		addItem(new Item(10, 5, 8, ItemType.SWORD));
		
		tradeState = TradeState.NOT_TRADING;
		hasOffer = new Boolean(false);
	}
	
	@Override
	public void update(float delta)
	{	
		velocity = moveDirection.cpy().scl(WALK_SPEED);
		
		position.add(velocity.cpy().scl(1f/delta));
		
		if(!World.isLocal() && (moveUp || moveDown || moveRight || moveLeft))
		{
			Server.getConnectionManager().sendToAll(new UpdateEntityMessage(id, velocity, position));
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

	public void dealDamage(int damage, DamageSource source)
	{
		damage -= defencePower;		
		if(damage > 0) health -= damage;
		
		Server.getConnectionManager().sendToAll(new EntityHitMessage(this, damage, source));
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
		List<Entity> entities = Server.getMap().getEntitiesInRange(position, ATTACK_RANGE);
		for(Entity entity : entities)
		{
			if(entity instanceof Damageable && entity != this)
			{				
				((Damageable) entity).dealDamage(BASE_PLAYER_ATTACK, DamageSource.NORMAL);
			}
		}
	}
	
	public void addGold(int goldAmount)
	{
		gold += goldAmount;
	}
	
	public void removeGold(int goldAmount)
	{
		gold -= goldAmount;
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
	
	public TradeState getTradeState()
	{
		return this.tradeState;
	}
	
	public void setTradeState(TradeState state)
	{
		this.tradeState = state;
	}
	
	public Boolean getHasOffer() 
	{
		return hasOffer;
	}

	public void setHasOffer(Boolean hasOffer) 
	{
		this.hasOffer = hasOffer;
	}
	
	public long getTradingWithId()
	{
		return tradingWithId;
	}

	public void setTradingWithId(long tradingWithId)
	{
		this.tradingWithId = tradingWithId;
	}
	
	public void die()
	{
		alive = false;
	}
}
