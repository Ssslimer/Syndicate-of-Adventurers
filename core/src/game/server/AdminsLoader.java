package server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class AdminsLoader
{
	private static final Path DEFAULT_PATH = Paths.get("admins.txt");
	private Path path;
	
	public AdminsLoader()
	{
		this.path = DEFAULT_PATH;
	}
	
	public AdminsLoader(Path path)
	{
		this.path = path;
	}
	
	public List<String> load()
	{
		List<String> admins = new ArrayList<>();
		
		try(Stream<String> stream = Files.lines(path))
		{
	        stream.forEach(e -> admins.add(e));
		}
		catch(IOException e)
		{
			System.out.println("Error with loading admins " + e.getMessage());
			return Collections.emptyList();
		}
		
		return admins;
	}
}
