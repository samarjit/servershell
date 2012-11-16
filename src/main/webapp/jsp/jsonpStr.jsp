<%@page session="false"%><%@page import="java.net.*,java.io.*,org.apache.log4j.*" %><%
Logger logger = Logger.getLogger("jsonp_jsp");
try {
	String reqUrl = request.getQueryString();
 	String callbackName = request.getParameter("callback");
 	String jsessionid = (request.getSession(false) ==null)?null: request.getSession(false).getId();
 	StringBuffer finalStr = new StringBuffer(1000);
 	
 	if(reqUrl == null) throw new Exception("USAGE: jsonp.jsp?http://jsonurlhere?param1=value1&param2=value2&callback=somecallback [callback fn is "+
 		 	"optional if not provided then default callback(); will be used ]");
 	
 	URL urlTmp = new URL(reqUrl);
//	String urlreqrite = "192.168.100.36:8080"+urlTemp.getFile();
//	String urlrewrite = "http://172.16.101.6:1774"+urlTmp.getFile();
	String urlrewrite = "http://10.32.98.72:8082"+urlTmp.getFile();
	URL url = new URL(urlrewrite);
	HttpURLConnection con = (HttpURLConnection)url.openConnection();
	con.setDoInput(true);
	con.setDoOutput(true);
	con.setRequestMethod(request.getMethod());
	if(jsessionid != null && !"".equals(jsessionid)){
		con.setRequestProperty("Cookie", "JSESSIONID="+jsessionid);
	}
	
	int clength = request.getContentLength();
	if(clength > 0) {
		con.setDoInput(true);
		byte[] idata = new byte[clength];
		request.getInputStream().read(idata, 0, clength);
		con.getOutputStream().write(idata, 0, clength);
	}
	response.setContentType(con.getContentType());
 
	BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
	String line;
	
	while ((line = rd.readLine()) != null) {
		finalStr.append(line); 
	}
	rd.close();
	con.disconnect();
	
	if(callbackName == null || "".equals(callbackName)){
		callbackName = "callback";
	}
	
	finalStr.insert(0,callbackName+"('");
	finalStr.append("');");
	//System.out.println("finalStr:"+finalStr.toString());
	
	
	

	out.write(finalStr.toString());
	
	
} catch(Exception e) {
	System.out.println("Exception occurred while converting to jsonp: "+e);
	logger.debug("Exception occurred while converting to jsonp: "+e);
	response.setContentType("text/plain");
	response.setStatus(500);
	out.write("ERROR occurred while converting to jsonp: "+e);
}%>