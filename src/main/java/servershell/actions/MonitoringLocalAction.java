package servershell.actions;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.SessionAware;

import servershell.be.dao.BackendException;
import servershell.util.CmdRunner;
import servershell.util.CompoundResource;

import com.opensymphony.xwork2.ActionSupport;

public class MonitoringLocalAction extends ActionSupport implements SessionAware {

    public static void main(String [] ar) {
    	
    	Pattern p = Pattern.compile("(tail).*?(-\\d)\\s*?(\\S+)");
		Matcher m = p.matcher("tail   -4  some.xml");
		m.find();System.out.println(m.group(1)+"  "+m.group(2)+ "   "+m.group(3));
		
		String ss[] = "a   dggfg".split(" "); 
		System.out.println(ss.length);
	}
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(MonitoringLocalAction.class);

	private String cmd;
	private Map<String, Object> session;
	private String name;
	private String role;
	private String password;
	private InputStream inputStream;
	private String pageup;
	private String pagedown;
	
	@Action(value = "monitoring", results = { @Result(name = "success", type = "stream", params = { "contentType", "text/html", "inputName", "inputStream"}) })
	public String execute() {
		logger.debug("monitoring action..");
		ResourceBundle rb = ResourceBundle.getBundle("config");
		String message = "";
		String jsonString = "";
		JSONObject jobj = new JSONObject();
		
		try {
			if (cmd == null) {
				addActionError("Cmd is required");
				throw new BackendException("Cmd is required");
			}
			if((session == null || !session.containsKey("name") ) && !cmd.startsWith("login")){
				addActionError("User is not logged in please login first..");
				throw new BackendException("User needs to login..");
			}
			if (cmd.startsWith("login")) {
				URL url = this.getClass().getResource("/user.properties");
				try {
					if (name == null) {
						throw new BackendException("Name is required");
					}

					if (password == null) {
						throw new BackendException("Password is required");
					}
					
					FileReader userFR = new FileReader(url.getPath());
					Properties propUser = new Properties();
					propUser.load(userFR);
					String cred = propUser.getProperty("name."+name);
					for (Object itr : propUser.keySet()) {
						logger.info("propUser:"+itr);
					}
					String[] credentials = (cred ==null)?null:cred.split(",");
					if(credentials == null || credentials.length == 0){
						addActionError("Unknown user..name."+name);
						throw new BackendException("Unknown User");
					}
					if(credentials[0].equals(password)){
						addActionMessage("User found..");
						session.put("name", name);
						
					}else{
						addActionError("Invalid password ..");
						throw new BackendException("Invalid password .."+name);
					}
					if(credentials[1].equals("ADMIN")){
						role = "ADMIN";
						session.put("role", role);
						addActionMessage("role is admin");
					}
					
				} catch (FileNotFoundException e) {
					logger.error(e);
				} catch (IOException e) {
					logger.error(e);
				}
			}
			
			if (cmd.startsWith("ls")) {
				String[] path = cmd.split(" ");
				String relPath = "*";
				if (path.length > 1) {
					relPath = path[1];
				}

				String rootPath = (String) session.get("pwd");
				//rootPath += "/" + relPath;

				jobj.put("dirroot", rootPath);
				message += rootPath;
				File f = new File(rootPath);
				// put in security of path matching
				FileFilter regex = new WildcardFileFilter(relPath);
				File[] flist =  f.listFiles(regex);
				SimpleDateFormat sm = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
				HashMap<Long, String> hm1 = new HashMap<Long, String>();
				HashMap<Long, String> hm2 = new HashMap<Long, String>();
				for (File fl : flist) {
					if (fl.isDirectory()) {
						//jobj.put("dir", fl.lastModified() + " " + fl.getPath());
						hm1.put(fl.lastModified(), String.format("dir   %25s  %s  <br/>\r\n",sm.format(fl.lastModified()),fl.getName()));
					}
				}
				for (File fl : flist) {
					if (fl.isFile()) {
						hm2.put(fl.lastModified(), String.format("file  %25s  %s  <br/>\r\n",sm.format(fl.lastModified()),fl.getName()));
//						jobj.put("file", fl.length() + "\t" + fl.lastModified() + "\t" + fl.getPath());
					}
				}
				TreeSet<Long> ar = new TreeSet<Long>(hm1.keySet());
				for (Iterator<Long> ltime = ar.descendingIterator();ltime.hasNext();) {
					message += hm1.get(ltime.next());
				}
				TreeSet<Long> ar2 = new TreeSet<Long>(hm2.keySet());
				for (Iterator<Long> ltime = ar2.descendingIterator();ltime.hasNext();) {
					message += hm2.get(ltime.next());
				}

			} else if (cmd.startsWith("cd")) {
				String[] path = cmd.split(" ");
				String relPath = ".";
				if (path.length > 1) {
					relPath = path[1];
				}
				String rootPath = (String) session.get("pwd");
				if(relPath.equals("..")){
					File f = new File(rootPath);
					rootPath = f.getParent();
				}else{
					File f = new File(rootPath,relPath);
					if(!f.exists()){
						throw new BackendException("Path does not exists.."+rootPath+"/"+relPath);
					}
					rootPath = f.getAbsolutePath();
				}
				session.put("pwd", rootPath);
				jobj.put("pwd", rootPath);
			} else if (cmd.startsWith("home")) {
				String rootPath = (String) session.get("pwd");
				rootPath = System.getProperty("user.home");
				session.put("pwd", rootPath);
				jobj.put("pwd", rootPath);
			} else if (cmd.startsWith("download")) {
				String rootPath = CompoundResource.getString(rb, "application_home");
				jobj.put("download", "not implemented"+rootPath);
			} else if (cmd.startsWith("upload")) {
				jobj.put("download", "not implemented");
			} else if (cmd.startsWith("tail")) {
				String rootPath = (String) session.get("pwd");
				
				String tempPath = CompoundResource.getString(rb, "application_home");
				name =  (String) session.get("name");
				File f = new File(tempPath,name);
				if(!f.exists()){
					addActionMessage("creating directory: "+f.getAbsolutePath());
					f.mkdir();
				}
				Pattern p = Pattern.compile("(tail).*?(-\\d*)\\s*?(\\S+)");
				Matcher m = p.matcher(cmd);
				m.find();
				
				System.out.println(m.group(1)+"  "+m.group(2)+ "   "+m.group(3));
				if(m.group(1) == null || m.group(2)==null || m.group(3) == null){
					addActionError("usage: tail -10 f.txt");
				}else{
					String lines = ""; 
					if(m.group(2) ==null || "-".equals(m.group(2))){
						lines = "-10";
					}else{
						lines = m.group(2);
					}
					
					File f2 = new File(rootPath+"/"+m.group(3));
					if(!f2.exists()){
						addActionError("file does not exists:"+(rootPath+"/"+m.group(3)));
					}else{
						String tempcmd = "tail "+lines+rootPath+"/"+m.group(3);
						File tailfile = new File(f, "tailfile.txt");
						if(tailfile.exists()){
							tailfile.delete();
							try {
								tailfile.createNewFile();
							} catch (IOException e) {
								logger.error("tailfile.txt creation error "+tailfile.getAbsolutePath() , e);
								addActionError("tailfile.txt creation error "+tailfile.getAbsolutePath());
							}
						}
						CmdRunner.process(tempcmd,null, tailfile);
						try {
							jsonString = FileUtils.readFileToString(tailfile);
						} catch (IOException e) {
							logger.error("tailfile read error:",e);
							addActionError("tailfile read error!");
						}
					}
				}
			} else if (cmd.startsWith("log")) {

			}
			
			
		} catch (BackendException e) {
			addActionError("Validation Error :" +e.toString());
		} catch (Exception e) {
			StackTraceElement[] stackTrace = e.getStackTrace();
			String str = null;
			for (StackTraceElement stElm : stackTrace) {
				str += stElm.toString()+"<br/>";
			}
			addActionError("Unknown Exception "+e.getMessage()+ str);
			logger.error(" Unkown Exception",e);
		}

		if (getActionErrors().size() > 0) {
			jsonString = getActionErrors().toString();
		}else{
			jsonString = getActionMessages()+"  "+message + " "+jobj.toString(3);
		}
		inputStream = new ByteArrayInputStream(jsonString.getBytes());
		return SUCCESS;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public InputStream getInputStream() {
		return inputStream;
	}
 
}
