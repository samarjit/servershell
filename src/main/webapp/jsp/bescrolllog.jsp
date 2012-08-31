<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE xhtml PUBLIC "-//W3C//DTD XHTML 1.1 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BE Scrolllog</title>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@include file="../index.jsp" %>
<s:head/>
<sj:head debug="true" defaultLoadingText="loading..." defaultErrorText="Error occurred.." loadAtOnce="true" compressed="false" />
<script type="text/javascript">
var seq = 0;
var lasteol = false;
var lastline = "";
function refresh(cmd){
	$.get("${pageContext.request.contextPath}/bescrolllog.action?prevpos="+$("#prevpos").val()+"&belogpath="+$("#belogpath").val()+"&cmd="+cmd+"&pagesize="+$("#pagesize").val(),function(data){
		$("#result").text(data);
		
		var json = $.parseJSON(data);
		
		if(json.message != ""){
			/*lasteol = (json.endswith == "EOL")?true: false;
			if(lasteol == false){
				if(json.message.substring(5) ==""){
					lastline = lastline+json.lastline;
				}else{
					json.message = lastline+ json.message.substring(5);
					lastline = json.lastline;
				}
				
				$("#lastline"+parseInt(seq-1)).remove();
			}
			if(json.message.substring(5) !=""){
				$("#runlogdiv").append("<div id='r"+seq+"'>"+json.message+"</div>");
			}
			*/$("#runlogdiv").append("<div id='lastline"+seq+"' style='color:green'>"+json.message+"</div>");
			seq++;
			
			$("#prevpos").val(json.prevpos);
			
		}
	}); 
	
}
var refreshinterval = null;
function autorefresh(){
	refreshinterval = setInterval('refresh()', 5000);
}
function stopautorefresh(){
	clearInterval(refreshinterval );
}
function clearresult(){
	$("#runlogdiv div").remove();
}
</script>
</head>
<body>
<br/>
<button type="button" onclick="autorefresh()">autorefresh</button>
<button type="button" onclick="stopautorefresh()">stopautorefresh</button>
<button type="button" onclick="clearresult()">clear</button>
<form action="bescrolllog.action" id="form1">
BE Logpath<input name="belogpath" id="belogpath" value="C:/Users/Samarjit/Desktop/Book1.txt"  size="100" />
Prevpos: <input type="text" name="prevpos" id="prevpos" value="0" /> 
Page Size: <input type="text" name="pagesize" id="pagesize" value="10" />

<button type="button" id="ref" onclick="refresh('pageup')">Up</button>
<button type="button" id="ref" onclick="refresh('pagedown')">Down</button>
<button type="button" id="ref" onclick="refresh('getalluptoend')">Get all</button>
</form>
<div id="res"></div>
<div id="result">

</div>
<pre id="runlogdiv"></pre>
</body>
</html>