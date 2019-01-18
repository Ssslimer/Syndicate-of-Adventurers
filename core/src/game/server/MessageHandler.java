package server;

import java.io.IOException;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.badlogic.gdx.math.Vector3;

import entities.EntityPlayer;
import entities.Item;
import networking.MessageStatus;
import networking.MessageType;
import networking.MoveDirection;
import networking.messages.Message;
import networking.messages.fromclient.ChatMessage;
import networking.messages.fromclient.LoginMessage;
import networking.messages.fromclient.RegisterMessage;
import networking.messages.fromserver.AuthLoginMessage;
import networking.messages.fromserver.AuthRegisterMessage;
import networking.messages.fromserver.SendMapMessage;
import networking.messages.fromserver.UpdateChatMessage;
import networking.messages.ingame.AttackMessage;
import networking.messages.ingame.MoveMessage;
import networking.messages.trade.TradeDecisionMessage;
import networking.messages.trade.TradeOfferMessage;
import networking.messages.trade.TradeStartMessage;

/** Thread for processing messages from clients */
public class MessageHandler extends Thread
{
	private final Server server;
	
	private Queue<MessageTask> messages = new ConcurrentLinkedQueue<>();
	private boolean shouldWait = true;
	
	private Random rand;
	
	public MessageHandler(Server server)
	{
		super("Message Handler");		
		this.server = server;
		rand = new Random();
	}
	
	@Override
	public void run()
	{
		setPriority(10);
		Executor executor = Executors.newFixedThreadPool(1);
		
		while(true)
		{
			if(shouldWait)
			{
				synchronized(this)
				{
					try{wait();}
					catch(InterruptedException e) {e.printStackTrace();}
				}
			}
			
			executor.execute(() ->
			{
				try
				{
					MessageTask task = messages.poll();
					if(task == null) shouldWait = true;
					else processMessage(task);
				}
				catch(IOException e)
				{
					System.out.println(e.getMessage());
				}
			});
		}
	}
	
	void handleMessage(ConnectionToClient source, Message message)
	{
		messages.add(new MessageTask(source, message));
		shouldWait = false;		
		synchronized(this){notify();}
	}
	
	private void processMessage(MessageTask task) throws IOException
	{	
		ConnectionToClient connectionWithClient = task.getMessageOwner();			
		Message message = task.getMessage();
		
		System.out.println("NEW MESSAGE: " + message.getMessageType().toString()); /** TODO remove in the future */
		
		switch(message.getMessageType())
		{			
			case PING:
				connectionWithClient.sendMessageToClient(message);
			break;
			
			case REGISTER:
				processRegister(connectionWithClient, (RegisterMessage) message);
			break;
			
			case LOGIN: 
				processLogin(connectionWithClient, (LoginMessage) message);			
			break;

			case QUIT:
				/** TODO check whether this is enough */
				connectionWithClient.stopCommunication();
			break;
			
			case MOVE:
				if(connectionWithClient.isLogedIn()) processMove(connectionWithClient, (MoveMessage) message);
			break;
				
			case ATTACK:
				if(connectionWithClient.isLogedIn()) processAttack(connectionWithClient, (AttackMessage) message);
			break;
			
			case CHAT_MESSAGE:	
				if(connectionWithClient.isLogedIn()) processChat(connectionWithClient, (ChatMessage) message);
			break;
			
			case TRADE_START:
				if(connectionWithClient.isLogedIn()) processTradeStart(connectionWithClient, (TradeStartMessage) message);
			break;
				
			case TRADE_OFFER:
				if(connectionWithClient.isLogedIn()) processTradeOffer(connectionWithClient, (TradeOfferMessage) message);
			break;
			
			case TRADE_DECISION:
				if(connectionWithClient.isLogedIn()) processTradeDecision(connectionWithClient, (TradeDecisionMessage) message);
			break;
				
			default:/** TODO maybe delete message to client ? */
				System.out.println("Client send wrong command!!");
		}
	}
	
	private void processRegister(ConnectionToClient connectionWithClient, RegisterMessage message) throws IOException
	{		
		connectionWithClient.stopCommunication();
		
		String login = message.getLogin();
		char[] password = message.getPassword().toCharArray();
		String hashedPassword = server.getAuthManager().hashPassword(password);
			
		if(server.getAuthManager().isPlayerRegistered(login))
		{
			connectionWithClient.sendMessageToClient(new AuthRegisterMessage(MessageStatus.GIVEN_LOGIN_EXISTS, -1));	
		}
		else
		{
			server.getAuthManager().registerPlayerIfNotRegistered(login, hashedPassword);
			
			long sessionID = generateSessionID();
			Server.addClient(sessionID, login);
			
			connectionWithClient.login();
			connectionWithClient.sendMessageToClient(new AuthRegisterMessage(MessageStatus.OK, sessionID));			
			connectionWithClient.sendMessageToClient(new SendMapMessage(Server.getMap()));
			
			System.out.println("Player: " + login + " has loged in for the first time");
			Server.getMap().spawnEntity(new EntityPlayer(new Vector3(0, 0, 2), login));
		}
	}
	
