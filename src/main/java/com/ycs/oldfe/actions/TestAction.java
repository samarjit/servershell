package com.ycs.oldfe.actions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionSupport;

public class TestAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	public InputStream inputStream = null;
	
	public JSONObject jobj = null;
	
	@Action(value="test-action",results={@Result(name="input",type="dispatcher",location="/jsp/globalerror.jsp"),@Result(name="success",type="stream",params={"inputName","inputStream"})})
	public String execute(){
		System.out.println("TestAction to REMOVE");
		if(jobj == null) jobj =  new JSONObject();
		String message = "Not Processed";
		
		if(jobj != null && jobj.keySet().size() > 0){
			System.out.println(jobj.keySet().size());
			message = jobj.toString(4);
		}else{
			message = "test-action?jobj.form1={'user':'cde'}&jobj.bulkcmd=GridOnLoad&screenName=ProgramSetup";
		}
		
		
		inputStream = new ByteArrayInputStream(message.getBytes() );
		return SUCCESS;
	}

	@Override
	public String toString() {
		return "TestAction [inputStream=" + inputStream + ", jobj=" + jobj + "]";
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public JSONObject getJobj() {
		return jobj;
	}

	public void setJobj(JSONObject jobj) {
		this.jobj = jobj;
	}

	
}
