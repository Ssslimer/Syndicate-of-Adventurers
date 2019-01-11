package networking.messages.fromclient;

import networking.MessageType;

public class RegisterMessage extends ClientMessage
{
	private static final long serialVersionUID = -2615994729186676330L;
	
	private String login, password;
	
	public RegisterMessage(String login, String password)
	{
		super(MessageType.REGISTER, -1);
		this.login = login;
		this.password = password;
	}
	
	public String getLogin()
	{
		return login;
	}
	
	public String getPassword()
	{
		return password;
	}
}