	private void processLogin(ConnectionToClient connectionWithClient, LoginMessage message) throws IOException
	{
		String login = message.getLogin();
		String password = message.getPassword();
			
		if(!server.getAuthManager().isPlayerRegistered(login))
		{
			connectionWithClient.sendMessageToClient(new AuthLoginMessage(MessageStatus.NOT_REGISTRED, -1));
		}
		else
		{
			if(server.getAuthManager().checkPassword(login, password))
			{
				long sessionID = generateSessionID();
				Server.addClient(sessionID, login);
				connectionWithClient.login();
				connectionWithClient.sendMessageToClient(new AuthLoginMessage(MessageStatus.OK, sessionID));
				connectionWithClient.sendMessageToClient(new SendMapMessage(Server.getMap()));
				
				System.out.println("Player: " + login + " has loged in");
				Server.getMap().spawnEntity(new EntityPlayer(new Vector3(0, 0, 2), login));
			}
			else connectionWithClient.sendMessageToClient(new AuthLoginMessage(MessageStatus.WRONG_PASSWORD, -1));
		}
	}
	
	private void processMove(ConnectionToClient connectionWithClient, MoveMessage message)
	{
		if(connectionWithClient.isLogedIn())
		{
			MoveDirection direction = message.getDirection();
			boolean ifStop = message.getIfToStop();		
			String login = Server.getLogin(message.getSessionId());
			
			Server.getMap().getPlayer(login).setMoveDirection(direction, ifStop);
		}
	}
	
	private void processAttack(ConnectionToClient connectionWithClient, AttackMessage message)
	{
		if(connectionWithClient.isLogedIn())
		{
			String login = Server.getLogin(message.getSessionId());
			Server.getMap().getPlayer(login).attack();
		}
	}
	
	private void processChat(ConnectionToClient connectionWithClient, ChatMessage message)
	{
		if(connectionWithClient.isLogedIn())
		{
			String nick = Server.getLogin(message.getSessionId());
			
			for(ConnectionToClient connectionToClient : Server.getConnectionManager().getAllConnections())
			{
				connectionToClient.sendMessageToClient(new UpdateChatMessage(nick + ": " + message.getText()));
			}
			Server.addChatMessage(nick + ": " + message.getText());
			System.out.println("[" + nick + "]:" + message.getText());
		}
	}
	
	private void processTradeStart(ConnectionToClient connectionWithClient, TradeStartMessage message)
	{
		
	}
	
	private void processTradeOffer(ConnectionToClient connectionWithClient, TradeOfferMessage message)
	{
		
	}
	
	private void processTradeDecision(ConnectionToClient connectionWithClient, TradeDecisionMessage message)
	{
		if(message.getOfferAccepted())
		{
			Item sellingItem = message.getSellingItem();
			Item offeringItem = message.getOffer().getItem();
			int offeringGold = message.getOffer().getGoldAmount();
			
			long sellerId = message.getSessionId();
			long buyerId = message.getOtherTraderId();
			
			EntityPlayer sellerEntity = (EntityPlayer)Server.getMap().getEntity(sellerId);
			EntityPlayer buyerEntity = (EntityPlayer)Server.getMap().getEntity(buyerId);
			
			sellerEntity.removeItem(sellingItem);
			if(offeringItem != null) sellerEntity.addItem(offeringItem);
			if(offeringGold > 0) sellerEntity.addGold(offeringGold);
			
			buyerEntity.addItem(sellingItem);
			if(offeringItem != null) buyerEntity.removeItem(offeringItem);
			if(offeringGold > 0) buyerEntity.removeGold(offeringGold);
		}
	}
	
	private long generateSessionID() 
	{
		long sessionID;
		
		do
		{
			sessionID = rand.nextLong();
			
		}while(!Server.IdNotOccupied(sessionID));
		
		return sessionID;
	}

	private class MessageTask
	{
		private final ConnectionToClient messageOwner;
		private final Message message;
		
		private MessageTask(ConnectionToClient messageOwner, Message message)
		{
			this.messageOwner = messageOwner;
			this.message = message;
		}

		private ConnectionToClient getMessageOwner()
		{
			return messageOwner;
		}

		private Message getMessage()
		{
			return message;
		}
	}
}
