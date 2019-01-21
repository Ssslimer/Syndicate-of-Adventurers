package client;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ChatTest 
{
	private static final int MAX_SIZE = 30;
	
	@Test
	void test() 
	{
		assertNotNull(Chat.getChatMessages());
		assertTrue(Chat.getChatMessages().isEmpty());
		
		String message = "test";
		Chat.updateChat(message);
		
		assertTrue(!Chat.getChatMessages().isEmpty());
		assertTrue(Chat.getChatMessages().contains(message));
		
		//Checking whether Chat will delete old message
		for(int i = 0; i < 30; i++)
		{
			String msg = "Message: " + (i + 1);
			Chat.updateChat(msg);
		}
		
		assertTrue(Chat.getChatMessages().size() <= MAX_SIZE);
		assertTrue(!Chat.getChatMessages().contains(message));
	}

}
