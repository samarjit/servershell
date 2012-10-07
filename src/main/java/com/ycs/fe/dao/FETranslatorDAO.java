package com.ycs.fe.dao;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;
import com.ycs.fe.dto.PrepstmtDTO.DataType;
import com.ycs.fe.dto.PrepstmtDTOArray;
import com.ycs.fe.dto.ResultDTO;
import com.ycs.fe.exception.BackendException;
import com.ycs.fe.exception.SentenceParseException;
import com.ycs.fe.util.ParseSentenceOgnl;

public class FETranslatorDAO {
	private Logger logger = Logger.getLogger(this.getClass());
	/**
	 * Executes select box query and puts in valueStack 
	 * 
	 * CREATE TABLE query_table(QUERYID varchar2(20), QUERY varchar2(500), active varchar2(1));
	 * 
	 * @param sqlquery or queryid value which will be used for lookup
	 * @param stackid contains the value stack key against which value is going to be stored
	 * @param type can be SQL for normal sql or QUERYID for referenced queries from QUERY_TABLE 
	 * @throws BackendException 
	 */
	public void executequery(String sqlquery, String stackid, String type) throws BackendException {
		ValueStack stack = ActionContext.getContext().getValueStack();
		DBConnector dbconn = new DBConnector();
		if( "SQL".equals(type)){
			//proceed with normal sqlquery without modification
		}else if("QUERYID".equals(type)){
			//proceed with SQL QUERY instead of sqlquery, sqlquery gets new value
			String SQL = "select QUERY from QUERY_TABLE where QUERYID=? and ACTIVE='Y'" ;
			CachedRowSet crs = null;
			try {
				PrepstmtDTOArray arprep = new PrepstmtDTOArray();
				arprep.add(DataType.STRING, sqlquery);
				crs = dbconn.executePreparedQuery(SQL,arprep );
				String query = null;
				while (crs.next()) {
					query = crs.getString("QUERY");
				}
				logger.debug("Query found ="+query);
				if(query == null || query.length() ==0){
					logger.error("QueryID #"+sqlquery+"# not found");
					throw new BackendException("Query Not Found");
				}
				
				sqlquery = query;
			} catch (SQLException e) {
				logger.error("DAO Exception QUERY retreive failure by QUERYID("+sqlquery+"):",e);
				throw new BackendException("error.SelectQueryFailed",e);
			} catch (BackendException e) {
				throw new BackendException("error.SelectQueryFailed",e);
			} finally {
				if (crs != null) {
					try {
						crs.close();
					} catch (SQLException e1) {
						logger.error("DAO Exception closing resultset connection",e1);
						//throw new BackendException("error.resultsetCloseConnection");
					}
					crs = null;
				}
			}	
		}
		if(sqlquery!= null && sqlquery.trim().length() >0 ){
	    	Map<String, String> values = new HashMap<String, String>();
	    	Map<String, Object> context = new HashMap<String, Object>();
			
			CachedRowSet crs = null;
			try {
				//case i insensitive m multiline s doall  
				if(sqlquery.matches("(?ims:select).*(?ims:from).*")){
					logger.debug("valid query processing...");
				}else{
					logger.debug("invalid query skipping...");
					return;
				}
					
				crs = dbconn.executeQuery(sqlquery);
				ResultSetMetaData md = crs.getMetaData();
				int colcount = md.getColumnCount();
				if(colcount == 2){
					while (crs.next()) {
						values.put(crs.getString(1),crs.getString(2));
					}
					 
				}else{
					while (crs.next()) {
						values.put("value",crs.getString(1));
					}
				}
			} catch (SQLException e) {
				logger.error("Exception occurred in accessing ResultSet",e);
				throw new BackendException("error.SelectQueryFailed",e);
			} catch (BackendException e) {
				throw new BackendException("error.SelectQueryFailed",e);
			} finally {
				if (crs != null) {
					try {
						crs.close();
					} catch (SQLException e1) {
						logger.error("DAO Exception closing resultset connection",e1);
						//throw new BackendException("error.resultsetCloseConnection");
					}
					crs = null;
				}
			}
			context.put(stackid, values);
//	    	stack.push(context);
			stack.set(stackid, values);
	    }
	    
	}
	
	
	public ResultDTO executecrud(String screenName, String sqlquery, String stackid,  JSONObject jsonObject, PrepstmtDTOArray prepar, String errorTemplate, String messageTemplate){
		ValueStack stack = ActionContext.getContext().getValueStack();
		DBConnector dbconn = new DBConnector();
		String retval = "";
		ResultDTO resultDTO = new ResultDTO();
		if(sqlquery!= null && sqlquery.trim().length() >0 ){
	    	List<Map<String, String>> values = new ArrayList<Map<String, String>>();
	    	Map<String, String> row ; 
	    	HashMap<String, ResultDTO> context = new HashMap<String, ResultDTO>();
			
			CachedRowSet crs = null;
			int countrec = 0;
			String text = "";
			try {
				//case i insensitive m multiline s doall  
				if(sqlquery.matches("(?ims:select)[\\S\\s]*(?ims:from)[\\S\\s]*")){
					logger.debug("valid query processing...");
					
					
					crs = dbconn.executePreparedQuery(sqlquery, prepar);
					ResultSetMetaData md = crs.getMetaData();
					int colcount = md.getColumnCount();
					
						while (crs.next()) {
							row = new HashMap<String, String>();
							for (int i = 1; i <= colcount; i++) {
								row.put(md.getColumnLabel(i), crs.getString(i));
							}
							values.add(row);
							countrec++;	
						}
						retval = String.valueOf(countrec) ;
					
						if(jsonObject !=null) 
							text = ParseSentenceOgnl.parse(messageTemplate, jsonObject);
					resultDTO.addMessage("SUCCESS:"+String.valueOf(countrec)+"|"+text);
				}else if(sqlquery.matches("[\\S\\s]*(?ims:insert)[\\S\\s]*(?ims:into)[\\S\\s]*")){
					countrec = dbconn.executePreparedUpdate(sqlquery, prepar);
					if(jsonObject !=null) 
						text = ParseSentenceOgnl.parse(messageTemplate, jsonObject);
					resultDTO.addMessage("SUCCESS:"+String.valueOf(countrec)+"|"+text);
				}else if(sqlquery.matches("[\\S\\s]*(?ims:update|delete)[\\S\\s]*(?ims:where)[\\S\\s]*")){
					countrec = dbconn.executePreparedUpdate(sqlquery, prepar);
					if(jsonObject !=null) 
						text = ParseSentenceOgnl.parse(messageTemplate, jsonObject);
					resultDTO.addMessage("SUCCESS:"+String.valueOf(countrec)+"|"+text);
				
				}else{
					logger.debug("invalid query skipping..."+sqlquery);
					 
					 resultDTO.addError("ERROR:Invalid Query");
					 //return resultDTO; ? how it worked before , coz' even then Invalid query came!
				}
				
			} catch (SQLException e) {
				logger.error("Accessing Result set"+e);
				if(jsonObject !=null)
					try {
						text = ParseSentenceOgnl.parse(errorTemplate, jsonObject);
					} catch (SentenceParseException e1) {
						logger.error("error.parsingerrortemplate", e1);
					}
				retval = "ERROR:"+e.getLocalizedMessage()+"|"+text;
				resultDTO.addError("error.QueryExecutionFailed");
			} catch (SentenceParseException e) {
				logger.error("Exception occured in parsing", e);
				resultDTO.addError("error.QueryExecutionFailed");
			} catch (BackendException e) {
				 resultDTO.addError("error.QueryExecutionFailed");
				//throw new BackendException("error.SelectQueryFailed",e);
			} finally {
				if (crs != null) {
					try {
						crs.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					crs = null;
				}
			}
			
			HashMap<String, Object> hm = new HashMap<String, Object>();
			ArrayList<Map<String, String>> ar = new ArrayList<Map<String, String>>(values);
			hm.put(stackid, ar);
			resultDTO.setData(hm);
			
			ResultDTO tempresDTO = (ResultDTO) stack.getContext().get("resultDTO");
			logger.debug("previously set resultDTO in FEtranaltorDAO="+JSONSerializer.toJSON(tempresDTO));
			if(tempresDTO != null){
			  tempresDTO.merge(resultDTO);
			  resultDTO = null;
			  resultDTO = tempresDTO;
			} 
			
			
			logger.debug(screenName+stackid+"="+resultDTO.getData().toString());
			//context.put("resultDTO", resultDTO);
			
	    	stack.getContext().put("resultDTO",resultDTO);
			//stack.set("resultDTO", resultDTO);
			//stack.push(resultDTO);
			
	    	logger.debug("ValueStack="+stack);
	    }
		return resultDTO;
	}


	public int executeCountQry(String screenName, String sqlquery, String panelname, PrepstmtDTOArray prepar) throws BackendException {
		DBConnector dbconn = new DBConnector();
		int returncount = -1;
		if(sqlquery!= null && sqlquery.trim().length() >0 ){
	    	CachedRowSet crs = null;
			 
			try {
				//case i insensitive m multiline s doall  
				if(sqlquery.matches("(?ims:select)[\\S\\s]*(?ims:from)[\\S\\s]*")){
					logger.debug("valid query processing...");
					
					
					crs = dbconn.executePreparedQuery(sqlquery, prepar);
					
						while (crs.next()) {
							returncount = crs.getInt(1);
							 
						}
						 
				}else{
					logger.debug("invalid query skipping..."+sqlquery);
					 return -1;
				}
				
			} catch (SQLException e) {
				logger.debug("DAO Exception:"+e);
			} catch (BackendException e) {
				throw new BackendException("error.CountQueryFailed",e);
			} finally {
				if (crs != null) {
					try {
						crs.close();
					} catch (SQLException e1) {
						logger.error("Exception occurred in closing resultset", e1);
					}
					crs = null;
				}
			}
		
		}
		return returncount;
	}
	

}
