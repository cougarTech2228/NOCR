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
	
	public int matchWidth = 0;
	public int matchHeight = 0;
	public int[] matchTeam = new int[4];
	public int[] matchMatch = new int[4];
	public ArrayList<Integer[]> matchRegions = new ArrayList<Integer[]>(0);
	public ArrayList<String[]> matchCriteria = new ArrayList<String[]>(0);
	public ArrayList<String[]> matchCropout = new ArrayList<String[]>(0);
	
	public int pitWidth = 0;
	public int pitHeight = 0;
	public int[] pitTeam = new int[4];
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
		loadScanConfig();
		
		try
		{
			loadMatchConfig();
			loadPitConfig();			
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
		fileSettings.add(root.getChild("baseline").getValue());
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
	public void loadScanConfig()
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
	public void loadMatchConfig() throws DataConversionException
	{
		Element root = doc.getRootElement().getChild("match");
		matchRegions.clear();
		matchCriteria.clear();
		matchCropout.clear();
		
		matchTeam[0] = Integer.valueOf(root.getChild("team").getAttributeValue("x"));
		matchTeam[1] = Integer.valueOf(root.getChild("team").getAttributeValue("y"));
		matchTeam[2] = Integer.valueOf(root.getChild("team").getAttributeValue("w"));
		matchTeam[3] = Integer.valueOf(root.getChild("team").getAttributeValue("h"));
		
		matchMatch[0] = Integer.valueOf(root.getChild("match").getAttributeValue("x"));
		matchMatch[1] = Integer.valueOf(root.getChild("match").getAttributeValue("y"));
		matchMatch[2] = Integer.valueOf(root.getChild("match").getAttributeValue("w"));
		matchMatch[3] = Integer.valueOf(root.getChild("match").getAttributeValue("h"));
		
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
	public void loadPitConfig() throws DataConversionException
	{
		Element root = doc.getRootElement().getChild("pit");
		pitRegions.clear();
		pitCriteria.clear();
		pitCropout.clear();
		
		pitTeam[0] = Integer.valueOf(root.getChild("team").getAttributeValue("x"));
		pitTeam[1] = Integer.valueOf(root.getChild("team").getAttributeValue("y"));
		pitTeam[2] = Integer.valueOf(root.getChild("team").getAttributeValue("w"));
		pitTeam[3] = Integer.valueOf(root.getChild("team").getAttributeValue("h"));
		
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