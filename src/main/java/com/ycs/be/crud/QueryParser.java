package com.ycs.be.crud;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import ognl.OgnlException;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import com.opensymphony.xwork2.ActionContext;
import com.ycs.be.dto.InputDTO;
import com.ycs.be.dto.PrepstmtDTO;
import com.ycs.be.dto.PrepstmtDTOArray;
import com.ycs.be.dto.ResultDTO;
import com.ycs.be.dto.PrepstmtDTO.DataType;
import com.ycs.be.exception.BackendException;
import com.ycs.be.exception.DataTypeException;
import com.ycs.be.exception.QueryParseException;

public class QueryParser{
	private static Logger logger = Logger.getLogger(QueryParser.class);
	
	/**
	 * @param nodeList List<org.dom4j.Element>[in]
	 * @param hmfielddbtype [out]
	 * @throws DataTypeException 
	 * @throws JSONException
	 * @throws Exception
	 */
	public  void populateFieldDBType(List<Element> nodeList, HashMap<String, DataType> hmfielddbtype) throws DataTypeException {
		for (Element nodeelm : nodeList) {
			String col = nodeelm.attributeValue("column");
			String fldname = nodeelm.attributeValue("name");
			String dbtype = nodeelm.attributeValue("dbdatatype");
			
			if(fldname != null && !"".equals(fldname)){
				if(dbtype != null  ){
//					updatequery += " "+col+"= TO_DATE('"+jsonObject.getString(fldname)+"', 'DD/MM/YYYY')";
					hmfielddbtype.put(fldname, PrepstmtDTO.getDataTypeFrmStr(dbtype)  );
					hmfielddbtype.put(col,PrepstmtDTO.getDataTypeFrmStr(dbtype));
				}else{
//					updatequery += " "+col+"='"+jsonObject.getString(fldname)+"',";
					hmfielddbtype.put(fldname, PrepstmtDTO.getDataTypeFrmStr("STRING"));
					hmfielddbtype.put(col,PrepstmtDTO.getDataTypeFrmStr("STRING"));
				}
			}
		}
		logger.debug("populateFieldDBType():"+hmfielddbtype);
	}
	
