package com.moag.game.networking;

public class NetworkingEnums
{
	public enum MessageContent
	{
		STRING, REGISTER, LOGIN, WRONG_COMMAND;
	}

	public enum RegisterStatus
	{
		OK, ERROR, OCCUPIED;
	}
	
	public enum LoginStatus
	{
		OK, WRONG_PASSWORD, NOT_REGISTERED;
	}
}
