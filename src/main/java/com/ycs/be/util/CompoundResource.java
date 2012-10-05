package com.ycs.be.util;

import java.io.StringReader;
import java.io.StringWriter;
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
			Template t = new Template("name", new StringReader(rb.getString(key)), new Configuration());
			StringWriter out = new StringWriter();
			t.process(rb, out );
			ret = out.toString();
		} catch (Exception e) {
			logger.error(e);
			ret = rb.getString(key);
		}
		return ret;
	}

}
