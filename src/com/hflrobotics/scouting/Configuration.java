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
	public int matchWidth = 0;
	public int matchHeight = 0;
	public ArrayList<Integer[]> matchRegions = new ArrayList<Integer[]>(0);
	public ArrayList<String[]> matchCriteria = new ArrayList<String[]>(0);
	public ArrayList<String[]> matchCropout = new ArrayList<String[]>(0);
	
	public int pitWidth = 0;
	public int pitHeight = 0;
	public ArrayList<Integer[]> pitRegions = new ArrayList<Integer[]>(0);
	public ArrayList<String[]> pitCriteria = new ArrayList<String[]>(0);
	public ArrayList<String[]> pitCropout = new ArrayList<String[]>(0);
	
	public ArrayList<String> scanSettings = new ArrayList<String>(0);
	public ArrayList<String> fileSettings = new ArrayList<String>(0);
	public double[]	imageBaseline = new double[7];
	
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
	 * Calls all the loadConfig functions
	 */
	public void load()
	{
		try
		{
			loadMatchConfig();
			loadPitConfig();
			loadScanConfig();
			loadBaselineConfig();
			loadFileConfig();
		} 
		catch (DataConversionException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads baseline data from the config.xml and stores it into global variables
	 * @throws DataConversionException
	 */
	private void loadFileConfig()
	{
		Element root = doc.getRootElement().getChild("files");
		scanSettings.clear();
		fileSettings.add(root.getChild("matchToBeScanned").getValue());
		fileSettings.add(root.getChild("matchScanned").getValue());
		fileSettings.add(root.getChild("matchData").getValue());
		fileSettings.add(root.getChild("pitToBeScanned").getValue());
		fileSettings.add(root.getChild("pitScanned").getValue());
		fileSettings.add(root.getChild("pitData").getValue());
	}
	
	/**
	 * Loads baseline data from the config.xml and stores it into global variables
	 */
	private void loadBaselineConfig()
	{
		Element root = doc.getRootElement().getChild("sheet").getChild("baseline");
		imageBaseline[0] = Double.valueOf(root.getChild("ul").getAttributeValue("xOff"));
		imageBaseline[1] = Double.valueOf(root.getChild("ul").getAttributeValue("yOff"));
		imageBaseline[2] = Double.valueOf(root.getChild("rotation").getValue());
		imageBaseline[3] = Double.valueOf(root.getChild("crop").getAttributeValue("x"));
		imageBaseline[4] = Double.valueOf(root.getChild("crop").getAttributeValue("y"));
		imageBaseline[5] = Double.valueOf(root.getChild("crop").getAttributeValue("w"));
		imageBaseline[6] = Double.valueOf(root.getChild("crop").getAttributeValue("h"));
	}
	
	/**
	 * Loads scan data from the config.xml and stores it into global variables
	 */
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
		
		matchWidth = Integer.valueOf(root.getChild("regionSize").getAttributeValue("w"));
		matchHeight = Integer.valueOf(root.getChild("regionSize").getAttributeValue("h"));
				
		for(Element aRegion : root.getChild("regions").getChildren("region"))
		{
			Integer[] region = 
				{
					aRegion.getAttribute("x").getIntValue(), 
					aRegion.getAttribute("y").getIntValue()
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
		
		pitWidth = Integer.valueOf(root.getChild("regionSize").getAttributeValue("w"));
		pitHeight = Integer.valueOf(root.getChild("regionSize").getAttributeValue("h"));
		
		for(Element aRegion : root.getChild("regions").getChildren("region"))
		{
			Integer[] region = 
				{
					aRegion.getAttribute("x").getIntValue(), 
					aRegion.getAttribute("y").getIntValue()
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