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
		//Fix problem with HP OfficeJet scanners referring to themselves as cameras
		Configuration.addDeviceType(".*fficejet.*", true);
		manager = Manager.getInstance();
		devices = manager.listDevices();
	}

	/**
	 * Do not use in production
	 */
	public void testFunc()
	{
		Scanner scanner = (Scanner) devices.get(0);
		scanner.setMode(Scanner.BLACK_AND_WHITE);
		scanner.setResolution(300);
		scanner.setFrame(0, 0, 2550, 3300);
		scanner.setDuplexEnabled(false);
		/*try
		{
			BufferedImage bimage = SynchronousHelper.scanImage(scanner);
			File outputfile = new File("C:/Users/cougartech/Documents/Scouting/test/" + Math.random() + ".png");
		    ImageIO.write(bimage, "png", outputfile);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}*/
		 int feederUnit=scanner.getFeederFunctionalUnit();
		    if (feederUnit<0)
		    {
			      feederUnit=0; // 0 designates a default unit

		    }
		    
		    ScanSession session=new ScanSession();
		    try {
		      session.startSession(scanner, feederUnit);
		      File file=null; 
		      while (null!=(file=session.getImageFile())) {
		        
		    	  BufferedImage bimage = SynchronousHelper.scanImage(scanner);
					File outputfile = new File("C:/Users/cougartech/Documents/Scouting/test/" + Math.random() + ".png");
				    ImageIO.write(bimage, "png", outputfile);
		        
		      }
		    } catch (Exception ex) { // check if error is related to empty ADF
		      if (session.isEmptyFeeder())
		        System.out.println("No more sheets in the document feeder");
		      else
		        ex.printStackTrace();
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
