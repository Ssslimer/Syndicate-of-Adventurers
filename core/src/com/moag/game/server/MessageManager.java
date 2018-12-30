package com.moag.game.server;

public class MessageManager extends Thread
{
	private static boolean wasMessageHandled = false;
	
	public void run()
	{
		while(true)
		{
			if(!ConnectionManager.getConnectionsToClients().isEmpty())
			{
				if(ConnectionManager.queueIsNotEmpty())
				{
					long currentID = ConnectionManager.getHeadMessage().getSessionID();
					ConnectionManager.getConnectionsToClients().get(currentID).canHandleMessage();
					while(!wasMessageHandled)
					{
						//then given connection server signalizes that he handled it by invoking MessageHandled() method
					}
					
					wasMessageHandled = false;
				}
			}
		}
	}
	
	public static void messageHandled()
	{
		wasMessageHandled = true;
	}
}
