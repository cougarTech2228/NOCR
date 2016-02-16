package com.hflrobotics.scouting;

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
	
	public void test() throws NotFoundException, ChecksumException, FormatException, IOException
	{
		Map hintMap = new HashMap();
		File file = new File("C:/Users/cougartech/Documents/Scouting/test/CCI02152016.png");
		//File file = new File("C:/Users/cougartech/Documents/Scouting/test/0.jpg");
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		QRCodeMultiReader reader = new QRCodeMultiReader();
		BinaryBitmap bitmap= new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(file))));
				
		for(Result aResults : reader.decodeMultiple(bitmap, hintMap))
		{
			System.out.println(aResults.getText());
		}
	}
	
	public void test1()
	{
		File file = new File("C:/Users/cougartech/Documents/Scouting/test/9.png");
		BufferedImage image = null;
		
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
	    		double deg = Math.toDegrees(Math.atan((y2 - y1) / (x2 - x1)));
	    		System.out.println("Adjust by (deg): " + (90 - Math.abs(deg)));
	    	}
	    	
	    	if(theResult.getText().equals("lowerRight"))
	    	{
	    		double x1 = theResult.getResultPoints()[0].getX();
	    		double x2 = theResult.getResultPoints()[1].getX();
	    		double y1 = theResult.getResultPoints()[0].getY();
	    		double y2 = theResult.getResultPoints()[1].getY();
	    		double deg = Math.toDegrees(Math.atan((y2 - y1) / (x2 - x1)));
	    		System.out.println("Adjust by (deg): " + (90 - Math.abs(deg)));
	    	}
	    }
	    
	    /*
	    for(Object result : results.toArray())
	    {
	    	Result theResult = (Result) result;
	    	System.out.println(theResult.getText());
	    	for(ResultPoint points : theResult.getResultPoints())
	    	{
	    		System.out.println(points.getX() + ", " + points.getY());
	    	}
	    	System.out.println("--");
	    }
	    */
	    /*double x1 = 0;
	    double y1 = 0;
	    double x2 = 0;
	    double y2 = 0;	    
	    
	    for(Object result : results.toArray())
	    {
	    	Result theResult = (Result) result;
	    	
	    	if(theResult.getText().equals("upperLeft"))
	    	{
	    		x1 = (int) theResult.getResultPoints()[1].getX();
	    		y1 = (int) theResult.getResultPoints()[1].getX();
	    	}
	    	
	    	if(theResult.getText().equals("lowerRight"))
	    	{
	    		x2 = (int) theResult.getResultPoints()[1].getX();
	    		y2 = (int) theResult.getResultPoints()[1].getX();
	    	}
	    }
	    
	    double slope = (y2 - y1) / (x2 - x1);
	    double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	    double angle = Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
	    System.out.println("1: " + x1 + "," + y1);
	    System.out.println("2: " + x2 + "," + y2);
	    System.out.println("Slope: " + slope);
	    System.out.println("Dist: " + distance);
	    System.out.println("Angle: " + angle);*/
	}
}
	


