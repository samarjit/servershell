package com.ycs.fe.commandprocessor;

import java.util.Date;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.ws.WebServiceException;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Node;

import repo.txnmap.generated.Root;
import repo.txnmap.generated.Txn;

import com.ycs.fe.dto.InputDTO;
import com.ycs.fe.dto.ResultDTO;
import com.ycs.fe.exception.FrontendException;
import com.ycs.fe.exception.SentenceParseException;
import com.ycs.fe.util.ParseSentenceOgnl;
import com.ycs.fe.util.ScreenMapRepo;
import com.ycs.fe.ws.Exception_Exception;
import com.ycs.fe.ws.SPCall;
import com.ycs.fe.ws.SPCallService;

/**
 * jsonRecord will be like
 * "txnrec":[{single:"",multiple:[{aaa:'11',bbb:'22',ccc:
 * '33'},{aaa:'1',bbb:'2',ccc:'3'}], command="TXNPROC1"}]}
 * 
 * @author Samarjit
 * 
 */
public class DmCommandProcessor implements BaseCommandProcessor {
	private Logger logger = Logger.getLogger(getClass());

	/**
	 * jsonRecord will be like
	 * "txnrec":[{single:"",multiple:[{aaa:'11',bbb:'22',
	 * ccc:'33'},{aaa:'1',bbb:'2',ccc:'3'}], command="TXNPROC1"}]}
	 * 
	 */

