package com.ycs.be.dao;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import oracle.jdbc.pool.OracleConnectionCacheManager;
import oracle.jdbc.pool.OracleDataSource;

import org.apache.log4j.Logger;

public class MiniConnectionPoolHelper {
	 
	    private final  static String CACHE_NAME = "MYCACHE";
	    private  static  MiniConnectionPoolManager poolMgr = null;
	 
	    private static Logger logger = Logger.getLogger(MiniConnectionPoolHelper.class.getName());
	    public  static void setUp() {
	            logger.info("Initialisation of OracleDataSource MiniConnectionPoolHelper");
	            try {
	                 
	                ClassLoader loader = MiniConnectionPoolHelper.class.getClassLoader();	
	                Properties prop = new Properties();
	                prop.load(loader.getResourceAsStream("path_config.properties"));
	                
	                 
	                PrintWriter pr = new PrintWriter( new OutputStreamWriter(System.err));
	                
	                
	                oracle.jdbc.pool.OracleConnectionPoolDataSource dataSource = new oracle.jdbc.pool.OracleConnectionPoolDataSource();
	                dataSource.setDriverType ("thin");
	                dataSource.setLogWriter(pr);
//	                dataSource.setServerName ("server1.yourdomain.com");
//	                dataSource.setPortNumber (1521);
//	                dataSource.setServiceName ("db1.yourdomain.com");
	                dataSource.setURL(prop.getProperty("DBURL"));
	                dataSource.setUser (prop.getProperty("DBUSER"));
	                dataSource.setPassword (prop.getProperty("DBPASSWORD"));
	                poolMgr = new MiniConnectionPoolManager(dataSource, 4);
	                
	            }
	            catch (SQLException e) {
	                e.printStackTrace();
	            } catch (IOException e) {
					e.printStackTrace();
				}
	        }
	   
	    /**
	     * private constructor for static class
	     */
	    private MiniConnectionPoolHelper() { }
	   
	     
	   
	    public static Connection getConnection() 
	       throws SQLException 
	    {
	      if (poolMgr == null) {
	          throw new SQLException("OracleDataSource is null.");   
	      }
	      return poolMgr.getValidConnection();
	    }
	   
	    public static void closePooledConnections() throws SQLException{
	    	logger.debug("closing all DB connection pools");
	    	if (poolMgr != null ) {
	          poolMgr.dispose();
	      }
	    }
	   
	    public static void listCacheInfos() throws SQLException{
	      OracleConnectionCacheManager occm =
	          OracleConnectionCacheManager.getConnectionCacheManagerInstance();
	      System.out.println
	          ("MiniConnectionPoolHelper.class "+occm.getNumberOfAvailableConnections(CACHE_NAME)
	              + " connections are available in cache " + CACHE_NAME);
	      System.out.println
	          ("MiniConnectionPoolHelper.class "+occm.getNumberOfActiveConnections(CACHE_NAME)
	              + " connections are active");
	 
	    }
	    
	    protected void finalize() throws Throwable {
	        try {
	        	closePooledConnections();        // close open files
	        } finally {
	            super.finalize();
	        }
	    }
	    
	 }
