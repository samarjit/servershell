package com.ycs.fe;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.opensymphony.xwork2.ActionInvocation;

import com.ycs.fe.crud.SelectOnLoad;
import com.ycs.fe.exception.FrontendException;

public abstract class HTMLProcessor {
	private Logger logger = Logger.getLogger("com.ycs.fe.HTMLProcessor");
	public HTMLProcessor() {
		super();
	}

	protected String fileReadAll(String filename) {
		String str ="";
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(filename));
			String tmp = "";
			while((tmp=bfr.readLine())!= null){
				str +=tmp;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	 
	public void populateValueStack(ActionInvocation invocation, String resultCode){
//	ResultConfig resultConfig = invocation.getProxy().getConfig().getResults().get(resultCode);
//		logger.debug("Result classname = "+resultConfig.getClassName()); 
//		
//		String tplpath = ServletActionContext.getServletContext().getRealPath("WEB-INF/classes/map");
//		String xmlFileName = resultConfig.getParams().get("resultxml");
	 
		String screenName1 = (String) invocation.getInvocationContext().getValueStack().findValue("screenName",String.class);
		String jsonsubmitStr = (String) invocation.getInvocationContext().getValueStack().findValue("submitdata",String.class);
		JSONObject jsonsubmitdata = new JSONObject().getJSONObject(jsonsubmitStr);
//		JSONObject jsonsubmitdata = (JSONObject) invocation.getInvocationContext().getValueStack().findValue("submitdata",JSONObject.class);
		logger.debug("For screenName:"+screenName1);
		 
		try {
			new SelectOnLoad().selectOnLoad(screenName1, jsonsubmitdata);
		} catch (FrontendException e) {
			logger.error("error.selectonload", e);
		} catch (Exception e) {
			logger.error("error.selectonload", e);
		}
		
	}

	public abstract boolean getLastResult();

	public abstract void appendXmlFragment(DocumentBuilder docBuilder, Node parent, NodeList fragment) throws IOException, SAXException;

	public abstract void appendXmlFragment(DocumentBuilder docBuilder, Node parent, String fragment) throws IOException, SAXException;

	public abstract String process(String inputXML, ActionInvocation invocation);

}