package com.ycs.be.actions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import com.ycs.be.commandprocessor.CommandProcessor;
import com.ycs.be.crud.SelectOnLoad;
import com.ycs.be.dto.InputDTO;
import com.ycs.be.dto.ResultDTO;
import com.ycs.be.exception.FrontendException;


public class CmdServiceAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(CmdServiceAction.class);
	private InputStream inputStream;
	private String jsonsubmitdata;
	private String screenName;
	private String submitdataObj;
	
	@Action(value="remoteCommandProcessor", results ={@Result(name="success",type="stream")})
	public String remoteCommandProcessor() throws Exception{
		System.out.println(">>>>incoming value : +++ "+ submitdataObj);
		logger.debug(">>>>incoming value : +++ "+ submitdataObj);
		String tmpResDTO = "Query Service Error in remoteCommandProcessor";
		Map<String,Object> submitdata =  new Gson().fromJson(submitdataObj, Map.class);
		InputDTO inpDTO = new InputDTO();
		inpDTO.setData(submitdata);
		ServletActionContext.getContext().getValueStack().getContext().put("inputDTO", inpDTO);
//		 String propval = ServletActionContext.getContext().getValueStack().findString("#inputDTO.data.form1[0].CARD_NO");
//		 System.out.println("@@@@@@@@@from value stack="+propval+"    --"+ActionContext.getContext().get("inputDTO"));
		Map<String, Object> sessionvars =   (Map<String,Object>) inpDTO.getData().get("sessionvars"); //object
		System.out.println("InputDTO.getData() is "+inpDTO.getData().toString());
		if(sessionvars!=null && !sessionvars.isEmpty())
		for (Iterator iterator = sessionvars.keySet().iterator(); iterator.hasNext();) {
			String sessionkey = (String) iterator.next();
			String sessionval =  (String) sessionvars.get(sessionkey); //string
			System.out.println("sessionvars getValue :"+sessionval);
			ServletActionContext.getContext().getSession().put(sessionkey, sessionval);
		}

		CommandProcessor processor = new CommandProcessor();
		ResultDTO resultDTO = processor.commandProcessor(submitdata, screenName);
		tmpResDTO = new Gson().toJson(resultDTO).toString();
		logger.debug("<<<<Return value : +++ " + tmpResDTO);
		System.out.println("<<<<Return value : +++ " + tmpResDTO);
		inputStream = new ByteArrayInputStream(tmpResDTO.getBytes());
		return SUCCESS ;
	}
	
	@Action(value="selectOnLoad", results ={@Result(name="success",type="stream")})
	public String selectOnLoad(){
		
		String tmpResDTO = "Query Service Error in selectOnLoad";
		
		SelectOnLoad sl = new SelectOnLoad();
		Map<String,Object> jsonsubmitdata1 = new Gson().fromJson(jsonsubmitdata, Map.class);

		InputDTO inpDTO = new InputDTO();
		inpDTO.setData(jsonsubmitdata1);
		ServletActionContext.getContext().getValueStack().set("inputDTO", inpDTO);
		Map<String,Object> sessionvars = null;
		if(!inpDTO.getData().isEmpty())sessionvars = (Map<String,Object>) inpDTO.getData().get("sessionvars"); //object
		System.out.println(inpDTO.getData().toString());
		if(sessionvars!=null && !sessionvars.isEmpty())
		for (Iterator iterator = sessionvars.keySet().iterator(); iterator.hasNext();) {
			String sessionkey = (String) iterator.next();
			String sessionval =  (String) sessionvars.get(sessionkey); //string
			System.out.println("sessionvars getValue :"+sessionval);
			ServletActionContext.getContext().getSession().put(sessionkey, sessionval);
		}
		
		try {
			sl.selectOnLoad(screenName, jsonsubmitdata1 );
			tmpResDTO = (String) ServletActionContext.getContext().getValueStack().getContext().get("resDTO");
		} catch (FrontendException e) {
			e.printStackTrace();
			tmpResDTO = "";
		} 
		inputStream = new ByteArrayInputStream(tmpResDTO.getBytes());
		return SUCCESS ;
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}

	public String getJsonsubmitdata() {
		return jsonsubmitdata;
	}

	public void setJsonsubmitdata(String jsonsubmitdata) {
		this.jsonsubmitdata = jsonsubmitdata;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getSubmitdataObj() {
		return submitdataObj;
	}

	public void setSubmitdataObj(String submitdataObj) {
		this.submitdataObj = submitdataObj;
	}

}
