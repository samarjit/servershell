package servershell.fe.actions;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import servershell.util.AccessRights;
import servershell.util.CompoundResource;
import servershell.util.ResourceBundleReloader;
import servershell.util.SendToBE;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.ycs.fe.cache.AppCacheManager;
import com.ycs.oldfe.commandprocessor.Constants;

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
	private Gson gson = new Gson();
	
	@SuppressWarnings("deprecation")
	@Action(value="brevengg")
	public String brevengg(){
		String message = "Before processing in FE ";
		 
		try {
			message = SendToBE.sendToBE(sqlstring, "berevengg.action");
			 
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
		logger.debug(ftlbasepath);
		logger.debug(ftlfilename);
		logger.debug(StringUtils.abbreviate(htmlscreen1,200)+"...");
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
			message = "Directory "+dir.getAbsolutePath()+" does not exist.";
		}
		
		inputStream = new ByteArrayInputStream(message.getBytes());
		return SUCCESS;
	}
	
	public String fepagexmlpath;
	public String bepagexmlpath;
	public String pagexmlfilename;
	public String pagexml;
	@Action(value="createxml")
	public String createxml(){
		String message= "Not processed!";
		logger.debug(fepagexmlpath);
		logger.debug(bepagexmlpath);
		logger.debug(pagexmlfilename);
		logger.debug(StringUtils.abbreviate(pagexml,200));
		File dir = new File(fepagexmlpath);
		if(dir.exists()){
			File f = new File(dir,pagexmlfilename);
			try {
				FileWriter fw = new FileWriter(f);
				fw.write(pagexml);
				fw.close();
				
				///saving config to BE//
				HashMap<String,Object> jobj = new HashMap<String,Object>();
				jobj.put("filepath", bepagexmlpath);
				jobj.put("filename", pagexmlfilename);
				jobj.put("fefile", pagexml);
				jobj.put("create", "true");
				
				message = SendToBE.sendToBE(gson.toJson(jobj), "besyncxml.action");
				///saving config to BE//
				
				AppCacheManager.removeCache("xmlcache");
				message += " File "+f.getAbsolutePath() +" created successfully, length="+f.length()+ new Date();
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

	public String fescreenmappath;
	public String bescreenmappath;
	public String screenmapfilename;
	public String screenmapxml;
	@Action(value="loadscreenmap")
	public String loadscreenmap(){
		String message= "Not processed!";
		logger.debug("FE:"+fescreenmappath);
		logger.debug("BE:"+bescreenmappath);
		logger.debug(screenmapfilename);
		logger.debug("loadding...");
		
		
		HashMap<String,Object> jres =new HashMap<String,Object>();
		File dir = new File(fescreenmappath);
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
		logger.debug("FE:"+fescreenmappath);
		logger.debug("BE:"+bescreenmappath);
		logger.debug(screenmapfilename);
		logger.debug(screenmapxml);
		logger.debug("saving...");
		
		File dir = new File(fescreenmappath);
		if(dir.exists()){
			File f = new File(dir,screenmapfilename);
			try {
				if(f.exists()){
					FileWriter fw = new FileWriter(f);
					fw.write(screenmapxml);
					fw.close();
					
					///saving config to BE//
					HashMap<String,Object> jobj = new HashMap<String,Object>();
					jobj.put("filepath", bescreenmappath);
					jobj.put("filename", screenmapfilename);
					jobj.put("fefile", screenmapxml);
					jobj.put("create", "false");
					logger.debug("send to BE:"+gson.toJson(jobj));
					message = SendToBE.sendToBE(gson.toJson(jobj), "besyncxml.action");
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
		logger.debug(filepath);
		logger.debug(filename);
		logger.debug("loadding...");
		HashMap<String,Object> jres =new HashMap<String,Object>();
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
		logger.debug(filepath);
		logger.debug(filename);
		logger.debug(StringUtils.abbreviate(fefile, 200));
		logger.debug("saving...");
		
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
		logger.debug(filepath);
		logger.debug(filename);
		logger.debug("loadding...");
		HashMap<String,Object> jres =new HashMap<String,Object>();
		
		try{
			HashMap<String,Object> jobj = new HashMap<String,Object>();
			jobj.put("filepath", filepath);
			jobj.put("filename", filename);
			jobj.put("fefile", fefile);
			
			String jsonmsg = SendToBE.sendToBE(gson.toJson(jobj), "beloadfile.action");
			jres = gson.fromJson(jsonmsg, HashMap.class);
		}catch(Exception e){
			logger.error(e.toString());
			message = "File writing error "+e.toString();
			jres.put("errors", message);
		}
		
		inputStream = new ByteArrayInputStream(gson.toJson(jres).toString().getBytes());
		return SUCCESS;
	}
	
	@Action(value="bsavefefile")
	public String bsavefefile(){
		String message= "Not processed!";
		logger.debug(filepath);
		logger.debug(filename);
		logger.debug(StringUtils.abbreviate(fefile,200));
		logger.debug("saving in BE ...");
		
		 
		try{
			HashMap<String,Object> jobj = new HashMap<String,Object>();
			jobj.put("filepath", filepath);
			jobj.put("filename", filename);
			jobj.put("fefile", fefile);
			
			message = SendToBE.sendToBE(gson.toJson(jobj), "besavefile.action");
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
			HashMap<String,Object> jobj = new HashMap<String,Object>();
			jobj.put("filepath", filepath);
			jobj.put("filename", filename);
			message = SendToBE.sendToBE(gson.toJson(jobj), "becreatefile.action");
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
			@RequiredFieldValidator(fieldName="fefile",message="fefile is required, it should contain all the data to be saved")
	})
	@Action("saveuserpref")
	public String saveuserpref(){
		String f = filepath;
		String sm = fescreenmappath;
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
				
				BufferedReader sr = new BufferedReader(new StringReader(fefile));
				String line = null;
				while((line = sr.readLine()) != null){
					System.out.println("line="+line+" "+fefile);
					int idx = line.indexOf('=');
					
					String part1 = line.substring(0,idx);
					String part2 = line.substring(idx+1);
					
					prop.setProperty(part1,part2);
					
				}
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
	
	@Validations(requiredFields  ={
			@RequiredFieldValidator(fieldName="fefile",message="fefile is required, it should contain title_configname to be removed from this pagedata")
	})
	@Action("removeuserpref")
	public String removeuserpref(){
		String f = filepath;
		String sm = fescreenmappath;
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
				
				BufferedReader sr = new BufferedReader(new StringReader(fefile));
				String keyLine = null;
				Properties cloneProp = (Properties) prop.clone();
				for (Object keyObj : cloneProp.keySet()) {
					keyLine = (String)keyObj;
					if(keyLine.contains(fefile)){
						prop.remove(keyObj);
					}
				}
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
	
	@Action("finduserpref")
	@Validations(requiredFields = {
			@RequiredFieldValidator(fieldName="fefile",message="fefile is required, it should contain pagetitle")
	})
	public String finduserpref(){
		
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
				
				HashMap<String,Object> jsonres = new HashMap<String,Object>();
				HashMap<String,Object> fieldValues = null;
				String keyLine = null;
				String configName = null;
				String filedName = null;
				
				for (Object keyObj : prop.keySet()) {
					keyLine = (String)keyObj;
					if(keyLine.contains(fefile)){
						
						if (keyLine.indexOf('_') > -1 && keyLine.indexOf('#') > -1) {
							configName = keyLine.substring(keyLine.indexOf('_') + 1, keyLine.indexOf('#'));
							filedName = keyLine.substring(keyLine.indexOf('#') + 1);
							if (!jsonres.containsKey(configName)) {
								fieldValues = new HashMap<String,Object>();
							}else{
								fieldValues = (HashMap<String,Object>) jsonres.get(configName);
							}
							fieldValues.put(filedName, prop.getProperty(keyLine));
							jsonres.put(configName, fieldValues);
						}
					}
				}
				
				
				jobj.put("props", jsonres);
				 
				
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
	
	@Action(value="fedeletefile")
	public String fedeletefile(){
		String message= "Not processed!";
		
		File dir = new File(filepath);
		if(dir.exists()){
			File f = new File(dir,filename);
			try {
				if(f.exists()){
					ResourceBundle rb = ResourceBundle.getBundle("config");
					File deleteDir = new File(CompoundResource.getString(rb,"application_home"));
					SimpleDateFormat sm = new SimpleDateFormat("yyyyMMddhhmmss");
					String dt = sm.format(new Date());
					if(deleteDir.exists()){
						File deletedFile =  new File(deleteDir,filename+"."+dt+".DELETED");
						message = "Rename to "+deletedFile.getAbsolutePath()+" succeeded "+f.renameTo(deletedFile);
					}else{
						message = "Renameto "+deleteDir.getAbsolutePath()+" directory not found";
					}
					
				}else{
					 
					message = "File not found at "+f.getAbsolutePath() + " "+new Date();
				}
//				message = " File "+f.getAbsolutePath() +" created successfully, length="+f.length()+ new Date();
			} catch (Exception e) {
				logger.error(e.toString());
				message = "File writing error "+e.toString();
			}
			
		}else{
			message = "Directory "+dir.getAbsolutePath()+" does not exist.";
		}
		
		inputStream = new ByteArrayInputStream(message.getBytes());
		return SUCCESS;
	}
	
	@Action(value="bdeletefile")
	public String bdeletefile(){
		String message= "Not processed!";
		HashMap<String,Object> jobj = new HashMap<String,Object>();//.fromObject(data);
		 jobj.put("filepath", filepath);
		 jobj.put("filename", filename);
		
			try {
				String res = SendToBE.sendToBE(gson.toJson(jobj), "bedeletefile.action");
				message = res;
			} catch (Exception e) {
				logger.error(e.toString());
				message = "BE process error "+e.toString();
			}
			
		
		inputStream = new ByteArrayInputStream(message.getBytes());
		return SUCCESS;
	}
	
	
	@Action(value="breloadconfig")
	public String breloadconfig(){
		jobj.put("fereloaded", "true");
		ResourceBundleReloader.reloadBundles();
		
		try {
			String res = SendToBE.sendToBE("reload", "bereloadconfig.action");
			jobj.put("bereloaded", res);
		} catch (IOException e) {
			addActionError("BE relaod failed, "+e);
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
