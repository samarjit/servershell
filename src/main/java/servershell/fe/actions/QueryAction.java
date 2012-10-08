package servershell.fe.actions;



 

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.json.JSONObject;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.SessionAware;

import servershell.be.dao.BackendException;
import servershell.util.SendToBE;

import com.opensymphony.xwork2.ActionSupport;


public class QueryAction extends ActionSupport implements SessionAware{
	
	 
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(QueryAction.class);
	
	private String query;
	private InputStream inputStream;
	private Map<String, Object> session;
	private String cmd;
	
	@Action(value="query", results ={@Result(name="success",type="stream")})
	public String execute(){
			logger.info("This is from log back front end query captured:"+query);
			ResourceBundle rb = ResourceBundle.getBundle("config");
			String jsonString = "";
			try{
				String name = null, role = null;
				
				if (cmd == null) {
					addActionError("Cmd is required");
					throw new BackendException("Cmd is required");
				}
				
				if((session  == null || !session.containsKey("name") ) && !cmd.equals("login")){
					addActionError("User needs to log in");
					throw new BackendException("User needs to log in required");
				}else{
					role = (String) session.get("role");
					name = (String) session.get("name");
				}
				if(query != null ){
					if(query.endsWith(";")){
						query = query.substring(0, query.length()-1);
					}
				}
				if(query.matches("(?ims:select)[\\S\\s]*(?ims:from)[\\S\\s]*")){
					jsonString = sendToBE(query, rb);
				}else if(query.matches("[\\S\\s]*(?ims:insert)[\\S\\s]*(?ims:into)[\\S\\s]*") ){
					if(role.equals("ADMIN")){
						jsonString = sendToBE(query, rb);
					}else{
						addActionError("Insert not allowed for user "+name);
						throw new BackendException("Insert not allowed for user "+name);
					}
				}else if(query.matches("[\\S\\s]*(?ims:update|delete)[\\S\\s]*(?ims:where)[\\S\\s]*")){
					if(role.equals("ADMIN")){
						jsonString = sendToBE(query, rb);
					}else{
						addActionError("Update not allowed for user "+name);
						throw new BackendException("Update not allowed for user "+name);
					}
				}else if(query.matches("[\\S\\s]*(?ims:desc)[\\S\\s]*(?ims:select)[\\S\\s]*") ){ //describe
					String qr = query.substring(4).trim();
					jsonString = SendToBE.sendToBE(qr, "berevengg.action");
					jsonString = "<pre>"+JSONObject.fromObject(jsonString).getString("tabledetails")+"</pre>";
					
				}else if(query.matches("[\\S\\s]*(?ims:desc)[\\S\\s]*(?ims:\\w+)[\\S\\s]*") ){ //describe
					String qr = query.substring(4).trim();
					qr = "select * from "+qr+" where 1=2";
					jsonString = SendToBE.sendToBE(qr, "berevengg.action");
					jsonString = "<pre>"+JSONObject.fromObject(jsonString).getString("tabledetails")+"</pre>";
				}else{
					logger.debug("invalid query skipping..."+query);
					addActionError("invalid query skipping...");
					throw new BackendException("invalid query skipping... it is not matching insert/update/delete");
					 
				}
				
				
			}catch(BackendException e){
				addActionError("Validation Error :" +e.toString());
			} catch (Exception e) {
				StackTraceElement[] stackTrace = e.getStackTrace();
				String str = "";
				for (StackTraceElement stElm : stackTrace) {
					str += stElm.toString()+"<br/>";
				}
				addActionError("Unknown Exception "+e.toString()+"  "+ str);
				logger.error(" Unkown Exception",e);
			}
			
			if (getActionErrors().size() > 0) {
				jsonString = "errors:"+ getActionErrors().toString()+"  messages:"+getActionMessages();
			}else{
				jsonString = getActionMessages()+"  "+jsonString;
			}
			inputStream = new ByteArrayInputStream(jsonString.getBytes());
			return SUCCESS;
	}



	private String sendToBE(String qry, ResourceBundle rb) throws IOException {
		String jsonString = "This is test";
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(rb.getString("be.webservice.basepath")+"/bequery.action");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("query",qry));
		try {
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			ResponseHandler<String>  responseHandler = new BasicResponseHandler();
			String responseBody = client.execute(post, responseHandler);
			System.out.println(responseBody.trim());
		
			logger.debug("This is logback debug " + responseBody.trim());
			jsonString = responseBody.trim();
		} catch (UnsupportedEncodingException e) {
			logger.error("",e);
			throw e;
		} catch (ClientProtocolException e) {
			logger.error("",e);
			throw e;
		} catch (IOException e) {
			logger.error("",e);
			throw e;
		}
		return jsonString;
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

	
}
