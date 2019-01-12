package networking;

public enum MessageType 
{
	REGISTER, 
	LOGIN,
	QUIT,
	LOAD_MAP,
	PING,
	
	MOVE,
	ATTACK,
	CHAT_MESSAGE,
	
	FROM_SERVER, 
	UPDATE_ENTITY,
	UPDATE_CHAT
}
