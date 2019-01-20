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
	
	TRADE_START,
	TRADE_OFFER,
	TRADE_DECISION,
	TRADE_END,
	
	UPDATE_TRADE_START,
	UPDATE_TRADE_OFFER,
	UPDATE_TRADE_DECISION,
	
	FROM_SERVER,
	UPDATE_ENTITY,
	UPDATE_CHAT,
	SPAWN_ENTITY,
	DAMAGE_ENTITY,
	UPDATE_TRADE,
	ENTITY_DEATH,
	PLAYER_LOGOUT
}
