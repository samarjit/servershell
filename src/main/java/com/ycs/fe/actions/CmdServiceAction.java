package com.ycs.fe.actions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.Iterator;

import net.sf.json.JSONObject;

import ognl.Ognl;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import servershell.be.dto.InputDTO;
import servershell.be.dto.ResultDTO;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.ycs.fe.commandprocessor.CommandProcessor;
import com.ycs.fe.commandprocessor.FrontendException;
import com.ycs.fe.commandprocessor.SelectOnLoad;

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
		JSONObject submitdata =  JSONObject.fromObject(submitdataObj);
		InputDTO inpDTO = new InputDTO();
		inpDTO.setData(submitdata);
		ServletActionContext.getContext().getValueStack().getContext().put("inputDTO", inpDTO);
//		 String propval = ServletActionContext.getContext().getValueStack().findString("#inputDTO.data.form1[0].CARD_NO");
//		 System.out.println("@@@@@@@@@from value stack="+propval+"    --"+ActionContext.getContext().get("inputDTO"));
		JSONObject sessionvars =   inpDTO.getData().getJSONObject("sessionvars");
		System.out.println("InputDTO.getData() is "+inpDTO.getData().toString());
		if(sessionvars!=null && !sessionvars.isNullObject())
		for (Iterator iterator = sessionvars.keys(); iterator.hasNext();) {
			String sessionkey = (String) iterator.next();
			String sessionval =  sessionvars.getString(sessionkey);
			System.out.println("sessionvars getValue :"+sessionval);
			ServletActionContext.getContext().getSession().put(sessionkey, sessionval);
		}

		CommandProcessor processor = new CommandProcessor();
		ResultDTO resultDTO = processor.commandProcessor(submitdata, screenName);
		tmpResDTO = JSONObject.fromObject(resultDTO).toString();
		logger.debug("<<<<Return value : +++ " + tmpResDTO);
		System.out.println("<<<<Return value : +++ " + tmpResDTO);
		inputStream = new ByteArrayInputStream(tmpResDTO.getBytes());
		return SUCCESS ;
	}
	
	@Action(value="selectOnLoad", results ={@Result(name="success",type="stream")})
	public String selectOnLoad(){
		
		String tmpResDTO = "Query Service Error in selectOnLoad";
		
		SelectOnLoad sl = new SelectOnLoad();
		JSONObject jsonsubmitdata1 = JSONObject.fromObject(jsonsubmitdata);

		InputDTO inpDTO = new InputDTO();
		inpDTO.setData(jsonsubmitdata1);
		ServletActionContext.getContext().getValueStack().set("inputDTO", inpDTO);
		JSONObject sessionvars = null;
		if(!inpDTO.getData().isNullObject())sessionvars = inpDTO.getData().getJSONObject("sessionvars");
		System.out.println(inpDTO.getData().toString());
		if(sessionvars!=null && !sessionvars.isNullObject())
		for (Iterator iterator = sessionvars.keys(); iterator.hasNext();) {
			String sessionkey = (String) iterator.next();
			String sessionval =  sessionvars.getString(sessionkey);
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
