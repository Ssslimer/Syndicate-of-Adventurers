package util;

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
	
	private Element server_info;
	
	public ServerPropertiesLoader(Path path)
	{	
		load(path);
	}
	
	public ServerPropertiesLoader()
	{	
		load(XML_DEFAULT_FILE_PATH);
	}
	
	private void load(Path path)
	{
		try 
		{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(path.toFile());
			doc.getDocumentElement().normalize();

			NodeList list = doc.getElementsByTagName("server-info");
			server_info = (Element) list.item(0);
			
		} 
		catch(ParserConfigurationException e)
		{
			e.printStackTrace();
		} 
		catch(SAXException e)
		{
			e.printStackTrace();
		} 
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public String getIP()
	{	
		return server_info.getElementsByTagName("ip").item(0).getTextContent();
	}
	
	public int getPortNumber()
	{
		return Integer.parseInt(server_info.getElementsByTagName("port").item(0).getTextContent());
	}
	
	public int getTPS()
	{
		return Integer.parseInt(server_info.getElementsByTagName("tps").item(0).getTextContent());
	}
}
