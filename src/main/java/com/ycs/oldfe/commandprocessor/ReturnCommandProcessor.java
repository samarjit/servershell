package com.ycs.oldfe.commandprocessor;

import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import servershell.be.dto.PageReturnType;
import servershell.be.dto.ResultDTO;

/**
 * The first command of the dataset is processed to resolve result, all the subsequent rows of data-sets are ignored.
 * The above confusion does not occur when bulkcmd is processed as there is only one command per request.
 * @author Samarjit
 * @version 1.0
 */
public class ReturnCommandProcessor {
	private static Logger logger =   Logger.getLogger(ReturnCommandProcessor.class);
	/**
	 * @param screenName used to search the xml for bulkcmd and onload
	 * nextScreenName will be assigned to screenName after returning to action class. This will be used only if 
	 *  decorator Interceptor (pre-result)-> populateValueStack() for next page will get called. It has nothing to do with cusotm XML result
     *
	 * @param submitdataObj from which the command or bulkcmd will match with &lt;cmd .. result="ajax" /> 
	 * to resolve a return struts name, nextScreenName (if *.page) and result pages  ajax,  *.ftl, *.vm, custom *.page everything else is dispatcher (*.jsp)
	 * @param resDTO if result is coming back from Backend then use that result to override the result of mapping XML
	 * @return PageReturnType object
	 * @throws FrontendException
	 */
	public PageReturnType getReturnType(String screenName, JSONObject submitdataObj, ResultDTO resDTO) throws FrontendException{
		PageReturnType pgReturnType = new PageReturnType();
		
		Element rootXml = new ScreenMapRepo().findMapXMLRoot(screenName);
		@SuppressWarnings("unchecked")
		Set<String>  itr =  ( (JSONObject) submitdataObj).keySet(); 
		pgReturnType.nextScreenName = screenName;
		pgReturnType.resultPage = screenName;
		
		if(submitdataObj == null ||  submitdataObj.isNullObject()){ //onload assume return type is self- changed on nov 3
//			pgReturnType.nextScreenName = screenName;
//			pgReturnType.resultName = screenName;
//			pgReturnType.resultPage = screenName;
			Element elmCmd = (Element) rootXml.selectSingleNode("/root/screen/commands/onload");
    		System.out.println("/root/screen/commands/onload");
    		String strResult  = elmCmd.attributeValue("result");
    		resolveResult(pgReturnType, strResult);
    		pgReturnType.nextScreenName = screenName;
		}else{
			if(resDTO!= null && resDTO.getResult()!=null && !"".equals(resDTO.getResult()))	{
				String strResultScrName = resDTO.getResultScrName();
	    		pgReturnType.nextScreenName = (strResultScrName != null && !strResultScrName.equals(""))?strResultScrName:screenName;
				resolveResult(pgReturnType, resDTO.getResult());
			}else if(submitdataObj.get("bulkcmd") !=null){
				 String cmd =     submitdataObj.getString("bulkcmd");
		    		Element elmCmd = (Element) rootXml.selectSingleNode("/root/screen/commands/bulkcmd[@name='"+cmd+"' ] ");
		    		System.out.println("/root/screen/commands/bulkcmd[@name='"+cmd+"' ] ");
		    		String strResult  = elmCmd.attributeValue("result");
		    		String strResultScrName = elmCmd.attributeValue("resultScrName");
		    		pgReturnType.nextScreenName = (strResultScrName != null && !strResultScrName.equals(""))?strResultScrName:screenName;
		    		resolveResult(pgReturnType, strResult);
			 } else {
				for (String dataSetkey : itr) { //form1, form2 ...skip txnrec,sessionvars
			    	//txnrec & sessionvars is just a group of data not a processing command
	//		    	if( dataSetkey.equals("txnrec")   || dataSetkey.equals("sessionvars")||  dataSetkey.equals("pagination"))continue;
					if(! dataSetkey.startsWith("form"))continue;
					
			    	JSONArray dataSetJobj = ((JSONObject) submitdataObj).getJSONArray(dataSetkey);
			    	for (Object jsonRecord : dataSetJobj) { //rows in dataset a Good place to insert DB Transaction
			    		String cmd = ((JSONObject) jsonRecord).getString("command");
			    		if(cmd !=null && !"".equals(cmd)){
				    		Element elmCmd = (Element) rootXml.selectSingleNode("/root/screen/commands/cmd[@name='"+cmd+"' ] ");
				    		System.out.println("/root/screen/commands/cmd[@name='"+cmd+"' ] ");
				    		String strResult  = elmCmd.attributeValue("result");
				    		String strResultScrName = elmCmd.attributeValue("resultScrName");
				    		pgReturnType.nextScreenName = (strResultScrName != null && !strResultScrName.equals(""))?strResultScrName:screenName;
				    		resolveResult(pgReturnType, strResult);
				    		break;
			    		}
			    	}
			    }
			 }
		}
		
		if(!(pgReturnType.resultName != null && !"".equals(pgReturnType.resultName)))
				logger.error("The Result could not be resolved");
		
		return pgReturnType;
	}
	
	/**
	 * @param pgReturnType result is returned by reference
	 * @param strResult is used to resolve result ajax,  *.ftl, *.vm, custom *.page *.jsp,*.html, *.htm everything else will need explicit 
	 * result mapping. The returned valuex will be treated as <result name=valuex/>.  
	 */
	public void resolveResult(PageReturnType pgReturnType, String strResult){
		if("".equals(strResult) || "ajax".equals(strResult)){
			pgReturnType.resultName = strResult;
		}else if(strResult.endsWith(".page")){
			pgReturnType.resultName = "customXMLRes";
			pgReturnType.resultPage = strResult;
			pgReturnType.nextScreenName = strResult.substring(0,strResult.length() - 5);
		}else if(strResult.endsWith(".ftl")){
			pgReturnType.resultName = "freemarker";
			pgReturnType.resultPage = strResult;
//			pgReturnType.nextScreenName = strResult.substring(0,strResult.length() - 4);
		}else if(strResult.endsWith(".vm")){
			pgReturnType.resultName = "velocity";
			pgReturnType.resultPage = strResult;
//			pgReturnType.nextScreenName = strResult.substring(0,strResult.length() - 3);
		}else if(strResult.endsWith(".jsp")){
			pgReturnType.resultName = "dispatcher";
			pgReturnType.resultPage = strResult;
//			pgReturnType.nextScreenName = strResult.substring(0,strResult.length() - 4);
		}else if(strResult.endsWith(".html")){
			pgReturnType.resultName = "dispatcher";
			pgReturnType.resultPage = strResult;
//			pgReturnType.nextScreenName = strResult.substring(0,strResult.length() - 5);
		}else if(strResult.endsWith(".htm")){
			pgReturnType.resultName = "dispatcher";
			pgReturnType.resultPage = strResult;
//			pgReturnType.nextScreenName = strResult.substring(0,strResult.length() - 4);
		}else{
			pgReturnType.resultName = strResult;
			pgReturnType.resultPage = strResult;
//			if(strResult.lastIndexOf('.') >-1){
//			pgReturnType.nextScreenName = strResult.substring(0,strResult.lastIndexOf('.'));
//			}else{
//				pgReturnType.nextScreenName = strResult;
//			}
			logger.error("Make sure result is defined in struts.xml for "+strResult);
		}
	}
	
}
