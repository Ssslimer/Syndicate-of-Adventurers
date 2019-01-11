package client.chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Chat 
{
	private static List<String> chatMessages = Collections.synchronizedList(new ArrayList<>());
	
	public static void updateChat(List<String> newChatMessages)
	{
		for(String str : newChatMessages)
		{
			chatMessages.add(str);
		}
	}
	
	public static List<String> getChatMessages()
	{
		return chatMessages;
	}
}
