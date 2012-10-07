package com.ycs.fe.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import ognl.Ognl;
import ognl.OgnlException;

import org.apache.log4j.Logger;

import com.ycs.fe.exception.SentenceParseException;

public class ParseSentenceOgnl {
	private static Logger logger = Logger.getLogger(ParseSentenceOgnl.class);

	public static String parse(String sentence, Object root) throws SentenceParseException {
		String parsedresult = "";
		try {
			String PATTERN = "\\:(inp|res|vs)\\.?([^,'\\s\\|]*)\\|?([^,'\\s]*)";// "\\:(\\w*)\\[?(\\d*)\\]?\\.?([^,\\s\\|]*)\\|?([^,\\s]*)";

			Pattern pattern = Pattern.compile(PATTERN, Pattern.DOTALL | Pattern.MULTILINE);

			logger.debug("Input Query:" + sentence + " \nlength:" + sentence.length());
			logger.debug("Root Object =" + root);
			logger.debug("PATTERN=" + PATTERN);

			Matcher m1 = pattern.matcher(sentence); // get a matcher object
			int count = 0;
			int prevend = 0;

			while (m1.find()) {

				String prop = m1.group();
				logger.debug("Start preparing '" + prop + "' start=" + m1.start() + " end=" + m1.end() + " grp1=" + m1.group(1) + " grp2="
						+ m1.group(2) + " grp3=" + m1.group(3) + " ");
				// do ognl because (inp|res|vs)is not ""

				if (m1.group(1) != null && !"".equals(m1.group(1))) {
					if ("inp".equals(m1.group(1))) { // :form[0].param ===  :param use jsonObject and get group(3) val
						logger.debug(" Processing with #inputDTO");
						String expr = m1.group(2);
						Object pval =  Ognl.getValue(expr, root);
						String propval = "";
						if(pval instanceof String ){
							 propval = (String)pval;
						}else if(pval instanceof JSONArray){
							propval = pval.toString();
						}else if (pval instanceof JSONObject){
							propval = pval.toString();
						}
						parsedresult += sentence.substring(prevend, m1.start());//
						parsedresult += propval;

					/*} else if ("res".equals(m1.group(1))) { // :formXX[0].param
						logger.debug(" Processing with #resultDTO");
						String expr = m1.group(2);
						String propval = (String) Ognl.getValue(expr, root);
						parsedresult += sentence.substring(prevend, m1.start());//
						parsedresult += propval;
					} else if ("vs".equals(m1.group(1))) {
						logger.debug(" Processing with ValueStack");
						String expr = m1.group(2);
						String propval = (String) Ognl.getValue(expr, root);
						logger.debug("Ognl Expression result=" + propval);
						parsedresult += "?";*/
					} else {
						// adding expression for easy error debugging
						parsedresult += sentence.substring(prevend, m1.start());
						parsedresult += m1.group() + "[ERROR]";
						logger.fatal("BUG BUG Unsupported Ognl expression in Sentence (:res,:vs) are not supported now!" + m1.group());
						throw new SentenceParseException(parsedresult);
					}
				} else { // fill with present panel row object :formxparam
					logger.debug(" Processing without ValueStack property=" + m1.group(2));
					String propval;

					propval = (String) Ognl.getValue(m1.group(2), root);
					parsedresult += sentence.substring(prevend, m1.start());//
					parsedresult += propval;

					logger.debug("else no dot " + m1.group(2));
				}

				prevend = m1.end();
				count++;
			}
			logger.debug("Last part end=" + prevend + " Query part to append:" + sentence.substring(prevend));
			parsedresult += sentence.substring(prevend);

		} catch (OgnlException e) {
			e.printStackTrace();
			throw new SentenceParseException("OgnlException...",e); //This line was removed in a later version, why?
		}

		return parsedresult;
	}
}
