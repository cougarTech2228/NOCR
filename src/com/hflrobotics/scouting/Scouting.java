package com.hflrobotics.scouting;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.opencsv.CSVWriter;

public class Scouting
{
	ScannerInteface scanner = new ScannerInteface();
	Configuration config = new Configuration("C:/Users/cougartech/Documents/Scouting/config.xml");
	ImageHandler handler = new ImageHandler();
	
	// x, y, w, h
	int[][] sampleRegion =
	{
			{ 1, 1, 5, 5 },
			{ 1, 10, 5, 5 },
			{ 1, 19, 5, 5 },
			{ 10, 1, 5, 5 },
			{ 10, 10, 5, 5 },
			{ 10, 19, 5, 5 },
			{ 19, 1, 5, 5 },
			{ 19, 10, 5, 5 },
			{ 19, 19, 5, 5 } };

	// itemLocation, type
	String[][] directoryConfig =
	{
			{ "/pitToBeScanned", "dir" },
			{ "/pitScanned", "dir" },
			{ "/matchToBeScanned", "dir" },
			{ "/matchScanned", "dir" },
			{ "/pitData.csv", "csv" },
			{ "/matchData.csv", "csv" } };

	// fileName, x, y, w, h
	String[][] cropSection =
	{ 
			{"comment", "10", "10", "5", "5"}, 
			{"comment2", "19", "19", "5", "5"} };

	public static void main(String[] args)
	{
		GUI gui = new GUI();
	}

	private void loadFromConfig()
	{
		config.load();
	}
	
	public void scanPit()
	{
		loadFromConfig();
		config.loadScanConfig();
		scanner.scanToDir(config.fileSettings.get(3), 0, config.scanSettings);
	}
	
	public void scanMatch()
	{
		loadFromConfig();
		config.loadScanConfig();
		scanner.scanToDir(config.fileSettings.get(0), 0, config.scanSettings);
	}
	
