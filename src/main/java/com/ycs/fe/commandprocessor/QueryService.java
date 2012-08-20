package com.ycs.fe.commandprocessor;

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
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

public class QueryService {

private static Logger logger = Logger.getLogger(QueryService.class);

	public String remoteCommandProcessor(String submitdataObj, String screenName) {
		String ret = "";
		try {
			ret = sendToBE("remoteCommandProcessor.action",screenName, submitdataObj, "submitdataObj");
		} catch (IOException e) {
			logger.error("remoteCommandProcessor in FE call",e);
		}
		return ret;
	}

	public String selectOnLoad(String screenName1, String jsonsubmitdata) {
		String ret = "";
		try {
			sendToBE("selectOnLoad.action",screenName1, jsonsubmitdata, "jsonsubmitdata");
		} catch (IOException e) {
			logger.error("selectOnLoad in FE call",e);
		}
		return ret;
	}

	private String sendToBE(String actionName,String screenName, String submitData, String submitdataKey) throws IOException {
		String jsonString = "This is test";
		HttpClient client = new DefaultHttpClient();
		 ResourceBundle rb = ResourceBundle.getBundle("config");
		HttpPost post = new HttpPost(rb.getString("be.webservice.basepath")+"/"+actionName);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("screenName",screenName));
		nameValuePairs.add(new BasicNameValuePair(submitdataKey,submitData));
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
	
}