	/**
	 * Hash dependency on Action context for accessing ValueStack. Three conditions might come get data from immediate dataset, get data from inputdata set as whole :inp, get data from previous results :res. 
	 * @param updatequery [in] Not null it is returned after replacemeent of :xxxx with '?' as i prepared statement
	 * @param panelname [not used] will be inputstackid if it is used later 
	 * @param jsonObject [in] containing key/value pair of properties to be filled in query :xxxx can be null is there is no :xxx
	 * @param arparam [out]
	 * @param hmfielddbtype [in]
	 * @param prevResultDTO previous results
	 * @param jsonInput Complete input Json request
	 * @return updatequery is returned after replacemeent of :xxxx with '?' as i prepared statement
	 * @throws DataTypeException 
	 * @throws QueryParseException 
	 * @throws Exception
	 */
	public  String parseQuery(String updatequery,String panelname,JSONObject jsonObject, PrepstmtDTOArray  arparam, HashMap<String, DataType> hmfielddbtype, InputDTO jsonInput, ResultDTO prevResultDTO) throws BackendException{
		//Where
//		String updatewhere = crudnode.selectSingleNode("sqlwhere").getText();
		String PATTERN = "\\#(inp|res|vs|ses)?\\.?([^,\\s\\|\\)]*)\\|?([^,\\s,\\|\\)]*)";//"\\:(\\w*)\\[?(\\d*)\\]?\\.?([^,\\s\\|]*)\\|?([^,\\s]*)";
		
		Pattern   pattern = Pattern.compile(PATTERN,Pattern.DOTALL|Pattern.MULTILINE);
		updatequery = updatequery.trim();
		logger.debug("Input Query:"+updatequery+" \nlength:"+updatequery.length());
		logger.debug("InputJSON record part="+jsonObject);
		logger.debug("PATTERN="+PATTERN);
		
		Matcher m1 = pattern.matcher(updatequery); // get a matcher object
	       int count = 0;
	       int end = 0;
	       String parsedquery = "";
	    try{   
	       while(m1.find()) {
	          
	          String prop =  m1.group();
	          logger.debug("Start preparing ["+prop +"] start="+ m1.start() + " end="+m1.end()+" grp1="+m1.group(1)+" grp2="+m1.group(2)+" grp3="+m1.group(3)+" ");
	          if(m1.group(1)!=null && ! "".equals(m1.group(1))){//do ognl because (inp|res|vs) is not ""
	        	  if("inp".equals( m1.group(1))){ //:form[0].param === :param use jsonObject and get group(3) val 
	        		  logger.debug(" Processing with #inputDTO");
	        		  String expr = m1.group(2);
	        		  String propval = ActionContext.getContext().getValueStack().findString("#inputDTO.data."+expr);
	        		  String propname;
	        		  propname = expr.substring(expr.lastIndexOf('.')+1, expr.length());
	        		  logger.debug("Ognl inp Expression result "+propname+" = "+propval);

	        			  parsedquery += updatequery.substring(end,m1.start());//
	        			  
	        			  if(!"".equals(m1.group(3)) ){
	        				  arparam.add(PrepstmtDTO.getDataTypeFrmStr(m1.group(3)),propval);
	        			  }else{
	        				  arparam.add(hmfielddbtype.get(propname),propval);
	        			  }
	        				   
	        			  
	        			 
	        			  parsedquery += "?";
	        		   
	        		  end = m1.end(); 
	        		  logger.info("Ognl inp Expression result inp "+propname+" = "+propval);
//	        		  logger.debug("This is not prefered Mode with dot"+m1.group(2)+". "+propname);
	        	  }else  if("res".equals( m1.group(1))){ //:formXX[0].param
	        		  logger.debug(" Processing with #resultDTO");
	        		  //TODO: implement for object filling from related panels.
	        		  String expr = m1.group(2);
	        		  //String propval = ActionContext.getContext().getValueStack().findString("#resultDTO.data."+expr);
	        		  String propval = ActionContext.getContext().getValueStack().findString("#resultDTO.data."+expr);
	        		  String propname;
	        		  propname = expr.substring(expr.lastIndexOf('.')+1, expr.length());
	        		  logger.info("Ognl inp Expression result res "+propname+" = "+propval);
	        		  
	        		  if(!"".equals(m1.group(3)) ){
        				  arparam.add(PrepstmtDTO.getDataTypeFrmStr(m1.group(3)),propval);
        			  }else{
        				  arparam.add(hmfielddbtype.get(propname),propval);
        			  }
	        		  
	        		  parsedquery += updatequery.substring(end,m1.start());//
	        		  parsedquery += "?";
	        		  end = m1.end(); 
	        	  }else if("vs".equals( m1.group(1))){
	        		  logger.debug(" Processing with ValueStack");
	        		  //TODO: implement for object filling from related panels.
	        		  String expr = m1.group(2);
	        		  //expr = expr.substring(4); //remove ':vs' in :vs.xxe[].xp
	        		  String propval = ActionContext.getContext().getValueStack().findString(expr);
	        		  String propname;
	        		  propname = expr.substring(expr.lastIndexOf('.')+1, expr.length());
	        		  logger.info("Ognl inp Expression result vs "+propname+" = "+propval);
	        		  
	        		  if(!"".equals(m1.group(3)) ){
        				  arparam.add(PrepstmtDTO.getDataTypeFrmStr(m1.group(3)),propval);
        			  }else{
        				  arparam.add(hmfielddbtype.get(propname),propval);
        			  }
	        		  
	        		  parsedquery += updatequery.substring(end,m1.start());//
	        		  parsedquery += "?";
	        		  end = m1.end();  
	        	  }else if("ses".equals(m1.group(1))){
	        		  logger.debug("Process with session");
	        		  String expr = m1.group(2);
	        		  //expr = expr.substring(4); //remove ':ses' in :vs.xxe[].xp
	        		  String propval = (String) ActionContext.getContext().getSession().get(expr);
	        		  String propname;
	        		  propname = expr.substring(expr.lastIndexOf('.')+1, expr.length());
	        		  logger.debug("Ognl inp Expression result sess "+propname+" = "+propval);
	        		  
	        		  if(!"".equals(m1.group(3)) ){
        				  arparam.add(PrepstmtDTO.getDataTypeFrmStr(m1.group(3)),propval);
        			  }else{
        				  arparam.add(hmfielddbtype.get(propname),propval);
        			  }
	        		  
	        		  parsedquery += updatequery.substring(end,m1.start());//
	        		  parsedquery += "?";
	        		  end = m1.end();
	        	  }else{
	        		  parsedquery += updatequery.substring(end,m1.start());//adding expression for easy error debuggin
	        		  parsedquery += m1.group()+"[ERROR]";
	        		 logger.fatal("BUG BUG Unsupported Ognl expression in SQL"+m1.group());
	        		 throw new QueryParseException(parsedquery);
	        	  }
	          }else{ //fill with present panel row object :formxparam
	        	  logger.debug(" Processing without jsonRecord property="+m1.group(2));
	        	  String propval;
	        	  if(jsonObject!=null && jsonObject.has(m1.group(2)) ){
	        		  String propname = m1.group(2);
	        		  propval = jsonObject.getString(m1.group(2));
	        		  parsedquery += updatequery.substring(end,m1.start());//
	        		  logger.info("Ognl inp Expression result "+propname+" = "+propval);
	        		  
	        		  if(!"".equals(m1.group(3)) ){
        				  arparam.add(PrepstmtDTO.getDataTypeFrmStr(m1.group(3)),propval);
        			  }else{
        				  arparam.add(hmfielddbtype.get(propname),propval);
        			  }
        			   
	        		  parsedquery += "?";
	        	  }else{
	        		  parsedquery += updatequery.substring(end,m1.start());//adding expression for easy error debuggin
	        		  parsedquery += m1.group()+"[ERROR]";
	        		  logger.error("BUG BUG Property="+m1.group(2)+" not found in input JSON record JSON="+jsonObject);
	        		  throw new QueryParseException(parsedquery);
	        	  }
	        	  
        		  end = m1.end(); 
//        		  logger.debug("else no dot "+m1.group(2));
	          }
	          
	         
	          count++;
	       }
		   logger.debug("Last part end="+end+ " Query part to append:"+updatequery.substring(end));
	       parsedquery += updatequery.substring(end);
	       updatequery = parsedquery;
	       logger.debug("Parsed Query:"+ parsedquery);
	    }catch(QueryParseException e){
	    	throw new BackendException("error.queryparsing",e);
	    } catch (DataTypeException e) {
	    	logger.error("error.queryparsing", e);
	    	throw new BackendException("error.queryparsing",e);
		}
	       return parsedquery;
	}
	
}
