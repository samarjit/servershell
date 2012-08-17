package servershell.be.actions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import servershell.be.dto.ResultDTO;
import servershell.util.ReverseEngineerXml;

import com.opensymphony.xwork2.ActionSupport;

@Result(type="stream", name="success")
public class BEScreenAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(BEScreenAction.class);
	public InputStream inputStream;
	public String data;
	
	@Action(value="becreatescreen")
	public String becreatescreen(){
		String message = "Before processing in BE ";
		ReverseEngineerXml re = new ReverseEngineerXml();
		try {
			String sqlstring = data;
			message = re.getStringResult(sqlstring );
		} catch (Exception e) {
			logger.error("BE",e);
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
