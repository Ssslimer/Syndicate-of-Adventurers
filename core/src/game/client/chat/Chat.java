package client.chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Chat 
{
	private static final int MAX_SIZE = 30;
	
	private static List<String> chatMessages = Collections.synchronizedList(new ArrayList<>());
	
	public static void updateChat(List<String> newChatMessages)
	{
		for(String str : newChatMessages)
		{
			chatMessages.add(str);
		}
		
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
