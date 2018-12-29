package com.moag.game.networking;

public class RegisterMessage extends Message
{
	private static final long serialVersionUID = -2615994729186676330L;
	
	private String password;
	
	public RegisterMessage(String login, String password)
	{
		this.messageType = MessageType.REGISTER;
		
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
