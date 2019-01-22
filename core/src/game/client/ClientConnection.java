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
import networking.messages.fromclient.trade.TradeDecisionMessage;
import networking.messages.fromclient.trade.TradeEndMessage;
import networking.messages.fromclient.trade.TradeOfferMessage;
import networking.messages.fromclient.trade.TradeStartMessage;
import networking.messages.fromserver.DamageEntityMessage;
import networking.messages.fromserver.DeathEntityMessage;
import networking.messages.fromserver.SendMapMessage;
import networking.messages.fromserver.SpawnEntityMessage;
import networking.messages.fromserver.UpdateChatMessage;
import networking.messages.fromserver.UpdateEntityMessage;
import networking.messages.fromserver.UpdateMoneyMessage;
import networking.messages.fromserver.auth.AuthLoginMessage;
import networking.messages.fromserver.auth.AuthRegisterMessage;
import networking.messages.fromserver.auth.PlayerLogoutMessage;
import networking.messages.fromserver.trade.AuctionUpdateListMessage;
import networking.messages.fromserver.trade.UpdateTradeDecisionMessage;
import networking.messages.fromserver.trade.UpdateTradeEndMessage;
import networking.messages.fromserver.trade.UpdateTradeOfferMessage;
import networking.messages.fromserver.trade.UpdateTradeStartEntityMessage;
import networking.messages.ingame.AttackMessage;
import networking.messages.ingame.MoveMessage;
//github.com/Ssslimer/Syndicate-of-Adventurers.git
import world.World;

public class ClientConnection extends Thread
{
	private Socket clientSocket;
	private ObjectInputStream streamFromServer;
	
	private final String ip;
	private final int port;
	
	private boolean isLogedIn;
	private String login;
	private long sessionID;
	
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
		if(isLogedIn) sender.addMessage(new MoveMessage(sessionID, direction, toStop));
	}

	public void attack()
	{
		if(isLogedIn) sender.addMessage(new AttackMessage(sessionID));
	}
	
	public void sentChatMessage(String chatMessageString)
	{
		if(isLogedIn) sender.addMessage(new ChatMessage(sessionID, chatMessageString));
	}
	
	public void sentTradeStartMessage(Item item)
	{
		if(isLogedIn) sender.addMessage(new TradeStartMessage(sessionID, login, item));
	}
	
	public void sentTradeOfferMessage(String buyerLogin, String sellerLogin, Item buyerItem, Item sellerItem)
	{
		if(isLogedIn) sender.addMessage(new TradeOfferMessage(sessionID, sellerLogin, buyerLogin, buyerItem, sellerItem));
	}
	
	public void sentTradeDecisionMessage(boolean offerAccepted, String sellerLogin, String buyerLogin, Item buyerItem, Item sellerItem)
	{
		if(isLogedIn) sender.addMessage(new TradeDecisionMessage(sessionID, offerAccepted, sellerLogin, buyerLogin, buyerItem, sellerItem));
	}
	
	public void sentEndTradeMessage(Item item)
	{
		if(isLogedIn) sender.addMessage(new TradeEndMessage(sessionID, login, item));
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
	    		this.sessionID = fromServer.getSessionID(); 
	    		
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
	    		this.sessionID = fromServer.getSessionID(); 
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
			break;
			
			case UPDATE_TRADE_OFFER:
				if(MyGame.getGameMap() == null) break;
				UpdateTradeOfferMessage tradeOfferUpdate = (UpdateTradeOfferMessage) callback;
				MyGame.getGameMap().updateTradeOffer(tradeOfferUpdate.getSellerLogin(), tradeOfferUpdate.getBuyerLogin(), tradeOfferUpdate.getBuyerItem(), tradeOfferUpdate.getSellerItem());
			break;
			
			case UPDATE_TRADE_DECISION:
				if(MyGame.getGameMap() == null) break;
				UpdateTradeDecisionMessage msg = (UpdateTradeDecisionMessage) callback;
				MyGame.getGameMap().updateTradeDecision(msg.isOfferAccepted(), msg.getSellerLogin(), msg.getBuyerLogin(), msg.getBuyerItem(), msg.getSellerItem());
			break;
			
			case UPDATE_TRADE_END:
				if(MyGame.getGameMap() == null) break;
				UpdateTradeEndMessage tradeEndMsg = (UpdateTradeEndMessage) callback;
				MyGame.getGameMap().updateTradeEnd(tradeEndMsg.getLogin());
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
			
			case UPDATE_MONEY:
				if(MyGame.getGameMap() == null) break;
				UpdateMoneyMessage moneyMessage = (UpdateMoneyMessage) callback;
				MyGame.getPlayer().setGold(moneyMessage.getMoney());
			break;
			
			case AUCTION_UPDATE_LIST:
				MyGame.getTradeManager().setAuctions(((AuctionUpdateListMessage) callback).getAuctions());
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
		sender.addMessage(new PingMessage(sessionID, System.currentTimeMillis()));
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

	public void sendMessage(Message message)
	{
		if(isLogedIn) sender.addMessage(message);
	}
	
	public long getSessionID()
	{
		return sessionID;
	}
}