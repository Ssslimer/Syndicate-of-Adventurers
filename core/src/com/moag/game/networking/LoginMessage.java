package com.moag.game.networking;

public class LoginMessage extends Message
{
	private static final long serialVersionUID = 1444243380705734934L;
	
	String password;
	
	public LoginMessage(String login, String password)
	{
		this.messageType = MessageType.LOGIN;
		
		this.clientID = login;
		this.password = password;
	}
	
	public String getLogin()
	{
		return clientID;
	}
	
	public String getPassword()
	{
		return password;
	}

}
