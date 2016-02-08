package com.hflrobotics.scouting;

import java.io.File;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class Configuration
{
	
	Document doc;
	
	public Configuration(String configLocation)
	{
		File configFile = new File(configLocation);
        SAXBuilder saxBuilder = new SAXBuilder();
        try
		{
			doc = saxBuilder.build(configFile);
		}
		catch(JDOMException | IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Do not use in production
	 */
	public void test()
	{
		Element root = doc.getRootElement();
		//root.getChild("fileStruct").getChildren("file").get(0).getValue();
		
		System.out.print(root.getChild("fileStruct").getChildren("file").get(0).getAttributeValue("type"));
	}
}