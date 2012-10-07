package com.ycs.fe.actions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionSupport;
import com.ycs.fe.cache.ScreenDetails;
import com.ycs.fe.commandprocessor.CommandProcessor;
import com.ycs.fe.dto.PaginationDTO;
import com.ycs.fe.dto.PagingFilterRule;
import com.ycs.fe.dto.PagingFilters;
import com.ycs.fe.dto.ResultDTO;
import com.ycs.fe.exception.FrontendException;
import com.ycs.fe.util.ScreenMapRepo;



/**
 * The following comment "remove this for strict security" should be removed to prevent from 
 * COLUMN names being sent in the request to work successfully.
 * @author Samarjit
 * @Date 28-Aug-2011
 *
 */
public class JqgridRpc extends ActionSupport {
	 
	private static final long serialVersionUID = -623830420192157346L;


	private Logger logger = Logger.getLogger(JqgridRpc.class);
	
	
	private String command;
	private String screenName;
	private InputStream inputStream;
	private String panelName;
	private String submitdata = "{}";
    private String data;
	private int page;
	private int rows;
	private String sidx;
	private String sord;

	private String filters;
	
	private String searchField;
	private String searchString;
	private String searchOper;
	private static final String STACK = "formpagination";
	public JqgridRpc() {
		super();
		screenName="ProgramSetup";
		panelName="form1";
		command="selectonload";
	}
	
	
	
