<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE xhtml PUBLIC "-//W3C//DTD XHTML 1.1 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BE Runlog</title>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@include file="../index.jsp" %>
<s:head/>
<sj:head debug="true" defaultLoadingText="loading..." defaultErrorText="Error occurred.." loadAtOnce="true" compressed="false" />
<script type="text/javascript">
var seq = 0;
var lasteol = false;
var lastline = "";
function refresh(){
	$.get("${pageContext.request.contextPath}/berunlog.action?prevpos="+$("#prevpos").val()+"&belogpath="+$("#belogpath").val(),function(data){
		$("#result").text(data);
		
		var json = $.parseJSON(data);
		
		if(json.message != ""){
			lasteol = (json.endswith == "EOL")?true: false;
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
			$("#runlogdiv").append("<div id='lastline"+seq+"' style='color:green'>"+lastline+"</div>");
			seq++;
			$("#prevpos").val(json.pos);
			
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
<form action="berunlog.action" id="form1">
BE Logpath<input name="belogpath" id="belogpath" value="C:/Users/Samarjit/Desktop/profilelist.txt"  size="100" />
<input type="text" name="prevpos" id="prevpos" value="0" /> 
<button type="button" id="ref" onclick="refresh()">refresh</button>
<sj:submit  targets="res"  ></sj:submit>

</form>
<div id="res"></div>
<div id="result">

</div>
<pre id="runlogdiv"></pre>
</body>
</html>