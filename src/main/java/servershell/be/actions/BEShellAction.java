package servershell.be.actions;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ResourceBundle;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import servershell.fe.actions.FileAction;
import servershell.util.CompoundResource;

import com.opensymphony.xwork2.ActionSupport;

public class BEShellAction extends ActionSupport {
	private static final long serialVersionUID = 12L;
	
	private InputStream inputStream;
	private String cmd;
	
	private static Logger logger = Logger.getLogger(BEShellAction.class);
	private String filename;
	private String beUploadDir;
	
	private File fileUpload;
	private String fileUploadFileName;
	private String fileUploadContentType;
	private String name;
	
	/**
	 * 
	 * @param filename
	 * @return
	 */
	@Action(value="bedownloadbe", results={@Result(type="stream")})
	public String download(){
		logger.info("This file is being downloaded:"+filename);
		String errors = null; 
		
		ResourceBundle rb = ResourceBundle.getBundle("config");
//		HttpPost post = new HttpPost(rb.getString("be.webservice.basepath")+"/bequery.action");
		try {
			File file = new File(filename);
			if(! file.exists()){
				FileInputStream fin = new FileInputStream(file);
				inputStream = fin;
			}else{
				errors = "File Not Found: "+filename;
			}
			
		}catch(Exception e){
			logger.error("",e);
			errors = e.getLocalizedMessage();
		}
		
		
		if(errors != null){
			inputStream = new ByteArrayInputStream(errors.getBytes());
		}else{
			
		}
		
		return SUCCESS;
	}

	/**
	 * 
	 * @param filename
	 * @return
	 */
	@Action(value="beuploadbe", results={@Result(type="stream")})
	public String upload(){
		String jsonString = "";
		JSONObject jobj= new JSONObject() ;
		String message = "";
		
//		ResourceBundle rb = ResourceBundle.getBundle("config");
		
		try {
			
//			File userDir = createUserDir(rb);
			filename = fileUploadFileName;
			File uploadDir = new File(beUploadDir);
			boolean status = false;
			logger.debug("beUploadDir exists? "+beUploadDir+ uploadDir.exists());
			if(uploadDir.exists()){
				File destFile = new File(uploadDir, filename);
				logger.debug("Destination BE path:"+destFile.getAbsolutePath());
				status = fileUpload.renameTo(destFile);
			}
			message = "Upload completed in BE.. response="+status ;
			logger.debug("Upload completed in BE.. response="+status);
			
		} catch (Exception e) {
			logger.error("",e);
			StackTraceElement[] stackTrace = e.getStackTrace();
			String str = null;
			for (StackTraceElement stElm : stackTrace) {
				str += stElm.toString()+"<br/>";
			}
			addActionError(str);
		}
		
		
		
		if (getActionErrors().size() > 0) {
			jsonString = getActionErrors().toString();
		}else{
			JSONArray jar = new JSONArray();
			jar.addAll(getActionMessages());
			jobj.put("actionMessages", jar);
			jobj.put("message", message);
			jsonString =  jobj.toString();
		}
		inputStream = new ByteArrayInputStream(jsonString.getBytes());
		
		return SUCCESS;
	}
	
	
	private File createUserDir(ResourceBundle rb) {
		String tempPath = CompoundResource.getString(rb, "application_home");
		
		if (name == null || !"".equals(name)){
			addActionError("name is empty, please login... ");
		}
		File f = new File(tempPath,name);
		if(!f.exists()){
			addActionMessage("creating directory: "+f.getAbsolutePath());
			f.mkdir();
		}
		return f;
	}
	
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InputStream getInputStream() {
		return inputStream;
	}



	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getBeUploadDir() {
		return beUploadDir;
	}

	public void setBeUploadDir(String beUploadDir) {
		this.beUploadDir = beUploadDir;
	}

	public File getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(File fileUpload) {
		this.fileUpload = fileUpload;
	}

	public String getFileUploadFileName() {
		return fileUploadFileName;
	}

	public void setFileUploadFileName(String fileUploadFileName) {
		this.fileUploadFileName = fileUploadFileName;
	}

	public String getFileUploadContentType() {
		return fileUploadContentType;
	}

	public void setFileUploadContentType(String fileUploadContentType) {
		this.fileUploadContentType = fileUploadContentType;
	}
	
	
	
}
