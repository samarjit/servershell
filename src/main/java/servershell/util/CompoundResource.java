package servershell.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class CompoundResource {
	private static Logger logger = LoggerFactory.getLogger(CompoundResource.class);
 
	 
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
			logger.error(" unknown exception in getString() ",e);
			ret = rb.getString(key);
		}
		return ret;
	}

}
