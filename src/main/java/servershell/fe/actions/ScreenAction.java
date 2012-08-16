package servershell.fe.actions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionSupport;

@Result(type="stream", name="success")
public class ScreenAction extends ActionSupport{
	public InputStream inputStream;
	private static final long serialVersionUID = 1L;
	
	@Action
	public String bcreatescreen(){
		String message = "Before processing in FE ";
		inputStream = new ByteArrayInputStream(message.getBytes());
		return "success";
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	

}
