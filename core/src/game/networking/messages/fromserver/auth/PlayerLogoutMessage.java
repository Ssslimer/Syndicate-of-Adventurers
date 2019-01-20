package networking.messages.fromserver.auth;

import networking.MessageType;
import networking.messages.Message;

public class PlayerLogoutMessage extends Message
{
	private static final long serialVersionUID = 4790433071670983923L;

	private final long playerId;
	
	public PlayerLogoutMessage(long playerId)
	{
		super(MessageType.PLAYER_LOGOUT);
		this.playerId = playerId;
	}

	public long getPlayerId()
	{
		return playerId;
	}
}