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

	  static {
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
		File file = new File("C:/Users/cougartech/Documents/Scouting/test/CCI02152016.png");
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
	    	ResultPoint[] points = theResult.getResultPoints();
	    	System.out.println(theResult.getText());
	    	
	    	for(ResultPoint point : points)
	    	{
	    		System.out.println("X: " + point.getX() + "  Y: " + point.getY() );
	    	}
	    	System.out.println("--");
	    }
	}
}
	


