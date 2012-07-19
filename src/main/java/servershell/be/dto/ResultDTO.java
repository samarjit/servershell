package servershell.be.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

public class ResultDTO {

private List<String> messages;
private List<String> errors;
private Map<String, List<String>> fieldErrors;
private HashMap<String, Object>  data;
private Map<String,Map<String,Integer>> pagination; //{currentpage:,totalpage:,totalrec:,pagesize:}
private String result; //it will override the result described in command result type in screen xml
private Map<String, String> sessionvars;
private String resultScrName;

public ResultDTO() {
	data = new HashMap<String,Object>();
	errors = new ArrayList<String>();
	messages = new ArrayList<String>();
	pagination = new HashMap<String, Map<String,Integer>>();
	HashMap<String, Integer> hm = new HashMap<String, Integer>();
	hm.put("currentpage",1);
	hm.put("totalpage",1);
	hm.put("totalrec",1);
	hm.put("pagesize",1);
	pagination.put("formx", hm);
}


public void addError(String e){
	errors.add(e);
}

public void addMessage(String m){
	messages.add(m);
}

//public String toString(){
//	return "MESSAGE: from toString()";//+messages+",ERRORS:"+errors+",DATA:"+data;
//}


/**
 * @param panelname
 * @param currentpage
 * @param totalpages
 * @param totalrec
 * @param pagesize
 */
public void setPageDetails(String panelname,int currentpage, int totalpages,int totalrec, int pagesize) {
	 if(pagination ==null)pagination = new HashMap<String, Map<String,Integer>>();
	 Map<String, Integer> hm = pagination.get(panelname);
	 if(hm != null ){
		 hm.put("currentpage",currentpage);
		 hm.put("totalpage",totalpages);
		 hm.put("totalrec",totalrec);
		 hm.put("pagesize",pagesize);
	 }else{
		 hm = new HashMap<String, Integer>();
		 hm.put("currentpage",currentpage);
		 hm.put("totalpage",totalpages);
		 hm.put("totalrec",totalrec);
		 hm.put("pagesize",pagesize);
	 }
	 pagination.put(panelname, hm);
}

public List<String> getMessages() {
	return messages;
}
public void setMessages(List<String> messages) {
	this.messages = messages;
}
public List<String> getErrors() {
	return errors;
}
public void setErrors(List<String> errors) {
	this.errors = errors;
}
public HashMap<String, Object>  getData() {
	return data;
}
public void setData(HashMap<String, Object>  jobj) {
	this.data = jobj;
}

private void setPagination(Map<String, Map<String, Integer>> pagination) {
	this.pagination = pagination;
}
public Map<String, Map<String, Integer>> getPagination() {
	return pagination;
}

public Map<String, List<String>> getFieldErrors() {
	return fieldErrors;
}


public void setFieldErrors(Map<String, List<String>> fieldErrors) {
	this.fieldErrors = fieldErrors;
}

public void addFieldError(String fieldName, String errorText){
	List<String> tfieldErrors = fieldErrors.get(fieldName);
	if(tfieldErrors != null && tfieldErrors.size() > 0){
		tfieldErrors.add(errorText);
	}else{
		ArrayList<String> tmpFieldErrors = new ArrayList<String>();
		tmpFieldErrors.add(errorText);
		fieldErrors.put(fieldName, tmpFieldErrors);
	}
}

/**
 * Merges tempDTO into this object. It overrites previous object with same outstack for pagination. Like previous pagination 
 * with form1:{val1:1} will be overwritten by input form1:{val2:2}. It wont become form1:{val1:1,val2:2} instead the output will be form1:{val2};
 * But with "data", an attempt will be made to merge with depth 2. So the above case will be merged in case of data. But if this fails, it will fall back 
 * and act like pagination merge.
 *  
 * @param tempDTO
 */
 
@SuppressWarnings("unchecked")
public void merge(ResultDTO tempDTO){
	

	HashMap<String, Object> tempdata = tempDTO.getData();
	Map<String, Map<String, Integer>> temppagination = tempDTO.getPagination();
	for (String keyi : tempdata.keySet()) {
		Object val = tempdata.get(keyi);
		Object thisdataval = null;
		if(data.containsKey(keyi))
			thisdataval  = data.get(keyi);
		
		if(thisdataval!=null && val instanceof List<?> && thisdataval instanceof List<?> ){
			((List<Map<String,String>>)thisdataval).addAll((List<Map<String, String>>) val); 
		}else{
			data.putAll(tempDTO.getData());
		}
	}
	
	
	errors.addAll(tempDTO.getErrors());
	messages.addAll(tempDTO.getMessages());
	pagination.putAll(tempDTO.getPagination());
	result = tempDTO.getResult();
	
	if(tempDTO.getSessionvars()!=null){
		if(sessionvars == null)
			sessionvars = new HashMap<String, String>();
	sessionvars.putAll(tempDTO.getSessionvars());
	}
}


public String getResult() {
	return result;
}


public void setResult(String result) {
	this.result = result;
}


public Map<String, String> getSessionvars() {
	return sessionvars;
}


public void setSessionvars(Map<String, String> sessionvars) {
	this.sessionvars = sessionvars;
}


public String getResultScrName() {
	return resultScrName;
}


public void setResultScrName(String resultScrName) {
	this.resultScrName = resultScrName;
}


public static ResultDTO fromJsonString(JSONObject resDTOjson){
	ResultDTO tempDTO = new ResultDTO();
	 HashMap<String,Object> tmpHm = new HashMap<String, Object>();
	 JSONObject data1 = resDTOjson.getJSONObject("data");
	 tmpHm.putAll(data1);
	 tempDTO.setData(tmpHm);
	 tempDTO.setErrors(resDTOjson.getJSONArray("errors"));
	 tempDTO.setMessages(resDTOjson.getJSONArray("messages"));
	 Map<String, Map<String,Integer>> pagination =   (Map<String, Map<String, Integer>>) resDTOjson.getJSONObject("pagination");
	 System.out.println(pagination);
	 tempDTO.setPagination(pagination);
	 tempDTO.setSessionvars(resDTOjson.getJSONObject("sessionvars"));
	 if(resDTOjson.get("result")!=null)
	   tempDTO.setResult(resDTOjson.getString("result"));
	return tempDTO;
	
}

}
