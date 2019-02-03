package utils;

import java.util.regex.Pattern;

public class ServerAddressValidator
{
    // https://www.mkyong.com/regular-expressions/how-to-validate-ip-address-with-regular-expression/
    private static final String IP_ADDRESS_PATTERN = 
    		"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
    		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
    		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
    		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	
	public static boolean isIPAddressCorrect(String ip)
	{
		if(ip == null) return false;
		return Pattern.matches(IP_ADDRESS_PATTERN, ip);
	}
	
	public static boolean isPortCorrect(String s)
	{
		int port = -1;
	    try
	    {
	    	port = Integer.parseInt(s);
	    }
	    catch(NumberFormatException e)
	    {
	    	return false;
	    }
	    
	    return (port >= 0 && port <= 65535);
	}
}
