package com.moag.game.networking;

public enum MessageStatus 
{
	/**LoginMessage statuses*/
	WRONG_PASSWORD,
	NOT_REGISTRED,
	
	/**RegisterMessage statuses*/
	GIVEN_LOGIN_EXISTS,
	
	/**General statuses*/
	STATUS_OK,
	ERROR_OCCURED
}
