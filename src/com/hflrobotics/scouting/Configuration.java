package com.hflrobotics.scouting;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class Configuration
{	
	Document doc;
	int regionSize;
	public ArrayList<Integer[]> matchRegions = new ArrayList<Integer[]>(0);
	public ArrayList<String[]> matchCriteria = new ArrayList<String[]>(0);
	public ArrayList<String[]> matchCropout = new ArrayList<String[]>(0);
	public ArrayList<Integer[]> pitRegions = new ArrayList<Integer[]>(0);
	public ArrayList<String[]> pitCriteria = new ArrayList<String[]>(0);
	public ArrayList<String[]> pitCropout = new ArrayList<String[]>(0);
	public ArrayList<String> scanSettings = new ArrayList<String>(0);
	
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
		try
		{
			loadMatchConfig();
			loadPitConfig();
			loadScanConfig();
		} 
		catch (DataConversionException e)
		{
			e.printStackTrace();
		}
	}
	
	private void loadScanConfig()
	{
		Element root = doc.getRootElement().getChild("scan");
		scanSettings.clear();
		scanSettings.add(root.getChildText("scanner"));
		scanSettings.add(root.getChildText("resolution"));
		scanSettings.add(root.getChildText("width"));
		scanSettings.add(root.getChildText("height"));
	}
	
	/**
	 * Loads match data from the config.xml and stores it into global variables
	 * @throws DataConversionException
	 */
	private void loadMatchConfig() throws DataConversionException
	{
		Element root = doc.getRootElement().getChild("match");
		matchRegions.clear();
		matchCriteria.clear();
		matchCropout.clear();
				
		for(Element aRegion : root.getChild("regions").getChildren("region"))
		{
			Integer[] region = 
				{
					aRegion.getAttribute("x").getIntValue(), 
					aRegion.getAttribute("x").getIntValue()
				};
			matchRegions.add(region);
		}
				
		for(Element aCriteria : root.getChild("criteria").getChildren("criterion"))
		{
			String[] criterion = 
				{
					aCriteria.getAttributeValue("header"), 
					aCriteria.getValue()
				};
			matchCriteria.add(criterion);
		}	
				
		for(Element aCropOut : root.getChild("cropOut").getChildren("crop"))
		{
			String[] crop = 
				{
					aCropOut.getValue(), 
					aCropOut.getAttributeValue("x"),
					aCropOut.getAttributeValue("x"),
					aCropOut.getAttributeValue("w"),
					aCropOut.getAttributeValue("h"),
					aCropOut.getAttributeValue("x"),
					aCropOut.getAttributeValue("fileType"),
					aCropOut.getAttributeValue("extract"),
				};
			matchCropout.add(crop);
		}
	}
	
	/**
	 * Loads pit data from the config.xml and stores it into global variables
	 * @throws DataConversionException
	 */
	private void loadPitConfig() throws DataConversionException
	{
		Element root = doc.getRootElement().getChild("pit");
		pitRegions.clear();
		pitCriteria.clear();
		pitCropout.clear();
				
		for(Element aRegion : root.getChild("regions").getChildren("region"))
		{
			Integer[] region = 
				{
					aRegion.getAttribute("x").getIntValue(), 
					aRegion.getAttribute("x").getIntValue()
				};
			pitRegions.add(region);
		}
				
		for(Element aCriteria : root.getChild("criteria").getChildren("criterion"))
		{
			String[] criterion = 
				{
					aCriteria.getAttributeValue("header"), 
					aCriteria.getValue()
				};
			pitCriteria.add(criterion);
		}	
				
		for(Element aCropOut : root.getChild("cropOut").getChildren("crop"))
		{
			String[] crop = 
				{
					aCropOut.getValue(), 
					aCropOut.getAttributeValue("x"),
					aCropOut.getAttributeValue("x"),
					aCropOut.getAttributeValue("w"),
					aCropOut.getAttributeValue("h"),
					aCropOut.getAttributeValue("x"),
					aCropOut.getAttributeValue("fileType"),
					aCropOut.getAttributeValue("extract"),
				};
			pitCropout.add(crop);
		}
	}
}