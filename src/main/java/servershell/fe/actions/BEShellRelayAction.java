package servershell.fe.actions;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import servershell.util.AccessRights;
import servershell.util.SendToBE;

import com.opensymphony.xwork2.ActionSupport;


 
@ParentPackage("default")
@Results(value={
@Result(type="stream", name="success")
,@Result(type="json", name="json",params={"contentType","text/html","ignoreHierarchy","false","includeProperties","jobj.*,actionErrors.*,actionMessages.*,fieldErrors.*"})
,@Result(type="json", name="input",params={"contentType","text/html","ignoreHierarchy","false","includeProperties","jobj.*,actionErrors.*,actionMessages.*,fieldErrors.*"})
})
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
	public String expression;
	
	public void validate(){
		String user = (String) ServletActionContext.getRequest().getSession().getAttribute("name");
		String role = (String) ServletActionContext.getRequest().getSession().getAttribute("role");
		System.out.println("Role = "+role+" User="+user);
		String actionName = ServletActionContext.getActionMapping().getName();
		
		if(user == null){
			addFieldError("user","User must be logged in ..");
		}else{
			if(!AccessRights.isAccessible(role,actionName))
				addActionError("User "+user+" does not have accesss to "+actionName+".action");
		}
	}
	
	@Action(value="bscrolllog")
	public String scrolllog(){
		String message = "";
		
		try{
			String url = "prevpos="+prevpos+"&belogpath="+URLEncoder.encode(belogpath)+"&cmd="+cmd+"&pagesize="+pagesize;
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
				logger.error(" Exception "+e,e);
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
	
	/**
	 * rootPath
	 * relPath
	 * @return
	 */
	@Action(value="bls")
	public String bls  (){
		String message = "ls not completed";
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
		if("".equals(message))message="No data Found"+new Date().toString();
		inputStream  = new ByteArrayInputStream(message.getBytes());
		return SUCCESS;
	}
	
	
	public String filename;
	/**
	 * filename
	 * rootPath
	 * expression
	 * @return
	 */
	@Action(value="bgrep")
	public String grep  (){
		String  message = ""; 
		
		try {
			JSONObject jobj = new JSONObject();
			jobj.put("expression", expression);
			jobj.put("filename", filename); 
			jobj.put("rootPath", rootPath);
			String data = jobj.toString();
			message = SendToBE.sendToBE(data, "begrep.action");
		} catch (IOException e) {
			logger.error("IOException cannot contact backend: "+e.toString());
			message = "Cannot connect to backend- "+ e.toString();
		}	
		
		inputStream  = new ByteArrayInputStream(message.getBytes());
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
