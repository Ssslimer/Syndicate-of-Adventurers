package entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector3;

import networking.MoveDirection;
import networking.messages.fromserver.DamageEntityMessage;
import networking.messages.fromserver.DeathEntityMessage;
import networking.messages.fromserver.UpdateEntityMessage;
import server.Server;
import trade.BuyerOffer;
import trade.Offer;
import trade.TradeState;
import world.World;

public class EntityPlayer extends Entity implements Damageable
{
	private static final long serialVersionUID = 4071973552148454031L;
	
	private static final int BASE_ATTACK = 10;
	private static final int BASE_DEFENCE = 0;
	private static final int BASE_HEALTH = 100;
	private static final float WALK_SPEED = 5f;
	private static final float ATTACK_RANGE = 5f;
	
	private String login;
	private int health, attack, defence;
	private int gold;
	
	private List<Item> equipment = new ArrayList<>();
	
	boolean moveUp, moveDown, moveLeft, moveRight;
	
	private Boolean hasOffer = new Boolean(false);
	private TradeState tradeState = TradeState.NOT_TRADING;
	private long tradingWithId = -1;
	
	private Offer sellingOffer = new Offer("", null);
	private BuyerOffer buyingOffer = new BuyerOffer("", null);
	
	public EntityPlayer(Vector3 position, String login) 
	{
		super(position);
		
		this.login = login;
		
		health = BASE_HEALTH;
		attack = BASE_ATTACK;
		defence = BASE_DEFENCE;
		gold = 0;
		
		addItem(new Item(1, 2, 0, ItemType.SWORD));
		addItem(new Item(2, 15, 3, ItemType.SHIELD));
		addItem(new Item(10, 5, 8, ItemType.SWORD));			
	}
	
	public EntityPlayer(EntityPlayer player)
	{
		super(player.getPosition());
		
		equipment = player.getItems();
		determinePlayerStats();
		
		addItem(new Item(1, 2, 0, ItemType.SWORD));
		addItem(new Item(2, 15, 3, ItemType.SHIELD));
		addItem(new Item(10, 5, 8, ItemType.SWORD));
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
		equipment.add(item);
		
		health += item.getHPBonus();
		attack += item.getAttack();
		defence += item.getDefence();
	}
	
	public void removeItem(Item item)
	{
		if(equipment.contains(item))
		{
			equipment.remove(item);
			
			health -= item.getHPBonus();
			attack -= item.getAttack();
			defence -= item.getDefence();
		}
	}

	private void determinePlayerStats()
	{
		for(Item item : equipment)
		{
			health += item.getHPBonus();
			attack += item.getAttack();
			defence += item.getDefence();
		}
	}

	public void dealDamage(int damage, DamageSource source)
	{
		damage -= defence;		
		if(damage > 0) health -= damage;
		
		if(health <= 0)
		{
			isAlive = false;
			Server.getMap().removePlayer(this);
			Server.getConnectionManager().sendToAll(new DeathEntityMessage(id));
		}
		else
		{
			Server.getConnectionManager().sendToAll(new DamageEntityMessage(this, damage, source));
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
		List<Entity> entities = Server.getMap().getEntitiesInRange(position, ATTACK_RANGE);
		for(Entity entity : entities)
		{
			if(entity instanceof Damageable && entity != this)
			{				
				((Damageable) entity).dealDamage(attack, DamageSource.NORMAL);
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
		return attack;
	}
	
	public int getDefence()
	{
		return defence;
	}
	
	public List<Item> getItems()
	{
		return equipment;
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

	public Offer getSellingOffer()
	{
		return this.sellingOffer;
	}
	
	public void setSellingOffer(Offer offer)
	{
		this.sellingOffer = offer;
	}
	
	public BuyerOffer getBuyingOffer()
	{
		return this.buyingOffer;
	}
	
	public void setBuyingOffer(BuyerOffer offer)
	{
		this.buyingOffer = offer;
	}
}
