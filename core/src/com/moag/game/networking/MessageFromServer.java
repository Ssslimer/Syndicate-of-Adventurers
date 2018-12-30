package com.moag.game.networking;

public class MessageFromServer extends Message
{
	private static final long serialVersionUID = 2431956245233772488L;

	private boolean wasSuccessful;
	private String messageString;
	private MessageStatus status;
	
	public MessageFromServer(boolean wasSuccessful, String messageString)
	{
		this.messageType = MessageType.FROM_SERVER;
		
		this.wasSuccessful = wasSuccessful;
		this.messageString = messageString;
	}
	
	public MessageFromServer(boolean wasSuccessful, MessageStatus status)
	{
		this.messageType = MessageType.FROM_SERVER;
		
		this.wasSuccessful = wasSuccessful;
		this.status = status;
	}
	
	public boolean getWasSuccessful()
	{
		return wasSuccessful;
	}
	
	public String getMessageString()
	{
		return messageString;
	}
	
	public MessageStatus getMessageStatus()
	{
		return status;
	}
}
