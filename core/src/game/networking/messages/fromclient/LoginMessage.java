package networking.messages.fromclient;

import networking.MessageType;

public class LoginMessage extends ClientMessage
{
	private static final long serialVersionUID = 1444243380705734934L;
	
	private String login, password;
	
	public LoginMessage(String login, String password)
	{
		super(MessageType.LOGIN, -1);

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