package com.ycs.fe.crud;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.dom4j.Node;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;
import com.ycs.fe.dao.FETranslatorDAO;
import com.ycs.fe.dto.InputDTO;
import com.ycs.fe.dto.PaginationDTO;
import com.ycs.fe.dto.PagingFilterRule;
import com.ycs.fe.dto.PrepstmtDTO;
import com.ycs.fe.dto.PrepstmtDTO.DataType;
import com.ycs.fe.dto.PrepstmtDTOArray;
import com.ycs.fe.dto.ResultDTO;
import com.ycs.fe.exception.BackendException;
import com.ycs.fe.exception.DataTypeException;
import com.ycs.fe.exception.FrontendException;
import com.ycs.fe.util.ScreenMapRepo;

public class JsrpcPojo {
private Logger logger = Logger.getLogger(getClass()); 
	public ResultDTO selectData(String screenName, String panelname,  JSONObject jsonObject, InputDTO jsonInput, ResultDTO prevResultDTO) {
		logger.debug("calling first default(first) sqlselect query");
		return selectData(screenName, panelname,"sqlselect", jsonObject, jsonInput, prevResultDTO);
	}
	
	public ResultDTO selectData(String screenName, String panelname,String querynode, JSONObject jsonRecord, InputDTO jsonInput, ResultDTO prevResultDTO) {
		 
		 
//			String tplpath = ServletActionContext.getServletContext().getRealPath("WEB-INF/classes/map");
			String parsedquery = "";
			ResultDTO resultDTO = new ResultDTO();
			try {
//				String pageconfigxml =  ScreenMapRepo.findMapXMLPath(screenName);
//				org.dom4j.Document document1 = new SAXReader().read(pageconfigxml);
//				org.dom4j.Element root = document1.getRootElement();
				Element root = ScreenMapRepo.findMapXMLRoot(screenName);
				Node crudnode = root.selectSingleNode("/root/screen/crud");
				Node queryNode = crudnode.selectSingleNode(querynode);
				if(queryNode == null)throw new FrontendException("<"+querynode+"> node not defined");
				
				String outstack = ((Element) queryNode).attributeValue("outstack"); 
				panelname = outstack;
				
				String updatequery = "";
				updatequery += queryNode.getText();
				
				Element errorNode = (Element) queryNode.selectSingleNode("error");
				String errorTemplate = "";
				if(errorNode !=null)errorTemplate=errorNode.attributeValue("message");
				
				Element messageNode = (Element) queryNode.selectSingleNode("message");
				String messageTemplate = "";
				if(messageNode !=null)messageTemplate=messageNode.attributeValue("message");
				
				List<Element> nodeList = crudnode.selectNodes("//fields/field/*");
				logger.debug("fields size:"+nodeList.size());
				HashMap<String, DataType> hmfielddbtype = new HashMap<String, PrepstmtDTO.DataType>();
				QueryParser.populateFieldDBType(nodeList, hmfielddbtype);
				
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
				List<Element> primarykeys = crudnode.selectNodes("//fields/field/*[@primarykey]");
				FETranslatorDAO fetranslatorDAO = new FETranslatorDAO();
				//pagination
				Element countqrynode = (Element)queryNode.selectSingleNode("countquery");
				if(countqrynode != null){
					
					String strpagesize = countqrynode.attributeValue("pagesize");
					int pagesize = 0;
					if(strpagesize != null ){
						pagesize = Integer.parseInt(strpagesize);
					}
					String countquery = countqrynode.getText();
					
					
					if(countquery != null){
						PrepstmtDTOArray  arparam = new PrepstmtDTOArray();
						parsedquery = QueryParser.parseQuery(countquery, outstack, jsonRecord, arparam, hmfielddbtype,jsonInput, prevResultDTO);
						int reccount = fetranslatorDAO.executeCountQry(screenName, parsedquery, outstack, arparam);
						logger.debug("Processing count query"+countquery);
						if(reccount > pagesize){
							JSONObject jobject = jsonInput.getData().getJSONObject("pagination");
							int pageno = 0;
							PaginationDTO pageDTO= null; 
							if(jobject.size()>0 ){
								JSONObject	panel =  jobject.getJSONObject(outstack);
								pageDTO = (PaginationDTO) JSONObject.toBean(panel, PaginationDTO.class);
								pageno =  pageDTO.getPage();// panel.getInt("currentpage");
							}else{
								pageno = 1;
								logger.debug("Pagination assuming first page as no page data is given" );
							}
								int pagecount = (int) Math.ceil((double)reccount / pagesize); 
								 
								//submitdata={form1:[{row:0,}],pagination:{form1:{currentpage:1,pagecount:200}}, bulkcmd:''...} 
								ValueStack stack = ActionContext.getContext().getValueStack();
								ResultDTO tempresDTO = (ResultDTO) stack.getContext().get("resultDTO");
								if(tempresDTO == null){
									tempresDTO = new ResultDTO();
								}
								tempresDTO.setPageDetails(outstack, pageno, pagecount, reccount , pagesize);
								logger.debug("Now setetting resultDTO in JsonRPC pojo="+JSONSerializer.toJSON(tempresDTO));
								stack.getContext().put("resultDTO",tempresDTO); 
								logger.debug("Pagination set with pageno:"+pageno+"totalrec:"+reccount+" pagecount:"+pagecount+" pagesize:"+pagesize);
								int recfrom = pageno * pagesize;
								int recto = recfrom + pagesize;
								jsonRecord.put("recto", recto); //put into current row value the recfrom and recto so that it can be used in count query
								jsonRecord.put("recfrom", recfrom);
								hmfielddbtype.put("recto",PrepstmtDTO.getDataTypeFrmStr("INT") );
								hmfielddbtype.put("recfrom",PrepstmtDTO.getDataTypeFrmStr("INT"));
							
								//dynamically modify query for pagination
								String sql = updatequery;
								Pattern pattern = Pattern.compile("([\\S\\s]*)(?ims:order)[\\S\\s]*(?ims:by)([\\S\\s]*)");
								Matcher m1 = pattern.matcher(sql);
								String selectPart = null;
								String selectWherePart = null;
								String secondPart = null;
								String wherePart  = null;
								String orderByPart = null;
								if(m1.find()){
									selectWherePart = m1.group(1); //before order by
									orderByPart = m1.group(2); //after order by
									
								}else{
									selectWherePart  = sql;
								}	
								
								//no where clause specified check order by
								Pattern pattern1 = Pattern.compile("([\\S\\s]*)(?ims:where)([\\S\\s]*)");
								Matcher m2 = pattern1.matcher(selectWherePart);
								if(m2.find()){
									selectPart = m2.group(1); //before where 
									wherePart = m2.group(2);  //after where
								}else{
									selectPart = selectWherePart;
								}
									
								if(pageDTO.getSidx() != null && pageDTO.getSord() != null){
									orderByPart = pageDTO.getSidx() +" "+pageDTO.getSord();
								}
								updatequery = "select " + selectPart;
								String joiner = " WHERE ";
								if (wherePart != null){
									joiner = " AND ";
								}
								wherePart += joiner;
								String wherePart2 = "";
								boolean first = true;
								//filter
								for (PagingFilterRule element : pageDTO.getFilters().getRules()) {
									String data = element.getData();
									DataType dbtype = hmfielddbtype.get(element.getField());
									switch(dbtype){
										case  INT:
										case  LONG:
										case  FLOAT:
										case  DOUBLE: 
											break;
										case  DATEDDMMYYYY:  data = " TO_DATE('"+data+"',"+PrepstmtDTO.DATE_NS_FORMAT+") ";
											break;
										case  TIMESTAMP:
										case  DATE_NS: data = " TO_DATE('"+data+"',"+PrepstmtDTO.DATE_NS_FORMAT+") ";
										    break;
										case  STRING:
											  data = "'"+data+"'";
											break;
										default: 	
											data = "'"+data+"'";
									}
									wherePart2 += (first)?"":joiner + "   "+ element.getField()+ findOp(element.getOp())+" "+ data +"  ";
									joiner = " " + pageDTO.getFilters().getGroupOp() + " ";
									first = false;
								}
								
								DataType dbtype = hmfielddbtype.get(pageDTO.getSearchField());
								
								String data = pageDTO.getSearchString();
								switch(dbtype){
								case  INT:
								case  LONG:
								case  FLOAT:
								case  DOUBLE: 
									break;
								case  DATEDDMMYYYY:  data = " TO_DATE('"+data+"',"+PrepstmtDTO.DATE_NS_FORMAT+") ";
									break;
								case  TIMESTAMP:
								case  DATE_NS: data = " TO_DATE('"+data+"',"+PrepstmtDTO.DATE_NS_FORMAT+") ";
								    break;
								case  STRING:
									  data = "'"+data+"'";
									break;
								default: 	
									data = "'"+data+"'";
							}
								
								wherePart2 += joiner + " "+pageDTO.getSearchField() +findOp(pageDTO.getSearchOper())  + " " +data +" ";  
										
								if(wherePart2 != null && !"".equals(wherePart2))
									wherePart += "( "+ wherePart2 +" )";
								
								if (wherePart != null){
									updatequery +=  wherePart;
								}
								
								if(orderByPart!= null && !"".equals(orderByPart)){
									updatequery += " order by "+ orderByPart ;
								}
								
								sql = "select * from (select v.*, ROWNUM rn from ("
								 + updatequery
								 + " ) v where rownum < :recto) where rn >= :recfrom";
						}
					}
				}
				//pagination end
				
				PrepstmtDTOArray  arparam = new PrepstmtDTOArray();
				parsedquery = QueryParser.parseQuery(updatequery, panelname, jsonRecord, arparam, hmfielddbtype,  jsonInput, prevResultDTO);
				
			       logger.debug("JsonRPC query:"+parsedquery+"\n Expanded prep:"+arparam.toString(parsedquery));
			       fetranslatorDAO = new FETranslatorDAO();
			       resultDTO = fetranslatorDAO.executecrud(screenName, parsedquery, panelname, jsonRecord, arparam, errorTemplate,messageTemplate);
			       
			}catch (DataTypeException e){
				logger.error("error.datatypeUndefined", e);
				resultDTO.addError("error.datatypeundefined");
			} catch (FrontendException e) {
				resultDTO.addError("error.readingxml");
			} catch (BackendException e) {
				resultDTO.addError("error.queryfailed");
			}  
		return resultDTO;
	}

