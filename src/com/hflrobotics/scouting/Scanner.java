package com.hflrobotics.scouting;

import java.util.ArrayList;
import java.util.List;

import eu.gnome.morena.Configuration;
import eu.gnome.morena.Device;
import eu.gnome.morena.Manager;

public class Scanner
{

	Manager manager;
	List<Device> devices;

	public Scanner()
	{
		Configuration.addDeviceType(".*fficejet.*", true);
		manager = Manager.getInstance();
		devices = manager.listDevices();
	}

	public void testFunc()
	{
		for(int i = 0; i < devices.size(); i++)
		{
			if(devices.get(i) instanceof Scanner)
			{
				System.out.println("Scanner: " + devices.get(i));
			}
			else
			{
				System.out.println("Camera: " + devices.get(i));
			}
			// System.out.println(devices.get(i));
		}
	}

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
