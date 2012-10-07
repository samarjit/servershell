package com.ycs.fe.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Element;
import org.dom4j.Node;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.LocaleProvider;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.TextProviderFactory;
import com.ycs.fe.dto.PrepstmtDTO;
import com.ycs.fe.dto.PrepstmtDTO.DataType;
import com.ycs.fe.dto.ResultDTO;
import com.ycs.fe.exception.DataTypeException;
import com.ycs.fe.exception.FrontendException;
import com.ycs.fe.exception.ValidationException;

public class FEValidator  implements LocaleProvider{

	private static Logger logger = Logger.getLogger(FEValidator.class);
	private TextProvider textProvider;
	private ResultDTO resultDTO = null;
	public ResultDTO validate(String screenName, JSONObject submitdataObj) throws ValidationException{
		try{
		Element rootElm = ScreenMapRepo.findMapXMLRoot(screenName);
		
		Map<String,Object> s = (Map<String,Object>)submitdataObj;
		for (Entry<String, Object> itr : s.entrySet()) { //form1, form2 ...skip txnrec, sessionvars, bulkcmd
			if(itr.getKey().equals("sessionvars")){
				//These will be most likely have not yet been set, validate directly from sessoin variable
				JSONObject sessvars = (JSONObject) itr.getValue();
				System.out.println("Need to validate these:"+ sessvars);
			}else if(itr.getKey().equals("bulkcmd")){
				//do nothing
			}else if(itr.getKey().equals("txnrec")){
				JSONObject txnrec = (JSONObject) itr.getValue();
				JSONObject singlerec = (JSONObject) txnrec.getJSONObject("single");
				for (Iterator keyitr = singlerec.keys(); keyitr.hasNext();) {
					String keystr = (String) keyitr.next();
					validateNode(screenName, rootElm, singlerec, keystr, null);
				} 
				JSONArray armultirec = txnrec.getJSONArray("multiple");
				for (Iterator iterator = armultirec.iterator(); iterator.hasNext();) { //rows
					JSONObject joMulti = (JSONObject) iterator.next(); //each row
					for (Iterator keyitr = joMulti.keys(); keyitr.hasNext();) {
						String keystr = (String) keyitr.next();
						validateNode(screenName, rootElm, joMulti, keystr, null);
					} 
				}  
			}else{ //form1,form2 ... data vaalidation
				JSONArray rows =  (JSONArray) itr.getValue();
				for (Iterator iterator = rows.iterator(); iterator.hasNext();) { //rows
					JSONObject joMulti = (JSONObject) iterator.next(); //each row
					for (Iterator keyitr = joMulti.keys(); keyitr.hasNext();) {
						String keystr = (String) keyitr.next();
						validateNode(screenName, rootElm, joMulti, keystr, null);
					} 
				}
			}
		}
		
		validateSessionVariables(screenName);
		}catch(FrontendException e){
			throw new ValidationException("error.validationfailed",e);
		}
		return resultDTO;
	}

	private void validateSessionVariables(String screenName) throws ValidationException{
		try{
		Element rootElm = ScreenMapRepo.findMapXMLRoot(screenName);
		Node sessionVar = rootElm.selectSingleNode("/root/screen/sessionvars");
		   if(sessionVar != null){
			String strSessionVar = sessionVar.getText();
			Map<String, String> sessionMap = new HashMap<String,String>();
			if(strSessionVar != null || !"".equals(strSessionVar)){
				String[] arSessionVar = strSessionVar.split(",");
				if(arSessionVar.length >0){
					for (String sessVariable : arSessionVar) {
							String[] sessionField = sessVariable.trim().split("\\|");
						String sessionData = "";
						if(sessionField.length >1){
							//datatype is defined and it is required
								sessionData = (String) ServletActionContext.getContext().getSession().get(sessionField[0]);
								System.out.println("sessionData:"+sessionData);
//							if(sessionField[1].equals("INT")){
//								sessionData.matches("0-9");
//							}
							JSONObject sessionJson = new JSONObject();
							sessionJson.put(sessionField[0], sessionData);
							validateNode(screenName, rootElm, sessionJson, sessionField[0], sessionField[1]);
						}
						sessionMap.put(sessionField[0], sessionData);
					}
				}
			}
		   }
		}  catch(FrontendException e){
			throw new ValidationException("error.validationsessionvar",e);
		}
	}

