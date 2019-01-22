package util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import server.AuthManager;

public class AuthManagerTest
{
	private static final Path PATH_TO_FILES = Paths.get("src", "test", "res", "save", "auth");	
	
	@AfterEach
	public void clearAuthFolder()
	{
		try
		{
			for(File file : PATH_TO_FILES.toFile().listFiles()) 
			{
				Files.delete(file.toPath());
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRegister()
	{
		AuthManager authManager = new AuthManager(PATH_TO_FILES);

		assertFalse(authManager.isPlayerRegistered("Maciej"));
		
		try
		{
			String hashedPassword = authManager.hashPassword("test_password".toCharArray());
			authManager.registerPlayerIfNotRegistered("Maciej", hashedPassword);
		}
		catch(IOException e)
		{
			fail(e.getStackTrace().toString());
		}
		
		assertTrue(authManager.isPlayerRegistered("Maciej"));
	}
	
	@Test
	public void testLogin()
	{
		AuthManager authManager = new AuthManager(PATH_TO_FILES);

		try
		{
			String hashedPassword = authManager.hashPassword("test_password".toCharArray());
			authManager.registerPlayerIfNotRegistered("Maciej", hashedPassword);
			assertTrue(authManager.checkPassword("Maciej", "test_password"));
		}
		catch(IOException e)
		{
			fail(e.getStackTrace().toString());
		}
	}
}
