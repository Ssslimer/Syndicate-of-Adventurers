package server;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.badlogic.gdx.math.Vector3;

import entities.EntityPlayer;
import entities.Item;
import networking.MessageStatus;
import networking.MoveDirection;
import networking.messages.Message;
import networking.messages.fromclient.ChatMessage;
import networking.messages.fromclient.LoginMessage;
import networking.messages.fromclient.RegisterMessage;
import networking.messages.fromclient.trade.AuctionOfferPriceMessage;
import networking.messages.fromclient.trade.AuctionOpenMessage;
import networking.messages.fromclient.trade.TradeDecisionMessage;
import networking.messages.fromclient.trade.TradeEndMessage;
import networking.messages.fromclient.trade.TradeOfferMessage;
import networking.messages.fromclient.trade.TradeStartMessage;
import networking.messages.fromserver.SendMapMessage;
import networking.messages.fromserver.UpdateChatMessage;
import networking.messages.fromserver.auth.AuthLoginMessage;
import networking.messages.fromserver.auth.AuthRegisterMessage;
import networking.messages.fromserver.trade.UpdateTradeDecisionMessage;
import networking.messages.fromserver.trade.UpdateTradeEndMessage;
import networking.messages.fromserver.trade.UpdateTradeOfferMessage;
import networking.messages.fromserver.trade.UpdateTradeStartEntityMessage;
import networking.messages.ingame.AttackMessage;
import networking.messages.ingame.MoveMessage;
import trade.Offer;
import trade.TradeState;

/** Thread for processing messages from clients */
public class MessageHandler extends Thread
{
	private final Server server;
	
