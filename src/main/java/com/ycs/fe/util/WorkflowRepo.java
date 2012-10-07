package com.ycs.fe.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

public class WorkflowRepo {
	private static HashMap<String,String> workflowlocationcache = null;
	private static  Logger logger = Logger.getLogger(WorkflowRepo.class);
	private WorkflowRepo instance;
	
	static{
		
	}
	
	private WorkflowRepo(){}
	
	/**
	 * This function parses the given file.
	 */
	private void init(){
		InputStream  wffis =  WorkflowRepo.class.getResourceAsStream("/repo/workflow/workflowfactory.xml");
		Document doc = null;
//		propertyset = new HashMap();
		workflowlocationcache = new HashMap<String, String>();
		try {
			doc = new SAXReader().read(wffis);
			
		 	  List<Element> nl = doc.selectNodes("workflow");
	          for(int i = 0 ;i<nl.size();i++){
	        	  Node node = nl.get(i);
	        	  if(node.getNodeType() == Document.ELEMENT_NODE){
	        		   Element elm = (Element) node;
	        		   String key = elm.attributeValue("key");
	        		   String val = elm.attributeValue("value");
	        		   workflowlocationcache.put(key	, val);
	        	  }
	          }
	  
		} catch (DocumentException e) {
			logger.debug("Document read Exception",e);
			e.printStackTrace();
		}  
		
		
	}
	
	public Element getWorkflow(String workflowName){
		String workflowFile = 	WorkflowRepo.workflowlocationcache.get(workflowName);
		Element retElm= null;
		try {
			InputStream wfis = WorkflowRepo.class.getResourceAsStream(workflowFile);
			if(wfis == null) throw new IOException("File not found");
			Document doc1 = new SAXReader().read(wfis);
			retElm = doc1.getRootElement();
		}catch(Exception e){
			logger.debug("Document read Exception",e);
		}
		return retElm;
	}
	
}
