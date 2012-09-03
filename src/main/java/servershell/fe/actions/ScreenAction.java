package servershell.fe.actions;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import servershell.util.AccessRights;
import servershell.util.CompoundResource;
import servershell.util.SendToBE;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.ycs.fe.commandprocessor.AppCacheManager;
import com.ycs.fe.commandprocessor.Constants;

@ParentPackage("default")
@Results(value={
@Result(type="stream", name="success")
,@Result(type="json", name="json",params={"contentType","text/html","ignoreHierarchy","false","includeProperties","jobj.*,actionErrors.*,actionMessages.*,fieldErrors.*"})
,@Result(type="json", name="input",params={"contentType","text/html","ignoreHierarchy","false","includeProperties","jobj.*,actionErrors.*,actionMessages.*,fieldErrors.*"})
})
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
	
	public String ftlbasepath;
	public String ftlfilename;
	public String htmlscreen1;
	
	@Action(value="createhtml")
	public String createhtml(){
		String message= "Not processed!";
		System.out.println(ftlbasepath);
		System.out.println(ftlfilename);
		System.out.println(htmlscreen1);
		File dir = new File(ftlbasepath);
		if(dir.exists()){
			File f = new File(dir,ftlfilename);
			try {
				FileWriter fw = new FileWriter(f);
				fw.write(htmlscreen1);
				fw.close();
				message = " File "+f.getAbsolutePath() +" created successfully, length="+f.length()+ new Date();
			} catch (IOException e) {
				logger.error(e.toString());
				message = "File writing error "+e.toString();
			}
			
		}else{
			message = "Directory "+ftlbasepath+" does not exist.";
		}
		
		inputStream = new ByteArrayInputStream(message.getBytes());
		return SUCCESS;
	}
	
	public String xmlscreenpath;
	public String xmlscreenname;
	public String screenxml;
	
	@Action(value="createxml")
	public String createxml(){
		String message= "Not processed!";
		System.out.println(xmlscreenpath);
		System.out.println(xmlscreenname);
		System.out.println(screenxml);
		File dir = new File(xmlscreenpath);
		if(dir.exists()){
			File f = new File(dir,xmlscreenname);
			try {
				FileWriter fw = new FileWriter(f);
				fw.write(screenxml);
				fw.close();
				
				///saving config to BE//
				JSONObject jobj = new JSONObject();
				jobj.put("filepath", filepath);
				jobj.put("filename", xmlscreenname);
				jobj.put("fefile", fefile);
				jobj.put("create", "true");
				
				message = SendToBE.sendToBE(jobj.toString(), "besyncxml.action");
				///saving config to BE//
				
				AppCacheManager.removeCache("xmlcache");
				message += " File "+f.getAbsolutePath() +" created successfully, length="+f.length()+ new Date();
			} catch (IOException e) {
				logger.error(e.toString());
				message = "File writing error "+e.toString();
			}
			
		}else{
			message = "Directory "+ftlbasepath+" does not exist.";
		}
		
		inputStream = new ByteArrayInputStream(message.getBytes());
		return SUCCESS;
	}

	public String screenmappath;
	public String screenmapfilename;
	public String screenmapxml;
	@Action(value="loadscreenmap")
	public String loadscreenmap(){
		String message= "Not processed!";
		logger.debug("FE:"+screenmappath);
		logger.debug("BE:"+filepath);
		logger.debug(screenmapfilename);
		logger.debug("loadding...");
		
		
		JSONObject jres =new JSONObject();
		File dir = new File(screenmappath);
		if(dir.exists()){
			File f = new File(dir,screenmapfilename);
			try {
				jres.put("data", message);
				if(f.exists()){
					String fefile = FileUtils.readFileToString(f);
					jres.put("data", fefile);
				}else{
					jres.put("errors", "screenmap File not found "+f.getAbsolutePath()) ;
				}
//				message = " File "+f.getAbsolutePath() +" created successfully, length="+f.length()+ new Date();
			} catch (IOException e) {
				logger.error(e.toString());
				message = "File writing error "+e.toString();
				jres.put("errors", message);
			}
			
		}else{
			message = "Directory "+dir.getAbsolutePath()+" does not exist.";
			jres.put("errors", message);
		}
		
		
		inputStream = new ByteArrayInputStream(jres.toString().getBytes());
		return SUCCESS;
	}
	
	@Action(value="screenmapsave")
	public String screenmapsave(){
		String message= "Not processed!";
		logger.debug("FE:"+screenmappath);
		logger.debug("BE:"+filepath);
		logger.debug(screenmapfilename);
		logger.debug(screenmapxml);
		logger.debug("saving...");
		
		File dir = new File(screenmappath);
		if(dir.exists()){
			File f = new File(dir,screenmapfilename);
			try {
				if(f.exists()){
					FileWriter fw = new FileWriter(f);
					fw.write(screenmapxml);
					fw.close();
					
					///saving config to BE//
					JSONObject jobj = new JSONObject();
					jobj.put("filepath", filepath);
					jobj.put("filename", screenmapfilename);
					jobj.put("fefile", screenmapxml);
					jobj.put("create", "false");
					logger.debug("send to BE:"+jobj.toString());
					message = SendToBE.sendToBE(jobj.toString(), "besyncxml.action");
					///saving config to BE//
					
					AppCacheManager.removeCache("xmlcache");
					message += " File "+f.getAbsolutePath() +" saved successfully, length="+f.length()+ new Date();
				}else{
					message = "File not found "+f.getAbsolutePath();
				}
//				message = " File "+f.getAbsolutePath() +" created successfully, length="+f.length()+ new Date();
			} catch (IOException e) {
				logger.error(e.toString());
				message = "File writing error "+e.toString();
			}
			
		}else{
			message = "Directory "+dir.getAbsolutePath()+" does not exist.";
		}
		
		inputStream = new ByteArrayInputStream(message.getBytes());
		return SUCCESS;
	}
	
	public String filepath;
	public String filename;
	public String fefile;
	@Action(value="loadfefile")
	public String loadfefile(){
		String message= "Not processed!";
		System.out.println(filepath);
		System.out.println(filename);
		System.out.println("loadding...");
		JSONObject jres =new JSONObject();
		File dir = new File(filepath);
		if(dir.exists()){
			File f = new File(dir,filename);
			try {
				jres.put("data", message);
				if(f.exists()){
					String fefile = FileUtils.readFileToString(f);
					jres.put("data", fefile);
				}else{
					jres.put("errors", "File not found "+f.getAbsolutePath()) ;
				}
//				message = " File "+f.getAbsolutePath() +" created successfully, length="+f.length()+ new Date();
			} catch (IOException e) {
				logger.error(e.toString());
				message = "File writing error "+e.toString();
				jres.put("errors", message);
			}
			
		}else{
			message = "Directory "+dir.getAbsolutePath()+" does not exist.";
			jres.put("errors", message);
		}
		
		inputStream = new ByteArrayInputStream(jres.toString().getBytes());
		return SUCCESS;
	}
	
	@Action(value="savefefile")
	public String savefefile(){
		String message= "Not processed!";
		System.out.println(filepath);
		System.out.println(filename);
		System.out.println(fefile);
		System.out.println("saving...");
		
		File dir = new File(filepath);
		if(dir.exists()){
			File f = new File(dir,filename);
			try {
				if(f.exists()){
					FileWriter fw = new FileWriter(f);
					fw.write(fefile);
					fw.close();
					AppCacheManager.removeCache("xmlcache");
					message = " File "+f.getAbsolutePath() +" saved successfully, length="+f.length()+ new Date();
				}else{
					message = "File not found "+f.getAbsolutePath();
				}
//				message = " File "+f.getAbsolutePath() +" created successfully, length="+f.length()+ new Date();
			} catch (IOException e) {
				logger.error(e.toString());
				message = "File writing error "+e.toString();
			}
			
		}else{
			message = "Directory "+dir.getAbsolutePath()+" does not exist.";
		}
		
		inputStream = new ByteArrayInputStream(message.getBytes());
		return SUCCESS;
	}
	
	@Action(value="createfefile")
	public String createfefile(){
		String message= "Not processed!";
		File dir = new File(filepath);
		if(dir.exists()){
			File f = new File(dir,filename);
			try {
				if(f.exists()){
					
					message = " File "+f.getAbsolutePath() +" already exists, length="+f.length()+ new Date();
				}else{
					f.createNewFile();
					message = "File created at "+f.getAbsolutePath() + " "+new Date();
				}
//				message = " File "+f.getAbsolutePath() +" created successfully, length="+f.length()+ new Date();
			} catch (IOException e) {
				logger.error(e.toString());
				message = "File writing error "+e.toString();
			}
			
		}else{
			message = "Directory "+dir.getAbsolutePath()+" does not exist.";
		}
		
		inputStream = new ByteArrayInputStream(message.getBytes());
		return SUCCESS;
	}
	
	@Action("logout")
	public String logout(){
		ServletActionContext.getRequest().getSession().invalidate();
		jobj.put("logout", "successfull.."+new Date());
		return "json";
	}
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
	//returns all json result
	public HashMap<String, Object> jobj = new HashMap<String, Object>();

