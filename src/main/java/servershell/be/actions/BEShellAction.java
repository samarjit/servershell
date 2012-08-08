package servershell.be.actions;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

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
			if(file.exists()){
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
	
	long prevpos = 0;
	String belogpath = "";
	@Action(value="berunlog", results={@Result(type="stream")})
	public String berunlog(){
		String res = "empty no response";
		 
		JSONObject jsonMessage = new JSONObject();
		String message = "";
		String endchar = "";
		String lastline = "";
		try {
			RandomAccessFile raf = new RandomAccessFile(belogpath, "r");
			System.out.println("berunlog action .."+prevpos);
			
			
			long len = raf.length();
			byte str[] = new byte[200];
			if(prevpos < len){
				raf.seek(prevpos);
				StringWriter strw = new StringWriter();
				while((raf.read(str))!= -1){
					strw.write(new String(str));
				}
				
				BufferedReader stringReader = new BufferedReader(new StringReader(strw.toString()));
				String tempLine = "";
				String tempLastLine = "";
				while((tempLine = stringReader.readLine()) != null){
					tempLastLine = lastline;
					lastline = tempLine.trim();
					message += tempLastLine +"<br/>";
				}
				//find last EOL and truncate string upto last EOL
				raf.seek(raf.getFilePointer() - 1);
				byte lastbyte = raf.readByte();
//				logger.debug("Message ends with:"+ raf.readByte()+" " +raf.readByte()+" " +raf.readByte()+" " + raf.readByte()+" " +raf.readByte()+" " +raf.readByte());
				if(lastbyte == '\n')endchar = "EOL";
			}
			long pos = raf.getFilePointer();
			raf.close();
			jsonMessage.put("time",new Date().toString());
			jsonMessage.put("prevpos",prevpos);
			jsonMessage.put("pos",pos);
			jsonMessage.put("endswith",endchar);
			jsonMessage.put("message", message);
			jsonMessage.put("lastline", lastline);
			
			res = jsonMessage.toString();
		} catch (Exception e) {
			logger.error(e);
			 addActionError("Exception in berunlog please check:"+e +"  "+belogpath);
			 
		}
		
		if(getActionErrors().size() > 0){
			res = getActionErrors().toString();
		}
		logger.debug(res);
		inputStream = new ByteArrayInputStream(res.getBytes());
		return SUCCESS;
	}
	
	int pagesize  = 0;
	
	/**
	 * belogpath in
	 * pagesize in
	 * cmd pageup/pagedown/getalluptoend
	 * 
	 * 
	 * @return
	 */
	@Action(value="bescrolllog", results={@Result(type="stream")})
	public String scrolllog(){
		
		
//		File f = createUserDir(rb);
		String message = "";
		try{
			
			logger.debug("BEscrollog started .."+cmd);
			RandomAccessFile raf = new RandomAccessFile(belogpath, "r");
			FileChannel fc = raf.getChannel();
			ArrayList<String> ar = new ArrayList<String>(50);
			byte bytear[] = new byte[200];
			
			
			long pos = raf.getFilePointer();
			
			
			long len = raf.length();
			
			//read 20 lines
			int numlines = -1;
	//		File logpage = new File(f, "logfile.txt");
			StringWriter strw = new StringWriter();
			
			int ipageup = pagesize;// ("pageup".equals(cmd))? pagesize: 0;
			int ipagedown = pagesize;// ("pagedown".equals(cmd))? pagesize: 0;
			
			long templen = (prevpos == 0|| prevpos > len)? len: prevpos; 
			final int READBYTE = 200;
			int bytestoread = READBYTE;
			byte [] halfline = new byte[200];
			byte []reverseline = new byte[200];
			byte [] tocopy = new byte [200];
			boolean breakflag = false;
			//cases 200 bytes not even 1 line completed
			//case 200 bytes 3 lines completed but only 1 like was asked for
			if ("pageup".equals(cmd)) {
				while (numlines <= ipageup && templen > 0) {
					if (templen > 0) {
						if (templen > READBYTE) {
							raf.seek(templen - bytestoread);
							templen = templen - bytestoread;
						} else {
							raf.seek(0);
							bytestoread = (int) templen;
							templen = 0;
	
						}
						try {
							Arrays.fill(bytear, (byte)0);
							raf.readFully(bytear, 0, bytestoread);
//							reverse(bytear);
						} catch (IOException e) {
							throw e;
						}
						
						int lastcopypoint = 0;
						Arrays.fill(tocopy, (byte)0);
						int i=0;
						
//						for (byte b : bytear) {
//							System.out.print((char)b);
//						}
//						System.out.println("going into loop");
						for (i = bytestoread -1 ;i  >=0 ; i--) {
							byte b = bytear[i];
							if (b == '\n') {
								numlines++;
								if(numlines == ipageup){
									 templen = templen  + i;
//									 halfline = Arrays.copyOfRange(bytear,0, i);
//									 reverse(halfline);
//									 System.out.println("Half Line:"+new String(halfline));
//									 ar.add(new String(halfline));
									 tocopy[i] = b;
									 System.out.print((char)b);
									 System.out.println("numlines:"+numlines+ " ipageup:"+ipageup+" templen:"+templen+" bytestoread:"+bytestoread+" i="+i);
									 breakflag = true;
									 break;
								}else{
//									tocopy = Arrays.copyOfRange(bytear, lastcopypoint, i);
//									reverse(tocopy);
//									System.out.println("One Line:"+new String(tocopy));
//									lastcopypoint = i;
//									ar.add(new String(tocopy));
								}
								 
								
							}else{
								tocopy[i] = b;
								System.out.print((char)b);
							}
						}
//						ArrayUtils.reverse(bytear);
//						reverse(bytear);
//						for (int j=bytestoread-1 ; j >=0; j--) {
//							System.out.print((char)bytear[j]);
//						}
//						System.out.print("|");
						ar.add(new String(tocopy,0,bytestoread));
						
					} 
					
					if(breakflag)break;
				}
				//reverse(tocopy);
				
				
				
				prevpos = templen;
				for (int i = ar.size() - 1; i >= 0; i--) {
					byte[] bar = ar.get(i).getBytes();
					//reverse(bar);
					strw.write(new String(bar));
				}
				System.out.println("Full Text:\n"+strw.toString());
			}else if("pagedown".equals(cmd)){
				System.out.println("templen:"+templen+" len:"+len);
				while (numlines <= ipagedown && templen < len) {
					if (templen < len) {
						if ((len - templen )> READBYTE) {
							raf.seek(templen );
							templen = templen + bytestoread;
						} else {
							raf.seek(templen);
							bytestoread = (int) (len - templen);
							templen = len;
	
						}
						try {
							raf.readFully(bytear, 0, bytestoread);
						} catch (IOException e) {
							throw e;
						}
						
						Arrays.fill(tocopy, (byte)0);
						int i = 0;
						for (i = 0; i< bytestoread ; i++) {
							byte b = bytear[i];
							if (b == '\n') {
								numlines++;
								if(numlines == ipageup){
									System.out.println("templen (before):"+templen);
									 templen = templen - (bytestoread- i);
//									 halfline = Arrays.copyOfRange(bytear,0, i);
									 System.out.println("numlines:"+numlines+ " ipagedown:"+ipagedown+" templen:"+templen+" bytestoread:"+bytestoread+" i="+i);
									 tocopy[i] = b;
//									 System.out.print((char)b);
									 breakflag = true;
									 break;
								}
							}else{
								tocopy[i] = b;
//								System.out.print((char)b);
							}
	
						}
						ar.add(new String(tocopy,0,i));
						
					}
					if(breakflag)break;
				}
				System.out.println("2templen:"+templen+" len:"+len);
				for (int i = 0 ; i < ar.size(); i++) {
					strw.write(ar.get(i));
				}
				prevpos = templen;
				System.out.println(strw.toString());
			}else if("getalluptoend".equals(cmd)){
				raf.seek(prevpos);
				
				while((raf.read(bytear))!= -1){
					strw.write(new String(bytear));
					Arrays.fill(bytear, (byte)0);
				}
				strw.write(new String(bytear));
				System.out.println(strw.toString());
				
			}
			
			
			BufferedReader stringReader = new BufferedReader(new StringReader(strw.toString()));
			String tempLine = "";
			while((tempLine = stringReader.readLine()) != null){
				message += tempLine +"<br/>";
			}
			
			JSONObject jsonMessage = new JSONObject();
			jsonMessage.put("time",new Date().toString());
			jsonMessage.put("prevpos",prevpos);
			jsonMessage.put("pos",pos);
//			jsonMessage.put("endswith",endchar);
			jsonMessage.put("message", message);
//			jsonMessage.put("lastline", lastline);
			
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
	
    public static void reverse(byte[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        byte tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
	
	 

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public String getBelogpath() {
		return belogpath;
	}

	public void setBelogpath(String belogpath) {
		this.belogpath = belogpath;
	}

	public long getPrevpos() {
		return prevpos;
	}

	public void setPrevpos(long prevpos) {
		this.prevpos = prevpos;
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
	
	public static void main(String[] args) throws Exception {
		String belogpath ="C:/Users/Samarjit/Desktop/Book1.txt";
		int pagesize = 4;
		String cmd = "pageup";///pagedown/getalluptoend
		char ch = '\0';
		long prevpos = 0; 
		System.out.println("Starting shell mode , press 'q'<enter> to exit");
		while((ch = (char) System.in.read())!= 'q'){
			BEShellAction be = new BEShellAction();			
			be.setBelogpath(belogpath);
			be.setPagesize(pagesize);
			be.setPrevpos(prevpos);
			if(ch == 'u')
			be.setCmd("pageup");
			else if(ch == 'd')be.setCmd("pagedown");
			else if(ch == 'a')be.setCmd("getalluptoend");
			else continue;
			
			System.out.println("calling servershell with prevpos:"+prevpos);
			be.scrolllog();
			prevpos = be.getPrevpos();
		}
	}
	
}
