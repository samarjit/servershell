package com.ycs.be.crud;

import java.util.HashMap;
import java.util.List;



import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.ycs.be.dto.InputDTO;
import com.ycs.be.dto.PrepstmtDTO;
import com.ycs.be.dto.PrepstmtDTOArray;
import com.ycs.be.dto.ResultDTO;
import com.ycs.be.dto.PrepstmtDTO.DataType;
import com.ycs.be.util.ScreenMapRepo;

public class DeleteData {
private Logger logger = Logger.getLogger(getClass()); 
	public String delete(String screenName, String panelname, HashMap<String,Object> jsonObject,  InputDTO jsonInput, ResultDTO prevResultDTO) {
		logger.debug("calling first default(first) sqldelete query");
		return delete(screenName, panelname,"sqldelete", jsonObject, jsonInput, prevResultDTO);
	}
	public String delete(String screenName, String panelname,String querynode,  HashMap<String,Object> jsonObject,  InputDTO jsonInput, ResultDTO prevResultDTO) {	 
		
		String parsedquery = "";
		QueryParser queryParser = new QueryParser();
			try {
				String xmlconfigfile =  ScreenMapRepo.findMapXMLPath(screenName);
				org.dom4j.Document document1 = new SAXReader().read(xmlconfigfile);
				org.dom4j.Element root = document1.getRootElement();
				Node crudnode = root.selectSingleNode("//crud");
				Node node = crudnode.selectSingleNode(querynode);
				if(node == null)throw new Exception("<"+querynode+"> node not defined");
				
				String outstack = ((Element) node).attributeValue("outstack"); 
				panelname = outstack;
				
				String updatequery = "";
				updatequery += node.getText();
				
				List<Element> nodeList = crudnode.selectNodes("../fields/field/*");
				logger.debug("fields size:"+nodeList.size());
				HashMap<String, DataType> hmfielddbtype = new HashMap<String, PrepstmtDTO.DataType>();
				queryParser.populateFieldDBType(nodeList, hmfielddbtype);
				
				/*Pattern pattern  = Pattern.compile(":(\\w*)",Pattern.DOTALL|Pattern.MULTILINE);
				Matcher m = pattern.matcher(updatequery);
				while(m.find()){
					String val = "";
					logger.debug(m.group(0)+ " "+ m.group(1));
					if(jsonObject.has(m.group(1))){
						val = jsonObject.getString(m.group(1));
						updatequery = updatequery.replaceAll(":"+m.group(1), val);
					}
				}*/
				//SET
				List<Element> primarykeys = crudnode.selectNodes("../fields/field/*[@primarykey]");
				
				PrepstmtDTOArray  arparam = new PrepstmtDTOArray();
				parsedquery = queryParser.parseQuery(updatequery, panelname, jsonObject, arparam, hmfielddbtype, jsonInput, prevResultDTO);
			       
			       logger.debug("UPDATE query:"+parsedquery+"\n Expanded prep:"+arparam.toString(updatequery));
			}catch(Exception e){
				logger.debug("Exception caught in InsertData",e);
			}
		return parsedquery;
	}

}
