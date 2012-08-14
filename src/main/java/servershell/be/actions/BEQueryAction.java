package servershell.be.actions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import servershell.be.dao.DBConnector;
import servershell.be.dto.PrepstmtDTO;
import servershell.fe.actions.QueryAction;

import com.opensymphony.xwork2.ActionSupport;

public class BEQueryAction extends ActionSupport{
	 
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(QueryAction.class);
	
	private String query;
	private InputStream inputStream;
	
    
	@SuppressWarnings("unused")
	private static int getRowCount(ResultSet rs) throws SQLException {
		   int current = rs.getRow();
		   rs.last();
		   int count = rs.getRow();
		   if(count == -1)
		       count = 0;
		   if(current == 0)
		       rs.beforeFirst();
		   else
		       rs.absolute(current);
		   return count;
	} 
	
	@Action(value="bequery", results ={@Result(name="success",type="stream")})
	public String execute(){
			logger.info("This is from is be query= "+ query);
			String jsonString = "This is be query "+query;
			DBConnector db = new DBConnector();
			try {
				Map<String, String> row ;
				List<Map<String, String>> values = new ArrayList<Map<String, String>>();
				int countrec = 0;
				if(query.matches("(?ims:select)[\\S\\s]*(?ims:from)([\\S\\s]*)")){
					
					Pattern p  = Pattern.compile("(?ims:select)[\\S\\s]*(?ims:from)([\\S\\s]*)");
					Matcher m = p.matcher(query);
					m.find();
					logger.debug("select query grp 1 grp 3"+m.group(1));
					String countQuery = "select count(1) from "+m.group(1);
					CachedRowSet crs1 = db.executeQuery(countQuery);
					int totalcount = 0;
					while(crs1.next()){
						totalcount = crs1.getInt(1);
						}
					crs1.close();
					if(totalcount < 1000){
					CachedRowSet crs = db.executeQuery(query);
					ResultSetMetaData md = crs.getMetaData();
					int colcount = md.getColumnCount();
					boolean first = true;
					String cols = "";
					jsonString = "";
						while (crs.next()) {
							row = new HashMap<String, String>();
							jsonString += "<tr>";
							for (int i = 1; i < colcount; i++) {
								if(md.getColumnTypeName(i ).equals("DATE")|| md.getColumnTypeName(i ).equals("TIMESTAMP")){
									Date dt = null;
									if(md.getColumnTypeName(i ).equals("DATE")) dt = crs.getDate(i);
									if(md.getColumnTypeName(i ).equals("TIMESTAMP")) dt = crs.getTimestamp(i);
									
									String dtStr = (dt == null)?"":PrepstmtDTO.getDateStringFormat(dt,"yyyy-MM-dd HH:mm:ss");
									row.put(md.getColumnLabel(i), dtStr);
									jsonString +=  "<td>"+ dtStr +" </td>";
								}else{
									String res = (crs.getString(i) ==null?"":crs.getString(i));
									row.put(md.getColumnLabel(i), res);
									jsonString +=  "<td>"+ res +" </td>";
								}
								if(first){
									cols += "<th>"+md.getColumnLabel(i) +" </th>";
									
								}
								
							}
							first = false;
							jsonString += "</tr>";
							values.add(row);
							countrec++;	
						}
						jsonString = "<table class='grid' border=1 ><tr>"+cols+"</tr>"+jsonString +"</table>";
						String.valueOf(countrec);
					}else{
						addActionMessage("There are more than 1000 rcords please modify query and add more filter criterion");
					}
						addActionMessage("number of records from count query "+totalcount);
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
