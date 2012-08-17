<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="sj" uri="/struts-jquery-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Reverse Engineering Tool</title>
<s:head />
<sj:head jqueryui="true" defaultIndicator="indicator" defaultLoadingText="Loading..." />


<script src="${pageContext.request.contextPath}/ace/ace.js"   charset="utf-8"></script>
<script src="${pageContext.request.contextPath}/ace/mode-xml.js"   charset="utf-8"></script>
<script src="${pageContext.request.contextPath}/ace/mode-html.js"   charset="utf-8"></script>
 
<link rel="stylesheet" href="${pageContext.request.contextPath}/codemirror/codemirror.css">
<script src="${pageContext.request.contextPath}/codemirror/codemirror.js"></script>
<script src="${pageContext.request.contextPath}/codemirror/xml.js"></script>
<script src="${pageContext.request.contextPath}/codemirror/javascript.js"></script>
<script src="${pageContext.request.contextPath}/codemirror/htmlmixed.js"></script>

<style type="text/css">
.CodeMirror {border-top: 1px solid black; border-bottom: 1px solid black;}
#.ace_editor: position: relative;
</style> 

<script type="text/javascript">
var editor,editor2,editor3,editor4;
$.subscribe('onsuccesRevEng',function(event,data){
	var json = $.parseJSON(event.originalEvent.request.responseText);
	$("#tabledetails").text(json.tabledetails);
	$("#htmlscreen1").val(json.htmlscreen);
	$("#htmlscreen").text(json.htmlscreen);
	$("#htmlscreenview").text(json.htmlscreenview);
	$("#aliasquery").text(json.aliasquery);
	$("#pagexml").text(json.pagexml);
	$("#screenmapxml").text(json.screenmapxml);
	$("#miscellaneous").text(json.miscellaneous);
	
	   editor = ace.edit("pagexml");
  	 var XmlMode = require("ace/mode/xml").Mode;
     editor.getSession().setMode(new XmlMode());
     
       editor2 = ace.edit("htmlscreenview");
	 var XmlMode2 = require("ace/mode/xml").Mode;
	 editor2.getSession().setMode(new XmlMode2());    
	     
       editor3 = ace.edit("htmlscreen");
	 var XmlMode3 = require("ace/mode/html").Mode;
	 editor3.getSession().setMode(new XmlMode3());    
	     
       editor4 = CodeMirror.fromTextArea(document.getElementById("htmlscreen1"), {
         mode:  "text/html", tabMode: "indent" ,
         lineNumbers: true
      });		      
});
</script>
</head>
<body  >
<%@include file="../index.jsp" %>
Reverse Engg. Query: <form action="${pageContext.request.contextPath}/bcreatescreen.action" method="POST"> <textarea name="sqlstring" id="sqlstring"  cols="80" rown="5"></textarea>
<sj:submit button="true" buttonText="Rev Engg."   onSuccessTopics="onsuccesRevEng"  targets="revenggresult" value="Rev Engg."
indicator="indicator"
 />
<button type="submit">submit</button>
</form>
<div id="indicator" style="display:none">Loading ... </div>
<br/>
Reverse Engg. Add Column: <form> <input name="revenggqry" id="revenggqry" /><button type="button">Rev engg.</button></form><br/>
 
tabledetails: <pre id="tabledetails">g</pre>
htmlscreen1: <textarea id="htmlscreen1" rows="10" cols="80" style="width:1024px">g</textarea>
htmlscreen: <div style="width: 1024px; height: 400px;position:relative" > <div id="htmlscreen" style="width: 1024px; height: 400px;">g</div></div>
htmlscreenview:<div style="width: 1024px; height: 400px;position:relative" > <div id="htmlscreenview" style="width: 1024px; height: 400px;">g</div></div>
aliasquery: <pre id="aliasquery">g</pre>
pagexml:<div style="width: 1024px; height: 400px;position:relative" ><div id="pagexml" style="width: 1024px; height: 400px;">g</div></div>
screenmapxml:<pre id="screenmapxml">g</pre>
miscellaneous:<pre id="miscellaneous">g</pre>
Reverse Engg. Result:<textarea rows="1" cols="80" name="revenggresult" id="revenggresult"></textarea>
</body>
</html>