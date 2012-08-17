package servershell.fe.actions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import servershell.util.SendToBE;

import com.opensymphony.xwork2.ActionSupport;

@Result(type="stream", name="success")
public class ScreenAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(ScreenAction.class);
	
	public InputStream inputStream;
	public String sqlstring;
	
	@SuppressWarnings("deprecation")
	@Action(value="bcreatescreen")
	public String bcreatescreen(){
		String message = "Before processing in FE ";
		 
		try {
			message = SendToBE.sendToBE(sqlstring, "becreatescreen.action");
			 
		} catch (Exception e) {
			logger.error(e.toString());
			message = "{'errors':'"+URLEncoder.encode(e.toString())+"'}";
		}
		
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
