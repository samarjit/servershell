package com.ycs.fe.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.dom4j.Node;

import com.ycs.fe.exception.FrontendException;
import com.ycs.fe.util.ScreenMapRepo;

public class ScreenDetails {
	private static  Logger logger = Logger.getLogger(ScreenDetails.class);
	
public static class FieldElement{
	public String forid;
	public String name;
	public String column;
	public String dbcolsize;
	public String mandatory;
	public String mask;
	public String id;
	public String dbdatatype;
	public String replace;
}
public static class Command{
	public String name;
	public String opt;
	public String instack;
	public String result;
	
}

public static class Label{
	public String key;
	public String value;
	public String replace;
	public String forname;
}

public static class SessionField{
	String key;
	String datatype;
	public SessionField(String key, String datatype) {
		this.key = key;
		this.datatype = datatype;
	}
	
}

	public String screenName;
	public String xmlpath;
	public HashMap<String,Label> nameLabelMap;
	public HashMap<String,String> columnNameMap;
	public HashMap<String,String> nameColumnMap;
	public HashMap<String, FieldElement> foridElementMap;
	public HashMap<String, FieldElement> nameElementMap;
	public HashMap<String, FieldElement> columnElementMap;
	public ArrayList<SessionField> sessionvars;
	public HashMap<String, Command> cmdNameMap;
	public HashMap<String, Command> bulkcmdNameMap;
	public HashMap<String, Command> onloadcmdNameMap;
	public String strSessionVar;
	
	public ScreenDetails populateScrDetails(String scrName) throws FrontendException{
		ScreenDetails scr = null;
		try {
			screenName = scrName;
			xmlpath = ScreenMapRepo.findMapXMLPath(scrName);
			Element root = ScreenMapRepo.findMapXMLRoot(screenName);
			@SuppressWarnings("unchecked")
			List<Element> elm =   root.selectNodes("/root/panels/panel/fields/field/*"); 
			nameLabelMap = new HashMap<String, Label>();
			columnNameMap = new HashMap<String, String>();
			nameColumnMap = new HashMap<String, String>();
			foridElementMap = new HashMap<String, ScreenDetails.FieldElement>();
			nameElementMap = new HashMap<String, ScreenDetails.FieldElement>();
			columnElementMap = new HashMap<String, ScreenDetails.FieldElement>();
			sessionvars = new ArrayList<SessionField>();
			cmdNameMap = new HashMap<String, ScreenDetails.Command>();
			
			for (Element element : elm) {
				String replace = element.attributeValue("replace");
				String tagname = element.getName();
				if(tagname.equals("label")){
					Label label = new Label();
					String forname = element.attributeValue("forname");
					label.key = element.attributeValue("key");
					label.value = element.attributeValue("value");
					label.replace = replace;
					label.forname = forname;
					nameLabelMap.put(forname,label);
				}else{
					String column = element.attributeValue("column");
					String id = element.attributeValue("id");
					String name = element.attributeValue("name");
					String forid = element.attributeValue("forid");
					columnNameMap.put(column, name);
					nameColumnMap.put(name, column);
					FieldElement fieldelm = new FieldElement();
					fieldelm.forid = forid;     
					fieldelm.name = name;      
					fieldelm.column = column;
					fieldelm.dbcolsize = element.attributeValue("dbcolsize"); 
					fieldelm.mandatory  = element.attributeValue("mandatory");
					fieldelm.mask  = element.attributeValue("mask");      
					fieldelm.id = id;        
					fieldelm.dbdatatype  = element.attributeValue("dbdatatype");
					fieldelm.replace  = element.attributeValue("replace");   
					foridElementMap.put(forid, fieldelm);
					nameElementMap.put(name, fieldelm);
					columnElementMap.put(column, fieldelm);
					
				}
				
				
			}
			
			Node sessionVar = root.selectSingleNode("/root/screen/sessionvars");
			   if(sessionVar != null){
				   strSessionVar = sessionVar.getText();
					if(strSessionVar != null || !"".equals(strSessionVar)){
						String[] arSessionVar = strSessionVar.split(",");
						if(arSessionVar.length >0){
							for (String sessVariable : arSessionVar) {
								String[] sessionField = sessVariable.trim().split("\\|");
								String datatype = null;
								if(sessionField.length >1){
									datatype = sessionField[1];
								}
								sessionvars.add(new SessionField(sessionField[0], datatype));
							}
						}
					}
				  }
			   
			@SuppressWarnings("unchecked")
			List<Element> cmd =   root.selectNodes("/root/screen/commands/*");
			for (Element element : cmd) {
				Command command = new Command();
				command.instack = element.attributeValue("instack");
				command.name = element.attributeValue("name");
				command.opt = element.attributeValue("opt");
				command.result = element.attributeValue("result");
				cmdNameMap = new HashMap<String, ScreenDetails.Command>();
				bulkcmdNameMap = new HashMap<String, ScreenDetails.Command>();
				onloadcmdNameMap = new HashMap<String, ScreenDetails.Command>();
				
				if(element.getName().equals("cmd")){
					cmdNameMap.put(element.attributeValue("name") , command);
				}else if(element.getName().equals("bulkcmd")){
					bulkcmdNameMap.put(element.attributeValue("name"), command);
				}else if(element.getName().equals("onload")){
					onloadcmdNameMap.put(element.attributeValue("name"), command);
				}
			}
			
		} catch (FrontendException e) {
			logger.error("XML Load Exception   ScreenName="+scrName);
			throw new FrontendException("error.loadxml",e);
		}
		
		
		  
		  return scr;
	}
	
	public String getCmdProcessor(String optPart){
		if(optPart.equals("proc"))return "anyprocs";
		if(optPart.equals("buslogic"))return "bl";
		if(optPart.equals("txnproc"))return "dm";
		if(optPart.equals("jsonrpc"))return "crud";
		if(optPart.equals("selectonload"))return "crud";
		if(optPart.equals("sqlselect"))return "crud";
		if(optPart.equals("sqlinsert"))return "crud";
		if(optPart.equals("sqldelete"))return "crud";
		if(optPart.equals("sqlupdate"))return "crud";
		return null;
	}
	
}
