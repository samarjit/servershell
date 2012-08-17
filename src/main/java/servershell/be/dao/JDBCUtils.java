package servershell.be.dao;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.PooledConnection;

import oracle.jdbc.pool.OracleConnectionCacheManager;
import oracle.jdbc.pool.OracleDataSource;

import org.apache.log4j.Logger;

public class JDBCUtils {
    private final  static String CACHE_NAME = "MYCACHE";
    private  static OracleDataSource ods = null;
 
    private static Logger logger = Logger.getLogger(JDBCUtils.class.getName());
    
    public  static void setUp(){
            logger.info("Initialisation of OracleDataSource");
            try {
                ods = new OracleDataSource();
                
                ClassLoader loader = JDBCUtils.class.getClassLoader();	
                Properties prop = new Properties();
                prop.load(loader.getResourceAsStream("config.properties"));
                
                ods.setURL(prop.getProperty("DBURL"));
                ods.setUser( prop.getProperty("DBUSER"));
                ods.setPassword(prop.getProperty("DBPASSWORD"));
                // caching parms
                ods.setConnectionCachingEnabled(true); 
                ods.setConnectionCacheName(CACHE_NAME);
                PrintWriter pr = new PrintWriter( new OutputStreamWriter(System.err));
                ods.setLogWriter(pr);
                Properties cacheProps = new Properties();
                cacheProps.setProperty("MinLimit", "10");
                cacheProps.setProperty("MaxLimit", "20"); 
                cacheProps.setProperty("InitialLimit", "5"); 
                cacheProps.setProperty("ConnectionWaitTimeout", "5");
                cacheProps.setProperty("ValidateConnection", "true");
 
                ods.setConnectionCacheProperties(cacheProps);
                
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
    private JDBCUtils() { }
   
    public static Connection getConnection() throws SQLException {
      return getConnection("env. unspecified");
    }
   
   
    public static Connection getConnection(String env) 
       throws SQLException 
    {
//      System.out.println("JDBCUtils.class Request connection for " + env);
      if (ods == null) {
          throw new SQLException("OracleDataSource is null.");   
      }
      return ods.getConnection();
    }
   
    public static void closePooledConnections() throws SQLException{
      if (ods != null ) {
          ods.close();
      }
    }
   
    public static void listCacheInfos() throws SQLException{
      OracleConnectionCacheManager occm =
          OracleConnectionCacheManager.getConnectionCacheManagerInstance();
      System.out.println
          ("JDBCUtils.class "+occm.getNumberOfAvailableConnections(CACHE_NAME)
              + " connections are available in cache " + CACHE_NAME);
      System.out.println
          ("JDBCUtils.class "+occm.getNumberOfActiveConnections(CACHE_NAME)
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