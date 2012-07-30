package servershell.fe.actions;
import java.io.File;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionSupport;
 

public class FileUploadAction extends ActionSupport{
 

	private static final long serialVersionUID = 1L;
	private File fileUpload;
	private String fileUploadContentType;
	private String fileUploadFileName;
 
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
 
	@Action(value="upload1", results={@Result(name="success",type="dispatcher", location="/jsp/result.jsp" ),
			@Result(name="input",type="dispatcher", location="/jsp/upload.jsp" )})
	public String execute() throws Exception{
		System.out.println("fileUpload: "+fileUpload);
		System.out.println("fileUploadContentType: "+fileUploadContentType);
		System.out.println("fileUploadFileName: "+fileUploadFileName);
		return SUCCESS;
 
	}
 
	public String display() {
		return NONE;
	}
 
}