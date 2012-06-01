package servershell.actions;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class MonitoringLocalAction extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(MonitoringLocalAction.class);

	private String cmd;
	private Map<String, Object> session;
	private String name;
	private String role;
	private String password;
	private InputStream inputStream;

	@Action(value = "monitoring", results = { @Result(name = "success", type = "stream", params = { "contentType", "text/html", "inputName", "inputStream", }) })
	public String execute() {
		logger.debug("monitoring action..");
		String message;
		String jsonString = "";
		JSONObject jobj = new JSONObject();
		Map<String, String> nameMap = null;
		if (name != null && session.get(name) != null) {
			addActionError("Name is required");
		}

		if (password == null) {
			addActionError("Password is required");
		}

		if (cmd.startsWith("login")) {

		}

		if (cmd.startsWith("list")) {
			String[] path = cmd.split(" ");
			String relPath = path[1];
			if (relPath == null) {
				relPath = ".";
			}

			String rootPath = (String) session.get("pwd");
			rootPath += "/" + relPath;

			jobj.put("dirroot", rootPath);

			File f = new File(rootPath);
			// put in security of path matching
			File[] flist = f.listFiles();
			for (File fl : flist) {
				if (fl.isDirectory()) {
					jobj.put("dir", fl.lastModified() + " " + fl.getPath());
				} else {
					jobj.put("file", fl.length() + "\t" + fl.lastModified() + "\t" + fl.getPath());
				}
			}

		} else if (cmd.startsWith("cd")) {
			String[] path = cmd.split(" ");
			String relPath = path[1];
			if (relPath == null) {
				relPath = ".";
			}
			String rootPath = (String) session.get("pwd");
			rootPath += "/" + relPath;
			session.put("pwd", rootPath);
			jobj.put("pwd", rootPath);
		} else if (cmd.startsWith("home")) {
			String rootPath = (String) session.get("pwd");
			rootPath = System.getProperty("user.home");
			session.put("pwd", rootPath);
			jobj.put("pwd", rootPath);
		} else if (cmd.startsWith("download")) {
			String rootPath = System.getProperty("user.home") + "/ezlink_trs";
			jobj.put("download", "not implemented");
		} else if (cmd.startsWith("upload")) {
			jobj.put("download", "not implemented");
		} else if (cmd.startsWith("tail")) {

		} else if (cmd.startsWith("log")) {

		}

		if (getActionErrors().size() > 0) {
			jsonString = getActionErrors().toString();
		}
		inputStream = new ByteArrayInputStream(jsonString.getBytes());
		return SUCCESS;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

}
