package com.ycs.oldfe.commandprocessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Element;
import org.dom4j.Node;

import servershell.be.dao.BackendException;
import servershell.be.dao.FETranslatorDAO;
import servershell.be.dto.InputDTO;
import servershell.be.dto.ResultDTO;

/**
 * Used for prepopulating data onto value stack for. It can be called from any Action class or Interceptor. 
 * @author Samarjit
 *
 */
public class SelectOnLoad {
	private Logger logger = Logger.getLogger(this.getClass());
	
	public void selectOnLoad(String screenName1, JSONObject jsonsubmitdata ) throws FrontendException{
		
			if (Constants.APP_LAYER == Constants.FRONTEND) {
			try {
				Element rootXml = new ScreenMapRepo().findMapXMLRoot(screenName1);
				Node sessionVar = rootXml
						.selectSingleNode("/root/screen/sessionvars");
				if (sessionVar != null) {
					String strSessionVar = sessionVar.getText();
					Map<String, String> sessionMap = new HashMap<String, String>();
					if (strSessionVar != null || !"".equals(strSessionVar)) {
						String[] arSessionVar = strSessionVar.split(",");
						if (arSessionVar.length > 0) {
							for (String sessVariable : arSessionVar) {
								String[] sessionField = sessVariable.trim()
										.split("\\|");
								String sessionData = "";
								if (sessionField.length > 1) {
									//datatype is defined and it is required
									sessionData = (String) ServletActionContext
											.getContext().getSession()
											.get(sessionField[0]);
									System.out.println("sessionData:"
											+ sessionData);
									if (sessionField[1].equals("INT")) {
										sessionData.matches("0-9");
										//TODO some data validation

									}
								}
								sessionMap.put(sessionField[0], sessionData);
							}
						}
					}
					if(jsonsubmitdata == null || jsonsubmitdata.isNullObject())jsonsubmitdata = new JSONObject();
					logger.debug("output session data:"+JSONObject.fromObject(sessionMap));
					jsonsubmitdata.put("sessionvars", JSONObject.fromObject(sessionMap));
				}

			} catch (FrontendException e) {
				throw new FrontendException("error.selectOnloadFailed");
			}
		}
		if(Constants.CMD_PROCESSOR == Constants.APP_LAYER){
			localSelectOnLoad(   screenName1,   jsonsubmitdata );
		}else{
			remoteSelectOnLoad(  screenName1,   jsonsubmitdata.toString() );
		}
		
	}
	
