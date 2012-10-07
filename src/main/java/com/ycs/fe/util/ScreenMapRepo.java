package com.ycs.fe.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.ycs.fe.cache.AppCacheManager;
import com.ycs.fe.cache.ScreenDetails;
import com.ycs.fe.exception.BackendException;
import com.ycs.fe.exception.FrontendException;

 
 

public class ScreenMapRepo {
	private static  Logger logger = Logger.getLogger(ScreenMapRepo.class);
	
	
	/**
	 * This returns the mappings of screenName to XML config
	 * @param scrName
	 * @return path of mapping XML
	 * @throws BackendException 
	 */
	public static String findMapXMLPath(String scrName) throws FrontendException{
		String path = null;
		
//		InputStream scrxml = ScreenMapRepo.class.getResourceAsStream("/screenmap.xml");


		Document doc;
		try {
			Element root;
			
			net.sf.ehcache.Element scrXmlFromCache = AppCacheManager.getElementFromCache("xmlcache", "scrxmlroot");
					
			if(scrXmlFromCache == null){
//				String screenpath = ServletActionContext.getServletContext().getRealPath("WEB-INF/classes/map");
				URL url = ScreenMapRepo.class.getResource("/map");
				String screenpath = url.getPath();
				InputStream scrxml = new BufferedInputStream(new FileInputStream(screenpath+"/screenmap.xml"));
			
				doc = new SAXReader().read(scrxml);
				root = doc.getRootElement();
//				System.out.println("xmlcache -> scrxmlroot cache miss");
				AppCacheManager.putElementInCache("xmlcache", "scrxmlroot", root);
			}else{
				root = (Element) scrXmlFromCache.getObjectValue();
//				System.out.println("xmlcache -> scrxmlroot cache hit");
			}
			
			
			Element n = (Element) root.selectSingleNode("screen[@name='"+scrName+"']");
			if(n == null){
				logger.debug("Mapping of <screen name="+scrName+" /> is not defined in screenmap.xml!");
				return null;
			}
			
			path = n.attributeValue("mappingxml");
//			String tplpath = ServletActionContext.getServletContext().getRealPath("WEB-INF/classes");
			URL url = ScreenMapRepo.class.getResource("/map");
			String tplpath = new File(url.getPath()).getParent();
			File f = new File(tplpath+"/"+path);
			path = f.getAbsolutePath();
			
		} catch (DocumentException e) {
			logger.error("Reading xmlDocument using SAXReader", e);
			throw new FrontendException("error.readingScreenMapXML",e);
		} catch (FileNotFoundException e) {
			logger.error("ScreenMap. xml file not found.",e);
			throw new FrontendException("error.readingScreenMapXML",e);
		}

		return path;
	}
	
	public static Element findMapXMLRoot(String scrName) throws FrontendException{
		Element root = null;
		String path = findMapXMLPath(scrName);
		try {
			net.sf.ehcache.Element scrXmlFromCache = AppCacheManager.getElementFromCache("xmlcache", scrName);
				
			if(scrXmlFromCache == null){
		
				Document doc = new SAXReader().read(path);
				root = doc.getRootElement();
				System.out.println("xmlcache -> "+scrName+" cache miss");
				AppCacheManager.putElementInCache("xmlcache", scrName, root);
			}else{
				root = (Element) scrXmlFromCache.getObjectValue();	
				System.out.println("xmlcache -> "+scrName+" cache hit");
			}
		} catch (DocumentException e) {
			logger.error("XML Load Exception path="+path+" ScreenName="+scrName);
			throw new FrontendException("error.loadxml",e);
		}
		return root;
	}
	
	public static String findScrNameFromScreenName(String mappingxml){
		Document doc;
		String screenName = null;
		try {
			Element root;
		net.sf.ehcache.Element scrXmlFromCache = AppCacheManager.getElementFromCache("xmlcache", "scrxmlroot");
			
		if(scrXmlFromCache == null){
//			String screenpath = ServletActionContext.getServletContext().getRealPath("WEB-INF/classes/map");
			URL url = ScreenMapRepo.class.getResource("/map");
			String screenpath = url.getPath();
			InputStream scrxml = new BufferedInputStream(new FileInputStream(screenpath+"/screenmap.xml"));
		
			doc = new SAXReader().read(scrxml);
			root = doc.getRootElement();
			logger.debug("xmlcache -> scrxmlroot cache miss");
			AppCacheManager.putElementInCache("xmlcache", "scrxmlroot", root);
		}else{
			root = (Element) scrXmlFromCache.getObjectValue();	
			logger.debug("xmlcache -> scrxmlroot cache hit");
		}
		
		
		Element n = (Element) root.selectSingleNode("screen[@mappingxml='"+mappingxml+"']");
		screenName = n.attributeValue("name");
		}catch (DocumentException e) {
			logger.debug("XML Load Exception  mappingxml="+mappingxml);
		} catch (FileNotFoundException e) {
			logger.debug("File not found",e);
		}
		return screenName;
	}
	
	public static ScreenDetails findScreenDetails(String scrName) throws FrontendException{
		ScreenDetails screenDetails = null;
		
		try {
			net.sf.ehcache.Element scrDtl = AppCacheManager.getElementFromCache("xmlcache", "SCR_DTL"+scrName);
				
			if(scrDtl == null){
		
				
				screenDetails = new ScreenDetails();
				screenDetails.populateScrDetails(scrName);// doc.getRootElement();
				logger.debug("xmlcache1 -> "+scrName+" cache miss");
				AppCacheManager.putElementInCache("xmlcache", "SCR_DTL"+scrName, screenDetails);
			}else{
				screenDetails = (ScreenDetails) scrDtl.getObjectValue();	
				logger.debug("xmlcache1 -> "+scrName+" cache hit");
			}
		} catch (FrontendException e) {
			logger.error("XML Load Exception   ScreenName="+scrName);
			throw new FrontendException("error.loadxml",e);
		}
		return screenDetails;
	}
	
	public static void main(String[] args) throws FrontendException {
		System.out.println(ScreenMapRepo.findMapXMLPath("ProgramSetup"));
	}
}