	@Override
	public ResultDTO processCommand(String screenName, String querynodeXpath, JSONObject jsonRecord, InputDTO inputDTO, ResultDTO resultDTO) {
		HashMap<String, Object>  data = new HashMap<String, Object>();
		resultDTO = new ResultDTO();
		try {
//			String resultJsonConf = "{'cmd':'STUCAP','single':{'FF0151':'aaa','FF0148':'bbb','FF01258':'eee'},'multiple':[{'FF9000':111,'FF0151':222,'FF0152':333},{'FF0151':555},{'FF9000':456,'FF0151':765,'FF0152':877}]}";
			
			Element rootxml = ScreenMapRepo.findMapXMLRoot(screenName);
			Node crudnode = rootxml.selectSingleNode("/root/screen/dm");
			Node queryNode = crudnode.selectSingleNode(querynodeXpath);
		
//			Element rootXml = ScreenMapRepo.findMapXMLRoot(screenName);
//			Node selectSingleNode = rootXml.selectSingleNode(querynodeXpath);
			
			String jsonFromConf = queryNode.getText();
			String resultJsonConf = ParseSentenceOgnl.parse(jsonFromConf, jsonRecord);
			JSONObject jsonObj = JSONObject.fromObject(resultJsonConf);

			String unique = new String();
			String application_name = "ICICI_BRUSER3_1298884319363";
			String transcode = ""; // will be coming form command
			String netId = "BRUSER3";
			JSONObject singleData = null;
			JSONArray multipleData = null;
			// creating a unique id.
			
			// unique id = transaction code.
			unique += "Henry";
			unique += "_" + System.currentTimeMillis();

			logger.debug("calling DM command Processor");

			if (jsonObj.containsKey("single"))
				singleData = jsonObj.getJSONObject("single");

			if (jsonObj.containsKey("transcode"))
				transcode = jsonObj.getString("transcode");

			if (jsonObj.containsKey("multiple"))
				multipleData = jsonObj.getJSONArray("multiple");


			final JAXBContext jc = JAXBContext.newInstance(Root.class);
			final Root root = (Root) jc.createUnmarshaller().unmarshal(DmCommandProcessor.class.getClassLoader().getResourceAsStream("repo/txnmap/nrow_txnmap.xml"));// new
																																										// File("C:/Eclipse/workspace/FEtranslator1/src/repo/txnmap/nrow_txnmap.xml"));
			String[] arSingle = null;
			String[] arMultiple = null;
			for (Txn txn : root.getTxn()) {
				if (txn.getId().equals(transcode)) {
					String strReqSingle = txn.getReq().getSingle();
					if (strReqSingle != null) {
						arSingle = strReqSingle.split(",");
						logger.debug("Single : " + strReqSingle);
					}
					String strReqMultiple = txn.getReq().getMultiple();
					if (strReqMultiple != null) {
						arMultiple = strReqMultiple.split(",");
						logger.debug("Multiple :" + strReqMultiple);
					}
				}
			}

//			Document doc = DocumentFactory.getInstance().createDocument();
//			Element rootele = doc.addElement("IDCT");
//
//			rootele.addElement("TRANS_CODE").addText(transcode);
//			rootele.addElement("IDCT_ID").addText( application_name + "_" + unique);
//			rootele.addElement("DATETIME").addText(new Date().toString());
//			rootele.addElement("NET_ID").addText(netId);
//			rootele.addElement("MESSAGE_VER_NO").addText("1.0");
//			rootele.addElement("CHANNEL_ID").addText("WEB");
//			rootele.addElement("MESSAGE_DIGEST").addText("NO_DATA");
//			rootele.addElement("IDCT_STATUS").addText("NO_DATA");
//			rootele.addElement("IDCT_ERR_CODE").addText("NO_DATA");
//			rootele.addElement("IDCT_MESSAGE_TYPE").addText("01");
//			
//			if (arSingle != null) {
//				Element idctdata = rootele.addElement("IDCT_DATA");
//				for (String columnName : arSingle) {
//					int index = columnName.indexOf(":");
//					if (index != -1)
//						columnName = columnName.substring(0, index);
//					Object snglDtval = null;
//					if (singleData != null) {
//						if (singleData.containsKey(columnName))
//							snglDtval = singleData.get(columnName);
//						if (snglDtval != null) {
//							idctdata.addElement(columnName).addText(snglDtval.toString());
//						} else {
//							idctdata.addElement(columnName).addText("NO_DATA");
//						}
//					} else {
//						idctdata.addElement(columnName).addText("NO_DATA");
//					}
//				}
//			}
//			// Update multiple data in XML
//			if (arMultiple != null) {
//				if (multipleData != null) {
//					for (int i = 0; i < multipleData.size(); i++) {
//						Element idctdata = rootele.addElement("IDCT_DATA");
//						for (String columnName : arMultiple) {
//							int index = columnName.indexOf(":");
//							if (index != -1)
//								columnName = columnName.substring(0, index);
//							String mltplDtValue = null;
//
//							if (multipleData.getJSONObject(i).containsKey(columnName)) {
//								mltplDtValue = multipleData.getJSONObject(i).getString(columnName);
//							}
//							if (mltplDtValue != null) {
//								idctdata.addElement(columnName).addText(mltplDtValue);
//							} else {
//								idctdata.addElement(columnName).addText("NO_DATA");
//							}
//
//						} // end for
//					} // end for
//				} else {
//					Element idctdata = rootele.addElement("IDCT_DATA");
//					for (String columnName : arMultiple) {
//						int index = columnName.indexOf(":");
//						if (index != -1)
//							columnName = columnName.substring(0, index);
//						idctdata.addElement(columnName).addText("NO_DATA");
//					}
//				}
//			}
			
			String xml = "<?xml version='1.0'?>";
			xml += "<IDCT>";
			xml += "<TRANS_CODE>" + transcode + "</TRANS_CODE>";
			xml += "<IDCT_ID>" + application_name + "_" + unique + "</IDCT_ID>";
			xml += "<DATETIME>" + new Date().toString() + "</DATETIME>";
			xml += "<NET_ID>" + netId + "</NET_ID>";
			xml += "<MESSAGE_VER_NO>1.0</MESSAGE_VER_NO>";
			xml += "<CHANNEL_ID>WEB</CHANNEL_ID>";
			xml += "<MESSAGE_DIGEST>NO_DATA</MESSAGE_DIGEST>";
			xml += "<IDCT_STATUS>NO_DATA</IDCT_STATUS>";
			xml += "<IDCT_ERR_CODE>NO_DATA</IDCT_ERR_CODE>";
			xml += "<IDCT_MESSAGE_TYPE>01</IDCT_MESSAGE_TYPE>";
			// update single data in XML
			if (arSingle != null) {
				xml += "<IDCT_DATA>";
				for (String columnName : arSingle) {
					int index = columnName.indexOf(":");
					if (index != -1)
						columnName = columnName.substring(0, index);
					Object snglDtval = null;
					if (singleData != null) {
						if (singleData.containsKey(columnName))
							snglDtval = singleData.get(columnName);
						if (snglDtval != null) {
							xml += "<" + columnName + ">" + snglDtval.toString() + "</" + columnName + ">";
						} else {
							xml += "<" + columnName + ">NO_DATA</" + columnName + ">";
						}
					} else {
						xml += "<" + columnName + ">NO_DATA</" + columnName + ">";
					}
				}
				xml += "</IDCT_DATA>";
			}
			// Update multiple data in XML
			if (arMultiple != null) {
				if (multipleData != null) {
					for (int i = 0; i < multipleData.size(); i++) {
						xml += "<IDCT_DATA>";
						for (String columnName : arMultiple) {
							int index = columnName.indexOf(":");
							if (index != -1)
								columnName = columnName.substring(0, index);
							String mltplDtValue = null;

							if (multipleData.getJSONObject(i).containsKey(columnName)) {
								mltplDtValue = multipleData.getJSONObject(i).getString(columnName);
							}
							if (mltplDtValue != null) {
								xml += "<" + columnName + ">" + mltplDtValue + "</" + columnName + ">";
							} else {
								xml += "<" + columnName + ">NO_DATA</" + columnName + ">";
							}

						} // end for
						xml += "</IDCT_DATA>";
					} // end for
				} else {
					xml += "<IDCT_DATA>";
					for (String columnName : arMultiple) {
						int index = columnName.indexOf(":");
						if (index != -1)
							columnName = columnName.substring(0, index);
						xml += "<" + columnName + ">NO_DATA</" + columnName + ">";
					}
					xml += "</IDCT_DATA>";
				}
			}
			xml += "</IDCT>";
			
//			String xml = doc.asXML();
			xml = String.format("%06d", xml.length())+xml;
			logger.debug("Input Xml :  " + xml);
			System.out.println(xml);
			String outputxml = callPLSQLLocal(xml);
			data.put("DMResult", outputxml);
			resultDTO.setData(data);
			logger.debug("Output Xml :  " + outputxml);
		} catch (SentenceParseException e) {
			resultDTO.addError("error.sentenceparse");
			logger.error("error.sentenceparse", e);
		} catch (JSONException e) {
			resultDTO.addError("error.jsonexception");
			logger.error("error.jsonexception", e);
		} catch (WebServiceException e ){
			resultDTO.addError("error.webservice");
			logger.error("error.webservice", e);
		} catch (JAXBException e) {
			resultDTO.addError("error.jaxbexception");
			logger.error("error.jaxbexception", e);
		} catch (FrontendException e) {
			resultDTO.addError("error.readingxmlfile");
			logger.error("error.readingxmlfile", e);
		}
		return resultDTO;
	}

	public String callPLSQLLocal(String xml) {
		xml = String.format("%06d%s", xml.length(),xml);
		System.out.println(xml);
		SPCallService spcallsvc = new SPCallService();
		SPCall spcallendpoint  = spcallsvc.getSPCallPort();
		String retStr ="";
		try{
			retStr = spcallendpoint.callPLSQL(xml);
		}catch (Exception_Exception e) {
		}
		return retStr;
	}

	public static void main(String[] args) {
		DmCommandProcessor test = new DmCommandProcessor();
		String submitdatatxncode = "{'cmd':'STUCAP','single':{'FF0151':'aaa','FF0148':'bbb','FF01258':'eee'},'multiple':[{'FF9000':111,'FF0151':222,'FF0152':333},{'FF0151':555},{'FF9000':456,'FF0151':765,'FF0152':877}]}";
		submitdatatxncode = "{'transcode':'BNGPVW','multiple':[{'FF0143':'100001'},{'FF0143':'100002'}]}";
		JSONObject jsonRecord = JSONObject.fromObject(submitdatatxncode);
		test.processCommand(null, null, null, null, null);
	}
}
