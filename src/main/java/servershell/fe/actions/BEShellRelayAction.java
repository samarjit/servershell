package servershell.fe.actions;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import servershell.util.SendToBE;

import com.opensymphony.xwork2.ActionSupport;


@Results(
value={@Result(name="success",type="stream")}		
)
public class BEShellRelayAction extends ActionSupport {
	
	 
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(BEShellRelayAction.class);
	private InputStream inputStream;
	public String pos;
	public String prevpos;
	
	
	public String rootPath;
	public String homePath;
	public String name;
	public String cmd;
	public String cdpath;
	public String relPath;
	public String belogpath;
	public String pagesize;
	
	@Action(value="bscrolllog")
	public String scrolllog(){
		String message = "";
		
		try{
			String url = "prevpos="+prevpos+"&belogpath="+belogpath+"&cmd="+cmd+"&pagesize="+pagesize;
			logger.debug("url bescrolllog:"+url);
			message = SendToBE.sendToBE("", "bescrolllog.action?"+url);
				/*JSONObject jsonMessage = new JSONObject();
				jsonMessage.put("time",new Date().toString());
				jsonMessage.put("prevpos",prevpos);
				jsonMessage.put("pos",pos);
		//		jsonMessage.put("endswith",endchar);
				jsonMessage.put("message", message);
		//		jsonMessage.put("lastline", lastline);
				
				message = jsonMessage.toString();
				*/
			}catch(Exception e){
				logger.error(" Exception "+e);
				addActionError("Exception "+e);
			}
			
			if(getActionErrors().size() != 0){
				message = getActionErrors().toString();
			}
			
			inputStream  = new ByteArrayInputStream(message.getBytes());
		return SUCCESS;
	}
	
	@Action(value="bcd")
	public String cd(){
		String message = "";
		System.out.println("cd action... BEShellRelay");
		message = "some path";
		String data = rootPath+"/"+cdpath;
		
		try{
			
			
			message = SendToBE.sendToBE(data, "becd.action");
			JSONObject jsonMessage = new JSONObject();
			jsonMessage.put("time",new Date().toString());
			jsonMessage.put("rootPath",rootPath);
			jsonMessage.put("pos",pos);
	//		jsonMessage.put("endswith",endchar);
			jsonMessage.put("message", message);
	//		jsonMessage.put("lastline", lastline);
			
			message = jsonMessage.toString();
			
		}catch(Exception e){
			logger.error(" Exception "+e);
			addActionError("Exception "+e);
		}
		
		if(getActionErrors().size() != 0){
			message = getActionErrors().toString();
		}
		
		
		inputStream  = new ByteArrayInputStream(message.getBytes());
		return SUCCESS;
	}
	
	@Action(value="bpwd")
	public String pwd   (){
		String message;
		try {
			message = SendToBE.sendToBE("", "bepwd.action");
			rootPath = message;
		} catch (IOException e) {
			logger.error("IOException cannot contact backend: "+e.toString());
			message = "Cannot connect to backend- "+ e.toString();
		}
		inputStream  = new ByteArrayInputStream(message.getBytes());
		return SUCCESS;
	}
	
	@Action(value="bhome")
	public String home  (){
		String message;
		try {
			message = SendToBE.sendToBE("", "behome.action");
			rootPath = message;
		} catch (IOException e) {
			logger.error("IOException cannot contact backend: "+e.toString());
			message = "Cannot connect to backend- "+ e.toString();
		}
		inputStream  = new ByteArrayInputStream(message.getBytes());
		return SUCCESS;
	}
	
	@Action(value="bls")
	public String bls  (){
		String message;
		try {
			logger.debug("bls() called with rootPath:"+rootPath+" relPath:"+relPath);
			JSONObject jobj = new JSONObject();
			jobj.put("relPath", relPath);
			jobj.put("rootPath", rootPath);
			String data = jobj.toString();
			message = SendToBE.sendToBE(data, "bels.action");
			rootPath = message;
		} catch (IOException e) {
			logger.error("IOException cannot contact backend: "+e.toString());
			message = "Cannot connect to backend- "+ e.toString();
		}
		inputStream  = new ByteArrayInputStream(message.getBytes());
		return SUCCESS;
	}
	@Action(value="bgrep")
	public String grep  (){
		
		return SUCCESS;
	}
	public String execute(){
			logger.info("This is from log back");
			logger.debug("This is logback debug");
		return SUCCESS;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	
	public String getCdpath() {
		return cdpath;
	}

	public void setCdpath(String cdpath) {
		this.cdpath = cdpath;
	}

	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	public String getPrevpos() {
		return prevpos;
	}
	public void setPrevpos(String prevpos) {
		this.prevpos = prevpos;
	}
	public String getRootPath() {
		return rootPath;
	}
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
	public String getHomePath() {
		return homePath;
	}
	public void setHomePath(String homePath) {
		this.homePath = homePath;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	
	
}
