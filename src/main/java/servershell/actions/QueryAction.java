package servershell.actions;



 

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;


public class QueryAction extends ActionSupport {
	
	 
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(QueryAction.class);
	
	private String query;
	private InputStream inputStream;
	
	@Action(value="query", results ={@Result(name="success",type="stream")})
	public String execute(){
			logger.info("This is from log back front end query captured:"+query);
			String jsonString = "This is test";
			ResourceBundle rb = ResourceBundle.getBundle("config");
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(rb.getString("be.webservice.basepath")+"/bequery.action");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("query",query));
			try {
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				ResponseHandler<String>  responseHandler = new BasicResponseHandler();
				String responseBody = client.execute(post, responseHandler);
				System.out.println(responseBody.trim());
			
				logger.debug("This is logback debug " + responseBody.trim());
				jsonString = responseBody.trim();
			} catch (UnsupportedEncodingException e) {
				logger.error("",e);
			} catch (ClientProtocolException e) {
				logger.error("",e);
			} catch (IOException e) {
				logger.error("",e);
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
