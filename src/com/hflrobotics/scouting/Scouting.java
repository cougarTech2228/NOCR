package com.hflrobotics.scouting;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.opencsv.CSVWriter;

public class Scouting
{

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
		Scouting scouting = new Scouting();
		Configuration config = new Configuration("C:/Users/cougartech/Documents/Scouting_Sheet/config.xml");
		config.test();
	}

	
	/**
	 * Takes an image and creates cropped photos based on the specified cropSection 2D array
	 * @param file the File to crop from
	 * @param outputDir the directory of where to place the image
	 */
	private void extractCropSection(File file, String outputDir)
	{
		BufferedImage img = null;
		try
		{
			img = ImageIO.read(file);
		}
		catch(IOException ex)
		{

		}
		
		for(String[] aCropSection : cropSection)
		{
			File output = new File(outputDir + "/" + aCropSection[0] + ".jpg");
			
			try
			{
				output.createNewFile();
			}
			catch(IOException e1)
			{
				e1.printStackTrace();
			}
			
			//Write a sub image of the original to the directory
			try
			{
				ImageIO.write(img.getSubimage(Integer.valueOf(aCropSection[1]), Integer.valueOf(aCropSection[2]), Integer.valueOf(aCropSection[3]), Integer.valueOf(aCropSection[4])), "jpg", output);
			}
			catch(NumberFormatException | IOException ex)
			{
				ex.printStackTrace();
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
	 * Test method for using data set expressions
	 */
	@SuppressWarnings("unused")
	private void extract()
	{
		String toBeScannedDir = "C:/Users/Michael/Documents/Scouting/matchToBeScanned";
		ArrayList<String> availableSheets = getSheetFiles(toBeScannedDir);
		File toBeScanned = new File(toBeScannedDir + "/" + availableSheets.get(0));

		//Test set (header, expression)
		String[][] dataSets =
		{
				{ "low goal", "SUM:1,2" },
				{ "reach", "VALUE:0" } };

		int[] result = new int[2];
		byte[][] pixelMap = getPixelMap(toBeScanned);
		int[] sheetValues = getSheetValues(sampleRegion, pixelMap);

		for(int i = 0; i < dataSets.length; i++)
		{
			String[] dataSetExpression = dataSets[i][1].split(":", 2);

			if(dataSetExpression[0].equalsIgnoreCase("SUM"))
			{
				String[] summationComponents = dataSetExpression[1].split(",");

				for(int ii = 0; ii < summationComponents.length; ii++)
				{
					result[i] += Integer.valueOf(sheetValues[Integer.valueOf(summationComponents[ii])]);
				}
			}
			else
			{
				result[i] = sheetValues[Integer.valueOf(dataSetExpression[1])];
			}
		}

		//Test print out
		System.out.println(result[0] + "  " + result[1]);
	}

	@SuppressWarnings("unused")
	@Deprecated
	private void scanAvailableSheets(String toBeScannedDir, String scannedDir) throws IOException
	{
		ArrayList<String> availableSheets = getSheetFiles(toBeScannedDir);
		int[][] sampleRegion =
		{
				{ 650, 475, 10, 10 },
				{ 650, 530, 10, 10 },
				{ 650, 600, 10, 10 } };

		for(int i = 0; i < availableSheets.size(); i++)
		{
			File toBeScanned = new File(toBeScannedDir + "/" + availableSheets.get(i));
			CSVWriter csvWriter = new CSVWriter(
					new FileWriter("C:/Users/cougartech/Documents/Scouting_Sheet/data.csv", true));
			byte[][] pixelMap = getPixelMap(toBeScanned);
			int[] sheetValues = getSheetValues(sampleRegion, pixelMap);

			// Convert team and match number from seven segment to string
			String team = Integer
					.toString(decodeSevenSegment(sheetValues[0], sheetValues[1], sheetValues[2], sheetValues[3],
							sheetValues[4], sheetValues[5], sheetValues[6]))
					+ Integer.toString(decodeSevenSegment(sheetValues[7], sheetValues[8], sheetValues[9],
							sheetValues[10], sheetValues[11], sheetValues[12], sheetValues[13]))
					+ Integer.toString(decodeSevenSegment(sheetValues[14], sheetValues[15], sheetValues[16],
							sheetValues[17], sheetValues[18], sheetValues[19], sheetValues[20]))
					+ Integer.toString(decodeSevenSegment(sheetValues[21], sheetValues[22], sheetValues[23],
							sheetValues[24], sheetValues[25], sheetValues[26], sheetValues[27]));
			String match = Integer
					.toString(decodeSevenSegment(sheetValues[28], sheetValues[29], sheetValues[30], sheetValues[31],
							sheetValues[32], sheetValues[33], sheetValues[34]))
					+ Integer.toString(decodeSevenSegment(sheetValues[35], sheetValues[36], sheetValues[37],
							sheetValues[38], sheetValues[39], sheetValues[40], sheetValues[41]));

			String[] values =
			{ team, match, "01", "02" };

			// Appends value to the CSV and closes the FileWriter
			csvWriter.writeNext(values, false);
			csvWriter.close();

			// Renames image to scan ID and move image to scanned directory
			File newScanned = new File(toBeScannedDir + "/" + team + "_" + match + ".jpg");
			File scanned = new File(scannedDir + "/" + team + "_" + match + ".jpg");
			toBeScanned.renameTo(newScanned);
			Files.move(newScanned.toPath(), scanned.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}

	}

	@SuppressWarnings("unused")
	@Deprecated
	private String getMatchNumber(byte[][] pixelMap)
	{
		int x = 1880;
		int y = 170;
		int[] values = new int[14];

		for(int i = 0; i < 2; i++)
		{
			values[i] = getChecked(x, y, 40, 15, pixelMap);
			x += 130;
		}

		x = 1850;
		y += 85;

		for(int i = 2; i < 4; i++)
		{
			values[i] = getChecked(x, y, 40, 15, pixelMap);
		}

		x = 1850;
		y += 85;

		for(int i = 4; i < 6; i++)
		{
			values[i] = getChecked(x, y, 40, 15, pixelMap);
		}

		String result = "";
		for(int i = 0; i < 14; i++)
		{
			result = result + "/" + values[i];
		}

		return result;
	}

	@SuppressWarnings("unused")
	@Deprecated
	private String getTeamNumber(byte[][] pixelMap)
	{
		int x = 1240;
		int y = 170;
		int[] values = new int[28];

		// Horizontal segments
		for(int i = 0; i < 4; i++)
		{
			values[i] = getChecked(x, y, 40, 15, pixelMap);
			x += 130;
		}

		x = 1240;
		y += 85;

		for(int i = 4; i < 8; i++)
		{
			values[i] = getChecked(x, y, 40, 15, pixelMap);
			x += 130;
		}

		x = 1240;
		y += 85;

		for(int i = 8; i < 12; i++)
		{
			values[i] = getChecked(x, y, 40, 15, pixelMap);
			x += 130;
		}

		x = 1210;
		y = 200;

		// Vertical segments
		for(int i = 12; i < 20; i++)
		{
			values[i] = getChecked(x, y, 15, 40, pixelMap);
			x += 130;
		}

		x = 1210;
		y += 80;

		for(int i = 12; i < 20; i++)
		{
			values[i] = getChecked(x, y, 15, 40, pixelMap);
			x += 130;
		}

		return Integer
				.toString(decodeSevenSegment(values[0], values[13], values[21], values[8], values[20], values[12],
						values[4]))
				+ Integer.toString(decodeSevenSegment(values[1], values[15], values[23], values[9], values[22],
						values[14], values[5]))
				+ Integer.toString(decodeSevenSegment(values[2], values[17], values[25], values[10], values[24],
						values[16], values[6]))
				+ Integer.toString(decodeSevenSegment(values[3], values[19], values[27], values[11], values[25],
						values[18], values[7]));
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
	private int[] getSheetValues(int[][] sampleRegion, byte[][] pixelMap)
	{
		int[] result = new int[sampleRegion.length];
		int i = 0;

		for(int[] aSampleRegion : sampleRegion)
		{
			result[i] = getChecked(aSampleRegion[0], aSampleRegion[1], aSampleRegion[2], aSampleRegion[3], pixelMap);
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
	 * @param filePath specific directory of image
	 * @return 2D byte array of pixel data (0- white 1- black)
	 */
	private byte[][] getPixelMap(File file)
	{
		BufferedImage img = null;
		try
		{
			img = ImageIO.read(file);
		}
		catch(IOException ex)
		{
			System.out.print("heNeedSomeMilk");
		}

		byte[][] pixels = new byte[img.getWidth()][];
		FastRGB imgRGB = new FastRGB(img);

		for(int x = 0; x < img.getWidth(); x++)
		{
			pixels[x] = new byte[img.getHeight()];

			for(int y = 0; y < img.getHeight(); y++)
			{
				//If the pixel is not white (0xFFFFFFFF -- 0) then it is black (1)
				pixels[x][y] = (byte) (imgRGB.getRGB(x, y) == 0xFFFFFFFF ? 0 : 1);
			}
		}

		return pixels;
	}

	/**
	 * getSheetFiles
	 * 
	 * @param filePath specified directory to look in
	 * @return all file names in the directory that end with .jpg
	 */
	public ArrayList<String> getSheetFiles(String filePath)
	{
		File file = new File(filePath);
		String[] fileList = file.list();
		ArrayList<String> sheetFiles = new ArrayList<String>(0);

		for(String aFileList : fileList)
		{
			if(aFileList.substring(aFileList.length() - 4).equalsIgnoreCase(".jpg"))
			{
				sheetFiles.add(aFileList);
			}
		}

		return sheetFiles;
	}

}
