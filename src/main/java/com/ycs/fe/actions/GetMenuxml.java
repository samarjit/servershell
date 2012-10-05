package com.ycs.fe.actions;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class GetMenuxml extends ActionSupport implements SessionAware{
	
	private InputStream inputStream;
	
	@Action(value="getmenuxml",
			results={@Result(name="success",type="stream",params={"contentType","text/xml","inputName","inputStream"}),
					@Result(name="error", location="/pages/globalerror.jsp")
					}
	)
	public String execute(){
		String menuXml = "";
		List<String> errors = new ArrayList<String>();
		try {
			if(ActionContext.getContext().getSession() == null){
				errors.add("Session timed out");
				setActionErrors(errors);
				throw new Exception("Session timed out");
			}else{
			menuXml = (String) ActionContext.getContext().getSession().get("menuXml");
			if(menuXml == null){
				errors.add("Menu XML not found in session");
				setActionErrors(errors);
				throw new Exception("Menu XML not found in session");
			}
			menuXml.replaceAll("\r\n", "");
			System.out.println(menuXml);
			}
		} catch (Exception e) {
			System.out.println(errors);
			e.printStackTrace();
			menuXml = "";
			for (String string : errors) {
				menuXml += "<errors message=\""+string+"\"/>";
			}
			menuXml = "<root>"+menuXml+"</root>";
		}
		inputStream = new StringBufferInputStream(menuXml);
		return "success";
	}

	@Override
	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}

}
