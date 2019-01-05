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
	private static final Path DEFAULT_PATH = Paths.get("save", "auth");
	
	private final Argon2 argon2 = Argon2Factory.create();	
	private final Path path;
	
	private int hashingIterations = 10, hashingMemory = 65536, hashingParallelism = 1;
	
	public AuthManager()
	{
		this(DEFAULT_PATH);
	}
	
	public AuthManager(Path path)
	{
		this.path = path;
	}

	public void registerPlayerIfNotRegistered(String login, String hashedPassword) throws IOException
	{
		Path pathToFile = path.resolve(login + ".txt");

		Files.createDirectories(path);		
	
		try(PrintWriter writer = new PrintWriter(new FileWriter(pathToFile.toFile())))
		{
			writer.print(hashedPassword);			
		}
	}
	
	public boolean isPlayerRegistered(String login)
	{
		return Files.exists(path.resolve(login+".txt"));
	}
	
	public boolean checkPassword(String login, String password) throws IOException
	{
		Path pathToFile = path.resolve(login+".txt");
		List<String> lines = Files.readAllLines(pathToFile);
		if(lines.size() != 1) return false;
		return argon2.verify(lines.get(0), password);
	}
	
	public String hashPassword(char[] password)
	{
		return argon2.hash(hashingIterations, hashingMemory, hashingParallelism, password);
	}
}
