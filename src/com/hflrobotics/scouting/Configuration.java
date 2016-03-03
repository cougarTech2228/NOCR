package com.hflrobotics.scouting;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jdom2.DataConversionException;
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

	public ArrayList<Integer[]> getPitRegions() throws DataConversionException
	{
		Element root = doc.getRootElement().getChild("pit");
		ArrayList<Integer[]> pitRegions = new ArrayList<Integer[]>(0);
		
		for(Element aRegion : root.getChild("regions").getChildren("region"))
		{
			Integer[] region = 
				{
					aRegion.getAttribute("x").getIntValue(), 
					aRegion.getAttribute("y").getIntValue()
				};
			pitRegions.add(region);
		}
		
		return pitRegions;
	}
	
	public ArrayList<String[]> getPitCriteria()
	{
		Element root = doc.getRootElement().getChild("pit");
		ArrayList<String[]> pitCriteria = new ArrayList<String[]>(0);
		
		for(Element aCriteria : root.getChild("criteria").getChildren("criterion"))
		{
			String[] criterion = 
				{
					aCriteria.getAttributeValue("header"), 
					aCriteria.getValue()
				};
			pitCriteria.add(criterion);
		}
		
		return pitCriteria;
	}
	
	public ArrayList<String[]> getPitCropout()
	{
		Element root = doc.getRootElement().getChild("pit");
		ArrayList<String[]> pitCropout = new ArrayList<String[]>(0);
		
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
			pitCropout.add(crop);
		}
		
		return pitCropout;
	}
	
	public ArrayList<Integer[]> getMatchRegions() throws DataConversionException
	{
		Element root = doc.getRootElement().getChild("match");
		ArrayList<Integer[]> matchRegions = new ArrayList<Integer[]>(0);
		
		for(Element aRegion : root.getChild("regions").getChildren("region"))
		{
			Integer[] region = 
				{
					aRegion.getAttribute("x").getIntValue(), 
					aRegion.getAttribute("y").getIntValue()
				};
			matchRegions.add(region);
		}
		
		return matchRegions;
	}
	
	public ArrayList<String[]> getMatchCriteria()
	{
		Element root = doc.getRootElement().getChild("match");
		ArrayList<String[]> matchCriteria = new ArrayList<String[]>(0);
		
		for(Element aCriteria : root.getChild("criteria").getChildren("criterion"))
		{
			String[] criterion = 
				{
					aCriteria.getAttributeValue("header"), 
					aCriteria.getValue()
				};
			matchCriteria.add(criterion);
		}
		
		return matchCriteria;
	}
	
	public ArrayList<String[]> getMatchCropout()
	{
		Element root = doc.getRootElement().getChild("match");
		ArrayList<String[]> matchCropout = new ArrayList<String[]>(0);
		
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
		
		return matchCropout;
	}
	
	public int[] getPitTeam()
	{
		Element root = doc.getRootElement().getChild("pit");
		int[] pitTeam = new int[4];
		
		pitTeam[0] = Integer.valueOf(root.getChild("team").getAttributeValue("x"));
		pitTeam[1] = Integer.valueOf(root.getChild("team").getAttributeValue("y"));
		pitTeam[2] = Integer.valueOf(root.getChild("team").getAttributeValue("w"));
		pitTeam[3] = Integer.valueOf(root.getChild("team").getAttributeValue("h"));
		
		return pitTeam;
	}
	
	public int[] getMatchTeam()
	{
		Element root = doc.getRootElement().getChild("match");
		int[] matchTeam = new int[4];
		
		matchTeam[0] = Integer.valueOf(root.getChild("team").getAttributeValue("x"));
		matchTeam[1] = Integer.valueOf(root.getChild("team").getAttributeValue("y"));
		matchTeam[2] = Integer.valueOf(root.getChild("team").getAttributeValue("w"));
		matchTeam[3] = Integer.valueOf(root.getChild("team").getAttributeValue("h"));
		
		return matchTeam;
	}
	
	public int[] getMatchMatch()
	{
		Element root = doc.getRootElement().getChild("match");
		int[] matchMatch = new int[4];
		
		matchMatch[0] = Integer.valueOf(root.getChild("match").getAttributeValue("x"));
		matchMatch[1] = Integer.valueOf(root.getChild("match").getAttributeValue("y"));
		matchMatch[2] = Integer.valueOf(root.getChild("match").getAttributeValue("w"));
		matchMatch[3] = Integer.valueOf(root.getChild("match").getAttributeValue("h"));
		
		return matchMatch;
	}
	
	public int getMatchWidth()
	{
		Element root = doc.getRootElement().getChild("match");
		return Integer.valueOf(root.getChild("regionSize").getAttributeValue("w"));
	}
	
	public int getMatchHeight()
	{
		Element root = doc.getRootElement().getChild("match");
		return Integer.valueOf(root.getChild("regionSize").getAttributeValue("h"));
	}
	
	public int getPitWidth()
	{
		Element root = doc.getRootElement().getChild("pit");
		return Integer.valueOf(root.getChild("regionSize").getAttributeValue("w"));
	}
	
	public int getPitHeight()
	{
		Element root = doc.getRootElement().getChild("pit");
		return Integer.valueOf(root.getChild("regionSize").getAttributeValue("h"));
	}
	
	/**
	 * Loads baseline data from the config.xml and stores it into global variables
	 * @throws DataConversionException
	 */
	public ArrayList<String> getFileSettings()
	{
		ArrayList<String> fileSettings = new ArrayList<String>(0);
		Element root = doc.getRootElement().getChild("files");
		fileSettings.add(root.getChild("matchToBeScanned").getValue());
		fileSettings.add(root.getChild("matchScanned").getValue());
		fileSettings.add(root.getChild("matchData").getValue());
		fileSettings.add(root.getChild("pitToBeScanned").getValue());
		fileSettings.add(root.getChild("pitScanned").getValue());
		fileSettings.add(root.getChild("pitData").getValue());
		fileSettings.add(root.getChild("baseline").getValue());
		
		return fileSettings;
	}
	
	/**
	 * Loads baseline data from the config.xml and stores it into global variables
	 */
	public double[] getImageBaseline()
	{
		double[] imageBaseline = new double[7];
		Element root = doc.getRootElement().getChild("sheet").getChild("baseline");
		imageBaseline[0] = Double.valueOf(root.getChild("ul").getAttributeValue("xOff"));
		imageBaseline[1] = Double.valueOf(root.getChild("ul").getAttributeValue("yOff"));
		imageBaseline[2] = Double.valueOf(root.getChild("rotation").getValue());
		imageBaseline[3] = Double.valueOf(root.getChild("crop").getAttributeValue("x"));
		imageBaseline[4] = Double.valueOf(root.getChild("crop").getAttributeValue("y"));
		imageBaseline[5] = Double.valueOf(root.getChild("crop").getAttributeValue("w"));
		imageBaseline[6] = Double.valueOf(root.getChild("crop").getAttributeValue("h"));
		
		return imageBaseline;
	}
	
	/**
	 * Loads scan data from the config.xml and stores it into global variables
	 */
	public ArrayList<String> getScanConfig()
	{
		ArrayList<String> result = new ArrayList<String>(0);
		Element root = doc.getRootElement().getChild("scan");
		result.add(root.getChildText("scanner"));
		result.add(root.getChildText("resolution"));
		result.add(root.getChildText("width"));
		result.add(root.getChildText("height"));
		
		return result;
	}
}