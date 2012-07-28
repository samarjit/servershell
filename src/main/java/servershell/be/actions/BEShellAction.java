package servershell.be.actions;

import java.io.InputStream;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionSupport;

public class BEShellAction extends ActionSupport {
	private static final long serialVersionUID = 12L;
	
	private InputStream inputStream;
	private String cmd;
	
	@Action(value="success",results={@Result(type="stream")})
	public String execute(){
		
		return SUCCESS;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	
}
