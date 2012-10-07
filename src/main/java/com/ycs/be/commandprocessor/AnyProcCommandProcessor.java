package com.ycs.be.commandprocessor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.xml.ws.WebServiceException;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Node;

import com.ycs.be.dto.InputDTO;
import com.ycs.be.dto.ResultDTO;
import com.ycs.be.exception.BackendException;
import com.ycs.be.exception.FrontendException;
import com.ycs.be.exception.SentenceParseException;
import com.ycs.be.util.ParseSentenceOgnl;
import com.ycs.be.util.ScreenMapRepo;
import com.ycs.ws.client.SPCall;
import com.ycs.ws.client.SPCallService;

public class AnyProcCommandProcessor implements BaseCommandProcessor {

	private static Logger logger = Logger.getLogger(AnyProcCommandProcessor.class);

	@Override
	public ResultDTO processCommand(String screenName, String querynodeXpath, JSONObject jsonRecord, InputDTO inputDTO, ResultDTO resultDTO) {
		logger.debug("Processing AnyProc call");
		HashMap<String, Object>  data = new HashMap<String, Object>();
		resultDTO = new ResultDTO();
//		 String resultJsonConf =
//		 "{'procname':'WS_TEST_PROC','inputparam':[[{'NAME':'sam','EMAIL':'sam@yl.com'},{'NAME':'samarjit','EMAIL':'samarjit@yl.com'}],{'data1':'param2'}],'outputparam':'param3'}";

		try {
//			 String pageconfigxml =  ScreenMapRepo.findMapXMLPath(screenName);
//			 org.dom4j.Document document1 = new SAXReader().read(pageconfigxml);
//			org.dom4j.Element root = document1.getRootElement();
			Element root = ScreenMapRepo.findMapXMLRoot(screenName);
			Node crudnode = root.selectSingleNode("//anyprocs");
			Node queryNode = crudnode.selectSingleNode(querynodeXpath);
			
			String jsonFromConf = queryNode.getText();
			System.out.println("JsonRecord in any proc call :"+jsonRecord);
			String resultJsonConf = ParseSentenceOgnl.parse(jsonFromConf, jsonRecord);
			JSONObject jsObj = JSONObject.fromObject(resultJsonConf);

			Document doc = DocumentFactory.getInstance().createDocument();
			Element rootElement = doc.addElement("root");
			Element procele = rootElement.addElement("procedure");

			Element pname = procele.addElement("procname");
			Element inputele = procele.addElement("inputparam");
			Element outputele = procele.addElement("outputparam");
			String outstack = procele.attributeValue("outstack");
			String procname = jsObj.getString("procname");
			pname.addText(procname.toUpperCase());

			// JSON js = JSONSerializer.toJSON(json);
			// System.out.println(js.isArray());
			JSONArray inputarr = jsObj.optJSONArray("inputparam");
			JSONObject inputObj = jsObj.optJSONObject("inputparam");

			inputParamParser(doc, inputele, inputarr, inputObj);

			JSONArray outputarr = jsObj.optJSONArray("outputparam");
			String outputString = jsObj.optString("outputparam");

			outputParamParser(outputele, outputarr, outputString);

			String xmlString = doc.asXML();
			String resXML = callProcedure(xmlString);
			data.put(outstack, resXML);
			resultDTO.setData(data);
			
		} catch (SentenceParseException e) {
			resultDTO.addError("error.sentenceparse");
			logger.error("error.sentenceparse", e);
		} catch (JSONException e) {
			resultDTO.addError("error.jsonexception");
			logger.error("error.jsonexception", e);
		} catch (WebServiceException e ){
			resultDTO.addError("error.webservice");
			logger.error("error.webservice", e);
//		} catch (DocumentException e) {
//			resultDTO.addError("error.documentException");
//			logger.error("error.documentException", e);
		} catch (FrontendException e) {
			resultDTO.addError("error.readingxmlfile");
			logger.error("error.readingxmlfile", e);
		}
		return resultDTO;
	}

	public String callProcedure(String xmlString) {
		
		System.out.println("InputData :" + xmlString);
		SPCallService spcallsvc = new SPCallService();
		SPCall spcallendpoint  = spcallsvc.getSPCallPort();
		String resXML = spcallendpoint.callSP(xmlString);
//		MainWithoutType callproc = new MainWithoutType();
//		String htmlString = callproc.executeGenericProc(xmlString);
//		System.out.println("htmlString=" + htmlString);
		return resXML;
	}

	private void outputParamParser(Element outputele, JSONArray outputarr, String outputString) {
		if (outputarr != null) {
			for (int i = 0; i < outputarr.size(); i++) {
				String output = outputarr.getString(i);
				Element outparamele = outputele.addElement("parameter");
				outparamele.addText(output);
			}
		} else {
			Element outparamele = outputele.addElement("parameter");
			outparamele.addText(outputString);
		}
	}

	private void inputParamParser(Document doc, Element inputele, JSONArray inputarr, JSONObject inputObj) {
		if (inputarr != null) {
			for (int i = 0; i < inputarr.size(); i++) {
				JSONArray paramarr = inputarr.optJSONArray(i);
				JSONObject paramObj = inputarr.optJSONObject(i);
				Element inparamele = inputele.addElement("parameter");
				if (paramarr != null) {
					arrayParamParser(doc, paramarr, inparamele);
				} else {
					objectParamParser(doc, paramObj, inparamele);
				}
			}
		} else {
			Element inparamele = inputele.addElement("parameter");
			objectParamParser(doc, inputObj, inparamele);
		}
	}

	private void objectParamParser(Document doc, JSONObject paramObj, Element paramele) throws JSONException {
		Set<String> dataSet = paramObj.keySet();
		Iterator<String> it = dataSet.iterator();
		if (dataSet.size() == 1) {
			String key = it.next();
			Element dataele = paramele.addElement("data1");
			String val = paramObj.getString(key);
			dataele.addText(val);
		} else {
			Element structele = paramele.addElement("STRUCT");
			while (it.hasNext()) {
				String key = it.next();
				Element dataele = structele.addElement("data1");
				String val = paramObj.getString(key);
				dataele.addText(val);
				dataele.addAttribute("name", key);
			}
		}
	}

	private void arrayParamParser(Document doc, JSONArray paramarr, Element paramele) throws JSONException {
		Element arrele = paramele.addElement("ARRAY");
		for (int j = 0; j < paramarr.size(); j++) {
			JSONObject dataObj = paramarr.optJSONObject(j);
			String dataStr = paramarr.optString(j);
			if (dataObj != null) {
				Element structele = arrele.addElement("STRUCT");
				Set<String> dataSet = dataObj.keySet();
				Iterator<String> it = dataSet.iterator();
				while (it.hasNext()) {
					String key = it.next();
					Element dataele = structele.addElement("data1");
					dataele.addAttribute("name", key);
					String val = dataObj.getString(key);
					dataele.addText(val);
				}
			} else {
				Element dataele = arrele.addElement("data1");
				dataele.addText(dataStr);
			}
		}
	}

	public static void main(String[] args) throws BackendException {

		String json = "{'procname':'WS_TEST_PROC','inputparam':[[{'name':'AAA','email':'aaa@f'},{'name':'AAA','email':'aaa@f'}],{'data1':'2'},['aaaa','bbbb','cccc'],{'name':'AAA','email':'aaa@f'}],'outputparam':['param3','param4']}";
		// json =
		// "{'procname':'WS_TEST_PROC','inputparam':{'data1'=3},'outputparam':'param3'}}";

		
		new AnyProcCommandProcessor().processCommand(null, null, null, null, null);

	}
}
