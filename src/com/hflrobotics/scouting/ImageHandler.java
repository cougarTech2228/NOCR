package com.hflrobotics.scouting;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;

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

	/**
	 * Adjusts and crops the BufferedImage based on the two QR codes.
	 * @param img the BufferedImage to be manipulated
	 * @return adjusted and cropped BufferedImage
	 * @throws IOException
	 * @throws NotFoundException
	 */
	public BufferedImage manipulateImage(BufferedImage img, double[] config) throws IOException, NotFoundException
	{
		//	Config:	0- xOff1 baseline	1- yOff1 baseline
		double xOff1 = 0;
		double yOff1 = 0;
		double radOff = 0;
		double x1 = 0;
		double y1 = 0;
		double x2 = 0;
		double y2 = 0;
		
		//	Create binary image of the BufferedImage
		LuminanceSource lumSource = new BufferedImageLuminanceSource(img);
	    BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(lumSource));
	    Collection<Result> results = new ArrayList<>(1);

        // Look for multiple QR codes in the binary image
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
	    		xOff1 = config[0] - theResult.getResultPoints()[1].getX();
	    		yOff1 = config[1] - theResult.getResultPoints()[1].getY();
	    	}
	    }
	    
	    //	Translate the image so the "upperLeft" QR code is aligned with the actual
	    AffineTransform tx = new AffineTransform();
	    tx.translate(xOff1, yOff1);
	    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
	    img = op.filter(img, null);
	    
	    //		Create binary image of the BufferedImage
	    lumSource = new BufferedImageLuminanceSource(img);
	    bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(lumSource));
	    results = new ArrayList<>(1);

        // Look for multiple QR codes in the image
    	multiReader = new QRCodeMultiReader();
        theResults = multiReader.decodeMultiple(bitmap, HINTS);
        
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
	    		
	    	}
	    	else if(theResult.getText().equals("lowerRight"))
	    	{
	    		x2 = theResult.getResultPoints()[1].getX();
	    		y2 = theResult.getResultPoints()[1].getY();
	    	}
	    }

	    radOff = config[2] - Math.atan((y2 - y1) / (x2 - x1));
	    
	    //	Rotates image to align the "lowerRight" QR code
	    AffineTransform tx1 = new AffineTransform();
	    tx1.rotate(radOff, 0, 0);
	    AffineTransformOp op1 = new AffineTransformOp(tx1, AffineTransformOp.TYPE_BILINEAR);
	    img = op1.filter(img, null);

	    // TODO add func to grab these and store 'em 187.5	187.0	337.5	339.0
	    // 0.94022327296995122925
	    
	    // TODO Size of actual based on sheet -- needs to be drawn from config
	    //	Grab a crop that has no margin
	    //img = img.getSubimage(150, 150, 2250, 3000);
	    img = img.getSubimage((int) config[3], (int) config[4], (int) config[5], (int) config[6]);
	    
	    return img;
	}
	
	public void getBaseLine(File file, String newFile, double[] config) throws NotFoundException, IOException
	{
		double xOff = 0;
		double yOff = 0;
		double x2 = 0;
		double y2 = 0;
		double radOff = 0;
		
		//	Create binary image of the BufferedImage
		BufferedImage img = ImageIO.read(file);
		LuminanceSource lumSource = new BufferedImageLuminanceSource(img);
	    BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(lumSource));
	    Collection<Result> results = new ArrayList<>(1);

        // Look for multiple QR codes in the binary image
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
	    		xOff = theResult.getResultPoints()[1].getX();
	    		yOff = theResult.getResultPoints()[1].getY();
	    		
	    	}
	    	else if(theResult.getText().equals("lowerRight"))
	    	{
	    		x2 = theResult.getResultPoints()[1].getX();
	    		y2 = theResult.getResultPoints()[1].getY();
	    	}
	    }

	    radOff = Math.atan((y2 - yOff) / (x2 - xOff));
	    img = img.getSubimage((int) config[3], (int) config[4], (int) config[5], (int) config[6]);
	    File scanned = new File(newFile + xOff + "_" + yOff + "_" + radOff + ".png");
	    ImageIO.write(img, "png", file);
		Files.move(file.toPath(), scanned.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
}
	


