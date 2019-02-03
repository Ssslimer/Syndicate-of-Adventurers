package networking.messages.fromclient;

import org.apache.commons.lang3.builder.ToStringBuilder;

import networking.MessageType;

public class RegisterMessage extends ClientMessage
{
	private static final long serialVersionUID = -2615994729186676330L;
	
	private final String login, password;
	
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