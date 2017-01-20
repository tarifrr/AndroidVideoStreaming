package com.app.lcfs;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class Preview extends SurfaceView implements SurfaceHolder.Callback { 
  private static final String TAG = "Preview";

  SurfaceHolder mHolder;  
  public Camera camera; 
  public Parameters param;
  public Size size;
  public Socket[] toServer=new Socket[1];
  public DataOutputStream [] outStream=new DataOutputStream[1];
  public int count=0;

  
  Preview(Context context) {
    super(context);

    
    mHolder = getHolder();// initialize the Surface Holder  
    mHolder.addCallback(this); // add the call back to the surface holder
    mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); 
  }
  public static boolean takepic=true;
  // Called once the holder is ready
  public void surfaceCreated(SurfaceHolder holder) {  
    // The Surface has been created, acquire the camera and tell it where
    // to draw.
	  
    camera = Camera.open();// activate the camera 
    param=camera.getParameters();// acquire the parameters for the camera
    size=param.getPreviewSize();// get the size of each frame captured by the camera
    
    //camera.setParameters(param);// setting the parameters to the camera but this line is not required
    try{
    	
    	toServer[0]=new Socket("192.168.0.101",65000);// connect to server socket of Ip address 192.168.140.101 at port 65000
    	outStream[0]=new DataOutputStream(toServer[0].getOutputStream());//open an outputstream to the socket for sending the image data
       //toServer[1]=new Socket("192.168.140.104",8000);// connect to server socket of Ip address 192.168.140.101 at port 8000
	    //outStream[1]=new DataOutputStream(toServer[1].getOutputStream());//open an outputstream to the socket for sending the image data
	    //toServer[2]=new Socket("192.168.140.105",8000);// connect to server socket of Ip address 192.168.140.101 at port 8000
	    //outStream[2]=new DataOutputStream(toServer[2].getOutputStream());//open an outputstream to the socket for sending the image data
      
    
    
    }
  catch(Exception e){
    	Log.d("exception",e.toString());
    	}
    try {
      camera.setPreviewDisplay(holder);   
       
      camera.setPreviewCallback(new PreviewCallback() { 
        // Called for each frame previewed
        public void onPreviewFrame(byte[] data, Camera camera) { 
        
        try{
	        		 YuvImage yuv_image = new YuvImage(data,param.getPreviewFormat() , size.width, size.height, null); // all bytes are in YUV format therefore to use the YUV helper functions we are putting in a YUV object 
	        		 Rect rect = new Rect(0, 0, size.width, size.height);  
	                 ByteArrayOutputStream output_stream = new ByteArrayOutputStream();  
	                 yuv_image.compressToJpeg(rect, 80, output_stream);// image has now been converted to the jpg format and bytes have been written to the output_stream object
	                 
	                 
	                 byte [] tmp=output_stream.toByteArray();//getting the byte array
	                 
	                 for(int i=0;i<1;i++){
	                 outStream[i].writeInt(tmp.length);// sending the size of the array 
	                 outStream[i].write(tmp);// writing the array to the socket output stream
	                 outStream[i].flush();
	                 }
	                 
	                 System.gc();
	                 
					Log.d(TAG, "onPreviewFrame - wrote bytes: "+ data.length);
				        	
	        		
        }catch(Exception e){Log.d("exception",e.toString());}          
          
        }
      });
    } catch (IOException e) { 
    	Log.d("exception",e.toString());
    }
  }

  // Called when the holder is destroyed
  public void surfaceDestroyed(SurfaceHolder holder) {  
    camera.stopPreview(); // preview will stop once user exits the application screen
    camera = null;
  }

  // Called when holder has changed
  public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) { 
    camera.startPreview();// camera frame preview starts when user launches application screen
  }

}