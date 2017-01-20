/**
 * @(#)server.java
 *
 *
 * @author 
 * @version 1.00 2011/9/18
 */
import java.net.*;
import java.io.*;
import javax.imageio.*;
import java.awt.*;
import javax.swing.*;
import java.awt.image.*;

public class Main {
	
	public static JLabel jlb=new JLabel();
		    	    
    public static JFrame jfr=new JFrame();
		    	    
     public static void main(String[] args) {
        // TODO code application logic here
        
      try{	
    	
    	DataInputStream dis;
    	
    	ServerSocket server=new ServerSocket(65000);
    	
    	Socket socket=server.accept();
    	
    	
    	while(true){
    		
    			
			    			
    		
		    	    dis=new DataInputStream(socket.getInputStream()); 	
		    	
		    	    int len=dis.readInt();
		    	    
		    	    
		    	    byte [] buffer=new byte[len];
		    	      
		      	    dis.readFully(buffer,0,len);  
		    	     
		    	        
		    	    InputStream in = new ByteArrayInputStream(buffer);
		    	    
		    	    BufferedImage im=ImageIO.read(in);
		    	    
		    	    jlb.setIcon(new ImageIcon(im));
		    	    
		    	    jfr.add(jlb);
		    	    
		    	    jfr.pack();
		    	    
		    	    jfr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    	    
		    	    jfr.setVisible(true);
		    	    
		    	    System.gc();
		    	    
		    	    
    	    //dis.close();
    	    
    	 
    	
    	
    	}
    	
    	
      }
    
    	
    	
        
    
    catch(Exception e){e.printStackTrace();}
     
       
	
    }}