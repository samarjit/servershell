package servershell.fe.actions;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.SessionAware;

import servershell.be.dao.BackendException;
import servershell.util.CompoundResource;

import com.opensymphony.xwork2.ActionSupport;

public class FileAction extends ActionSupport implements SessionAware {

	
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(FileAction.class);
	private String filename;
	private InputStream inputStream;
	private Map<String, Object> session;
	private File fileUpload;
	private String fileUploadContentType;
	private String fileUploadFileName;
	
	private String feUploadDir;
	private String beUploadDir;
	
	
	/**
	 * 
	 * @param filename
	 * @return
	 */
	@Action(value="fedownload", results={@Result(type="stream")})
	public String fedownload(){
		logger.info("This file is being downloaded:"+filename);
		String errors = null; 
		
		 
		try {
			File f = new File(filename);
			if(f.exists()){
				FileInputStream fin = new FileInputStream(f);
			inputStream = fin;
			}else{
				errors = "File Does not exist: "+filename;
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
	@Action(value="download", results={@Result(type="stream")})
	public String download(){
		logger.info("This file is being downloaded:"+filename);
		String errors = null; 
		
		ResourceBundle rb = ResourceBundle.getBundle("config");
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(rb.getString("be.webservice.basepath")+"/download.action");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("filename",filename));
		try {
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

//			ResponseHandler<String>  responseHandler = new org.apache.http.impl.client. BasicResponseHandler();
			HttpResponse responseBody = client.execute(post);
			inputStream = responseBody.getEntity().getContent();
			
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
	 * @param in fileUpload
	 * @param in fileUploadContentType
	 * @param in fileUploadFileName
	 * @param in filename
	 * @return
	 */
	@Action(value="feupload", results={@Result(type="stream")})
	public String feupload(){
		
		String jsonString = "";
		JSONObject jobj= new JSONObject() ;
		String message = "";
		
		ResourceBundle rb = ResourceBundle.getBundle("config");
		
		try {
			if((session == null || !session.containsKey("name") )){
				addActionError("User is not logged in please login first..");
				throw new BackendException("User needs to login..");
			}
			
			File userDir = createUserDir(rb);
			filename = fileUploadFileName;
			File uploadDir = new File(feUploadDir);
			boolean status = false;
			if(uploadDir.exists()){
				File destFile = new File(uploadDir, filename);
				status = fileUpload.renameTo(destFile);
			}
			message = "Upload completed .. response="+status ;
			logger.debug("Upload completed .. response="+status);
			
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
	
	/**
	 * @param in fileUpload
	 * @param in fileUploadContentType
	 * @param in fileUploadFileName
	 * @param in filename
	 * @return
	 */
	@Action(value="upload", results={@Result(type="stream")})
	public String upload(){
		 
		String jsonString = "";
		JSONObject jobj= new JSONObject() ;
		String message = "";
		
		HttpClient client = new DefaultHttpClient();
		 ResourceBundle rb = ResourceBundle.getBundle("config");
		 HttpPost post = new HttpPost(rb.getString("be.webservice.basepath")+"/download.action");
		 MultipartEntity entity = new MultipartEntity( HttpMultipartMode.BROWSER_COMPATIBLE );
		 
		 try {
			if((session == null || !session.containsKey("name") )){
					addActionError("User is not logged in please login first..");
					throw new BackendException("User needs to login..");
				}
			 
			 File userDir = createUserDir(rb);
			 filename = fileUploadFileName;
			 File uploadFile = new File(userDir, filename);
			 // For File parameters
			 entity.addPart( "fileType", new FileBody(uploadFile , "application/zip" ));

			 // For usual String parameters
			 entity.addPart( "filename", new StringBody(filename));
			 entity.addPart( "beUploadDir", new StringBody(beUploadDir));
			 entity.addPart( "name", new StringBody((String)session.get("name")));

			 post.setEntity( entity );

			 // Here we go!
			 String response = EntityUtils.toString( client.execute( post ).getEntity(), "UTF-8" );

			 client.getConnectionManager().shutdown();
			 message = "Upload completed .. response="+response;
			 logger.debug("Upload completed .. response="+response);
			 
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
		String name =  (String) session.get("name");
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
	
	
	 

	public String getFeUploadDir() {
		return feUploadDir;
	}

	public void setFeUploadDir(String feUploadDir) {
		this.feUploadDir = feUploadDir;
	}

	public String getBeUploadDir() {
		return beUploadDir;
	}

	public void setBeUploadDir(String beUploadDir) {
		this.beUploadDir = beUploadDir;
	}

	public String getFileUploadContentType() {
		return fileUploadContentType;
	}
 
	public void setFileUploadContentType(String fileUploadContentType) {
		this.fileUploadContentType = fileUploadContentType;
	}
 
	public String getFileUploadFileName() {
		return fileUploadFileName;
	}
 
	public void setFileUploadFileName(String fileUploadFileName) {
		this.fileUploadFileName = fileUploadFileName;
	}
 
	public File getFileUpload() {
		return fileUpload;
	}
 
	public void setFileUpload(File fileUpload) {
		this.fileUpload = fileUpload;
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

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	
	
}
