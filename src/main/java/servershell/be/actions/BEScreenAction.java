package servershell.be.actions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionSupport;

@Result(type="stream", name="success")
public class BEScreenAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	public InputStream inputStream;
	
	@Action
	public String becreatescreen(){
		String message = "Before processing in BE ";
		System.out.println("be");
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
