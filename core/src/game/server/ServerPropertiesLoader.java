package server;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ServerPropertiesLoader 
{
	private static final Path XML_DEFAULT_FILE_PATH = Paths.get("server_properties.xml");
	private final Path path;
	
	private Element server_info;
	
	public ServerPropertiesLoader()
	{	
		this.path = XML_DEFAULT_FILE_PATH;
	}
	
	public ServerPropertiesLoader(Path path)
	{	
		this.path = path;
	}

	public ServerProperties load()
	{
		try 
		{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(path.toFile());
			doc.getDocumentElement().normalize();

			NodeList list = doc.getElementsByTagName("server-info");
			server_info = (Element) list.item(0);
			
			return new ServerProperties(getIP(), getPortNumber(), getTPS());		
		} 
		catch(ParserConfigurationException e)
		{
			System.out.println(e.getMessage());
		} 
		catch(SAXException e)
		{
			System.out.println(e.getMessage());
		} 
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
		
		return new ServerProperties();
	}
	
	private String getIP()
	{	
		return server_info.getElementsByTagName("ip").item(0).getTextContent();
	}
	
	private int getPortNumber()
	{
		return Integer.parseInt(server_info.getElementsByTagName("port").item(0).getTextContent());
	}
	
	private int getTPS()
	{
		return Integer.parseInt(server_info.getElementsByTagName("tps").item(0).getTextContent());
	}
}
