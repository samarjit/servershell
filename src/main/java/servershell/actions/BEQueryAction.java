package servershell.actions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

public class BEQueryAction extends ActionSupport{
	 
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(QueryAction.class);
	
	private String query;
	private InputStream inputStream;
	
	@Action(value="bequery", results ={@Result(name="success",type="stream")})
	public String execute(){
			logger.info("This is from is be query= "+ query);
			String jsonString = "This is be query "+query;
			
			
			logger.debug("This is logback debug");
			
			inputStream = new ByteArrayInputStream(jsonString.getBytes());
			return SUCCESS;
	}



	public String getQuery() {
		return query;
	}



	public void setQuery(String query) {
		this.query = query;
	}



	public InputStream getInputStream() {
		return inputStream;
	}

	
	
}
