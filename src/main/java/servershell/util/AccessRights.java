package servershell.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;

public class AccessRights {

	private static Logger logger = Logger.getLogger(AccessRights.class);

	public static boolean isAccessible(String role, String actionName) {
		Properties prop = new Properties();
		boolean retval = false;
		URL ur = AccessRights.class.getClassLoader().getResource("access.properties");
		try {
			prop.load(new FileInputStream(ur.getFile()));
			
			String val  = prop.getProperty(role);
			if(val.indexOf(actionName) > -1 )retval = true;
			
		} catch (FileNotFoundException e) {
			logger.error(" File not found "+ur);
		} catch (IOException e) {
			logger.error(" IO Exception while reading access.properties ");
		}
		return retval;
	}

	
}
