package networking;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.Test;

import networking.messages.fromclient.ClientMessage;

class MessageTest 
{
	Random rand = new Random();
	
	/**TODO add other messages tests */
	
	@Test
	void test() 
	{
		MessageType type = MessageType.QUIT;
		long id = rand.nextLong();
		ClientMessage message = new ClientMessage(type, id);
		
		assertNotNull(message);
		assertTrue(message.getMessageType() == type);
		assertTrue(message.getSessionId() == id);	
	}

}