//	@Validations(requiredFields={@RequiredFieldValidator(fieldName="sqlstring",type = ValidatorType.FIELD, message = "sqlstring is required")})
	@Action(value="bconfig")
	public String bconfig(){
		String message = "Not configured!";
		System.out.println("This is now configured FE");
		ServletActionContext.getRequest().getSession().setAttribute("processor", "FE");
		Constants.CMD_PROCESSOR = 0;
		Constants.APP_LAYER = 1;
		Constants.FRONTEND = 1;
		try {
			jobj.put("bemessage", message);
			message = SendToBE.sendToBE("data", "beconfig.action");
			jobj.put("bemessage", message);
			jobj.put("femessage", "configured FE");
		} catch (IOException e) {
			addActionError(e.toString());
		}
		
		 
		logger.debug("This is now configured BE");
		inputStream = new ByteArrayInputStream(message.getBytes());
		return "json";
	}
	
	@Action(value="bloadfefile")
	public String bloadfefile(){
		String message= "Not processed!";
		System.out.println(filepath);
		System.out.println(filename);
		System.out.println("loadding...");
		JSONObject jres =new JSONObject();
		
		try{
			JSONObject jobj = new JSONObject();
			jobj.put("filepath", filepath);
			jobj.put("filename", filename);
			jobj.put("fefile", fefile);
			
			String jsonmsg = SendToBE.sendToBE(jobj.toString(), "beloadfile.action");
			jres = JSONObject.fromObject(jsonmsg);
		}catch(Exception e){
			logger.error(e.toString());
			message = "File writing error "+e.toString();
			jres.put("errors", message);
		}
		
		inputStream = new ByteArrayInputStream(jres.toString().getBytes());
		return SUCCESS;
	}
	
	@Action(value="bsavefefile")
	public String bsavefefile(){
		String message= "Not processed!";
		System.out.println(filepath);
		System.out.println(filename);
		System.out.println(fefile);
		System.out.println("saving in BE ...");
		
		 
		try{
			JSONObject jobj = new JSONObject();
			jobj.put("filepath", filepath);
			jobj.put("filename", filename);
			jobj.put("fefile", fefile);
			
			message = SendToBE.sendToBE(jobj.toString(), "besavefile.action");
		}catch(Exception e){
			logger.error(e.toString());
			message = "exception while saving file in BE- "+e.toString();
		}
		
		inputStream = new ByteArrayInputStream(message.getBytes());
		return SUCCESS;
	}
	
	@Action(value="bcreatefefile")
	public String bcreatefefile(){
		String message= "Not processed!";
		try {
			JSONObject jobj = new JSONObject();
			jobj.put("filepath", filepath);
			jobj.put("filename", filename);
			message = SendToBE.sendToBE(jobj.toString(), "becreatefile.action");
		} catch (Exception e) {
			message = "Backend caused exception "+e.toString();
		}
		inputStream = new ByteArrayInputStream(message.getBytes());
		return SUCCESS;
	}
	
	@Action(value="login")
	public String loginaction(){
		HttpServletRequest request = ServletActionContext.getRequest();
		System.out.println(request.getParameter("PASSWORD")+" "+request.getParameter("ID"));
		jobj.put("ok", "working");
		return "json";
	}
	
	@Validations(requiredFields  ={
			@RequiredFieldValidator(fieldName="filepath",message="filepath is required"),
			@RequiredFieldValidator(fieldName="screenmappath",message="filepath is required"),
			@RequiredFieldValidator(fieldName="ftlbasepath",message="filepath is required")
	})
	@Action("savefebepath")
	public String savefebepath(){
		String f = filepath;
		String sm = screenmappath;
		String ftlbase = ftlbasepath;
		
		ResourceBundle rb = ResourceBundle.getBundle("config");
		String apppath = CompoundResource.getString(rb, "application_home");
		File appf = new File(apppath);
		try {
			
			 if(appf.exists()){
				File pathinfoFile = new File(appf.getAbsoluteFile(),"userprop.txt");
				if(!pathinfoFile.exists())pathinfoFile.createNewFile();
				
				Properties prop = new Properties();
				FileInputStream fis = new FileInputStream(pathinfoFile); 
				prop.load(fis);
				fis.close();
				
				prop.setProperty("filepath", filepath);
				prop.setProperty("screenmappath", screenmappath);
				prop.setProperty("ftlbasepath", ftlbasepath);
				
				
				FileOutputStream fos  = new FileOutputStream (pathinfoFile); 
				prop.store(fos, "Records saved on "+new Date().toString());
				fos.close();
				
				jobj.put("success", "data saved as on "+new Date().toString()+"  in "+ pathinfoFile.getAbsolutePath());
			}else{
				addActionError(appf.getAbsolutePath()+" as specified in config does not exist.");
			}
			
		} catch (Exception e) {
			logger.error("exception ",e);
			addActionError("exception "+e.toString());
		}
		
		return "json";
	}
	
	@Action("findfebepath")
	public String findfebepath(){
		
		ResourceBundle rb = ResourceBundle.getBundle("config");
		String apppath = CompoundResource.getString(rb, "application_home");
		File appf = new File(apppath);
		try {
			
			 if(appf.exists()){
				File pathinfoFile = new File(appf.getAbsoluteFile(),"userprop.txt");
				
				Properties prop = new Properties();
				FileInputStream fis = new FileInputStream(pathinfoFile); 
				prop.load(fis);
				fis.close();
				
				filepath = prop.getProperty("filepath");
				screenmappath = prop.getProperty("screenmappath");
				ftlbasepath = prop.getProperty("ftlbasepath" );
				
				jobj.put("filepath", filepath);
				jobj.put("screenmappath", screenmappath);
				jobj.put("ftlbasepath", ftlbasepath);
				
				jobj.put("success", "data as on "+new Date().toString()+" from "+pathinfoFile.getAbsolutePath());
			}else{
				addActionError(appf.getAbsolutePath()+" as specified in config does not exist.");
			}
			
		} catch (Exception e) {
			logger.error("exception ",e);
			addActionError("exception "+e.toString());
		}
		return "json";
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public HashMap<String, Object> getJobj() {
		return jobj;
	}

	public void setJobj(HashMap<String, Object> jobj) {
		this.jobj = jobj;
	}

}
