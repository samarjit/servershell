package com.ycs.be.dao;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import com.mchange.v2.c3p0.PooledDataSource;
import com.ycs.be.util.Constants;

public class C3P0Helper {
    private final  static String CACHE_NAME = "MYCACHE";
    private static DataSource dataSource;
    private static Logger logger = Logger.getLogger(C3P0Helper.class.getName());
    private static boolean initialized = false;
    public  static void setUp(){
	    	if(initialized) return;
            logger.info("Initialisation of C3P0Helper: "+initialized);
            initialized  = true;
            try {
                
            	final ResourceBundle prop = ResourceBundle.getBundle(Constants.PATH_CONFIG);
                
                // caching parms
                ComboPooledDataSource cpds = new ComboPooledDataSource();
                try {
                        cpds.setDriverClass(prop.getString("DRIVERNAME"));
                } catch (PropertyVetoException e) {
                        e.printStackTrace();
                }
                cpds.setJdbcUrl(prop.getString("DBURL"));
                cpds.setUser(prop.getString("DBUSER"));
                cpds.setPassword(prop.getString("DBPASSWORD"));
                cpds.setMinPoolSize(2);
                cpds.setAcquireIncrement(5);
                cpds.setMaxIdleTime(500); //max seconds the connection will remain idle before discarded. This can he hours
//                cpds.setMaxConnectionAge(10); //time after which a connection will be closed even though it many not be idle. This can be hours
                cpds.setMaxIdleTimeExcessConnections(20); //ideally this can be a few minutes or seconds
                cpds.setMaxPoolSize(10);
//                cpds.setCheckoutTimeout(3000); 
                cpds.setPreferredTestQuery("select 1 from dual");
                cpds.setTestConnectionOnCheckin(true); //not this does not test on check-out
                cpds.setTestConnectionOnCheckout(true); // should be removed in production, otherwise it will fire one extra query before every actual query exec
                cpds.setIdleConnectionTestPeriod(300); //3 minutes connection test interval
                cpds.setUnreturnedConnectionTimeout(30);//risky use only for debugging connection leaks
                cpds.setDebugUnreturnedConnectionStackTraces(true);
                cpds.setBreakAfterAcquireFailure(true);
                
                dataSource = cpds;
            }
            catch (Exception e) {
                e.printStackTrace();
			}
        }
    /**
     * private constructor for static class
     */
    private C3P0Helper() { }
   
    public static Connection getConnection() throws SQLException {
      return getConnection("env. unspecified");
    }
   
   
    public static Connection getConnection(String env) 
       throws SQLException 
    {
      System.out.println("C3P0Helper.class Request connection for " + env);
      if (dataSource == null) {
          throw new SQLException("OracleDataSource is null.");   
      }
      return dataSource.getConnection();
    }
   
    public static void closePooledConnections() throws SQLException{
      if (dataSource != null ) {
    	  if ( dataSource instanceof PooledDataSource)
    	  {
    	    PooledDataSource pds = (PooledDataSource) dataSource;
    	    pds.close();
    	  }     
    	  else{
    		  DataSources.destroy(dataSource);
    		  System.err.println("Not a c3p0 PooledDataSource!");
    	  }
      }
    }
   
    
    protected void finalize() throws Throwable {
        try {
        	closePooledConnections();        // close open files
        } finally {
            super.finalize();
        }
    }
    
 }