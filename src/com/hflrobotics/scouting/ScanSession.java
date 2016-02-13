package com.hflrobotics.scouting;
import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import eu.gnome.morena.Device;
import eu.gnome.morena.DeviceBase;
import eu.gnome.morena.TransferListener;


public class ScanSession 
{
  MultiFileTransferHandler th; 
  private LinkedBlockingQueue<String> queue;
  boolean transferFinished = false;
  private AtomicInteger blockedThreadCount;

  public static final String EOP=""; // string designating an End of operation

  public void startSession(Device device, int item) throws Exception
  { queue=new LinkedBlockingQueue<String>();
    blockedThreadCount=new AtomicInteger(0);
    transferFinished=false;
    th = new MultiFileTransferHandler();
    ((DeviceBase) device).startTransfer(th, item);
  }

  public File getImageFile()
  { String filename=queue.poll();
    if (filename==null && !transferFinished)
      try  {
        blockedThreadCount.incrementAndGet();
        filename=queue.take();
        blockedThreadCount.decrementAndGet();
      } catch (InterruptedException e) { }
    if (filename.isEmpty()) // EOP
      releaseBlockedThreads();
    return filename==null || filename.isEmpty()? null : new File(filename);
  }
  
  public boolean isEmptyFeeder() {
    return th!=null ? th.code==0 : false;
  }
  
  public int getErrorCode() {
    return th.code;
  }
  
  public String getErrorMessage() {
    return th.error;
  }
  
  private void releaseBlockedThreads()
  { int count=blockedThreadCount.getAndSet(0);
    if (count>0)
      for (int i=0; i<count; i++)
        try {
          queue.put(EOP);
        } catch (InterruptedException e)      {
           e.printStackTrace();
        }    
  }
  
  public static String getExt(File file)
  { String name=file.getName();
    int ix=name.lastIndexOf('.');
    if (ix>0 && ix+1<name.length())
      return name.substring(ix+1);
    return "";
  }
//========================================================
  
  
 /**
  * TransferDoneListener interface implementation that handles a scanned
  * document as a File.
  * 
  */
 class MultiFileTransferHandler implements TransferListener {
  int code;
  String error;
   
  long time;
  int pcounter; // page counter 

   
  public MultiFileTransferHandler()
  { time=System.currentTimeMillis();
    pcounter=0;
    code=-1;
    error="No error";
  }

  /**
    * Transferred image is handled in this callback method. File containing the
    * image is provided as an argument. The image type may vary depending on
    * the interface (Wia/ICA) and the device driver. Typical format includes
    * BMP for WIA scanners and JPEG for WIA camera and for ICA devices. Please
    * note that this method runs in different thread than that where the
    * device.startTransfer() has been called.
    * 
    * @param file
    *          - the file containing the acquired image
    * 
    * @see eu.gnome.morena.TransferDoneListener#transferDone(java.io.File)
    */

//   @Override
   public void transferDone(File file) {
     String parentPath=file.getParent();
     String ext=getExt(file);
     File newFile=new File(parentPath, getUniqueFileName("mi", ext));
     if (newFile.exists()) {
// find a new unique file name       
     }
     boolean renamed=file.renameTo(newFile);
     if (!renamed) {
       System.out.println("Cannot rename the file "+file+"  to  "+newFile);
       return;
     }
     try {
       queue.put(newFile.getAbsolutePath());
     } catch (InterruptedException e)      {
        e.printStackTrace();
     }
   }

   /**
    * This callback method is called when scanning process failed for any
    * reason. Description of the problem is provided.
    */

//   @Override
   public void transferProgress(int percent)
   { System.out.println("transfer "+percent+"%");
   }

//   @Override
   public void transferFailed(int code, String error) {
     this.code = code;
     this.error = error;
     transferFinished=true;
     try {
       queue.put(EOP);
     } catch (InterruptedException e)      {
        e.printStackTrace();
     }
   }

   private synchronized String getUniqueFileName(String prefix, String ext)
   { return new String(prefix+"_"+time+"_"+(++pcounter)+(!ext.isEmpty()?"."+ext:""));
   }

 }
 
}