	private void validateNode(String screenName, Element rootElm,
			JSONObject singlerec, String keystr, String overrideDatatype) {
		Element fieldNode = (Element) rootElm.selectSingleNode("/root/panels/panel/fields/field/*[@name='"+keystr+"']");
		if (fieldNode != null) {
			String strdbdatatype = fieldNode.attributeValue("dbdatatype");
			String strdbcolsize = fieldNode.attributeValue("dbcolsize");
			String strmandatory = fieldNode.attributeValue("mandatory");
			String fieldName = fieldNode.attributeValue("name");
			String strLabel = LabelFactory.INSTANCE.getLabel(screenName, fieldName );
			
			int colsize = -1;
			
			if(overrideDatatype != null && !"".equals(overrideDatatype)){
				strdbdatatype = overrideDatatype;
			}
			
			if (strdbcolsize != null && !"".equals(strdbcolsize)) {
				colsize = Integer.parseInt(strdbcolsize);
			}
			try {
				DataType dbdatatype = PrepstmtDTO
						.getDataTypeFrmStr(strdbdatatype);
				if (dbdatatype == DataType.STRING) {
					System.out.println("Validating string.....");
					addError("error.numberformat", keystr, strLabel,
							singlerec.getString(keystr));
					// filtering criterion required?
				} else if (dbdatatype == DataType.INT) {
					try {
						Integer.parseInt(singlerec.getString(keystr));
					} catch (NumberFormatException e) {
						addError("error.numberformat", keystr, strLabel,
								singlerec.getString(keystr));
					}
				} else if (dbdatatype == DataType.FLOAT) {
					try {
						Float.parseFloat(singlerec.getString(keystr));
					} catch (NumberFormatException e) {
						addError("error.float", keystr, strLabel,
								singlerec.getString(keystr));
					}
				} else if (dbdatatype == DataType.DOUBLE) {
					try {
						if (singlerec.getString(keystr) != null)
							Double.parseDouble(singlerec.getString(keystr));
					} catch (NumberFormatException e) {
						addError("error.double", keystr, strLabel,
								singlerec.getString(keystr));
					}
				} else if (dbdatatype == DataType.DATEDDMMYYYY) {
					try {
						if (singlerec.getString(keystr) != null)
							new SimpleDateFormat("DD/MM/yyyy").parse(singlerec
									.getString(keystr));
					} catch (ParseException e) {
						addError("error.dateDDMMyyyy", keystr, strLabel,
								singlerec.getString(keystr));
					}
				} else if (dbdatatype == DataType.DATE_NS) {
					try {
						if (singlerec.getString(keystr) != null)
							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
									.parse(singlerec.getString(keystr));
					} catch (ParseException e) {
						addError("error.date_ns", keystr, strLabel,
								singlerec.getString(keystr));
					}
				} else if (dbdatatype == DataType.TIMESTAMP) {
					try {
						if (singlerec.getString(keystr) != null)
							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
									.parse(singlerec.getString(keystr));
					} catch (ParseException e) {
						addError("error.timestamp", keystr, strLabel,
								singlerec.getString(keystr));
					}
				}
			} catch (DataTypeException e) {
				logger.error("datatype undefined", e);
				addError("error.datatypeundefined", keystr, strLabel,
						singlerec.getString(keystr));
			}
			if (strmandatory != null
					&& ("yes".equals(strmandatory) || "true"
							.equals(strmandatory))) {
				if (singlerec.getString(keystr).length() < 1) {
					addError("error.mandatory", keystr,	strLabel, singlerec.getString(keystr));
				}
			}
			if (colsize != -1 && singlerec.getString(keystr).length() > colsize) {
				addError("error.colsize", keystr, strLabel, singlerec.getString(keystr));
			}
		}//field not is not defined for this key like 'command' 
		else{
			logger .debug("xml not defined for key="+keystr);
		}
	}
	
	private void addError(String messageKey, String fieldName, String fieldLabel, String fieldValue) {
		if(fieldLabel != null && !"".equals(fieldLabel))fieldName = fieldLabel;
		
		String [] str2 = new String[]{ fieldName, fieldValue };
		logger.error("validation :"+getTextProvider().getText(messageKey,str2 ));
		
		if(resultDTO == null)resultDTO = new ResultDTO();
		
		resultDTO.addFieldError(fieldName, getTextProvider().getText(messageKey,str2 ));
	}

