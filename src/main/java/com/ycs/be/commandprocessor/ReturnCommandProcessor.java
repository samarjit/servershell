package com.ycs.be.commandprocessor;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import com.ycs.be.dto.PageReturnType;
import com.ycs.be.dto.ResultDTO;
import com.ycs.be.exception.FrontendException;
import com.ycs.be.util.ScreenMapRepo;

/**
 * The first command of the dataset is processed to resolve result, all the subsequent rows of data-sets are ignored.
 * The above confusion does not occur when bulkcmd is processed as there is only one command per request.
 * @author Samarjit
 * @version 1.0
 */
public class ReturnCommandProcessor {
	private static Logger logger =   Logger.getLogger(ReturnCommandProcessor.class);
	/**
	 * @param screenName
	 * @param submitdataObj from which the command or bulkcmd will match with &lt;cmd .. result="ajax" /> 
	 * to resolve a return struts name, nextScreenName (if *.page) and result pages  ajax,  *.ftl, *.vm, custom *.page everything else is dispatcher (*.jsp)
	 * @param resDTO if result is coming back from Backend then use that result to override the result of mapping XML
	 * @return PageReturnType object resultName, resultPage, nextScreenName
	 * eg. For ajax result the nextScreenName is not required as there is no chance of calling &lt;onload /> cmd.
	 * &lt;bulkcmd name="frmgridedit" opt="sqlupdate:frmgridedit" result="ajax"/> <br/>
	 * and struts.xml result is like <br/>
	 *  &lt;result name="ajax" type="stream"> <br/>
				&lt;param name="contentType">application/json&lt;/param><br/>
				&lt;param name="inputName">inputStream&lt;/param><br/>
			&lt;/result><br/>
	 * <br/>
	 * eg. If result is some_file.jsp then the parameters in bulkcmd and cmd can be defined like
	 * &lt;bulkcmd name="frmgridedit" opt="sqlupdate:frmgridedit" result="some_file.jsp" resultScrName="SomeOtherScreenName" /> <br/>
	 * This will create resultName=>dispatcher, nextScreenName=>SomeOtherScreenName, resultPage=some_file.jsp <br/>
	 * Action class# execute(){ return resultName; /*dispatcher* / } and struts.xml result will be defined like  <br/>
	 * &lt;result name="dispatcher">${resultPage} -> some_file.jsp &lt;/result>
	 * @throws FrontendException
	 */
	public PageReturnType getReturnType(String screenName, Map<String,Object> submitdataObj, ResultDTO resDTO) throws FrontendException{
		PageReturnType pgReturnType = new PageReturnType();
		
		Element rootXml = ScreenMapRepo.findMapXMLRoot(screenName);
		@SuppressWarnings("unchecked")
		Set<String>  itr =  (submitdataObj).keySet(); 
		pgReturnType.nextScreenName = screenName;
		pgReturnType.resultPage = screenName;
		
		if(submitdataObj == null ||  submitdataObj.isEmpty()){ //onload assume return type is self- changed on nov 3
//			pgReturnType.nextScreenName = screenName;
//			pgReturnType.resultName = screenName;
//			pgReturnType.resultPage = screenName;
			Element elmCmd = (Element) rootXml.selectSingleNode("/root/screen/commands/onload");
    		//System.out.println("/root/screen/commands/onload");
    		String strResult  = elmCmd.attributeValue("result");
    		resolveResult(pgReturnType, strResult);
    		pgReturnType.nextScreenName = screenName;
		}else{
			if(resDTO!= null && resDTO.getResult()!=null && !"".equals(resDTO.getResult()))	{
				String strResultScrName = resDTO.getResultScrName();
	    		pgReturnType.nextScreenName = (strResultScrName != null && !strResultScrName.equals(""))?strResultScrName:screenName;
				resolveResult(pgReturnType, resDTO.getResult());
			}else if(submitdataObj.get("bulkcmd") !=null){
				 String cmd =     (String) submitdataObj.get("bulkcmd"); //String
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
		    	
			    	List<Map<String,Object>> dataSetJobj = (List<Map<String, Object>>) ( submitdataObj).get(dataSetkey); //array
		    	for (Map<String,Object> jsonRecord : dataSetJobj) { //rows in dataset a Good place to insert DB Transaction
			    		String cmd =  (String) ( jsonRecord).get("command"); //string
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
		
		logger.info("Resolved Return Page, nextScreenName="+pgReturnType.nextScreenName+", resultName="+pgReturnType.resultName+", resultPage="+pgReturnType.resultPage);
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
		}else if(strResult.endsWith("page")){
			pgReturnType.resultName = "customXMLRes";
			pgReturnType.resultPage = strResult;
			pgReturnType.nextScreenName = strResult.substring(0,strResult.length() - 5);
		}else if(strResult.endsWith("ftl")){
			pgReturnType.resultName = "freemarker";
			pgReturnType.resultPage = strResult;
//			pgReturnType.nextScreenName = strResult.substring(0,strResult.length() - 4);
		}else if(strResult.endsWith("vm")){
			pgReturnType.resultName = "velocity";
			pgReturnType.resultPage = strResult;
//			pgReturnType.nextScreenName = strResult.substring(0,strResult.length() - 3);
		}else if(strResult.endsWith("jsp")){
			pgReturnType.resultName = "dispatcher";
			pgReturnType.resultPage = strResult;
//			pgReturnType.nextScreenName = strResult.substring(0,strResult.length() - 4);
		}else if(strResult.endsWith("html")){
			pgReturnType.resultName = "dispatcher";
			pgReturnType.resultPage = strResult;
//			pgReturnType.nextScreenName = strResult.substring(0,strResult.length() - 5);
		}else if(strResult.endsWith("htm")){
			pgReturnType.resultName = "dispatcher";
			pgReturnType.resultPage = strResult;
//			pgReturnType.nextScreenName = strResult.substring(0,strResult.length() - 4);
		}else{
//			pgReturnType.resultName = strResult;
//			pgReturnType.resultName = strResult;
//			if(strResult.lastIndexOf('.') >-1){
//			pgReturnType.nextScreenName = strResult.substring(0,strResult.lastIndexOf('.'));
//			}else{
//				pgReturnType.nextScreenName = strResult;
//			}
			logger.error("There is no result type mapping for "+strResult);
		}
		
	}
	
}
