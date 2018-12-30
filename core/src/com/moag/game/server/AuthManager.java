package com.moag.game.server;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class AuthManager
{
	static boolean registerPlayer(String login, String password)
	{
		Path pathToFolder = Paths.get("save", "auth");
		Path pathToFile = Paths.get("save", "auth", login+".txt");
		
		try
		{
			Files.createDirectories(pathToFolder);
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
			return false;
		}			
	
		try(PrintWriter writer = new PrintWriter(new FileWriter(pathToFile.toFile())))
		{
			writer.print(password);			
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
			return false;
		}
		
	    return true;
	}
	
	static boolean isPlayerRegistered(String login)
	{
		return Files.exists(Paths.get("save", "auth", login+".txt"));
	}
	
	static boolean checkPassword(String login, String password) throws IOException
	{
		Path pathToFile = Paths.get("save", "auth", login+".txt");
		List<String> lines = Files.readAllLines(pathToFile);
		if(lines.size() != 1) return false;
		
		Argon2 argon2 = Argon2Factory.create();
		
		return argon2.verify(lines.get(0), password);
	}
}
