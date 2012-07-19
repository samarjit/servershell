package com.ycs.fe.commandprocessor;

import java.io.InputStream;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class BusinessLogicFactory {
	private static final String BLCACHE="BUSINESS_LOGIC_CACHE";
	
	private static Log logger = LogFactory.getLog(BusinessLogicFactory.class);
	//private static  HashMap<String,BaseBL> blcache = new HashMap<String,BaseBL>();

	 
	public BusinessLogicFactory() {
		
	}
	 
	public static void createBL(Cache blcache2, String mappingxmlpath)  {
		 InputStream mappingxml = ScreenMapRepo.class.getResourceAsStream("/"+mappingxmlpath);
		 Document mapdoc;
		try {
			mapdoc = new SAXReader().read(mappingxml);
		 Element screenElm = (Element) mapdoc.selectSingleNode("/root/screen");
		 String screenName = screenElm.attributeValue("name");
		 Element e = (Element) screenElm.selectSingleNode("callbackclass");
		 logger.debug("creating Business Logic for "+screenName);
		
		 if(e != null){
			 System.out.println(e.getTextTrim());
			 String classname = e.getTextTrim();
			 if(classname == null || "".equals(classname)){
				 		logger.debug("Class name empty for screen:"+screenName);
				 		throw new ClassNotFoundException("BL not defined for screen ["+screenName+"]");
				 	}
			 		
			 BaseBL  baseBL = (BaseBL) Class.forName(classname).newInstance();
			 net.sf.ehcache.Element elm = new net.sf.ehcache.Element(screenName, baseBL); 
			 blcache2.put(elm);
		 }
		
		} catch (DocumentException e1) {
			logger.error("Exception in BL caching"+e1);
		} catch (InstantiationException e) {
			logger.error("Exception in BL caching"+e);
		} catch (IllegalAccessException e) {
			logger.error("Exception in BL caching"+e);
		} catch (ClassNotFoundException e) {
			logger.error("Exception in BL caching"+e.getLocalizedMessage());
		}
	}
	
	public static BaseBL getBusinessLogic(String screenName){
		Cache blcache =  AppCacheManager.getFromCache(BLCACHE);
		BaseBL bl = null;
		try {
			//Try to reload cache to get the value
			if(blcache.get(screenName) == null )refreshCache(blcache,screenName);
			
			//In case the refresh Cache does not find the Business Logic return null
			if(blcache.get(screenName) != null ) 
			bl = (BaseBL) blcache.get(screenName).getValue();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return bl;
	}

	private static void refreshCache(Cache blcache2, String screenName) {
		logger.debug("refreshing cache: It should not come here: Implementation TODO");
		InputStream scrxml = BusinessLogicFactory.class.getResourceAsStream("/map/screenmap.xml");
		System.out.println("InitCache");
		Document doc;
		try {
			doc = new SAXReader().read(scrxml);
			Element root = doc.getRootElement();
			Element nscr = (Element) root.selectSingleNode("screen[@name='"+screenName+"']");
//			 createBL(blcache2,nscr.attributeValue("mappingxml")); (back end business logic is used)
		} catch (DocumentException e) {
			logger.debug("Exception in BL caching"+e);
		}
	}

	public void createBLCache(Element root) {
		String path;
		List<Element> n =  root.selectNodes("screen");
		
		Cache blcache = new Cache(BLCACHE, 5000, false, false, 5, 2);
		AppCacheManager.addCache( blcache);
		if(n != null){
			 for (Element elm : n) {
				 path = elm.attributeValue("mappingxml");
				logger.debug("scrren clCache"+path);
//				 createBL(blcache , path);(back end business logic is used)
			}
		}
	}
 
}
