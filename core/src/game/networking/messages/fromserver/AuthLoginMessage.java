package networking.messages.fromserver;

import networking.MessageStatus;

public class AuthLoginMessage extends AuthMessage 
{
	private static final long serialVersionUID = -3329951535473699645L;
	
	private long sessionID;
	
	public AuthLoginMessage(MessageStatus status, long sessionID) {
		super(status);
		
		this.sessionID = sessionID;
	}
	
	public long getSessionID()
	{
		return this.sessionID;
	}

}
