package com.ycs.fe.actions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionSupport;
import com.ycs.fe.util.FEMapFileSync;

public class FileService extends ActionSupport{

	InputStream inputStream;
	
	@Action(value="file",results={@Result( name="success",type="stream",params={"contentType","application/json","inputName","inputStream"})})
	public String execute(){
		
		new FEMapFileSync().uploadFiles();
		
		inputStream = new ByteArrayInputStream("{'result':'success'}".getBytes());
		return SUCCESS;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	
}
