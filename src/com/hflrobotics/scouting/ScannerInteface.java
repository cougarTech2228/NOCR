package com.hflrobotics.scouting;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import eu.gnome.morena.Configuration;
import eu.gnome.morena.Device;
import eu.gnome.morena.Manager;
import eu.gnome.morena.Scanner;

public class ScannerInteface
{

	Manager manager;
	List<Device> devices;
	
	public ScannerInteface()
	{
		//Fixes problem with HP OfficeJet scanners referring to themselves as cameras
		Configuration.addDeviceType(".*fficejet.*", true);
		manager = Manager.getInstance();
		devices = manager.listDevices();
	}

	/**
	 * Scan images from scanner and place them in a directory
	 * @param dir directory to be scanned to
	 * @param scanID instance number of the scanner
	 */
	public void scanToDir(String dir, int scanID, ArrayList<String> config)
	{
		ScanSession session = new ScanSession();
		Scanner scanner = (Scanner) devices.get(scanID);
		scanner.setMode(Scanner.BLACK_AND_WHITE);
		scanner.setResolution(Integer.valueOf(config.get(1)));
		scanner.setFrame(0, 0, Integer.valueOf(config.get(2)), Integer.valueOf(config.get(3)));
		scanner.setDuplexEnabled(false);
		int feederUnit = scanner.getFeederFunctionalUnit();
		
	    if(feederUnit < 0)
	    {
		      feederUnit = 0;
	    }
		
	    try
	    {
	      session.startSession(scanner, feederUnit);
	      File file = null;
	      
	      while(null != (file=session.getImageFile()))
	      {
	    	  	BufferedImage bimage = ImageIO.read(file);
	    	  	File outputfile = new File(dir + Math.random() + ".png");
			    ImageIO.write(bimage, "png", outputfile);
	      }
	    } 
	    catch (Exception ex)
	    { 
	      if (session.isEmptyFeeder())
	      {
		        System.out.println("No more sheets in the document feeder");
	      }
	      else
	      {
		        ex.printStackTrace();
	      }
	    }
	}
	
	/**
	 * Gets a list of all the scanners that were detected
	 * @return all the scanners that were detected
	 */
	public ArrayList<String> getScanners()
	{
		ArrayList<String> scanners = new ArrayList<String>();

		for(int i = 0; i < devices.size(); i++)
		{
			if(devices.get(i) instanceof Scanner)
			{
				scanners.add(devices.get(i).toString());
			}
		}

		return scanners;
	}
}