	public void localSelectOnLoad( String screenName1, JSONObject jsonsubmitdata ) throws FrontendException {
		if(screenName1 != null && screenName1.length() >0)	{
			try {
//				String xmlconfigfile =  ScreenMapRepo.findMapXMLPath(screenName1);
//				org.dom4j.Document document1 = new SAXReader().read(xmlconfigfile);
//				org.dom4j.Element root = document1.getRootElement();
				Element root = new ScreenMapRepo().findMapXMLRoot(screenName1);
				HashMap<String, Object> adhocstackids = new HashMap<String, Object>();
				List<String> outstackList = new ArrayList<String>();
				//preload select queries
				List nodeList = root.selectNodes("//query");
				logger.debug("query list size:"+nodeList.size());
				for (Iterator queryList = nodeList.iterator(); queryList.hasNext();) {
					org.dom4j.Node node = (org.dom4j.Node) queryList.next();
					String stackid = ((org.dom4j.Element) node).attributeValue("stackid");
					logger.debug("Query Node:"+node.getText()+" stackid:"+stackid);
					String type = ((org.dom4j.Element) node).attributeValue("type");
					String sqlquery = node.getText();
					FETranslatorDAO feDAO = new FETranslatorDAO();
					feDAO.executequery(sqlquery,stackid,type); //outputs in different stack ids
					org.dom4j.Element e = (org.dom4j.Element) node;
					
					if(stackid != null && !"".equals(stackid))
						outstackList.add(stackid);
				}
				//preload selectonload queries
			/*	List selonloadnl = root.selectNodes("//selectonload");
				Element elm = (Element) root.selectSingleNode("/root/screen");
				String screenName = elm.attributeValue("name");
				logger.debug("query selectonload list size:"+selonloadnl.size());*/
				
				ResultDTO resDTO = new ResultDTO();
				for (String vstackkey : outstackList) {
					ResultDTO tempDTO = new ResultDTO();
					HashMap<String,Object> valueStack = new HashMap<String, Object>();
					valueStack.put(vstackkey, ServletActionContext.getContext().getValueStack().getContext().get(vstackkey));
					tempDTO.setData(valueStack);
					resDTO.merge(tempDTO);
				}
				
				/////command onload ////
				Element onloadElm = (Element) root.selectSingleNode("/root/screen/commands/onload");
				ResultDTO resultDTO = new ResultDTO();
				if (onloadElm != null) {
					String commandChain = onloadElm.attributeValue("opt");
					String[] opts = commandChain.split("\\|");
					InputDTO inputDTO = new InputDTO();
					inputDTO.setData(jsonsubmitdata);
					for (String opt : opts) {
						String[] sqlcmd = opt.split("\\:"); // get Id of query
						String querynodeXpath = sqlcmd[0] + "[@id='" + sqlcmd[1] + "']"; // Query node xpath
						Element processorElm = (Element) root.selectSingleNode("/root/screen/*/" + querynodeXpath + " ");
						String strProcessor = processorElm.getParent().getName();
						String outstack = processorElm.attributeValue("outstack");
	    		    BaseCommandProcessor cmdProcessor =  new CommandProcessorResolver().getCommandProcessor(strProcessor);
					resultDTO = cmdProcessor.processCommand(screenName1, querynodeXpath, null, inputDTO, resultDTO);				
						// if(outstack != null && !"".equals(outstack))
						// outstackList.add(outstack);
						// resDTO = rpc.selectData( screenName, null,
						// querynodeXpath , (JSONObject)jsonRecord);
					}
				}
				resultDTO.merge(resDTO);
				
				
				adhocstackids.put("adhocstackids", outstackList);
				resDTO.setData(adhocstackids);

				resultDTO.merge(resDTO);

				ServletActionContext.getContext().getValueStack().set("resDTO",JSONSerializer.toJSON(resultDTO).toString());
				System.out.println("SelectOnLoad::"+ JSONSerializer.toJSON(resultDTO).toString());
				System.out.println("SelectOnLoad::adhocstackids"+ JSONSerializer.toJSON(resultDTO.getData().get("adhocstackids")).toString());
				/////end command onload ////
				
				/*
				for (Iterator queryList = selonloadnl.iterator(); queryList.hasNext();) {
					org.dom4j.Node queryNode = (org.dom4j.Node) queryList.next();
					logger.debug("Query Node:"+queryNode.getText());
					String stackid = ((org.dom4j.Element) queryNode).attributeValue("outstack");
					String type = ((org.dom4j.Element) queryNode).attributeValue("type");
					String sqlquery = queryNode.getText();
					
					Element errorNode = (Element) queryNode.selectSingleNode("error");
					String errorTemplate = "";
					if(errorNode !=null)errorTemplate=errorNode.attributeValue("message");
					
					Element messageNode = (Element) queryNode.selectSingleNode("message");
					String messageTemplate = "";
					if(messageNode !=null)messageTemplate=messageNode.attributeValue("message");
					
					List<Element> nl = root.selectNodes("//fields/field/*");
					HashMap<String, DataType> hmfielddbtype= new HashMap<String, DataType>();
					QueryParser.populateFieldDBType(nl, hmfielddbtype);
					
					PrepstmtDTOArray arparam = new PrepstmtDTOArray();
					InputDTO inputDTO = new InputDTO();
					inputDTO.setData(jsonsubmitdata);
					String parsedquery = QueryParser.parseQuery(sqlquery, null, jsonsubmitdata, arparam, hmfielddbtype, inputDTO, resDTO );
					logger.debug("selonload Query query:"+parsedquery+"\n Expanded prep:"+arparam.toString(parsedquery));
					FETranslatorDAO feDAO = new FETranslatorDAO();
					ResultDTO resDTO = feDAO.executecrud(screenName,parsedquery,stackid,jsonsubmitdata, arparam, errorTemplate, messageTemplate );
					resDTO.merge(resultDTO);
					logger.debug("resDTO (gson converter)= "+new Gson().toJson(resDTO).toString());
					logger.debug("resDTO (JSONSerializer converter)= "+JSONSerializer.toJSON(resDTO).toString());
					ActionContext.getContext().getValueStack().set("resDTO", JSONSerializer.toJSON(resDTO).toString());
					ActionContext.getContext().getValueStack().getContext().put("ZHello", "World");
					ActionContext.getContext().getValueStack().set("ZHello2", "World2");
					org.dom4j.Element e = (org.dom4j.Element) queryNode;
					System.out.println("HTMLProcessor **************** populating value stack");
				}
				*/
			
			} catch (FrontendException e) {
				throw new FrontendException("error.selectOnloadFailed",e);
			} catch (BackendException e) {
				throw new FrontendException("error.selectOnloadFailed",e);
			} catch (ProcessorNotFoundException e) {
				throw new FrontendException("error.selectOnloadFailed",e);
			}
			
			 
			 
			
		}
	}
	
	public void remoteSelectOnLoad( String screenName1, String jsonsubmitdata ){
		logger.debug("Sent to BE:"+jsonsubmitdata);
		 QueryServiceService qss = new QueryServiceService();
		 QueryService queryServicePort = qss.getQueryServicePort();
		 String strResDTO = queryServicePort.selectOnLoad(screenName1, jsonsubmitdata);
		 JSONObject resDTOjson = JSONObject.fromObject(strResDTO);
		 JSONObject data = resDTOjson.getJSONObject("data");
		 logger.debug("returned result from select on load:"+strResDTO);
		 
		 ResultDTO tempDTO = ResultDTO.fromJsonString(resDTOjson);
		 
		 ServletActionContext.getContext().getValueStack().getContext().put("resultDTO", tempDTO);
		  
		
		 try {
			JSONArray adhocstackids = data.getJSONArray("adhocstackids");
			for (Iterator outstackItr = adhocstackids.iterator(); outstackItr.hasNext();) {
				String outstackid = (String) outstackItr.next();
				System.out.println("outstackid:" + outstackid);
							if(data.get(outstackid) != null )
								ServletActionContext.getContext().getValueStack().set(outstackid, data.get(outstackid));
			}
		} catch (Exception e) {
			logger.error("Result of select onload processing exception",e);
		}
	}
}