	private String findOp(String op) {
		if("eq".equals(op))
			return "=";
		if("lt".equals(op))
			return "<";
		if("gt".equals(op))
			return ">";
		
		return null;
	}

	public static void queryParseCheck(String query){
		String sql = query;
		Pattern pattern = Pattern.compile("([\\S\\s]*)(?ims:order)[\\S\\s]*(?ims:by)([\\S\\s]*)");
		Matcher m1 = pattern.matcher(sql);
		String selectPart = null;
		String selectWherePart = null;
		String secondPart = null;
		String wherePart = null  ;
		String orderByPart = null ;
		if(m1.find()){
			selectWherePart = m1.group(1); //before order by
			orderByPart = m1.group(2); //after order by
			
		}else{
			selectWherePart  = sql;
		}	
		
		//no where clause specified check order by
			Pattern pattern1 = Pattern.compile("([\\S\\s]*)(?ims:where)([\\S\\s]*)");
			Matcher m2 = pattern1.matcher(selectWherePart);
			if(m2.find()){
				selectPart = m2.group(1); //before where 
				wherePart = m2.group(2);  //after where
			}else{
				selectPart = selectWherePart;
			}
		System.out.println("select part:"+selectPart);	
		System.out.println("where part:"+ wherePart);	
		System.out.println("order by part:"+ orderByPart);
	}
	public static void main(String args[]){
		queryParseCheck("SELECT s,d from table1 where x=1 and sdd='d' order by 1");
		queryParseCheck("SELECT s,d from table1    order by 1");
		queryParseCheck("SELECT s,d from table1 where x=1 and sdd='d' ");
	}
}