	private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1); /** TODO add parameter from server config*/
	private Queue<MessageTask> messages = new ConcurrentLinkedQueue<>();
	private boolean shouldWait = false, isEmpty = true;

	public MessageHandler(Server server)
	{
		super("Message Handler");		
		this.server = server;
	}
	
	@Override
	public void run()
	{
		setPriority(10);

		while(true)
		{
			if(shouldWait || isEmpty)
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
					if(task == null) isEmpty = true;
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
		isEmpty = false;		
		synchronized(this){notify();}
	}
	
	private void processMessage(MessageTask task) throws IOException
	{	
		ConnectionToClient connectionWithClient = task.getMessageOwner();			
		Message message = task.getMessage();
		
		//System.out.println("NEW MESSAGE: " + message.getMessageType().toString()); /** TODO remove in the future */
		
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
			
			case TRADE_END:
				if(connectionWithClient.isLogedIn()) processTradeEnd(connectionWithClient, (TradeEndMessage) message);
			break;
				
			case AUCTION_OFFER_PRICE:
				if(connectionWithClient.isLogedIn()) processAuctionOffer(connectionWithClient, (AuctionOfferPriceMessage) message);
			break;
			
			case AUCTION_OPEN:
				if(connectionWithClient.isLogedIn()) processAuctionOpen(connectionWithClient, (AuctionOpenMessage) message);
			break;
			
			default: System.out.println("Client send wrong command!!");
		}
	}
	
	private void processAuctionOpen(ConnectionToClient connectionWithClient, AuctionOpenMessage message)
	{
		EntityPlayer owner = Server.getMap().getPlayer(connectionWithClient.getLogin());
		Server.getTradeManager().openAuction(owner, message.getItem(), message.getMinimalPrice());
	}

	private void processAuctionOffer(ConnectionToClient connectionWithClient, AuctionOfferPriceMessage message)
	{
		
	}

	private synchronized void processRegister(ConnectionToClient connectionWithClient, RegisterMessage message) throws IOException
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
			
			Server.addClient(connectionWithClient.getSessionID(), login);
			
			connectionWithClient.login();
			connectionWithClient.sendMessageToClient(new AuthRegisterMessage(MessageStatus.OK, connectionWithClient.getSessionID()));		
			
			Server.getMap().spawnEntity(new EntityPlayer(new Vector3(0, 0, 2), login));
			connectionWithClient.sendMessageToClient(new SendMapMessage(Server.getMap()));
			
			System.out.println("Player: " + login + " has loged in for the first time");
		}
	}
	
	private synchronized void processLogin(ConnectionToClient connectionWithClient, LoginMessage message) throws IOException
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
				Server.addClient(connectionWithClient.getSessionID(), login);
				connectionWithClient.setLogin(login);
				connectionWithClient.login();
				connectionWithClient.sendMessageToClient(new AuthLoginMessage(MessageStatus.OK, connectionWithClient.getSessionID()));

				Server.getMap().spawnEntity(new EntityPlayer(new Vector3(0, 0, 2), login));
				connectionWithClient.sendMessageToClient(new SendMapMessage(Server.getMap()));				
				System.out.println("Player: " + login + " has loged in");
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
			
			EntityPlayer player = Server.getMap().getPlayer(login);
			if(player == null) return;
			synchronized(player)
			{
				player.setMoveDirection(direction, ifStop);
			}
		}
	}
	
	private void processAttack(ConnectionToClient connectionWithClient, AttackMessage message)
	{
		if(connectionWithClient.isLogedIn())
		{
			String login = Server.getLogin(message.getSessionId());
			EntityPlayer player = Server.getMap().getPlayer(login);
			
			if(player == null) return;
			synchronized(player)
			{
				Server.getMap().getPlayer(login).attack();
			}
		}
	}
	
	private void processChat(ConnectionToClient connectionWithClient, ChatMessage message)
	{
		if(connectionWithClient.isLogedIn())
		{
			String nick = Server.getLogin(message.getSessionId());
			
			Server.getConnectionManager().sendToAll(new UpdateChatMessage(nick + ": " + message.getText()));
			Server.addChatMessage(nick + ": " + message.getText());
			System.out.println("[" + nick + "]:" + message.getText());
		}
	}
	
	private void processTradeStart(ConnectionToClient connectionWithClient, TradeStartMessage message)
	{	
		String login = Server.getLogin(message.getSessionId());
		EntityPlayer player = Server.getMap().getPlayer(login);
		player.setSellingOffer(new Offer(message.getLogin(), message.getItem()));
		player.setTradeState(TradeState.SELLING);
		
		Server.getConnectionManager().sendToAll(new UpdateTradeStartEntityMessage(login, message.getItem()));
	}
	
	private void processTradeOffer(ConnectionToClient connectionWithClient, TradeOfferMessage message)
	{
		String sellerLogin = message.getSellerLogin();
		String buyerLogin = message.getBuyerLogin();
		Item sellerItem = message.getSellerItem();
		Item buyerItem = message.getBuyerItem();
		
		System.out.println("SELLER: " + sellerLogin);
		System.out.println("BUYER: " + buyerLogin);
		
		EntityPlayer player = Server.getMap().getPlayer(sellerLogin);
		player.setHasOffer(true);
		
		Server.getConnectionManager().sendToAll(new UpdateTradeOfferMessage(sellerLogin, buyerLogin, buyerItem, sellerItem));
	}
	
	private void processTradeDecision(ConnectionToClient connectionWithClient, TradeDecisionMessage message)
    {
        System.out.println("HANDLER OFFER: " + message.getOfferAccepted());
        if(message.getOfferAccepted())
        {
            Item sellingItem = message.getSellerItem();
            Item offeringItem = message.getBuyerItem();

            String sellerLogin = message.getSellerLogin();
            String buyerLogin = message.getBuyerLogin();

            EntityPlayer sellerEntity = Server.getMap().getPlayer(sellerLogin);
            EntityPlayer buyerEntity = Server.getMap().getPlayer(buyerLogin);

            sellerEntity.removeItem(sellingItem);
            sellerEntity.addItem(offeringItem);

            buyerEntity.addItem(sellingItem);
            buyerEntity.removeItem(offeringItem);

            sellerEntity.setHasOffer(false);

            sellerEntity.setTradeState(TradeState.NOT_TRADING);
            buyerEntity.setTradeState(TradeState.NOT_TRADING);

            Server.getConnectionManager().sendToAll(new UpdateTradeDecisionMessage(message.getOfferAccepted(), sellerLogin, buyerLogin, offeringItem, sellingItem));
        }        
        else
        {
        	String sellerLogin = message.getSellerLogin();
        	EntityPlayer seller = Server.getMap().getPlayer(sellerLogin);
        	seller.setHasOffer(false);
        	
        	Server.getConnectionManager().sendToAll(new UpdateTradeDecisionMessage(message.getOfferAccepted(), sellerLogin, message.getBuyerLogin(), message.getBuyerItem(), message.getSellerItem()));
        	
        }
    }

	private void processTradeEnd(ConnectionToClient connectionWithClient, TradeEndMessage message)
	{
		String login = message.getLogin();
		EntityPlayer player = Server.getMap().getPlayer(login);
		player.setTradeState(TradeState.NOT_TRADING);
		
		Server.getConnectionManager().sendToAll(new UpdateTradeEndMessage(message.getLogin(), message.getItem()));
	}
	
	long generateSessionID() 
	{
		long sessionID;
		
		do
		{
			sessionID = Server.random.nextLong();
			
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
