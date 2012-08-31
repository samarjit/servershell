package servershell.util;

import java.io.IOException;
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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

public class SendToBE {

	private static Logger logger = Logger.getLogger(SendToBE.class);
	
	
	public static String sendToBE(String data, String urlaction) throws IOException {
		String jsonString = "jsonString not set";
		ResourceBundle rb = ResourceBundle.getBundle("config");
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(rb.getString("be.webservice.basepath")+"/"+urlaction);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("data",data));
		nameValuePairs.add(new BasicNameValuePair("sendtobe","sendtobe"));
		try {
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			ResponseHandler<String>  responseHandler = new CustomResponseHandler();
			String responseBody = client.execute(post, responseHandler);
			System.out.println(responseBody.trim());
		
			logger.debug("This is logback debug " + responseBody.trim());
			jsonString = responseBody.trim();
			
			client.getConnectionManager().shutdown();
		} catch (UnsupportedEncodingException e) {
			logger.error("",e);
			throw e;
		} catch (ClientProtocolException e) {
			logger.error(e.getCause(),e);
			throw   e;
		} catch (IOException e) {
			logger.error("",e);
			throw e;
		}
		return jsonString;
	}

}
