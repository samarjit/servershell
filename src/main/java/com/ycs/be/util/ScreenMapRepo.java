package com.ycs.be.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.ycs.be.cache.AppCacheManager;
import com.ycs.be.exception.BackendException;
import com.ycs.be.exception.FrontendException;

 
 

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
				logger.debug("Loading screenmap xml ... :"+screenpath+"/screenmap.xml");
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
		String path = null;
		try {
			net.sf.ehcache.Element scrXmlFromCache = AppCacheManager.getElementFromCache("xmlcache", scrName);
				
			if(scrXmlFromCache == null){
			path = findMapXMLPath(scrName);
			Document doc = new SAXReader().read(path);
			root = doc.getRootElement();
				logger.debug("xmlcache -> "+scrName+" cache miss");
				AppCacheManager.putElementInCache("xmlcache", scrName, root);
			}else{
				root = (Element) scrXmlFromCache.getObjectValue();	
				logger.debug("xmlcache -> "+scrName+" cache hit");
			}
		} catch (DocumentException e) {
			path = findMapXMLPath(scrName);
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
			logger.error("XML Load Exception  mappingxml="+mappingxml);
		} catch (FileNotFoundException e) {
			logger.error("File not found",e);
		}
		return screenName;
	}
	
	public static void main(String[] args) throws FrontendException {
		System.out.println(ScreenMapRepo.findMapXMLPath("ProgramSetup"));
	}
}
