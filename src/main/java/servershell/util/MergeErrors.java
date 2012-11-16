package servershell.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class MergeErrors {

	public static Map<String,Object> mergeErrorsAndJsonStr(Map<String,Object> jobj, String bejsonString){
			Map<String,Object> bejson = new Gson().fromJson(bejsonString, Map.class);
		return mergeErrorsAndJson(  jobj,   bejson);
	}
	
	public static Map<String,Object> mergeErrorsOnlyStr(Map<String,Object> jobj, String bejsonString){
		Map<String,Object> bejson = new Gson().fromJson(bejsonString, Map.class);
		return mergeErrorsAndJson(  jobj,   bejson);
	}
	
	/**
	 * @param jobj json object to be merged into, original contents are retained, the same is returned
	 * @param bejson json to be merged from 
	 * @return jobj - merged jobj with jsonFrom actionErrors, actionMessages, fieldErrors
	 */
	@SuppressWarnings("unchecked") 
	public static Map<String,Object> mergeErrorsAndJson(Map<String,Object> jobj, Map<String,Object> bejson){
		ActionSupport action = (ActionSupport) ActionContext.getContext().getActionInvocation().getAction();
		if(bejson.containsKey("actionErrors")){
			List actionErrors = (List) bejson.get("actionErrors"); //array
			for (
			Iterator<String> itr = actionErrors.iterator(); itr.hasNext();) {
				action.addActionError(itr.next());
			}  
		}
		if(bejson.containsKey("actionMessages")){
			List actionMessages = (List) bejson.get("actionMessages"); //array
			for (
			Iterator<String> itr = actionMessages.iterator(); itr.hasNext();) {
				action.addActionMessage(itr.next());
			} 
		}
		if(bejson.containsKey("fieldErrors")){
			Map<String,Object> actionMessages = (Map<String, Object>) bejson.get("fieldErrors"); //object
			
			for (Object entry : actionMessages.entrySet()) {
				Entry<String,String> ent = (Entry<String, String>) entry;
				action.addFieldError(ent.getKey(), ent.getValue()); 
			} 
		}
		
		//merge
		for (Object entry : bejson.entrySet()) {
			Entry<String,Object> ent = (Entry<String, Object>) entry;
			String keyStr = ent.getKey();
			if(!("actionErrors".equals(keyStr)|| "actionMessages".equals(keyStr)|| "fieldErrors".equals(keyStr) )){
				if("jobj".equals(keyStr)){
//					Map<String,Object> jobjbe = Map<String,Object>.fromObject(ent.getValue());
					jobj.putAll((Map<String,Object>)ent.getValue());
				}else{
					jobj.put(keyStr, ent.getValue()); 
				}
			}
		} 
		
		return jobj;
	}
	
	
	@SuppressWarnings("unchecked") 
	public static Map<String,Object> mergeErrorsOnly(Map<String,Object> jobj, Map<String,Object> bejson){
		ActionSupport action = (ActionSupport) ActionContext.getContext().getActionInvocation().getAction();
		if(bejson.containsKey("actionErrors")){
			List<String> actionErrors = (List<String>) bejson.get("actionErrors");
			for (Iterator<String> itr = actionErrors.iterator(); itr.hasNext();) {
				action.addActionError(itr.next());
			}  
		}
		if(bejson.containsKey("actionMessages")){
			List<String> actionMessages = (List<String>) bejson.get("actionMessages");
			for (Iterator<String> itr = actionMessages.iterator(); itr.hasNext();) {
				action.addActionMessage(itr.next());
			} 
		}
		if(bejson.containsKey("fieldErrors")){
			Map<String,Object> actionMessages = (Map<String, Object>) bejson.get("fieldErrors"); //object
			
			for (Object entry : actionMessages.entrySet()) {
				Entry<String,String> ent = (Entry<String, String>) entry;
				action.addFieldError(ent.getKey(), ent.getValue()); 
			} 
		}
		
		return jobj;
	}

}
