package world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.math.Vector3;

import entities.Entity;
import entities.EntityEnemy;
import entities.EntityPlayer;
import entities.Item;
import networking.messages.fromserver.SpawnEntityMessage;
import server.Server;
import trade.BuyerOffer;
import trade.Offer;
import trade.TradeState;

public class World implements Serializable
{	
	private static final long serialVersionUID = -787730389424356815L;
	
	private Map<Long, Entity> entities = new ConcurrentHashMap<>();
	private Map<String, EntityPlayer> players = new ConcurrentHashMap<>();
	private List<TerrainTile> terrain = new ArrayList<>();
	
	private static boolean isLocal = false;
	
	private Vector3 spawnPoint = new Vector3();
	
	public World()
	{
		generateMap();
		
		//if(!isLocal) spawnEntity(new EntityEnemy(new Vector3(5, 5, 2)));
		//if(!isLocal) spawnEntity(new EntityEnemy(new Vector3(5, 5, 2)));
	}

	public synchronized void spawnEntity(Entity entity)
	{		
		entities.put(entity.getId(), entity);
		if(entity instanceof EntityPlayer) players.put(((EntityPlayer) entity).getLogin(), (EntityPlayer) entity);
		
		if(!isLocal) Server.getConnectionManager().sendToAll(new SpawnEntityMessage(entity));
	}
	
	public void updateEntityPos(long id, Vector3 position, Vector3 velocity)
	{
		Entity entity = entities.get(id);
		if(entity == null) return;
		
		entity.setPosition(position);
		entity.setVelocity(velocity);
	}
	
	public void setEntityTradeStart(String login, Item item)
	{
		EntityPlayer entity = getPlayer(login);	
		if(entity == null) return;
		
		entity.setSellingOffer(new Offer(login, item));
		entity.setTradeState(TradeState.SELLING);
	}
	
	public void updateTradeOffer(String sellerLogin, String buyerLogin, Item buyerItem, Item sellerItem)
	{
		EntityPlayer seller = getPlayer(sellerLogin);
		seller.setHasOffer(true);
		
		System.out.println("PLAYER: " + seller.getLogin() + " HAS OFFER");
		
		/**TODO CHECK IF NECESSARY */
		EntityPlayer buyer = getPlayer(buyerLogin);
		buyer.setTradeState(TradeState.BUYING);
		
	}
	
	public void setEntityTradeStateBuying(String login, Item item)
	{
		EntityPlayer entity = getPlayer(login);
		entity.setBuyingOffer(new BuyerOffer(login, item));
		entity.setTradeState(TradeState.BUYING);
	}
	
	public void setEntityTradeEnd(String login)
	{
		/** TODO check if correct */
		EntityPlayer entity = getPlayer(login);
		entity.setSellingOffer(null);
		entity.setBuyingOffer(null);
		entity.setTradeState(TradeState.NOT_TRADING);
	}
	
	/** TODO tmp map for testing */
	private void generateMap()
	{
		for(int x = -10; x < 10; x++)
		{
			for(int y = -10; y < 10; y++)
			{
				Vector3 position = new Vector3(2.5f * x, 2.5f * y, 0);
				terrain.add(new TerrainTile(position, new Random().nextInt(8)));
			}
		}
	}

	public synchronized void update(float delta)
	{
		/** TODO fix bug */	
		for(Entity e : entities.values())
		{
			e.update(delta);
		}
					

//		if(!isLocal && Timer.getTickCount() % 100 == 0)
//		{
//			int posX = Server.random.nextInt(4) - 2;
//			int posY = Server.random.nextInt(4) - 2;
//			spawnEntity(new EntityEnemy(new Vector3(posX, posY, 2)));
//		}
	}

	public EntityPlayer findClosestTradingEntity(Vector3 pos)
	{
		EntityPlayer player = null;
		float maxDistance = 10;
		float currentDistance = 10;
		
		for(Entity e : entities.values())
		{
			if(e instanceof EntityPlayer)
			{
				if(((EntityPlayer)e).getTradeState() == TradeState.SELLING)
				{
					if(e.getPosition().dst(pos) < maxDistance && e.getPosition().dst(pos) < currentDistance)
					{
						currentDistance = e.getPosition().dst(pos);
						player = (EntityPlayer)e;
					}
				}
			}
		}
		
		return player == null ? null : player;
	}
	
	public EntityPlayer getClosestPlayerInRange(Entity entity, float range)
	{
		EntityPlayer closest = null;
		float smallestDistance = Float.MAX_VALUE;
		
		for(EntityPlayer player : players.values())
		{
			if(entity == player) continue;
			
			float distance = player.getPosition().dst(entity.getPosition());
			if(distance <= range && distance < smallestDistance)
			{
				closest = player;
				smallestDistance = distance;
			}
		}
		
		return closest;
	}
	
	public List<EntityPlayer> getPlayersInRange(Vector3 position, float range)
	{
		List<EntityPlayer> entitiesInRange = new ArrayList<>(); 
		for(EntityPlayer player : players.values())
		{
			if(player.getPosition().cpy().sub(position).len() <= range)
			{
				entitiesInRange.add(player);
			}
		}
		
		return entitiesInRange;
	}	
	
	public List<Entity> getEntitiesInRange(Vector3 position, float range)
	{
		List<Entity> entitiesInRange = new ArrayList<>(); 
		for(Entity entity : entities.values())
		{
			if(entity.getPosition().cpy().sub(position).len() <= range)
			{
				entitiesInRange.add(entity);
			}
		}
		
		return entitiesInRange;
	}	
	
	public void removeEntity(Entity entity)
	{
		entities.remove(entity.getId());
	}
	
	public void removePlayer(EntityPlayer player)
	{
		player.setDead();
		players.remove(player.getLogin());
		entities.remove(player.getId());
	}

	public Map<Long, Entity> getEntities()
	{
		return entities;
	}

	public List<TerrainTile> getTerrain()
	{
		return terrain;
	}
	
	public Map<String, EntityPlayer> getPlayers()
	{
		return players;
	}
	
	public Entity getEntity(long id)
	{
		return entities.get(id);
	}
	
	public EntityPlayer getPlayer(String login)
	{
		return players.get(login);
	}

	public static boolean isLocal()
	{
		return isLocal;
	}
	
	public static void setLocal(boolean b)
	{
		isLocal = b;
	}

	public Vector3 getSpawnPoint()
	{
		return spawnPoint;
	}

	public void setSpawnPoint(Vector3 spawnPoint)
	{
		this.spawnPoint = spawnPoint;
	}
}
