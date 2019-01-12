package networking.messages.fromserver;

import networking.MessageStatus;

public class AuthRegisterMessage extends AuthMessage 
{
	private static final long serialVersionUID = 3465926337376423203L;

	
	public AuthRegisterMessage(MessageStatus status) 
	{
		super(status);
	}

}
