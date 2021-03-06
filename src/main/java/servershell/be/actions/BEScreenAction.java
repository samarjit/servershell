package servershell.be.actions;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import servershell.util.CompoundResource;
import servershell.util.ResourceBundleReloader;
import servershell.util.ReverseEngineerXml;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import com.ycs.fe.cache.AppCacheManager;
import com.ycs.oldfe.commandprocessor.Constants;

@Result(type="stream", name="success")
public class BEScreenAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(BEScreenAction.class);
	public InputStream inputStream;
	public String data;
	
	@Action(value="berevengg")
	public String berevengg(){
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

	@Action(value="beconfig")
	public String beconfig(){
		String message = null;
		logger.debug("This is now configured BE");
		ServletActionContext.getRequest().getSession().setAttribute("processor", "BE");
		Constants.CMD_PROCESSOR = 0;
		Constants.APP_LAYER = 0;
		Constants.FRONTEND = 1;
		message= "now configured BE";
		logger.debug("This is now configured BE");
		inputStream = new ByteArrayInputStream(message.getBytes());
		return SUCCESS;
	}
	
	@Action(value="besyncxml")
	public String besyncxml(){
		String message = "Not processed in BE!";
		String filepath;
		String filename;
		String fefile;
		String create;
		logger.debug("besyncxml:"+data);
		Map<String, String> jobj = new Gson().fromJson(data, Map.class);
		filepath = jobj.get("filepath");
		filename = jobj.get("filename");
		fefile = jobj.get("fefile");
		create = jobj.get("create");
		
		logger.debug(filepath);
		logger.debug(filename);
		logger.debug(fefile);
		logger.debug("saving in BE...");
		
		File dir = new File(filepath);
		if(dir.exists()){
			File f = new File(dir,filename);
			try {
				if(f.exists() || "true".equals(create)){
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
	
	 
	@Action(value="beloadfile")
	public String loadfefile(){
		String message= "Not processed!";

		String filepath;
		String filename;
		Map<String, String> jobj = new Gson().fromJson(data, Map.class);
		filepath = jobj.get("filepath");
		filename = jobj.get("filename");
		logger.debug(filepath);
		logger.debug(filename);
		logger.debug("loadding...");
		Map<String,Object> jres =new HashMap<String,Object>();
		File dir = new File(filepath);
		if(dir.exists()){
			File f = new File(dir,filename);
			try {
				jres.put("data", message);
				if(f.exists()){
					String befile = FileUtils.readFileToString(f);
					jres.put("data", befile);
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
	
	@Action(value="besavefile")
	public String savefefile(){
		String message= "Not processed!";
		String filepath;
		String filename;
		String fefile;
		Map<String, String> jobj = new Gson().fromJson(data, Map.class);
		filepath = jobj.get("filepath");
		filename = jobj.get("filename");
		fefile = jobj.get("fefile");
		
		logger.debug(filepath);
		logger.debug(filename);
		logger.debug(fefile);
		logger.debug("saving...");
		
		File dir = new File(filepath);
		if(dir.exists()){
			File f = new File(dir,filename);
			try {
				if(f.exists()){
					FileWriter fw = new FileWriter(f);
					fw.write(fefile);
					fw.close();
					
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
	
	@Action(value="becreatefile")
	public String createfefile(){
		String message= "Not processed!";
		String filepath;
		String filename;
		Map<String, String> jobj = new Gson().fromJson(data, Map.class);
		filepath = jobj.get("filepath");
		filename = jobj.get("filename");
		
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
	
	@Action(value="bedeletefile")
	public String bedeletefile(){
		String message= "Not processed!";
		String filepath;
		String filename;
		Map<String, String> jobj = new Gson().fromJson(data, Map.class);
		filepath = jobj.get("filepath");
		filename = jobj.get("filename");
		
		File dir = new File(filepath);
		if(dir.exists()){
			File f = new File(dir,filename);
			try {
				if(f.exists()){
					ResourceBundle rb = ResourceBundle.getBundle("config");
					File deleteDir = new File(CompoundResource.getString(rb,"beapplication_home"));
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
	
	@Action("bereloadconfig")
	public String bereloadconfig(){
		ResourceBundleReloader.reloadBundles();
		inputStream = new ByteArrayInputStream("true".getBytes());
		return "success"; 
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	
}
