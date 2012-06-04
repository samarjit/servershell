package servershell.actions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import servershell.be.dao.DBConnector;

import com.opensymphony.xwork2.ActionSupport;

public class BEQueryAction extends ActionSupport{
	 
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(QueryAction.class);
	
	private String query;
	private InputStream inputStream;
	
	@Action(value="bequery", results ={@Result(name="success",type="stream")})
	public String execute(){
			logger.info("This is from is be query= "+ query);
			String jsonString = "This is be query "+query;
			DBConnector db = new DBConnector();
			try {
				Map<String, String> row ;
				List<Map<String, String>> values = new ArrayList<Map<String, String>>();
				int countrec = 0;
				String retval;
				if(query.matches("(?ims:select)[\\S\\s]*(?ims:from)[\\S\\s]*")){
					CachedRowSet crs = db.executeQuery(query);
					ResultSetMetaData md = crs.getMetaData();
					int colcount = md.getColumnCount();
					
						while (crs.next()) {
							row = new HashMap<String, String>();
							for (int i = 1; i <= colcount; i++) {
								row.put(md.getColumnLabel(i), crs.getString(i));
							}
							values.add(row);
							countrec++;	
						}
						retval = String.valueOf(countrec) ;
						addActionMessage("number of rows fetched: "+retval);
				}else{
					db.executeUpdate(query);
				}
			} catch (Exception e) {
				StackTraceElement[] stackTrace = e.getStackTrace();
				String str = null;
				for (StackTraceElement stElm : stackTrace) {
					str += stElm.toString()+"<br/>";
				}
				addActionError("Unknown Exception "+e.getMessage()+ str);
				logger.error(" Unkown Exception",e);
			}
			
			logger.debug("This is logback debug");
			
			if (getActionErrors().size() > 0) {
				jsonString = "errors:"+ getActionErrors().toString()+"  messages:"+getActionMessages();
			}else{
				jsonString = getActionMessages()+"  "+jsonString;
			}
			
			inputStream = new ByteArrayInputStream(jsonString.getBytes());
			return SUCCESS;
	}



	public String getQuery() {
		return query;
	}



	public void setQuery(String query) {
		this.query = query;
	}



	public InputStream getInputStream() {
		return inputStream;
	}

	
	
}
