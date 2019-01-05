package test.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import com.moag.game.util.ServerPropertiesLoader;

class ServerPropertiesLoaderTest
{
	@Test
	void loadServerPropertiesTest() 
	{
		ServerPropertiesLoader loader = new ServerPropertiesLoader(Paths.get("src", "test", "res", "server_properties.xml"));

		assertEquals("192.168.2.53", loader.getIP());
		assertEquals(4444, loader.getPortNumber());
		assertEquals(50, loader.getTPS());
	}

}