	public String createJSRule(String screenName ){
		String ruleJson = "";
		String fieldGroupRules = "";
		String fieldGroupMsg  = "";
		boolean fieldRulesFirst = true;
		try{
			Element rootElm = ScreenMapRepo.findMapXMLRoot(screenName);
			List<Element> fieldNodeList =   rootElm.selectNodes("/root/panels/panel/fields/field/*[@name]");
			for (Element fieldNode : fieldNodeList) {
				
			
				if (fieldNode != null) {
					String strdbdatatype = fieldNode.attributeValue("dbdatatype");
					String strdbcolsize = fieldNode.attributeValue("dbcolsize");
					String strmandatory = fieldNode.attributeValue("mandatory");
					String fieldName = fieldNode.attributeValue("name");
					String strLabel = LabelFactory.INSTANCE.getLabel(screenName, fieldName );
					int colsize = -1;
					if (strdbcolsize != null && !"".equals(strdbcolsize)) {
						colsize = Integer.parseInt(strdbcolsize);
					}
					JSONObject rules = new JSONObject();
					JSONObject messages = new JSONObject();
					JSONObject field = new JSONObject();
					
					String fldRuleStr = "";
					String fldMsgStr = "";
					boolean first1 =  true;
					if (strmandatory != null
							&& ("yes".equals(strmandatory) || "true"
									.equals(strmandatory))) {
						field.put("required", true);
						if(!first1){fldRuleStr += ","; fldMsgStr +=","; }first1 = false;
						fldRuleStr ="required: true";
						fldMsgStr = "required: '"+strLabel+" is required'";
					}
					if(strdbcolsize != null && !"".equals(strdbcolsize)){
						if(!first1){fldRuleStr += ","; fldMsgStr +=","; }first1 = false;
						fldRuleStr += "maxlength:"+strdbcolsize;
						fldMsgStr += "maxlength: 'The length of "+strLabel+" should be less than {0}'";
					}
					if(strdbdatatype != null && !"".equals(strdbdatatype)){
						DataType type = PrepstmtDTO.getDataTypeFrmStr(strdbdatatype);
						switch(type){
					  	case INT: 
					  	case LONG:
					  		if(!first1){fldRuleStr += ","; fldMsgStr +=","; }first1 = false;
					  			fldRuleStr += "integer:true";
					  			fldMsgStr += "integer: '"+strLabel+" must be integer'";break;
					  	case FLOAT: 
					  	case DOUBLE:
					  		if(!first1){fldRuleStr += ","; fldMsgStr +=","; }first1 = false;
					  			fldRuleStr += "number:true";
			  					fldMsgStr += "number: '"+strLabel+" must be decimal'";break;
					  	case DATEDDMMYYYY:
					  		if(!first1){fldRuleStr += ","; fldMsgStr +=","; }first1 = false;
						  		fldRuleStr += "dateITA:true";
			  					fldMsgStr += "dateITA: '"+strLabel+" must be date'";break;
					  	case DATE_NS:
					  	case TIMESTAMP:
					  		if(!first1){fldRuleStr += ","; fldMsgStr +=","; }first1 = false;
						  		fldRuleStr += "date:true";
			  					fldMsgStr += "number: '"+strLabel+" must be date'";break;
						case STRING:
							default:
						}
					}
					if(fldRuleStr.length() > 1){
						if(!fieldRulesFirst){
						fieldGroupRules += ",";
						fieldGroupMsg += ",";
						}
						fieldRulesFirst = false;
						fieldGroupRules += ""+fieldName+":{"+fldRuleStr+"}";
						fieldGroupMsg += ""+fieldName+":{"+fldMsgStr+"}";
					}
					
				}
			}
			if(fieldGroupRules.length() > 1){
				ruleJson += "{rules: {"+fieldGroupRules+"}, messages: {"+fieldGroupMsg+"}}";
			}
		} catch (FrontendException e) {
			logger.error("error.xmlfileaccess",e);
		} catch (Exception e) {
			logger.error("error.unknown",e);
		}
		return ruleJson;
	}
	
	private TextProvider getTextProvider()
	{
	  if (this.textProvider == null) {
	    TextProviderFactory tpf = new TextProviderFactory();
	     
	    this.textProvider = tpf.createInstance(super.getClass(), this);
	  }
	  return   this.textProvider;
	}

	@Override
	public Locale getLocale() {
		 ActionContext ctx = ActionContext.getContext();
		  if (ctx != null) {
		    return ctx.getLocale();
		  }
		  System.out.println("Action context not initialized");
		  return null;
	}
	
	public static void main(String[]  args){
		new FEValidator().createJSRule("ProgramSetup");
		
	}

}