	@Action(value="jqgrid",
			results={@Result(name="success",type="stream",params={"contentType","application/json","inputName","inputStream"})}
	)
	public String execute(){
		
		JSONObject jobj = null;
//		if("true".equals(command)){
			JSONObject oResult = new JSONObject();
//http://www.trirand.com/blog/jqgrid/server.php?q=2&_search=false&nd=1313507637422&rows=10&page=1&sidx=id&sord=desc			
//submitdata={form1:[{row:0,}],pagination:{form1:{currentpage:1,pagecount:200}}, bulkcmd:''...}
			ScreenDetails screenDetails = null;
			try {
				screenDetails = new ScreenMapRepo().findScreenDetails(screenName);
				HashMap<String, String> nameColumnMap = screenDetails.nameColumnMap;
				if(nameColumnMap.get(sidx)!=null)//remove this for strict security
					sidx = nameColumnMap.get(sidx);
				
				if(nameColumnMap.get(searchField)!=null)//remove this for strict security
					searchField = nameColumnMap.get(searchField);
				
			} catch (FrontendException e) {
				logger.error("screenDetailsRetrievalError",e);
			}
			
			JSONObject submitdataObj = JSONObject.fromObject(submitdata);
			PaginationDTO pageDTO = new PaginationDTO();
			pageDTO.setPage(page);
			pageDTO.setRows(rows);
			pageDTO.setSidx(sidx);
			pageDTO.setSord(sord);
			pageDTO.setSearchField(searchField);
			pageDTO.setSearchOper(searchOper);
			pageDTO.setSearchString(searchString);
			
			  if (filters != null && !"".equals(filters)) {
				  JSONObject filterJson = JSONObject.fromObject(filters);
				  JsonConfig jcfg = new JsonConfig();
				  jcfg.setRootClass(PagingFilters.class);
				PagingFilters filter = (PagingFilters) JSONObject.toBean(filterJson, jcfg);
				for (PagingFilterRule rule: filter.getRules()) {
					if(screenDetails.nameColumnMap.get(rule.getField()) !=null){ //remove this for strict security
						String aliasToColumn = screenDetails.nameColumnMap.get(rule.getField());
						rule.setField(aliasToColumn);
					}
				}
				System.out.println("filter from Gson:"+filter);
				if(filter != null)
					pageDTO.setFilters(filter);
				
				logger.debug("filters:" + JSONObject.fromObject(filter).toString() + " sord:" + sord + " sidx:" + sidx);
			  }
			  
			  JSONObject pagination = JSONObject.fromObject(pageDTO);
			  JSONObject pagestack = new JSONObject();
			  pagestack.put(STACK, pagination);
			  submitdataObj.put("pagination", pagestack);
//			  JSONObject bulkcmd = JSONObject.fromObject("{'bulkcmd':'gridtest'}");
//			  submitdataObj.put("bulkcmd", "prodgrid");
			  
			  logger.debug("send to BE :"+submitdataObj.toString());
			  			CommandProcessor cmdpr = new CommandProcessor();
			  			ResultDTO resDTO = cmdpr.commandProcessor(submitdataObj, screenName);
			  			logger.debug("back from cmd processor:"+ JSONObject.fromObject(resDTO));
			  			logger.debug("back from cmd processor pagination:"+  resDTO.getPagination());
			
			
			//convert back to jqgrid format
			/*for (Object row1 : jrow2) {
				JSONObject eachRow = (JSONObject)row1;
				String oid = null;
				JSONArray ocells = new JSONArray();
				JSONObject oRow = new JSONObject(); 
				for (Iterator keyItr = eachRow.keys(); keyItr.hasNext();) {
					String key = (String) keyItr.next();
					String value = eachRow.getString(key);
					
					if(oid == null)oid = key;
					ocells.add(value);
				}
				oRow.put("id", oid);
				oRow.put("cell", ocells);
				oAllrows.add(oRow);
			}
			*/
			  			
			JSONArray jrow2 = null;  			
			if (resDTO != null && JSONObject.fromObject(resDTO.getData()).containsKey(STACK)) {
				  jrow2 = JSONObject.fromObject(resDTO.getData()).getJSONArray(STACK);
				Map<String, Map<String, Integer>> pagingMultiForm = resDTO.getPagination();
			    Map<String, Integer> pageingRet = pagingMultiForm.get(STACK);
				if (pageingRet != null) {
					oResult.put("page", pageingRet.get("currentpage"));
					oResult.put("total", pageingRet.get("totalpage"));
					oResult.put("records", pageingRet.get("totalrec"));
					oResult.put("pagesize", pageingRet.get("pagesize"));
				}
				// oResult.put("rows", oAllrows );
				oResult.put("rows", jrow2);
				//"errors":[],"fieldErrors":null,"messages":["SUCCESS:10|"]
				JSONObject userdata = new JSONObject();
				userdata.put("errors", resDTO.getErrors());
				
				userdata.put("fieldErrors", (resDTO.getFieldErrors()==null)?null:resDTO.getFieldErrors());
				userdata.put("messages", resDTO.getMessages());
				
				oResult.put("userdata", userdata);

				jobj = oResult;
			}else{
				//error occurred. 
				jobj = new JSONObject();
				JSONObject userdata = new JSONObject();
				userdata.put("errors", resDTO.getErrors());
				
				userdata.put("fieldErrors", (resDTO.getFieldErrors()==null)?null:resDTO.getFieldErrors());
				userdata.put("messages", resDTO.getMessages());
				
				jobj.put("userdata", userdata);

			}
//		}
		logger.debug(jobj.toString());
//		JSONObject jobj = JSONObject.fromObject(resDTO);
//		try {
//			jobj.put("data",resDTO.getData());
//			jobj.put("pagination",resDTO.getPagination());
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		String json = jobj.toString();
		
		
		logger.debug("Sent back to client:"+json);
		inputStream = new ByteArrayInputStream(json.getBytes());
		 
		return "success";
	}
	public static void main(String[] args) {
	}
	
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getCommand() {
		return command;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setPanelName(String panelName) {
		this.panelName = panelName;
	}
	public String getPanelName() {
		return panelName;
	}
	public void setSubmitdata(String submitdata) {
		this.submitdata = submitdata;
	}
	public String getSubmitdata() {
		return submitdata;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getData() {
		return data;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public String getSidx() {
		return sidx;
	}
	public void setSidx(String sidx) {
		this.sidx = sidx;
	}
	public String getSord() {
		return sord;
	}
	public void setSord(String sord) {
		this.sord = sord;
	}

 

 



	public String getSearchField() {
		return searchField;
	}
	

	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}
	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	public String getSearchOper() {
		return searchOper;
	}
	public void setSearchOper(String searchOper) {
		this.searchOper = searchOper;
	}



	public String getFilters() {
		return filters;
	}



	public void setFilters(String filters) {
		this.filters = filters;
	}
	
	
 
	 
}
