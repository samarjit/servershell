package com.ycs.be.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;

import javax.activation.DataHandler;
import javax.imageio.ImageIO;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.ws.Endpoint;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.MTOM;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.ycs.be.cache.AppCacheManager;

@MTOM
@WebService(endpointInterface = "com.ycs.be.util.FileSync")
public class FileSyncImpl implements FileSync{

	private static Logger logger = Logger.getLogger(FileSyncImpl.class);
	
	@WebMethod
	public String getTimestampsMapXML(){
		InputStream scrxml;
		String returnJson = "{";
		try {
			AppCacheManager.removeCache("xmlcache");
//			ServletActionContext.create();
			String screenpath = ServletActionContext.getServletContext().getRealPath("WEB-INF/classes/map");
			System.out.println("screenpath="+screenpath);
			scrxml = new BufferedInputStream(new FileInputStream(screenpath+"/screenmap.xml"));
			File file = new File(screenpath+"/screenmap.xml");
			returnJson += "'screenmap':"+file.lastModified();
			Document doc = new SAXReader().read(scrxml);
			Element	root = doc.getRootElement();
			System.out.println("Got root element");
			
			for (Iterator<Element> lsItr = root.elements().listIterator(); lsItr.hasNext();) {
				String tplpath = ServletActionContext.getServletContext().getRealPath("WEB-INF/classes");
				Element elm = lsItr.next();
				String path = elm.attributeValue("mappingxml");
				String screenName = elm.attributeValue("name");
				File f = new File(tplpath+"/"+path);
				if(f.exists()){					
					returnJson +=  "," ;
					returnJson += "'"+screenName+"':"+f.lastModified()+"";
					logger.debug(screenName+" "+path+"  "+new Date(f.lastModified()));
				}
			} 
//			ServletActionContext.destroy();
		} catch (FileNotFoundException e) {
			logger.error("Local getTimestampsMapXML failed",e);
		} catch (DocumentException e) {
			logger.error("Local getTimestampsMapXML failed",e);
		}
		returnJson += "}";
		return returnJson;
	}
	
	 

	    // Use @XmlMimeType to map to DataHandler on the client side
	@WebMethod    
	public void uploadFile(String name, @XmlMimeType("application/octet-stream") DataHandler data) {
	        try {
//	        	ServletActionContext.create();
	        	String tplpath = ServletActionContext.getServletContext().getRealPath("WEB-INF/classes");
	            // StreamingDataHandler dh = (StreamingDataHandler)data;
	             File file = new File(tplpath+File.separatorChar+name);//File.createTempFile(name, "");
	             System.out.println("File to create in BE:"+tplpath+File.separatorChar+name);
//	             dh.moveTo(file);
//	             dh.close();
	             
	             if (file.getParentFile() != null && !file.getParentFile().mkdirs()) {
	            	    /* handle permission problems here */
	            	}
            	/* either no parent directories there or we have created missing directories */
            	if (file.createNewFile() || file.isFile()) {
            		logger.debug("receiving file:"+file.getAbsolutePath());
	             FileOutputStream fos = new FileOutputStream(file);
	             long bytesRead = 0; 
	             InputStream in = null; 
	               
	                 in = data.getInputStream(); 
	                 int byteRead = in.read(); 
	                 while( byteRead!=-1 ) { 
	                	 fos.write(byteRead);
	                     bytesRead++;                  //only count bytes for now 
	                     byteRead = in.read();     //to check if upload succeded
	                 } 
	                 in.close(); 
	                 fos.write(new byte[]{13,10});
	                 fos.close();
	                 
	             }
//	             ServletActionContext.destroy();
	        } catch(Exception e) {
	        	logger.debug("unknown exception",e);
	             throw new WebServiceException(e);
	        }
	 }
	 
	@WebMethod
	public Image downloadImage(String name) {
 
		try {
 
			File image = new File("c:\\images\\" + name);
			return ImageIO.read(image);
 
		} catch (IOException e) {
 
			e.printStackTrace();
			return null; 
 
		}
	}
 
	@WebMethod
	public String uploadImage(Image data) {
 
		try {
			if(data!=null){
				final BufferedImage buffImg = new BufferedImage(data.getWidth(null), data.getHeight(null), BufferedImage.TYPE_INT_RGB);  
				final Graphics2D g2 = buffImg.createGraphics();  
				g2.drawImage(data, null, null);  
				g2.dispose(); 
				ImageIO.write(buffImg, "gif", new File("cal.gif"));
				return "Upload Successful";
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
 
		throw new WebServiceException("Upload Failed!");
 
	}
	
	@WebMethod(exclude=true)
	public static void main(String[] args) {
		
		FileSync fileSync = new FileSyncImpl();
		System.out.println(fileSync.getTimestampsMapXML());
		Endpoint.publish("http://localhost:8183/WS/fservice", new  FileSyncImpl());
		
	}

}
