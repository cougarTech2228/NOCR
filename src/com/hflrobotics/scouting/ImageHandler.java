package com.hflrobotics.scouting;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javaxt.io.Image;

import com.google.zxing.multi.ByQuadrantReader;
import com.google.zxing.multi.GenericMultipleBarcodeReader;
import com.google.zxing.multi.MultipleBarcodeReader;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;

public class ImageHandler
{

	private static final Map<DecodeHintType,Object> HINTS;
	private static final Map<DecodeHintType,Object> HINTS_PURE;
	
	static
	{
	    HINTS = new EnumMap<>(DecodeHintType.class);
	    HINTS.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
	    HINTS.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.allOf(BarcodeFormat.class));
	    HINTS_PURE = new EnumMap<>(HINTS);
	    HINTS_PURE.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
	 }
	
	public ImageHandler()
	{
		
	}
	
	public void test3() throws IOException
	{
		File file = new File("C:/Users/cougartech/Documents/Scouting/test/1.png");
		File file2 = new File("C:/Users/cougartech/Documents/Scouting/test/1_.png");
		Image img = new Image(file);
		int width = img.getWidth();
		int height = img.getHeight();
		img.setCorners(// keep the upper left corner as it is
	            0,0, // UL

	            // push the upper right corner more to the bottom
	            width,5, // UR

	            // push the lower right corner more to the left
	            width,height, // LR

	            // push the lower left corner more to the right
	            0,height); // LL
		ImageIO.write(img.getBufferedImage(), "png", file2);
	}
	
	public void test2() throws IOException
	{
		File file = new File("C:/Users/cougartech/Documents/Scouting/test/1.png");
		File file2 = new File("C:/Users/cougartech/Documents/Scouting/test/1_.png");
		BufferedImage src = ImageIO.read(file);
		double deg = (Math.PI / 2) - test1();
		int w = src.getWidth();
		int h = src.getHeight();
		BufferedImage dst = new BufferedImage(w, h, src.getType());
		Graphics2D g2 = dst.createGraphics();
		g2.rotate(deg);
		g2.drawImage(src, 0, 0, null);
		g2.dispose();
		ImageIO.write(dst, "png", file2);
		System.out.println("Image Written w/ " + deg);
	}
	
	public void rotateImages() throws IOException
	{
		File file = new File("C:/Users/cougartech/Documents/Scouting/matchToBeScanned");
		String[] fileList = file.list();
		ArrayList<String> sheetFiles = new ArrayList<String>(0);

		for(String aFileList : fileList)
		{
			if(aFileList.substring(aFileList.length() - 4).equalsIgnoreCase(".png"))
			{
				double deg = 0;
				File file2 = new File("C:/Users/cougartech/Documents/Scouting/matchToBeScanned/" + aFileList);
				BufferedImage src = ImageIO.read(file2);
				LuminanceSource source = new BufferedImageLuminanceSource(src);
			    BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
			    Collection<Result> results = new ArrayList<>(1);

			    try
			    {
			      Reader reader = new MultiFormatReader();

			      try
			      {
			        // Look for multiple barcodes
			    	QRCodeMultiReader multiReader = new QRCodeMultiReader();
			        Result[] theResults = multiReader.decodeMultiple(bitmap, HINTS);
			        
			        if(theResults != null) 
			        {
			          results.addAll(Arrays.asList(theResults));
			        }
			      } 
			      catch(ReaderException re) 
			      {
			      }
			    }
			    catch(Exception e)
			    {
			    	e.printStackTrace();
			    }
			    
			    for(Object result : results.toArray())
			    {
			    	Result theResult = (Result) result;
			    	
			    	if(theResult.getText().equals("upperLeft"))
			    	{
			    		double x1 = theResult.getResultPoints()[0].getX();
			    		double x2 = theResult.getResultPoints()[1].getX();
			    		double y1 = theResult.getResultPoints()[0].getY();
			    		double y2 = theResult.getResultPoints()[1].getY();
			    		deg = (Math.PI / 2) - Math.atan((y2 - y1) / (x2 - x1));
			    	}
			    }

				int w = src.getWidth();
				int h = src.getHeight();
				BufferedImage dst = new BufferedImage(w, h, src.getType());
				Graphics2D g2 = dst.createGraphics();
				g2.rotate(deg);
				g2.drawImage(src, 0, 0, null);
				g2.dispose();
				ImageIO.write(dst, "png", file2);
				System.out.println("Image Written w/ " + deg);
			}
		}
	}
	
	public void cropImages() throws IOException
	{
		File file = new File("C:/Users/cougartech/Documents/Scouting/matchToBeScanned");
		String[] fileList = file.list();
		ArrayList<String> sheetFiles = new ArrayList<String>(0);

		for(String aFileList : fileList)
		{
			if(aFileList.substring(aFileList.length() - 4).equalsIgnoreCase(".png"))
			{
				double deg = 0;
				File file2 = new File("C:/Users/cougartech/Documents/Scouting/matchToBeScanned/" + aFileList);
				BufferedImage src = ImageIO.read(file2);
				BufferedImage dst = src.getSubimage(149, 149, 2251, 2999);
				ImageIO.write(dst, "png", file2);
				System.out.println("Image Written w/ " + deg);
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void qrCodes() throws NotFoundException, ChecksumException, FormatException, IOException
	{
		Map hintMap = new HashMap();
		File file = new File("C:/Users/cougartech/Documents/Scouting/test/24.png");
		//File file = new File("C:/Users/cougartech/Documents/Scouting/test/0.jpg");
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		QRCodeMultiReader reader = new QRCodeMultiReader();
		BinaryBitmap bitmap= new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(file))));
		
		for(Result aResults : reader.decodeMultiple(bitmap, hintMap))
		{
			System.out.println(aResults.getText());
		}
		System.out.println("done");
	}
	
	public void sharpenImage() throws IOException
	{
		File file = new File("C:/Users/cougartech/Documents/Scouting/test/1.png");
		File file2 = new File("C:/Users/cougartech/Documents/Scouting/test/1_.png");
		Image img = new Image(file);
		img.trim(255, 255, 255);
		ImageIO.write(img.getBufferedImage(), "png", file2);
		System.out.println("Image Blim Bammed!");
	}
	
	public double test1()
	{
		File file = new File("C:/Users/cougartech/Documents/Scouting/test/24.png");
		BufferedImage image = null;
		double deg = 0;
		
		try
		{
			image = ImageIO.read(file);
		} catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		LuminanceSource source = new BufferedImageLuminanceSource(image);
	    BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
	    Collection<Result> results = new ArrayList<>(1);

	    try
	    {
	      Reader reader = new MultiFormatReader();

	      try
	      {
	        // Look for multiple barcodes
	    	QRCodeMultiReader multiReader = new QRCodeMultiReader();
	        Result[] theResults = multiReader.decodeMultiple(bitmap, HINTS);
	        
	        if(theResults != null) 
	        {
	          results.addAll(Arrays.asList(theResults));
	        }
	      } 
	      catch(ReaderException re) 
	      {
	      }
	    }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	    
	    for(Object result : results.toArray())
	    {
	    	Result theResult = (Result) result;
	    	
	    	if(theResult.getText().equals("upperLeft"))
	    	{
	    		double x1 = theResult.getResultPoints()[0].getX();
	    		double x2 = theResult.getResultPoints()[1].getX();
	    		double y1 = theResult.getResultPoints()[0].getY();
	    		double y2 = theResult.getResultPoints()[1].getY();
	    		deg = Math.atan((y2 - y1) / (x2 - x1));
	    		System.out.println("Adjust by (deg): " + Math.toDegrees(deg));
	    		System.out.println("sss " + (90 - Math.toDegrees(deg)));
	    	}
	    }
	    
	    return deg;
	}
	
	public void test4() throws NotFoundException, IOException
	{
		File file = new File("C:/Users/cougartech/Documents/Scouting/test/24.png");
		BufferedImage image = null;
		double deg1 = 0;
		double deg2 = 0;
		double x1 = 0;
		double y1 = 0;
		double x2 = 0;
		double y2 = 0;

		image = ImageIO.read(file);
		
		LuminanceSource source = new BufferedImageLuminanceSource(image);
	    BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
	    Collection<Result> results = new ArrayList<>(1);

	        // Look for multiple barcodes
	    	QRCodeMultiReader multiReader = new QRCodeMultiReader();
	        Result[] theResults = multiReader.decodeMultiple(bitmap, HINTS);
	        
	        if(theResults != null) 
	        {
	          results.addAll(Arrays.asList(theResults));
	        }

	    
	    for(Object result : results.toArray())
	    {
	    	Result theResult = (Result) result;
	    	
	    	if(theResult.getText().equals("upperLeft"))
	    	{
	    		x1 = theResult.getResultPoints()[1].getX();
	    		y1 = theResult.getResultPoints()[1].getY();
	    		System.out.println("UL: " + x1 + "," + y1);
	    	}
	    	else if(theResult.getText().equals("lowerRight"))
	    	{
	    		x2 = theResult.getResultPoints()[1].getX();
	    		y2 = theResult.getResultPoints()[1].getY();
	    		System.out.println("LR: " + x2 + "," + y2);
	    	}
	    }
	    
	    deg1 = 53.2324078333933 - Math.toDegrees(Math.atan(y2 / x2));
	    deg2 = 44.92350367761102 - Math.toDegrees(Math.atan(y1 / x1));
	    System.out.println("Deg1: " + deg1);
	    System.out.println("Deg2: " + deg2);
	}
}
	


