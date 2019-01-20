package networking.messages.fromserver.auth;

import networking.MessageStatus;

public class AuthRegisterMessage extends AuthMessage 
{
	private static final long serialVersionUID = 3465926337376423203L;
	
	private final long sessionID;
	
	public AuthRegisterMessage(MessageStatus status, long sessionID)
	{
		super(status);
		
		this.sessionID = sessionID;
	}
	
	public long getSessionID()
	{
		return this.sessionID;
	}
}