package com.ycs.fe.actions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.google.gson.Gson;
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
	
	
	
	@Action(value="jqgrid",params={"configxml","ProductSetup.xml"},
			results={@Result(name="success",type="stream",params={"contentType","application/json","inputName","inputStream","resultxml","ProductSetup.xml"})}
//	results={@Result(name="success",location="/test.jsp")}
	)
	public String execute(){
//		System.out.println("js RPC called with command:"+command+" for screen:"+screenName);
//		BaseBL bl = BusinessLogicFactory.getBusinessLogic(screenName);
//		
//		if(bl != null)
//		  bl.preJsRPCListerner(ActionContext.getContext().getActionInvocation());
//		
//		String path = ScreenMapRepo.findMapXMLPath(screenName);
//		String parsedquery = "";
//		ResultDTO resDTO = new ResultDTO();
//		
//		ValueStack stack = ActionContext.getContext().getValueStack();
//		try {
//			 logger.debug(path);
//				Document doc = new SAXReader().read(path);
//				Element root = doc.getRootElement();
//				 
//				logger.debug("JsonRPC with submitdata="+submitdata);
//				JSONObject submitdataObj = JSONObject.fromObject(submitdata);
//			
//				InputDTO inputDTO = new InputDTO();
//				inputDTO.setData((JSONObject) submitdataObj);
//				
//				PaginationDTO pageDetails = new PaginationDTO(page,rows,sidx,sord);
//				inputDTO.setPagination(pageDetails);
//				
//				ActionContext.getContext().getValueStack().getContext().put("inputDTO", inputDTO);
//				
//				CommandProcessor cmdpr = new CommandProcessor();
//				resDTO = cmdpr.commandProcessor(submitdataObj, screenName);  
//			 
//		} catch (DocumentException e) {
//			resDTO.addError("ERROR:"+e);
//			e.printStackTrace();
//		} catch (Exception e) {
//			resDTO.addError("ERROR:"+e);
//			e.printStackTrace();
//		}
//		
//				
//		logger.debug(stack.getContext().get("resultDTO"));
//		
//		if(bl !=null)
//		 bl.postJsRPCListerner(ActionContext.getContext().getActionInvocation());
//		
//		Gson gson = new Gson();
//		String json1 = gson.toJson(stack.getContext().get("resultDTO"));
//		logger.debug("Gson result(not sent back to client):"+json1);
////		setResultDTO((ResultDTO)stack.getContext().get("resultDTO"));
//		
////		try {
////			OgnlContext context = new OgnlContext();
////			Object expression = Ognl.parseExpression("resultDTO.data.form1[0].txtnewprogname");
////			logger.debug(Ognl.getValue(expression,stack.getContext()));
//			logger.debug(stack.findString("#resultDTO.data.form1[0].countryofissue" ));
////		} catch (OgnlException e1) {
////			e1.printStackTrace();
////		}
		
			String jj = "{'page':'1','total':2,'records':'13','rows':[{'id':'13','cell':['13','2007-10-06','Client3','1000.00','0.00','1000.00','']}," +
					"{'id':'12','cell':['12','2007-10-06','Client2','700.00','140.00','840.00','']}," +
					"{'id':'11','cell':['11','2007-10-06','Client1','600.00','120.00','720.00','']}," +
					"{'id':'10','cell':['10','2007-10-06','Client2','100.00','20.00','120.00','']}," +
					"{'id':'9','cell':['9','2007-10-06','Client1','200.00','40.00','240.00','']}," +
					"{'id':'8','cell':['8','2007-10-06','Client3','200.00','0.00','200.00','']}," +
					"{'id':'7','cell':['7','2007-10-05','Client2','120.00','12.00','134.00','']}," +
					"{'id':'6','cell':['6','2007-10-05','Client1','50.00','10.00','60.00','']}," +
					"{'id':'5','cell':['5','2007-10-05','Client3','100.00','0.00','100.00','no tax at all']}," +
					"{'id':'4','cell':['4','2007-10-04','Client3','150.00','0.00','150.00','no tax']}]," +
					"'userdata':{'amount':3220,'tax':342,'total':3564,'name':'Totals:'}}";
			
			String jj2= "{'page':'1','total':34,'records':'34','rows':[{\"PLASTIC_CODE\":\"FEVCUS-FEVO Customer design\",\"PLASTIC_DESC\":\"FEVO Customer design\",\"PRODUCT_CODE\":\"TEST01\",\"PRODUCT_NAME\":\"TESTING\"}," + 
					"{\"PLASTIC_CODE\":\"EMVBLK-EMV Black(unprinted)\",\"PLASTIC_DESC\":\"EMV Black(unprinted)\",\"PRODUCT_CODE\":\"EMV083\",\"PRODUCT_NAME\":\"EMV Generic Product\"}," + 
					"{\"PLASTIC_CODE\":\"EMVBLK-EMV Black(unprinted)\",\"PLASTIC_DESC\":\"EMV Black(unprinted)\",\"PRODUCT_CODE\":\"EMV040\",\"PRODUCT_NAME\":\"EMV Reloadable Generic\"}," + 
					"{\"PLASTIC_CODE\":\"EMVBLK-EMV Black(unprinted)\",\"PLASTIC_DESC\":\"EMV Black(unprinted)\",\"PRODUCT_CODE\":\"EMV093\",\"PRODUCT_NAME\":\"EMV Reloadable SE OTC\"}," + 
					"{\"PLASTIC_CODE\":\"FEVCUS-FEVO Customer design\",\"PLASTIC_DESC\":\"FEVO Customer design\",\"PRODUCT_CODE\":\"EMV081\",\"PRODUCT_NAME\":\"EMV081\"}," + 
					"{\"PLASTIC_CODE\":\"EMVBLK4-EMV RLDB Corporate\",\"PLASTIC_DESC\":\"EMV RLDB Corporate\",\"PRODUCT_CODE\":\"EMV085\",\"PRODUCT_NAME\":\"EMV RLDB Corporate\"}," + 
					"{\"PLASTIC_CODE\":\"EMVBLK5-RLDB Bulk\",\"PLASTIC_DESC\":\"RLDB Bulk\",\"PRODUCT_CODE\":\"EMV086\",\"PRODUCT_NAME\":\"RLDB Bulk\"}," + 
					"{\"PLASTIC_CODE\":\"EMV-NFC\",\"PLASTIC_DESC\":\"NFC\",\"PRODUCT_CODE\":\"EMV087\",\"PRODUCT_NAME\":\"NFC\"}," + 
					"{\"PLASTIC_CODE\":\"EMVBLK-EMV Black(unprinted)\",\"PLASTIC_DESC\":\"EMV Black(unprinted)\",\"PRODUCT_CODE\":\"EMV094\",\"PRODUCT_NAME\":\"EMV GIFT OTC\"}," + 
					"{\"PLASTIC_CODE\":\"FEVSTD-FEVO Standard design\",\"PLASTIC_DESC\":\"FEVO Standard design\",\"PRODUCT_CODE\":\"FEVO04\",\"PRODUCT_NAME\":\"Standard RoadShow Gift\"}," + 
					"{\"PLASTIC_CODE\":\"FEVCUS-FEVO Customer design\",\"PLASTIC_DESC\":\"FEVO Customer design\",\"PRODUCT_CODE\":\"FEGTCU\",\"PRODUCT_NAME\":\"FEVO Gift card Customized\"}," + 
					"{\"PLASTIC_CODE\":\"FEVSTD-FEVO Standard design\",\"PLASTIC_DESC\":\"FEVO Standard design\",\"PRODUCT_CODE\":\"FEGTST\",\"PRODUCT_NAME\":\"FEVO Gift card Standard\"}," + 
					"{\"PLASTIC_CODE\":\"FEVWHT-FEVO White (unprinted) design\",\"PLASTIC_DESC\":\"FEVO White (unprinted) design\",\"PRODUCT_CODE\":\"FERSCU\",\"PRODUCT_NAME\":\"FEVO Road show Customized\"}," + 
					"{\"PLASTIC_CODE\":\"EZLSTD-EZL Standard design\",\"PLASTIC_DESC\":\"EZL Standard design\",\"PRODUCT_CODE\":\"FEVOSP\",\"PRODUCT_NAME\":\"FEVO Special Edition\"}," + 
					"{\"PLASTIC_CODE\":\"EZLWHT-EZL Standard design\",\"PLASTIC_DESC\":\"EZL Standard design\",\"PRODUCT_CODE\":\"EZLCUS\",\"PRODUCT_NAME\":\"Personalised FEVO with ez-link\"}," + 
					"{\"PLASTIC_CODE\":\"EZLSTD-EZL Standard design\",\"PLASTIC_DESC\":\"EZL Standard design\",\"PRODUCT_CODE\":\"EZLSPS\",\"PRODUCT_NAME\":\"FEVO Special Edition\"}," + 
					"{\"PLASTIC_CODE\":\"EZLSTD-EZLINK Standard design\",\"PLASTIC_DESC\":\"EZLINK Standard design\",\"PRODUCT_CODE\":\"ECLC52\",\"PRODUCT_NAME\":\"Fevo W EZL customised\"}," + 
					"{\"PLASTIC_CODE\":\"EZLWHT-EZLINK Custom design\",\"PLASTIC_DESC\":\"EZLINK Custom design\",\"PRODUCT_CODE\":\"EZLS56\",\"PRODUCT_NAME\":\"OTC FEVO w EZL Customized\"}," + 
					"{\"PLASTIC_CODE\":\"FEVWHT-FEVO White (unprinted) design\",\"PLASTIC_DESC\":\"FEVO White (unprinted) design\",\"PRODUCT_CODE\":\"EZLS21\",\"PRODUCT_NAME\":\"SPECIAL EDITION - FEVO\"}," + 
					"{\"PLASTIC_CODE\":\"EZLSTD-EZLINK Standard design\",\"PLASTIC_DESC\":\"EZLINK Standard design\",\"PRODUCT_CODE\":\"EZLS53\",\"PRODUCT_NAME\":\"Road show FEVO w EZL Standard\"}," + 
					"{\"PLASTIC_CODE\":\"EZLWHT-EZLINK Custom design\",\"PLASTIC_DESC\":\"EZLINK Custom design\",\"PRODUCT_CODE\":\"EZLC54\",\"PRODUCT_NAME\":\"Road show FEVO w EZL Customised\"}," + 
					"{\"PLASTIC_CODE\":\"EZLSTD-EZLINK Standard design\",\"PLASTIC_DESC\":\"EZLINK Standard design\",\"PRODUCT_CODE\":\"FEVO02\",\"PRODUCT_NAME\":\"Customised Online Gift\"}," + 
					"{\"PLASTIC_CODE\":\"FEVSTD-FEVO Standard design|FEVO White (unprinted) design\",\"PLASTIC_DESC\":\"FEVO Standard design\",\"PRODUCT_CODE\":\"FEVO01\",\"PRODUCT_NAME\":\"Standard Online Gift\"}," + 
					"{\"PLASTIC_CODE\":\"FEVSTD-FEVO Standard design|FEVO White (unprinted) design\",\"PLASTIC_DESC\":\"FEVO Standard design\",\"PRODUCT_CODE\":\"ECLC51\",\"PRODUCT_NAME\":\"Fevo W EZL standard\"}," + 
					"{\"PLASTIC_CODE\":\"EZLWHT-EZLINK Custom design\",\"PLASTIC_DESC\":\"EZLINK Custom design\",\"PRODUCT_CODE\":\"EZLS22\",\"PRODUCT_NAME\":\"SPECIAL EDITION - EZL\"}," + 
					"{\"PLASTIC_CODE\":\"FEVCUS-FEVO Customer design\",\"PLASTIC_DESC\":\"FEVO Customer design\",\"PRODUCT_CODE\":\"FEVO03\",\"PRODUCT_NAME\":\"Customised RoadShow Gift\"}," + 
					"{\"PLASTIC_CODE\":\"EZLSTD-EZLINK Standard design\",\"PLASTIC_DESC\":\"EZLINK Standard design\",\"PRODUCT_CODE\":\"EZONLI\",\"PRODUCT_NAME\":\"EZONLI\"}," + 
					"{\"PLASTIC_CODE\":\"EZLSTD-EZLINK Standard design\",\"PLASTIC_DESC\":\"EZLINK Standard design\",\"PRODUCT_CODE\":\"EZLS55\",\"PRODUCT_NAME\":\"OTC FEVO w EZL Standard\"}," + 
					"{\"PLASTIC_CODE\":\"EZLWHT-EZLINK Custom design\",\"PLASTIC_DESC\":\"EZLINK Custom design\",\"PRODUCT_CODE\":\"EZLS24\",\"PRODUCT_NAME\":\"OTC SPECIAL EDITION - EZL\"}," + 
					"{\"PLASTIC_CODE\":\"FEVSTD-FEVO Standard design\",\"PLASTIC_DESC\":\"FEVO Standard design\",\"PRODUCT_CODE\":\"EPFEVO\",\"PRODUCT_NAME\":\"EPFEVO MAIN\"}," + 
					"{\"PLASTIC_CODE\":\"EMVBLK-EMV Black(unprinted)\",\"PLASTIC_DESC\":\"EMV Black(unprinted)\",\"PRODUCT_CODE\":\"EMV082\",\"PRODUCT_NAME\":\"EMV Generic\"}, " + 
					"{\"PLASTIC_CODE\":\"EMVBLK-EMV Black(unprinted)\",\"PLASTIC_DESC\":\"EMV Black(unprinted)\",\"PRODUCT_CODE\":\"EMV084\",\"PRODUCT_NAME\":\"EMV084 Generic Product Type\"}, " + 
					"{\"PLASTIC_CODE\":\"EMVBLK-EMV Black(unprinted)\",\"PLASTIC_DESC\":\"EMV Black(unprinted)\",\"PRODUCT_CODE\":\"EMV091\",\"PRODUCT_NAME\":\"EMV Reloadable Generic\"}," + 
					"{\"PLASTIC_CODE\":\"EMVBLK-EMV Black(unprinted)\",\"PLASTIC_DESC\":\"EMV Black(unprinted)\",\"PRODUCT_CODE\":\"EMV092\",\"PRODUCT_NAME\":\"EMV Reloadable SE\"}]" + 
					"}";
		JSONObject jobj = null;
		jobj = JSONObject.fromObject(jj);
		if("true".equals(command)){
			JSONObject oResult = new JSONObject();
			JSONArray oAllrows = new JSONArray(); 
			JSONObject jobj2 = JSONObject.fromObject(jj2);
			JSONArray jrow = jobj2.getJSONArray("rows");
//http://www.trirand.com/blog/jqgrid/server.php?q=2&_search=false&nd=1313507637422&rows=10&page=1&sidx=id&sord=desc			
//submitdata={form1:[{row:0,}],pagination:{form1:{currentpage:1,pagecount:200}}, bulkcmd:''...}
			ScreenDetails screenDetails = null;
			try {
				screenDetails = ScreenMapRepo.findScreenDetails(screenName);
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
				PagingFilters filter = new Gson().fromJson(filters, PagingFilters.class);
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
			
			  JSONArray jrow2 = null;
			   if(JSONObject.fromObject(resDTO.getData()).containsKey(STACK))   
			     jrow2 =  JSONObject.fromObject(resDTO.getData()).getJSONArray(STACK);
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
			Map<String, Map<String, Integer>> pagingMultiForm = resDTO.getPagination();
			Map<String, Integer> pageingRet = pagingMultiForm.get(STACK);
			if(pageingRet != null){
				oResult.put("page", pageingRet.get("currentpage"));
				oResult.put("total", pageingRet.get("totalpage"));
				oResult.put("records", pageingRet.get("totalrec"));
				oResult.put("pagesize", pageingRet.get("pagesize") );
			}
//			oResult.put("rows", oAllrows );
			oResult.put("rows", jrow2);
			//"errors":[],"fieldErrors":null,"messages":["SUCCESS:10|"]
			JSONObject userdata = new JSONObject();
			userdata.put("errors", resDTO.getErrors());
			
			userdata.put("fieldErrors", (resDTO.getFieldErrors()==null)?null:resDTO.getFieldErrors());
			userdata.put("messages", resDTO.getMessages());
			
			oResult.put("userdata", userdata);
			jobj = oResult;
		}
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
