package com.ycs.fe;

 
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.rowset.CachedRowSet;

import net.sf.ehcache.CacheException;

import org.apache.log4j.Logger;
import org.h2.tools.RunScript;
import org.h2.tools.Server;

import com.ycs.fe.cache.AppCacheManager;
import com.ycs.fe.dao.DBConnector;

/**
 * This class is for future use in case some initialization of resources is to be done or some resource disallocation or some housekeeping
 * activity should be done here.
 * @author SAMARJIT
 *
 */
public class InitFE implements ServletContextListener {
	ServletContext context;
	
	@Override
	public void contextDestroyed(ServletContextEvent contextEvent) {
		context = contextEvent.getServletContext();
		System.out.println("FE Servlet context shutting down...");
		if(webserver != null)
		webserver.stop();
		if(tcpserver != null)
		tcpserver.stop();
	}

	/* Any AMS related initialization of resources can be done here
	 * For future use
	 */
	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {
	 	Logger logger  = Logger.getLogger(InitFE.class);
	 	logger.debug("servlet starting...");
	 	context = contextEvent.getServletContext();
	 	context.setAttribute("TEST", "TEST_VALUE");	
	 	AppCacheManager appcache;
		try {
			appcache = AppCacheManager.getInstance();
			appcache.setContext(context);
			appcache.initCache();
		} catch (CacheException e) {
			e.printStackTrace();
		}
	 	System.out.println("FE Servlet context Started!!!");
	 	//remove all the below lines in production
	 //initDb();
	 	//init some variables
	 	
	}
	
	/**
     * Initialize a database from a SQL script file.
     */
	Server webserver; 
	Server tcpserver;
    void initDb()  {
        try {
        	tcpserver =  Server.createTcpServer().start();
        	webserver = Server.createWebServer().start();
        	
        	/*List<String> command = Arrays.asList(new String[] {
	                "C:\\Program Files\\Java\\jdk1.6.0_23\\jre\\bin\\java.exe",
	                "-Xmx128m",
	                "-cp", "C:/Eclipse/workspace1/FEtranslator1/WebContent/WEB-INF/lib/h2-1.3.148.jar",
	                "org.h2.tools.Server"
	        });
        	for (String str : command) {
				System.out.println(str);
			}
        	
			ProcessBuilder builder = new ProcessBuilder(command );
			builder.redirectErrorStream(true);
			final Process process = builder.start();
			process.getOutputStream();
		    
			new Thread(new Runnable(){

				@Override
				public void run() {
					byte[] buf = new byte[1024];
					InputStream is = process.getInputStream();
					int nr;
					try {
						nr = is.read(buf);
						while (nr != -1)
						{
							System.out.write(buf, 0, nr);
							nr = is.read(buf);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
		    	
		    }).start();
			 */
        	Class.forName("org.h2.Driver");
			Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/mem:db1;DB_CLOSE_DELAY=-1","SA","");
			InputStream in = getClass().getResourceAsStream("script.sql");
			if (in == null) {
				System.out.println("Please add the file script.sql to the classpath, package " + getClass().getPackage().getName());
			} else {
				RunScript.execute(conn, new InputStreamReader(in));
				Statement stat = conn.createStatement();
				ResultSet rs = stat.executeQuery("SELECT TO_CHAR(bday,'DD/MM/yyyy hh24:mi') FROM TEST2");
			 	while (rs.next()) {
					System.out.println(rs.getString(1));
				}
				rs.close();
				stat.close();
				conn.commit();
				conn.close();
			}
			/*Process process = Runtime.getRuntime().exec(new String[] {
	                "C:\\Program Files\\Java\\jdk1.6.0_23\\jre\\bin\\java.exe",
	                "-Xmx128m",
	                "-cp", "WebContent/WEB-INF/lib/h2-1.3.148.jar;",
	                "org.h2.tools.Server"
	        });*/
			 
			
			
			 
			
			try{ 
		           
	                CachedRowSet crs = new DBConnector().executeQuery("select * from  test2"); 
	                while(crs.next()){ 
	                System.out.println("ARGUMENT_NAME:"+crs.getString(1)); 
	               // System.out.println(",DATA_TYPE:"+crs.getString("DATA_TYPE"));       
	                } 
	                crs.close(); 
            }catch(SQLException e){ 
                    e.printStackTrace(); 
            } 
	          
		} catch (Exception e) {
			System.out.println("Exception initializing memory H2 database"+e);
		}
    }
    
}
