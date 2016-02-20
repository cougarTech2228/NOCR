package com.hflrobotics.scouting;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

	public void manipulateImage() throws IOException, NotFoundException
	{
		// TODO change to accept and return BufferedImage
		File file = new File("C:/Users/cougartech/Documents/Scouting/test/8.png");
		File outputFile = new File("C:/Users/cougartech/Documents/Scouting/test/8__.png");
		
		double xOff1 = 0;
		double yOff1 = 0;
		double degOff = 0;
		double x1 = 0;
		double y1 = 0;
		double x2 = 0;
		double y2 = 0;
		BufferedImage img = ImageIO.read(file);
		
		LuminanceSource lumSource = new BufferedImageLuminanceSource(img);
	    BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(lumSource));
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
	    		xOff1 = 187.5 - theResult.getResultPoints()[1].getX();
	    		yOff1 = 187.0 - theResult.getResultPoints()[1].getY();
	    	}
	    }
	    
	    AffineTransform tx = new AffineTransform();
	    tx.translate(xOff1, yOff1);

	    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
	    img = op.filter(img, null);
	    
	    lumSource = new BufferedImageLuminanceSource(img);
	    bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(lumSource));
	    results = new ArrayList<>(1);

        // Look for multiple barcodes
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

	    // TODO Change this to just be radians
	    degOff = 53.87082534128857 - Math.toDegrees(Math.atan((y2 - y1) / (x2 - x1)));
	    
	    AffineTransform tx1 = new AffineTransform();
	    
	    tx1.rotate(Math.toRadians(degOff), 0, 0);

	    AffineTransformOp op1 = new AffineTransformOp(tx1, AffineTransformOp.TYPE_BILINEAR);
	    img = op1.filter(img, null);

	    // TODO add func to grab these and store 'em 187.5	187.0	337.5	339.0
	    
	    // TODO Size of actual based on sheet -- needs to be drawn from config
	    img = img.getSubimage(150, 150, 2250, 3000);
	    ImageIO.write(img, "png", outputFile);
	}
	
	public void test7()
	{
		
	}
}
	


