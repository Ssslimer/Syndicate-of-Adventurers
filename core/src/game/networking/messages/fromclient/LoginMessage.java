package networking.messages.fromclient;

import org.apache.commons.lang3.builder.ToStringBuilder;

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
	
	@Override
	public String toString()
	{
		return new ToStringBuilder(this)
				.appendSuper(super.toString())
				.append("login", login)
				.append("password not printed")
				.toString();
	}
}