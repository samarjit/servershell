package com.ycs.fe.actions;

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

import com.ycs.fe.dto.PageReturnType;
import com.ycs.fe.dto.ResultDTO;
import com.ycs.fe.exception.FrontendException;
import com.ycs.fe.util.ReplaceAlias;



/**
 * This action class is intended to be used for simple form data submits as normal forms. It will
 * serialize the forms data into backend json format and after that follow. The bulkcmd is formed by appending oper to bulkcmd.
 * submitdata={bulkcmd:'somecmd+oper',form1=[{key:val,keyy2:val2}]}
 * @author Samarjit
 *
 */
public class SimpleFormAction extends CommonActionSupport implements ParameterAware {
	private static Logger logger = Logger.getLogger(SimpleFormAction.class);
	private String bulkcmd;
	private static final long serialVersionUID = 1L;
	private Map<String, String[]> parameters;

	@Action(value="simpleform",results={
			@Result(name="success",type="stream",params={"contentType","text/html","inputName","inputStream"}),
			@Result(name="ajax",type="stream",params={"contentType","text/html","inputName","inputStream"})
			}
	)
	public String execute(){
		String resultHtml = "{}";
		ResultDTO resDTO = new ResultDTO() ;
		try {
			JSONObject submitdataObj = new JSONObject();
			JSONArray form1ar = new JSONArray();
			JSONObject form1obj= new JSONObject();
			
			JSONObject jsonObjForValidation = new JSONObject();
			JSONArray form1arForValidation = new JSONArray();
			JSONObject frmJsonForValidation = new JSONObject();
			String key2;
			if(bulkcmd != null){
				
				for (Entry<String, String[]> entry : parameters.entrySet()) {
					String[] val = entry.getValue();
					String key = entry.getKey();
					key2 = key;
//					key = ReplaceAlias.replaceAlias(screenName, key);
					if(!key.equals("bulkcmd") && !key.equals("screenName") && !key.equals("oper")){
						if(val.length== 1){
							form1obj.put(key, val[0]);
							jsonObjForValidation.put(key2,val[0]);
						}else{
							List<String> arVal = Arrays.asList(val);
							form1obj.put(key, arVal);
							jsonObjForValidation.put(key2, arVal);
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
//				System.out.println("Suitable to send to BE? screenName:"+screenName+" submitdata:"+submitdataObj.toString());
				JSONObject jsonRecord =   submitdataObj;
				
				//validation
				form1arForValidation.add(jsonObjForValidation);
				frmJsonForValidation.put("form1", form1arForValidation);
				resDTO = validate(frmJsonForValidation);
				
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
		PageReturnType pg;
		try {
			pg = setResult(resultHtml, jsonRecord, resDTO);
		} catch (Exception e) {
			logger.error("PageReturnType resolution error");
			pg = new PageReturnType();
			pg.resultName = "ajax";
		}
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
