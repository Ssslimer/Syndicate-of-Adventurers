package util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import utils.ServerAddressValidator;

public class ServerAddressValidatorTest
{
	/** Other tests should not be required, as the regex was tested by the creator */
	@Test
	public void isIPAddressCorrectTest()
	{
		assertFalse(ServerAddressValidator.isIPAddressCorrect(null));
		assertFalse(ServerAddressValidator.isIPAddressCorrect(""));
	}
	
	@Test
	public void isPortCorrectTest()
	{
		assertFalse(ServerAddressValidator.isPortCorrect(null));
		assertFalse(ServerAddressValidator.isPortCorrect(""));
		assertFalse(ServerAddressValidator.isPortCorrect("-1"));
		assertFalse(ServerAddressValidator.isPortCorrect("sdasd"));
		assertFalse(ServerAddressValidator.isPortCorrect("70000"));
		assertFalse(ServerAddressValidator.isPortCorrect("65536"));
		assertTrue(ServerAddressValidator.isPortCorrect("1"));
		assertTrue(ServerAddressValidator.isPortCorrect("65535"));
	}
}
