package com.ycs.fe.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.soap.MTOMFeature;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.google.gson.Gson;
//import com.sun.xml.internal.ws.developer.JAXWSProperties;

 

public class FEMapFileSync {
	private static Logger logger = Logger.getLogger(FEMapFileSync.class);
	public HashMap<String,String> filePaths = new HashMap<String, String>();
	
	public HashMap<String, Long> getTimestampsMapXML(){
		InputStream scrxml;
		String returnJson = "{";
		HashMap<String, Long> returnHm = new HashMap<String, Long>();
		try {
			filePaths.clear();
			
			String screenpath = ServletActionContext.getServletContext().getRealPath("WEB-INF/classes/map");
			System.out.println("screenpath="+screenpath);
			scrxml = new BufferedInputStream(new FileInputStream(screenpath+"/screenmap.xml"));
			File file = new File(screenpath+"/screenmap.xml");
			returnJson += "'screenmap':"+file.lastModified();
			returnHm .put("screenmap",file.lastModified());
			filePaths.put("screenmap", "map/"+file.getName());
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
				returnHm.put(screenName, f.lastModified());
				filePaths.put(screenName, path);	
				 logger.debug(screenName+" "+path+"  "+new Date(f.lastModified()));
				}
			} 
			 
		} catch (FileNotFoundException e) {
			logger.error("Local getTimestampsMapXML failed",e);
		} catch (DocumentException e) {
			logger.error("Local getTimestampsMapXML failed",e);
		}
		returnJson += "}";
		return returnHm;
	}
	
	 
	/**
	 * @return fileListToSync
	 */
	public ArrayList<String> compareFiles(String strRemoteFiles){
		Map<String,Object> jsonRemoteFiles = new Gson().fromJson(strRemoteFiles, Map.class );
		ArrayList<String> fileListToSync = new ArrayList<String>();
		HashMap<String, Long> hmLocalFiles = getTimestampsMapXML();
//		if(hmLocalFiles.get("screenmap") != jsonRemoteFiles.getLong("screenmap"))
//			fileListToSync.add(filePaths.get("screenmap"));
		HashMap<String, Long> localMapp = getTimestampsMapXML();
		System.out.println(localMapp);
		for (Entry<String, Long> entry : localMapp.entrySet()) {
			try {
					if(jsonRemoteFiles.get(entry.getKey())!= null && ((Long)jsonRemoteFiles.get(entry.getKey()) >= entry.getValue())){
						System.out.println("Same timestamp: " +entry.getKey());
					}else{	 
						String debugStr = "Local"+entry.getKey()+" "+new Date(entry.getValue());
						if(jsonRemoteFiles.get(entry.getKey())!= null)debugStr += " remote: "+ jsonRemoteFiles.get(entry.getKey());
						logger.debug(debugStr);
						fileListToSync.add(filePaths.get(entry.getKey()));
					}
				 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		return fileListToSync;
	}
	
	
	
	@SuppressWarnings("restriction")
	public void uploadFiles(){
		try {
			Logger logger = Logger.getRootLogger();
			Enumeration appenders = logger.getAllAppenders();
			for  (; appenders.hasMoreElements();) {
				Appender elm = (Appender) appenders.nextElement();
				
				System.out.println(elm.getName());
			}
			 
		 
			logger.removeAllAppenders();
			PropertyConfigurator.configureAndWatch(getClass().getResource("/log4j.properties").getPath());
			appenders = logger.getAllAppenders();
			for  (; appenders.hasMoreElements();) {
				Appender elm = (Appender) appenders.nextElement();
				
				System.out.println(elm.getName());
			}
			
			String tplpath = ServletActionContext.getServletContext().getRealPath("WEB-INF/classes");
			ResourceBundle rb = ResourceBundle.getBundle(Constants.PATH_CONFIG);
			String wsbasepath = rb.getString("be.webservice.basepath");
			
			URL url = new URL(wsbasepath+"/fservice?wsdl");
			QName qname = new QName("http://util.fe.ycs.com/", "FileSyncImplService");
			MTOMFeature feature = new MTOMFeature();
			Service service1 = Service.create(url, qname);
			FileSync fileServer = service1.getPort(FileSync.class, feature);
			BindingProvider bp = (BindingProvider) fileServer;
			 Map<String, Object> ctxt=((BindingProvider)fileServer).getRequestContext();
		      ctxt.put(/*JAXWSProperties.HTTP_CLIENT_STREAMING_CHUNK_SIZE*/"com.sun.xml.internal.ws.transport.http.client.streaming.chunk.size", 8192);
		      
			SOAPBinding binding = (SOAPBinding) bp.getBinding();
			binding.setMTOMEnabled(true);
			String receivedFromBE = fileServer.getTimestampsMapXML();
			logger.debug("receivedFromBE:"+receivedFromBE);
			ArrayList<String> fileListToSync = compareFiles(receivedFromBE);
			logger.fatal("List to sync:"+fileListToSync);
			
			for (String filePaths : fileListToSync) {
				DataHandler dh =  null;
//				com.ycs.ws.fserver.FileSyncImplService service = new FileSyncImplService();
				
//				com.ycs.ws.fserver.FileSync proxy = service.getFileSyncImplPort(feature);
//				Map<String, Object> ctxt1=((BindingProvider)proxy).getRequestContext();
//			    ctxt1.put(JAXWSProperties.HTTP_CLIENT_STREAMING_CHUNK_SIZE, 8192);
//			    dh =  new Holder(new DataHandler(new FileDataSource(filePaths)));
			    String fileName = tplpath+File.separatorChar+filePaths;
			    File fileToSend = new File(fileName);
			    if(fileToSend.exists()){
			    	logger.debug("sending file:"+filePaths);
			    	DataHandler data = new DataHandler(new FileDataSource(fileToSend)); 
			    	fileServer.uploadFile(filePaths, data);
			    }
//				OutputStream out = dh.getOutputStream();
//				byte[] bytes =  getBytesFromFile(filePaths);
//				out.write(bytes);
//				out.close();
			}
		} catch (MalformedURLException e) {
			logger.error("uploadFiles failed",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	 // Returns the contents of the file in a byte array.
    public static byte[] getBytesFromFile(String filename) throws IOException {
		File file = new File(filename);
        // Get the size of the file
        long length = file.length();

// Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];
        // Read in the bytes

        InputStream is = new FileInputStream(file);
    
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
        logger.debug("Number of bytes read:"+numRead);
        
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
    
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ResourceBundle rb = ResourceBundle.getBundle(Constants.PATH_CONFIG);
			String wsbasepath = rb.getString("be.webservice.basepath");
			URL url = new URL(wsbasepath+"/fservice?wsdl");
			QName qname = new QName("http://util.fe.ycs.com/", "FileSyncImplService");
			Service service = Service.create(url, qname);
			FileSync imageServer = service.getPort(FileSync.class);
			/************  test upload ****************/
//			Image imgUpload = ImageIO.read(new File("C:/Users/Samarjit/Desktop/tempFE/WebContent/css/images/calendar.gif"));
			//enable MTOM in client
			BindingProvider bp = (BindingProvider) imageServer;
			SOAPBinding binding = (SOAPBinding) bp.getBinding();
			binding.setMTOMEnabled(true);
			
			
//			String status = imageServer.uploadImage(imgUpload);
//			System.out.println("imageServer.uploadImage() : " + status);
			
			new FEMapFileSync().uploadFiles();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
		}
	}

}
