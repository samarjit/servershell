package com.ycs.fe.dto;

/**
 * @author Samarjit
 * resultName is struts result name
 * resultPage is the filename eg. *.html, *.htm, *.ftl or *.jsp or *.page (for custom result type)
 */
public class PageReturnType {
	/**
	 * can be the actual file name like *.html, *.htm, *.ftl or *.jsp or *.page
	 */
	public String resultPage;
	/**
	 * can be freemarker dispatcher or velocity or customXMLRes
	 */
	public  String resultName;
	
	/**
	 * This will contain the logical screenName where the result will be forwarded to 
	 */
	public String nextScreenName;
}
