package client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import com.badlogic.gdx.audio.Sound;

import entities.Entity;
import entities.EntityPlayer;
import entities.Item;
import networking.MessageStatus;
import networking.MoveDirection;
import networking.messages.Message;
import networking.messages.fromclient.ChatMessage;
import networking.messages.fromclient.LoginMessage;
import networking.messages.fromclient.PingMessage;
import networking.messages.fromclient.RegisterMessage;
import networking.messages.fromserver.DamageEntityMessage;
import networking.messages.fromserver.DeathEntityMessage;
import networking.messages.fromserver.SendMapMessage;
import networking.messages.fromserver.SpawnEntityMessage;
import networking.messages.fromserver.UpdateChatMessage;
import networking.messages.fromserver.UpdateEntityMessage;
import networking.messages.fromserver.UpdateTradeStartEntityMessage;
import networking.messages.fromserver.auth.AuthLoginMessage;
import networking.messages.fromserver.auth.AuthRegisterMessage;
import networking.messages.fromserver.auth.PlayerLogoutMessage;
import networking.messages.ingame.AttackMessage;
import networking.messages.ingame.MoveMessage;
import networking.messages.trade.TradeEndMessage;
import networking.messages.trade.TradeOfferMessage;
import networking.messages.trade.TradeStartMessage;
import world.World;

public class ClientConnection extends Thread
{
	private Socket clientSocket;
	private ObjectInputStream streamFromServer;
	
	private final String ip;
	private final int port;
	
	private boolean isLogedIn;
	private String login;
	private long sessionId;
	
	private MessageSender sender;

	public ClientConnection(String ip, int port)
	{
		super("Input from server");
		this.ip = ip;
		this.port = port;
	}
	
	@Override
	public void run()
	{	
		while(true)
		{
	    	Message fromServer = null;
			try
			{
				fromServer = getDataFromServer();
			}
			catch(SocketException e)
			{
				e.printStackTrace();
			}			
			catch(ClassNotFoundException e)
			{
				e.printStackTrace();
				continue;
			}
			catch(IOException e)
			{
				e.printStackTrace();
				continue;
			}
			
	    	handleCallback(fromServer);
		}
	}
	
	public void move(MoveDirection direction, boolean toStop)
	{
		if(isLogedIn) sender.addMessage(new MoveMessage(sessionId, direction, toStop));
	}

	public void attack()
	{
		if(isLogedIn) sender.addMessage(new AttackMessage(sessionId));
	}
	
	public void sentChatMessage(String chatMessageString)
	{
		if(isLogedIn) sender.addMessage(new ChatMessage(sessionId, chatMessageString));
	}
	
	public void sentTradeStartMessage(Item item)
	{
		System.out.println(login + "!!!!!!!!!!!!!!!!!");
		if(isLogedIn) sender.addMessage(new TradeStartMessage(sessionId, login, item));
	}
	
	public void sentTradeOfferMessage(String buyerLogin, String sellerLogin, Item buyerItem, Item sellerItem)
	{
		if(isLogedIn) sender.addMessage(new TradeOfferMessage(sessionId, sellerLogin, buyerLogin, buyerItem, sellerItem));
	}
	
	/** TODO DELETE DEPRACIETED SHIEEET */
	public void sentTradeOfferMessage(long sellerId, int goldAmount, Item sellingItem)
	{
		//if(isLogedIn) sender.addMessage(new TradeOfferMessage(sessionId, MyGame.getGameMap().get ,login, goldAmount, sellingItem));
	}
	
	public void sentTradeOfferMessage(long sellerId, Item item, Item sellingItem)
	{
		//if(isLogedIn) sender.addMessage(new TradeOfferMessage(sessionId, sellerId, item, sellingItem));
	}
	
	public void sentTradeOfferMessage(long sellerId, Item item, int goldAmount, Item sellingItem)
	{
		//if(isLogedIn) sender.addMessage(new TradeOfferMessage(sessionId, sellerId, goldAmount, item, sellingItem));
	}
	
	public void sentTradeDecisionMessage(boolean offerAccepted, long otherTraderId, Item sellingItem, int goldAmount)
	{
		//if(isLogedIn) sender.addMessage(new TradeDecisionMessage(sessionId, otherTraderId, offerAccepted, sellingItem, goldAmount));
	}
	
	public void sentTradeDecisionMessage(boolean offerAccepted, long otherTraderId, Item sellingItem, Item item)
	{
		//if(isLogedIn) sender.addMessage(new TradeDecisionMessage(sessionId, otherTraderId, offerAccepted, sellingItem, item));
	}
	
	public void sentTradeDecisionMessage(boolean offerAccepted, long otherTraderId, Item sellingItem, int goldAmount, Item item)
	{
		//if(isLogedIn) sender.addMessage(new TradeDecisionMessage(sessionId, otherTraderId, offerAccepted, sellingItem, goldAmount, item));
	}
	
	public void sentEndTradeMessage()
	{
		if(isLogedIn) sender.addMessage(new TradeEndMessage(sessionId));
	}
	
	public MessageStatus register(String login, String password)
	{
		try
    	{
			System.setProperty("javax.net.ssl.trustStore", "za.store");
			SocketFactory sslsocketfactory = SSLSocketFactory.getDefault();
			clientSocket = sslsocketfactory.createSocket(ip, port);
			System.out.println("Connected to server IP: " + ip + " Port: " + clientSocket.getPort());
			
			sender = new MessageSender(clientSocket);
			sender.start();
	
	    	streamFromServer = new ObjectInputStream(clientSocket.getInputStream());
	    	
	    	sender.addMessage(new RegisterMessage(login, password));
	    	
	    	AuthRegisterMessage fromServer = (AuthRegisterMessage) getDataFromServer();

	    	System.out.println("Message from server: " + fromServer.toString());
	    	if(fromServer.getMessageStatus() == MessageStatus.OK) 
	    	{
	    		isLogedIn = true;
	    		this.sessionId = fromServer.getSessionID(); 
	    		
	    		start();
	    	}	    	
	    	
	    	return fromServer.getMessageStatus();
    	}
    	catch(IOException | ClassNotFoundException e)
    	{  		
    		System.out.println(e.getMessage());
    		
    		stopConnection();
    		return MessageStatus.ERROR;
    	}
	}
	
