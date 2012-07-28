package servershell.fe.actions;


import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

public class ShellAction extends ActionSupport {
	
	 
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(ShellAction.class);
	
	public String execute(){
			logger.info("This is from log back");
			logger.debug("This is logback debug");
		return SUCCESS;
	}
}
