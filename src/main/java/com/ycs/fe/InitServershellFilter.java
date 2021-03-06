package com.ycs.fe;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.sf.ehcache.CacheException;

import org.apache.log4j.Logger;

import com.ycs.fe.cache.AppCacheManager;

 
/**
 * This class is for future use in case some initialization of resources is to be done or some resource disallocation or some housekeeping
 * activity should be done here.
 * @author SAMARJIT
 *
 */
public class InitServershellFilter implements ServletContextListener {
	ServletContext context;

	@Override
	public void contextDestroyed(ServletContextEvent contextEvent) {
		context = contextEvent.getServletContext();
		System.out.println("servershell Servlet context shutting down...");
		AppCacheManager.shutdown();
		com.ycs.be.cache.AppCacheManager.shutdown();
		com.ycs.fe.cache.AppCacheManager.shutdown();
	}

	/*
	 * Any AMS related initialization of resources can be done here For future
	 * use
	 */
	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {
		Logger logger = Logger.getLogger(InitServershellFilter.class);
		logger.debug("servershell servlet starting...");

		AppCacheManager appcache;
		com.ycs.be.cache.AppCacheManager appcachebe;
		com.ycs.fe.cache.AppCacheManager appcachefe;
		try {
			appcache = AppCacheManager.getInstance();
			appcachebe = com.ycs.be.cache.AppCacheManager.getInstance();
			appcachefe = com.ycs.fe.cache.AppCacheManager.getInstance();
//			appcachebe.initCache();
//			appcachefe.initCache();
		} catch (CacheException e) {
			e.printStackTrace();
		}
		System.out.println("servershell Servlet context Started!!!");
		// remove all the below lines in production
		// initDb();
		// init some variables

	}

}