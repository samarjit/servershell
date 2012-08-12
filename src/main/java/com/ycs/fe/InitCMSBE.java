package com.ycs.fe;

import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.sf.ehcache.CacheException;

import org.apache.log4j.Logger;

import com.ycs.fe.commandprocessor.AppCacheManager;

 
/**
 * This class is for future use in case some initialization of resources is to be done or some resource disallocation or some housekeeping
 * activity should be done here.
 * @author SAMARJIT
 *
 */
public class InitCMSBE implements ServletContextListener {
	ServletContext context;

	@Override
	public void contextDestroyed(ServletContextEvent contextEvent) {
		context = contextEvent.getServletContext();
		System.out.println("servershell Servlet context shutting down...");
		AppCacheManager.shutdown();
		 
	}

	/*
	 * Any AMS related initialization of resources can be done here For future
	 * use
	 */
	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {
		Logger logger = Logger.getLogger(InitCMSBE.class);
		logger.debug("servershell servlet starting...");
		
		AppCacheManager appcache;
		try {
			appcache = AppCacheManager.getInstance();
			//appcache.initCache();
		} catch (CacheException e) {
			e.printStackTrace();
		}
		System.out.println("servershell Servlet context Started!!!");
		// remove all the below lines in production
		// initDb();
		// init some variables

	}

}
