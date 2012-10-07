package com.ycs.be.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class CompoundResource {
	private static Logger logger = Logger.getLogger(CompoundResource.class);
 
	 
	/**
	 * @param rb
	 * @param key
	 * @return
	 */
	public static String getString(ResourceBundle rb, String key) {
		String ret = null;
		try {
//			Configuration c = new Configuration();
//			c.setObjectWrapper(new DefaultObjectWrapper());
//			c.setSharedVariable("USER_HOME", "config.fmpp");
			
			//c.
			Template t = new Template("name", new StringReader(rb.getString(key)), new Configuration());
			StringWriter out = new StringWriter();
			Map<String,String> ar = new HashMap<String,String>();
			for (String str: rb.keySet()) {
				ar.put(str, rb.getString(str));
			}
			ar.put("USER_HOME", System.getProperty("user.home").replace("\\","/"));
			t.process(ar, out );
			ret = out.toString();
		} catch (Exception e) {
			logger.error(" unknown exception in getString() ",e);
			ret = rb.getString(key);
		}
		return ret;
	}

}
