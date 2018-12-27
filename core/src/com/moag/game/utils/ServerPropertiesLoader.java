package com.moag.game.utils;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ServerPropertiesLoader 
{
	private static final String XML_FILE_PATH= "server_properties.xml";
	
	private Element server_info;
	
	public ServerPropertiesLoader()
	{	
		try 
		{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File(XML_FILE_PATH));
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
