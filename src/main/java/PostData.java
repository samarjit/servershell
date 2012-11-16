import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class PostData {

	public static String excutePost(String targetURL, String urlParameters)
	  {
	    URL url;
	    HttpURLConnection connection = null;  
	    try {
	      //Create connection
	      url = new URL(targetURL);
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("POST");
	      connection.setRequestProperty("Content-Type", 
	           "application/x-www-form-urlencoded");
				
	      connection.setRequestProperty("Content-Length", "" + 
	               Integer.toString(urlParameters.getBytes().length));
	      connection.setRequestProperty("Content-Language", "en-US");  
				
	      connection.setUseCaches (false);
	      connection.setDoInput(true);
	      connection.setDoOutput(true);

	      //Send request
	      DataOutputStream wr = new DataOutputStream (
	                  connection.getOutputStream ());
	      wr.writeBytes (urlParameters);
	      wr.flush ();
	      wr.close ();

	      //Get Response	
	      InputStream is = connection.getInputStream();
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	      String line;
	      StringBuffer response = new StringBuffer(); 
	      while((line = rd.readLine()) != null) {
	        response.append(line);
	        response.append('\r');
	      }
	      rd.close();
	      return response.toString();

	    } catch (Exception e) {

	      e.printStackTrace();
	      return null;

	    } finally {

	      if(connection != null) {
	        connection.disconnect(); 
	      }
	    }
	  }
	
	//https://forums.oracle.com/forums/thread.jspa?threadID=2353895
public static void executePost ( String targetURL, Par... parametros) {
		
		try {
    	    // Construct data
    	    String data = "";
    	    for ( Par par : parametros ) {
    	    	if ( data.length() > 1 ) data += "&";
    	    	data += URLEncoder.encode(par.param, "UTF-8") + "=" + URLEncoder.encode(par.value, "UTF-8");
    	    }
 
    	    // Connect
    	    URL url = new URL(targetURL);
    	    URLConnection conn = url.openConnection();
    	    
    	    // Send data
    	    conn.setDoOutput(true);
    	    conn.setDoInput(true); //
    	    
    	    // No caching, we want the real thing.
    	    conn.setUseCaches (false);
    	    // Specify the content type.
    	    conn.setRequestProperty ("Content-Type", "application/x-www-form-urlencoded");   	    
    	    
    	    
    	    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
    	    wr.write(data);
    	    wr.flush();
 
    	    // Get the response
    	    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    	    String line;
    	    while ((line = rd.readLine()) != null) {
    	        // Process line...
    	    	System.out.println(line);
    	    }
    	    wr.close();
    	    rd.close();
    	} catch (Exception e) {
    	}
		
		
	}

	class Par {
		
		String param;
		String value;
		
		public Par(String param, String value) {
			super();
			this.param = param;
			this.value = value;
		}
		
	}
	
	public static void main(String srgs[]) {
		  PostData.excutePost("http://192.168.100.36:8080/EZLinkPrepaidPoller/EmailServlet","<?xml version=\"1.0\" encoding=\"UTF-8\"?><msg><txnrefno>1205312176688181</txnrefno><message>Email message goes here</message><messagedate>2012-11-0" + 
				"5 14:36:32.0</messagedate><emailid>samarjit.s@yalamanchili.com.sg</emailid><mailfrom>prepaid@prepaidcardssupport.net</mailfrom><mailto>samarjit.s@yalamanchili.com.sg</mailto><subject>Failed Sanction screening after" + 
				" successful registration</subject><txntype>OFACKYCREJ</txntype><details>NO_DATA</details></msg>");
	}
}
