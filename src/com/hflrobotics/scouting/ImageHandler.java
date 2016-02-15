package com.hflrobotics.scouting;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import boofcv.abst.fiducial.FiducialDetector;
import boofcv.factory.fiducial.ConfigFiducialBinary;
import boofcv.factory.fiducial.FactoryFiducial;
import boofcv.factory.filter.binary.ConfigThreshold;
import boofcv.factory.filter.binary.ThresholdType;
import boofcv.gui.fiducial.VisualizeFiducial;
import boofcv.gui.image.ShowImages;
import boofcv.io.UtilIO;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.calib.IntrinsicParameters;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageType;
import georegression.struct.se.Se3_F64;

public class ImageHandler
{

	public ImageHandler()
	{
		
	}
	
	public void test()
	{
		// load the lens distortion parameters and the input image
		IntrinsicParameters param = new IntrinsicParameters(1200/2, 1200/2, 0, 1200/2, 1200/2, 1200, 1200);
		BufferedImage input = null;
		try
		{
			input = ImageIO.read(new File("C:/Users/cougartech/Documents/Scouting/pitToBeScanned/56.png"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		ImageFloat32 original = ConvertBufferedImage.convertFrom(input,true, ImageType.single(ImageFloat32.class));
 
		// Detect the fiducial
		FiducialDetector<ImageFloat32> detector = FactoryFiducial.squareBinary(
				new ConfigFiducialBinary(0.1), ConfigThreshold.local(ThresholdType.LOCAL_SQUARE, 10), ImageFloat32.class);

		detector.setIntrinsic(param);
 		detector.detect(original);
 
		// print the results
		Graphics2D g2 = input.createGraphics();
		Se3_F64 targetToSensor = new Se3_F64();
		for (int i = 0; i < detector.totalFound(); i++) {
			System.out.println("Target ID = "+detector.getId(i));
			detector.getFiducialToCamera(i, targetToSensor);
			System.out.println("Location:");
			System.out.println(targetToSensor);
 
			VisualizeFiducial.drawCube(targetToSensor,param,detector.getWidth(i), 3, g2);
			VisualizeFiducial.drawLabelCenter(targetToSensor,param,""+detector.getId(i), g2);
		}
		
		ShowImages.showWindow(input,"Fiducials",true);
	}	
}
