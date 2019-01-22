package util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import server.ServerProperties;
import server.ServerPropertiesLoader;

public class ServerPropertiesLoaderTest
{
	@Test
	public void loadServerPropertiesTest() 
	{
		ServerPropertiesLoader loader = new ServerPropertiesLoader(Paths.get("src", "test", "resources", "server_properties.xml"));
		ServerProperties properties = loader.load();
		assertEquals("192.168.2.53", properties.getIP());
		assertEquals(4444, properties.getPortNumber());
		assertEquals(50, properties.getTPS());
	}

}