	public void extractPit() throws IOException, NotFoundException
	{
		loadFromConfig();
		ArrayList<String> availableSheets = getSheetFiles(config.fileSettings.get(3));
		
		for(String sheet : availableSheets)
		{
			CSVWriter csvWriter = new CSVWriter(new FileWriter(config.fileSettings.get(5), true));
			File toBeScanned = new File(config.fileSettings.get(3) + sheet);
			BufferedImage img = ImageIO.read(toBeScanned);
			img = handler.manipulateImage(img, config.imageBaseline);
			
			byte[][] pixelMap = getPixelMap(img);
			int[] sheetValues = getSheetValues(config.pitRegions, config.pitHeight, config.pitWidth, pixelMap);
			String team = getTeam(pixelMap, config.pitTeam);
			
			csvWriter.writeNext(getDataset(config.pitCriteria, sheetValues, team, null), false);
			csvWriter.close();
			
			extractCropSection(img, pixelMap, config.pitCropout, config.fileSettings.get(4));
			
			// Renames image to scan ID and move image to scanned directory
			File scanned = new File(config.fileSettings.get(4) + team + ".png");
			Files.move(toBeScanned.toPath(), scanned.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
	}
	
	public void extractMatch() throws IOException, NotFoundException
	{
		loadFromConfig();
		ArrayList<String> availableSheets = getSheetFiles(config.fileSettings.get(0));
				
		for(String sheet : availableSheets)
		{
			CSVWriter csvWriter = new CSVWriter(new FileWriter(config.fileSettings.get(2), true));
			File toBeScanned = new File(config.fileSettings.get(0) + sheet);
			BufferedImage img = ImageIO.read(toBeScanned);
			
			img = handler.manipulateImage(img, config.imageBaseline);
			
			byte[][] pixelMap = getPixelMap(img);
			int[] sheetValues = getSheetValues(config.matchRegions, config.matchHeight, config.matchWidth, pixelMap);
			String team = getTeam(pixelMap, config.matchTeam);
			String match = getMatch(pixelMap, config.matchMatch);			
			
			csvWriter.writeNext(getDataset(config.matchCriteria, sheetValues, team, match), false);
			csvWriter.close();
			
			extractCropSection(img, pixelMap, config.matchCropout, config.fileSettings.get(1));
			
			// Renames image to scan ID and move image to scanned directory
			File scanned = new File(config.fileSettings.get(1) + team + "_" + match + ".png");
			Files.move(toBeScanned.toPath(), scanned.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}	
	}
	
	private String getTeam(byte[][] pixelMap, int[] config)
	{
		String result = "";

		int w = config[2];
		int h = config[3];
		int x = config[0];
		int y = config[1];
		int a, b, c, d, e, f, g;
		int digit[] = new int[4];
		
		for(int i = 0; i < 2; i++)
		{
			a = getChecked(x + h, y, w, h, pixelMap);
			g = getChecked(x + h, y + h + w, w, h, pixelMap);
			d = getChecked(x + h, y + w + w + h + h, w, h, pixelMap);
			
			b = getChecked(x + h + w, y + h, h, w, pixelMap);
			c = getChecked(x + h + w, y + w + w + h, h, w, pixelMap);
			
			e = getChecked(x, y + w + w + h, h, w, pixelMap);
			f = getChecked(x, y + h, h, w, pixelMap);
						
			digit[i] = decodeSevenSegment(a, b, c, d, e, f, g);
			x = x + (h * 3) + w; 
		}
		
		result = Integer.toString(digit[0]) + Integer.toString(digit[1]);
		
		return result;
	}
	
	private String getMatch(byte[][] pixelMap, int[] config)
	{
		String result = "";

		int w = config[2];
		int h = config[3];
		int x = config[0];
		int y = config[1];
		int a, b, c, d, e, f, g;
		int digit[] = new int[2];
		
		for(int i = 0; i < 2; i++)
		{
			a = getChecked(x + h, y, w, h, pixelMap);
			g = getChecked(x + h, y + h + w, w, h, pixelMap);
			d = getChecked(x + h, y + w + w + h + h, w, h, pixelMap);
			
			b = getChecked(x + h + w, y + h, h, w, pixelMap);
			c = getChecked(x + h + w, y + w + w + h, h, w, pixelMap);
			
			e = getChecked(x, y + w + w + h, h, w, pixelMap);
			f = getChecked(x, y + h, h, w, pixelMap);
						
			digit[i] = decodeSevenSegment(a, b, c, d, e, f, g);
			x = x + (h * 3) + w; 
		}
		
		result = Integer.toString(digit[0]) + Integer.toString(digit[1]);
		
		return result;
	}
	
	/**
	 * Retrieves a dataset for the CSV file based on the criteria and sheet values
	 * @param criteria
	 * @param sheetValues
	 * @param team
	 * @param match
	 * @return
	 */
	private String[] getDataset(ArrayList<String[]> criteria, int[] sheetValues, String team, String match)
	{
		String[] result = new String[criteria.size()];
		int i = 0;
		
		for(String[] aCriteria : criteria)
		{
			String[] exp = aCriteria[1].split(":");
			
			switch(exp[0])
			{
				case "BLANK":
					result[i] = "";
					break;
				
				case "TEAM":
					result[i] = team;
					break;
					
				case "MATCH":
					result[i] = match;
					break;
					
				case "SUM":
					String[] summationComponents = exp[1].split(",");

					for(int ii = 0; ii < summationComponents.length; ii++)
					{
						result[i] += Integer.valueOf(sheetValues[Integer.valueOf(summationComponents[ii])]);
					}					
					break;
					
				case "VALUE":
					if(sheetValues[Integer.valueOf(exp[1])] == 1)
					{
						result[i] = "yes";
					}
					else
					{
						result[i] = "no";
					}					
					break;
					
				case "SELECT":
					String[] option = exp[1].split(",");
					
					for(int ii = 0; ii < option.length; ii++)
					{
						if(sheetValues[Integer.valueOf(option[ii].split("-")[0])] == 1)
						{
							result[i] = option[ii].split("-")[1];
							break;
						}
						else
						{
							result[i] = "none";
						}
					}			
					break;
					
				default:
					result[i] = "";
					break;
			}
			
			i++;
		}
		
		return result;
	}
	
	/**
	 * Takes an image and creates cropped photos based on the specified cropSection 2D array
	 * @param img the BufferedImage to crop from
	 * @param outputDir the directory of where to place the image
	 * @throws IOException 
	 */
	private void extractCropSection(BufferedImage img, byte[][] map, ArrayList<String[]> cropSection, String outputDir) throws IOException
	{
		for(String[] aCropSection : cropSection)
		{
			switch(aCropSection[6])
			{
				case "HAS_VALUE":
					if(getChecked(Integer.valueOf(aCropSection[1]),	Integer.valueOf(aCropSection[2]), 
							Integer.valueOf(aCropSection[3]), Integer.valueOf(aCropSection[4]),	map) == 1)
					{
						File output = new File(outputDir + aCropSection[0]);
						output.createNewFile();

						img = img.getSubimage(Integer.valueOf(aCropSection[1]),
								Integer.valueOf(aCropSection[2]), 
								Integer.valueOf(aCropSection[3]),
								Integer.valueOf(aCropSection[4]));
						
						//Write a sub image of the original to the directory
						ImageIO.write(img, aCropSection[5], output);
					}
					break;
					
				default:
					File output = new File(outputDir + aCropSection[0]);
					output.createNewFile();

					img = img.getSubimage(Integer.valueOf(aCropSection[1]),
							Integer.valueOf(aCropSection[2]), 
							Integer.valueOf(aCropSection[3]),
							Integer.valueOf(aCropSection[4]));
					
					//Write a sub image of the original to the directory
					ImageIO.write(img, aCropSection[5], output);
					break;
			}			
		}
	}	
	
	/**
	 * Get number of occurrences of a string with in another string
	 * 
	 * @param str String to search
	 * @param findStr String to look for
	 * @return Count of occurrences
	 */
	private int stringContains(String str, String findStr)
	{
		int lastIndex = 0;
		int count = 0;

		//For every occurrence of findStr in str, increase the count
		while(lastIndex != -1)
		{
			lastIndex = str.indexOf(findStr, lastIndex);

			if(lastIndex != -1)
			{
				count++;
				lastIndex += findStr.length();
			}
		}

		return count;
	}

	@SuppressWarnings("unused")
	/**
	 * Whether the baseDir is setup
	 * 
	 * @param baseDir
	 * @return true-baseDir is setup false-baseDir is not setup
	 */
	private boolean directorySetup(String baseDir)
	{
		boolean result = true;

		for(String[] aDirectoryConfig : directoryConfig)
		{
			File file = new File(baseDir + aDirectoryConfig[0]);

			if(!file.exists())
			{
				result = false;
				break;
			}
		}

		return result;
	}

	@SuppressWarnings("unused")
	/**
	 * Creates folders and files that NOCR relies on if not existing
	 * 
	 * @param baseDir
	 */
	private void setupDirectory(String baseDir)
	{
		for(String[] aDirectoryConfig : directoryConfig)
		{
			File file = new File(baseDir + aDirectoryConfig[0]);

			if(!file.exists())
			{
				switch(aDirectoryConfig[1].toLowerCase())
				{
					//Directories will just be created
					case "dir":
						file.mkdir();
						break;

					//CSVs will be created
					case "csv":
						try
						{
							file.createNewFile();
						}
						catch(IOException e)
						{
							e.printStackTrace();
						}
						break;

					default:
						break;
				}
			}
		}
	}

	/**
	 * checks whether an expression is valid according to existing commands and
	 * guidelines
	 * 
	 * @param expression string to be evaluated
	 * @return true-valid expression false-invalid expression
	 */
	@SuppressWarnings("unused")
	private boolean verifyExpression(String expression)
	{
		boolean result = false;

		if(stringContains(expression, ":") == 1)
		{
			String[] splitExpression = expression.split(":", 2);

			switch(splitExpression[0].toLowerCase())
			{
				case "sum":
					result = verifyRegionSet(splitExpression[1]);
					break;

				case "value":
					result = verifyRegion(splitExpression[1]);
					break;

				case "select":
					break;

				case "team":
					break;

				case "match":
					break;

				case "blank":
					result = (splitExpression[1] != null); //Ensures there is no region set specified
					break;

				default:
					result = false;
					break;
			}
		}

		return result;
	}

	/**
	 * Ensures regionSet is formatted properly and does not specify any out of bounds regions
	 * @param regionSet specific regionSet
	 * @return whether regionSet is valid
	 */
	private boolean verifyRegionSet(String regionSet)
	{
		boolean result = true;

		if(stringContains(regionSet, ",") > 0)
		{
			String[] splitRegionSet = regionSet.split(",");

			//Makes sure specified region exists
			for(String aSplitRegionSet : splitRegionSet)
			{
				if(Integer.valueOf(aSplitRegionSet) >= sampleRegion.length)
				{
					result = false;
					break;
				}				
			}
		}

		return result;
	}

	/**
	 * Ensures region is formatted properly and does not specify any out of bounds regions
	 * @param region specific region
	 * @return whether region is valid
	 */
	private boolean verifyRegion(String region)
	{
		boolean result = false;

		//Makes sure specified region exists
		try
		{
			result = (Integer.valueOf(region) < sampleRegion.length);
		}
		catch(NumberFormatException ex)
		{
			result = false;
		}

		return result;
	}

	/**
	 * Converts the state of a seven segment to an integer representation
	 * @param a state of the a segment (0- false 1- true)
	 * @param b state of the b segment (0- false 1- true)
	 * @param c state of the c segment (0- false 1- true)
	 * @param d state of the d segment (0- false 1- true)
	 * @param e state of the e segment (0- false 1- true)
	 * @param f state of the f segment (0- false 1- true)
	 * @param g state of the g segment (0- false 1- true)
	 * @return integer value of the display
	 */
	private int decodeSevenSegment(int a, int b, int c, int d, int e, int f, int g)
	{
		int result = 0;
		//Creates a string with the segments concatenated
		String bits = Integer.toString(a) + Integer.toString(b) + Integer.toString(c) + Integer.toString(d)
				+ Integer.toString(e) + Integer.toString(f) + Integer.toString(g);

		switch(bits)
		{
			case "0110000":
				result = 1;
				break;

			case "1101101":
				result = 2;
				break;

			case "1111001":
				result = 3;
				break;

			case "0110011":
				result = 4;
				break;

			case "1011011":
				result = 5;
				break;

			case "1011111":
				result = 6;
				break;

			case "1110000":
				result = 7;
				break;

			case "1111111":
				result = 8;
				break;

			case "1111011":
				result = 9;
				break;

			default:
				result = 0;
				break;
		}

		return result;
	}

	/**
	 * 
	 * @param sampleRegion 2D array that specifies arrays
	 * @param pixelMap the byte array that represents the image
	 * @return
	 */
	private int[] getSheetValues(ArrayList<Integer[]> sampleRegion, int w, int h, byte[][] pixelMap)
	{
		int[] result = new int[sampleRegion.size()];
		int i = 0;

		for(Integer[] aSampleRegion : sampleRegion)
		{
			result[i] = getChecked(aSampleRegion[0], aSampleRegion[1], w, h, pixelMap);
			i++;
		}

		return result;
	}

	/**
	 * Checks the average amount of black pixels in a region to determine whether it is checked
	 * @param x the x position of the upper left corner
	 * @param y the y position of the upper left corner
	 * @param w the width of the region
	 * @param h the height of the region
	 * @param pixelMap the byte array that represents the image
	 * @return 0- unchecked 1- checked
	 */
	private int getChecked(int x, int y, int w, int h, byte[][] pixelMap)
	{
		long area = w * h;
		long sigma = 0;

		//Sums up the amount of black pixels
		for(int xSample = x; xSample < x + w; xSample++)
		{
			for(int ySample = y; ySample < y + h; ySample++)
			{
				sigma += pixelMap[xSample][ySample];
			}
		}

		if((double) sigma / area > 0.75)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}

	/**
	 * getPixelMap
	 * 
	 * @param img BufferedImage that has been manipulated
	 * @return 2D byte array of pixel data (0- white 1- black)
	 */
	private byte[][] getPixelMap(BufferedImage img)
	{
		byte[][] pixels = new byte[img.getWidth()][];
		/*FastRGB imgRGB = new FastRGB(img);

		for(int x = 0; x < img.getWidth(); x++)
		{
			pixels[x] = new byte[img.getHeight()];

			for(int y = 0; y < img.getHeight(); y++)
			{
				//If the pixel is not white (0xFFFFFFFF -- 0) then it is black (1)
				pixels[x][y] = (byte) (imgRGB.getRGB(x, y) == 0xFFFFFFFF ? 0 : 1);
			}
		}*/
		
		for(int x = 0; x < img.getWidth(); x++)
		{
			pixels[x] = new byte[img.getHeight()];

			for(int y = 0; y < img.getHeight(); y++)
			{
				//If the pixel is not white (0xFFFFFFFF -- 0) then it is black (1)
				pixels[x][y] = (byte) (img.getRGB(x, y) == 0xFFFFFFFF ? 0 : 1);
			}
		}
		
		return pixels;
	}

	/**
	 * getSheetFiles
	 * 
	 * @param filePath specified directory to look in
	 * @return all file names in the directory that end with .png
	 */
	private ArrayList<String> getSheetFiles(String filePath)
	{
		File file = new File(filePath);
		String[] fileList = file.list();
		ArrayList<String> sheetFiles = new ArrayList<String>(0);

		for(String aFileList : fileList)
		{
			if(aFileList.substring(aFileList.length() - 4).equalsIgnoreCase(".png"))
			{
				sheetFiles.add(aFileList);
			}
		}

		return sheetFiles;
	}

}
