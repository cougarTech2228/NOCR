package com.hflrobotics.scouting;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Configuration
{
	
	Document doc;
	
	public Configuration(String configLocation)
	{
		//Creates DOM structure based on the config xml
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(new File(configLocation));
		} 
		catch (ParserConfigurationException | SAXException | IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Do not use in production
	 */
	public void test()
	{
		System.out.println(doc.getElementsByTagName("match").item(0));
	}
}