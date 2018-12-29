package util;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.moag.game.util.ServerPropertiesLoader;

class ServerPropertiesLoaderTest
{
	@Test
	void loadServerPropertiesTest() 
	{
		ServerPropertiesLoader loader = new ServerPropertiesLoader();

		assertEquals("192.168.2.59", loader.getIP());
		assertEquals(4444, loader.getPortNumber());
		assertEquals(50, loader.getTPS());
	}

}