	public MessageStatus login(String login, String password)
	{
		try
    	{	
			System.setProperty("javax.net.ssl.trustStore", "za.store");
			SocketFactory sslsocketfactory = SSLSocketFactory.getDefault();
			clientSocket = sslsocketfactory.createSocket(ip, port);
			System.out.println("Connected to server IP: " + ip + " Port: " + port);
	
			sender = new MessageSender(clientSocket);
			sender.start();
			
	    	streamFromServer = new ObjectInputStream(clientSocket.getInputStream());
    	
	    	sender.addMessage(new LoginMessage(login, password));
	    	
	    	AuthLoginMessage fromServer = (AuthLoginMessage) getDataFromServer();
	    	if(fromServer.getMessageStatus() == MessageStatus.OK) 
	    	{
	    		isLogedIn = true;
	    		this.sessionId = fromServer.getSessionID(); 
	    		this.login = login;
	    		
	    		start();
	    	}
	    	
	    	return fromServer.getMessageStatus();
    	}
    	catch(Exception e)
    	{  		
    		System.out.println(e.getMessage());
    		
    		stopConnection();
    		return MessageStatus.ERROR;
    	}
	}

	private void handleCallback(Message callback)
	{	
		//System.out.println(serverCallback);
		
		switch(callback.getMessageType())
		{
			case PING:
				long ping = System.currentTimeMillis() - ((PingMessage) callback).getTime();
				System.out.println("PING " + ping);
			break;

			case LOAD_MAP:
				World world = ((SendMapMessage) callback).getMap();
				World.setLocal(true);
				MyGame.setGameMap(world);
				MyGame.setupPlayer(world.getPlayer(login));
			break;

			case UPDATE_ENTITY:
				if(MyGame.getGameMap() == null) break;
				UpdateEntityMessage message = (UpdateEntityMessage) callback;
				MyGame.getGameMap().updateEntityPos(message.getEntityId(), message.getPosition(), message.getVelocity());
			break;
			
			case UPDATE_CHAT:
				if(MyGame.getGameMap() == null) break;
				UpdateChatMessage updateChat = (UpdateChatMessage) callback;
				Chat.updateChat(updateChat.getText());
			break;
			
			case SPAWN_ENTITY:
				if(MyGame.getGameMap() == null) break;
				SpawnEntityMessage spawnMessage = (SpawnEntityMessage) callback;
				MyGame.getGameMap().spawnEntity(spawnMessage.getEntity());
			break;
			
			case UPDATE_TRADE_START: // we get info that other player started trade
				if(MyGame.getGameMap() == null) break;
				UpdateTradeStartEntityMessage tradeStartMessage = (UpdateTradeStartEntityMessage) callback;
				MyGame.getGameMap().setEntityTradeStart( tradeStartMessage.getLogin(), tradeStartMessage.getItem());
			
			case TRADE_OFFER: // we get info that other player send us an offer for our trade
			break;
			
			case DAMAGE_ENTITY:
				if(MyGame.getGameMap() == null) break;
				DamageEntityMessage damageEntity = (DamageEntityMessage) callback;
				/** TODO add some graphical effect*/
				Sound sound = MyGame.getResources().getSound("CLANG");
				sound.play(1f);
			break;
			
			case ENTITY_DEATH:
				if(MyGame.getGameMap() == null) break;
				DeathEntityMessage deathMessage = (DeathEntityMessage) callback;
				
				Entity entity = MyGame.getGameMap().getEntity(deathMessage.getEntityId());
					
				if(entity instanceof EntityPlayer) MyGame.getGameMap().removePlayer((EntityPlayer) entity);
				else MyGame.getGameMap().removeEntity(entity);
					
				MyGame.getRenderer().removeEntity(entity);
				/** TODO add some graphical effect and sound */
			break;
			
			case PLAYER_LOGOUT:
				if(MyGame.getGameMap() == null) break;
				PlayerLogoutMessage logoutMessage = (PlayerLogoutMessage) callback;
				EntityPlayer player = (EntityPlayer) MyGame.getGameMap().getEntity(logoutMessage.getPlayerId());
				MyGame.getGameMap().removePlayer(player);				
				MyGame.getRenderer().removeEntity(player);
			break;
			
			default:
				System.out.println("Unknown command");
		}
	}
	
	private Message getDataFromServer() throws ClassNotFoundException, IOException, EOFException
	{
		return (Message) streamFromServer.readObject();	
	}

	public void pingServer()
	{
		sender.addMessage(new PingMessage(sessionId, System.currentTimeMillis()));
	}
	
	public synchronized void stopConnection()
	{
		try
		{
			sender.close();
			streamFromServer.close();
			clientSocket.close();
		}
		catch(IOException e) 
		{
			System.out.println(e.getMessage());
		}
	}
	
	public boolean isServerOnline()
	{
		System.setProperty("javax.net.ssl.trustStore", "za.store");
		SocketFactory sslsocketfactory = SSLSocketFactory.getDefault();
		
		try(Socket socket = sslsocketfactory.createSocket(ip, port))
		{
			socket.close();
		}
		catch(Exception e)
		{
			return false;
		}

		return true;
	}
	
	public String getLogin()
	{
		return login;
	}
}