package client;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Chat 
{
	private static final int MAX_SIZE = 30;
	
	private static List<String> chatMessages = Collections.synchronizedList(new LinkedList<>());
	
	public static void updateChat(String s)
	{
		chatMessages.add(s);

		removeOldMessages();
	}
	
	public static List<String> getChatMessages()
	{
		return chatMessages;
	}
	
	private static void removeOldMessages()
	{
		while(chatMessages.size() > MAX_SIZE)
		{
			chatMessages.remove(0);
		}
	}
}
