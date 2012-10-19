package servershell.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class MergeErrors {

	public static JSONObject mergeErrorsAndJsonStr(JSONObject jobj, String bejsonString){
			JSONObject bejson = JSONObject.fromObject(bejsonString);
		return mergeErrorsAndJson(  jobj,   bejson);
	}
	
	public static JSONObject mergeErrorsOnlyStr(JSONObject jobj, String bejsonString){
		JSONObject bejson = JSONObject.fromObject(bejsonString);
		return mergeErrorsAndJson(  jobj,   bejson);
	}
	
	/**
	 * @param jobj json object to be merged into, original contents are retained, the same is returned
	 * @param bejson json to be merged from 
	 * @return jobj - merged jobj with jsonFrom actionErrors, actionMessages, fieldErrors
	 */
	@SuppressWarnings("unchecked") 
	public static JSONObject mergeErrorsAndJson(JSONObject jobj, JSONObject bejson){
		ActionSupport action = (ActionSupport) ActionContext.getContext().getActionInvocation().getAction();
		if(bejson.containsKey("actionErrors")){
			JSONArray actionErrors = bejson.getJSONArray("actionErrors");
			for (
			Iterator<String> itr = actionErrors.iterator(); itr.hasNext();) {
				action.addActionError(itr.next());
			}  
		}
		if(bejson.containsKey("actionMessages")){
			JSONArray actionMessages = bejson.getJSONArray("actionMessages");
			for (
			Iterator<String> itr = actionMessages.iterator(); itr.hasNext();) {
				action.addActionMessage(itr.next());
			} 
		}
		if(bejson.containsKey("fieldErrors")){
			JSONObject actionMessages = bejson.getJSONObject("fieldErrors");
			
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
//					JSONObject jobjbe = JSONObject.fromObject(ent.getValue());
					jobj.putAll((JSONObject)ent.getValue());
				}else{
					jobj.put(keyStr, ent.getValue()); 
				}
			}
		} 
		
		return jobj;
	}
	
	
	@SuppressWarnings("unchecked") 
	public static JSONObject mergeErrorsOnly(JSONObject jobj, JSONObject bejson){
		ActionSupport action = (ActionSupport) ActionContext.getContext().getActionInvocation().getAction();
		if(bejson.containsKey("actionErrors")){
			JSONArray actionErrors = bejson.getJSONArray("actionErrors");
			for (
					Iterator<String> itr = actionErrors.iterator(); itr.hasNext();) {
				action.addActionError(itr.next());
			}  
		}
		if(bejson.containsKey("actionMessages")){
			JSONArray actionMessages = bejson.getJSONArray("actionMessages");
			for (
					Iterator<String> itr = actionMessages.iterator(); itr.hasNext();) {
				action.addActionMessage(itr.next());
			} 
		}
		if(bejson.containsKey("fieldErrors")){
			JSONObject actionMessages = bejson.getJSONObject("fieldErrors");
			
			for (Object entry : actionMessages.entrySet()) {
				Entry<String,String> ent = (Entry<String, String>) entry;
				action.addFieldError(ent.getKey(), ent.getValue()); 
			} 
		}
		
		return jobj;
	}

}
