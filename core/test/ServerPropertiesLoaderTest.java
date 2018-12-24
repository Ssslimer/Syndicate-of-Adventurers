import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.moag.game.utils.ServerPropertiesLoader;

class ServerPropertiesLoaderTest {

	@Test
	void test() 
	{
		String expectedIp = "192.168.2.59";
		int expectedPort = 4444;
		
		String ip;
		int port;
		
		ServerPropertiesLoader loader = new ServerPropertiesLoader();
		
		ip = loader.getIP();
		port = loader.getPortNumber();
		
		assertNotNull(loader);
		
		assertNotNull(ip);
		
		assertTrue(port == expectedPort);
		assertTrue(ip.compareTo(expectedIp) == 0);
	}

}
