package com.ycs.be.cache;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.ObjectExistsException;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Singleton class to load cache and retrieve cache elements
 * @author Samarjit
 *
 */
public class AppCacheManager {
	private static AppCacheManager instance;
	private static Map cache;
	private static CacheManager singletonManager;
	public static AppCacheManager getInstance() throws CacheException{
		if(instance == null){
			instance = new AppCacheManager();
			cache = new HashMap();
			singletonManager = CacheManager.create();
		}
		return instance;
	}
	private AppCacheManager(){}
	
	
	private static Logger logger= Logger.getLogger(AppCacheManager.class);
	private static Cache localCache;
	
	public void initCache(){
		String path = null;
		//String tplpath = ServletActionContext.getServletContext().getRealPath("WEB-INF/classes/map"); 
		InputStream scrxml = AppCacheManager.class.getResourceAsStream("/map/screenmap.xml");
		System.out.println("InitCache");
		Document doc;
		BusinessLogicFactory blf = new BusinessLogicFactory();
		try {
		
			doc = new SAXReader().read(scrxml);
			Element root = doc.getRootElement();
			blf.createBLCache(root);
			
			 
			 
			 
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void addCache( Cache cache1){
		try {
			
			singletonManager.addCache(cache1);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (ObjectExistsException e) {
			e.printStackTrace();
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}
	
	public static void createCache(String cacheName){
		localCache = new Cache(cacheName, 50, false, false, 120, 120);
		  singletonManager.addCache(localCache);
	}
	public static void putElementInCache(String cachename,String key, Object object){
		try {
			net.sf.ehcache.Element element = new net.sf.ehcache.Element(key, object);
			if(singletonManager.getCache(cachename) == null){
				createCache(cachename);
			}
			singletonManager.getCache(cachename).put(element );
			 
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (ObjectExistsException e) {
			e.printStackTrace();
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}
	
	public static net.sf.ehcache.Element getElementFromCache(String cachename,String key){
		if(singletonManager.getCache(cachename) == null)return null;
		return singletonManager.getCache(cachename).get(key);	
	}
	
	public static Cache getFromCache(String key){
		return singletonManager.getCache(key);
	}
	
	
	/**
	 * @param args
	 * @throws CacheException 
	 */
	public static void main(String[] args) throws CacheException {
		  AppCacheManager.getInstance().initCache();
	}
	
	public static void removeCache(String cachename) {
		logger.debug("removing cache: "+cachename);
		singletonManager.removeCache(cachename);
	}
	public static void shutdown() {
		singletonManager.shutdown( );
	}
	
	
}
