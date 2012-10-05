package com.ycs.oldfe.actions;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.ParameterAware;

import servershell.be.dto.InputDTO;
import servershell.be.dto.PageReturnType;
import servershell.be.dto.ResultDTO;

import com.opensymphony.xwork2.ActionContext;



/**
 * This action class is intended to be used for simple form data submits as normal forms. It will
 * serialize the forms data into backend json format and after that follow. The bulkcmd is formed by appending oper to bulkcmd.
 * submitdata={bulkcmd:'somecmd+oper',form1=[{key:val,keyy2:val2}]}
 * @author Samarjit
 *
 */
public class SimpleFormAction extends CommonActionSupport implements ParameterAware {
	private  Logger logger = Logger.getLogger(SimpleFormAction.class);
	private String bulkcmd;
	private static final long serialVersionUID = 1L;
	private Map<String, String[]> parameters;

	@Action(value="simpleform",results={@Result(name="ajax",type="stream",params={"contentType","text/html","inputName","inputStream"})}
	)
	public String execute() throws Exception{
		String resultHtml = "{}";
		ResultDTO resDTO = new ResultDTO() ;
		try {
			JSONObject submitdataObj = new JSONObject();
			JSONArray form1ar = new JSONArray();
			JSONObject form1obj= new JSONObject();
			
			if(bulkcmd != null){
				
				for (Entry<String, String[]> entry : parameters.entrySet()) {
					String[] val = entry.getValue();
					String key = entry.getKey();
					key = new ReplaceAlias().replaceAlias(screenName, key);
					if(!key.equals("bulkcmd") && !key.equals("screenName") && !key.equals("oper")){
						if(val.length== 1){
							form1obj.put(key, val[0]);
						}else{
							List<String> arVal = Arrays.asList(val);
							form1obj.put(key, arVal);
						}
					}		
					if(key.equals("oper")){
						bulkcmd += val[0];
					}
				}
				
				form1ar.add(form1obj);
				submitdataObj.put("form1", form1ar);
				submitdataObj.put("bulkcmd", bulkcmd);
				
				submitdata = submitdataObj.toString();
				System.out.println("Suitable to send to BE? screenName:"+screenName+" submitdata:"+submitdataObj.toString());
				JSONObject jsonRecord =   submitdataObj;
				//for local processing
				InputDTO inputDTO = new InputDTO();
				inputDTO.setData((JSONObject) jsonRecord);
				ActionContext.getContext().getValueStack().getContext().put("inputDTO", inputDTO);
				
				resDTO = validate(jsonRecord);
				resDTO = commandProcessor(jsonRecord, resDTO);
				
			
				aresDTO  = resDTO;
			
			}else{
				resDTO.addError("bulkcmd.required");
			}
			populateActionErrors(resDTO);
			JSONObject resjsonResult = JSONObject.fromObject(resDTO);
			
			resultHtml = resjsonResult.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			resultHtml ="{'error':'"+e.getLocalizedMessage()+"'}";
		}
		logger.debug("End SimpleFormAction Sent back to client:"+resultHtml);
		inputStream = new ByteArrayInputStream(resultHtml.getBytes());
		
		System.out.println("result beginning to process");
		JSONObject jsonRecord = new JSONObject(true);
		if(bulkcmd!= null){
			jsonRecord = new JSONObject();
			jsonRecord.put("bulkcmd", bulkcmd);
		}
		PageReturnType pg = setResult(resultHtml, jsonRecord, resDTO);
		logger.error("Result Page:"+ pg.resultName);
		return pg.resultName;
		//return SUCCESS;//commonExecute();
	}


	public String getBulkcmd() {
		return bulkcmd;
	}


	public void setBulkcmd(String bulkcmd) {
		this.bulkcmd = bulkcmd;
	}


	@Override
	public void setParameters(Map<String, String[]> parameters) {
		this.parameters = parameters;
	}
}
