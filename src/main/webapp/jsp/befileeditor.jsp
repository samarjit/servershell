<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="sj" uri="/struts-jquery-tags" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BE File Editor</title>
<s:head/>
<sj:head  compressed="false" defaultLoadingText="Loading..." />

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
</style> 
 
<script type="text/javascript">
function screenMapLoad(event){
	$.getJSON("${pageContext.request.contextPath}/loadscreenmap.action",
			{'screenmappath':$("#screenmappath"),'screenmapfilename':$("#screenmapfilename")},
		function(data){
		   $("#screenmapxml").val(data.screenmapxml); 
	});
}

var editor = null,editor4=null, editor5=null;
$(document).ready(function (){
	editor5 = CodeMirror.fromTextArea(document.getElementById("fefile"), {
        mode:  "xml", tabMode: "indent" ,  lineNumbers: true,        matchBrackets: true
     });
	editor5.setSize("80%", "400");
});
$.subscribe("pageloaded",function (event,data){
	 $("#fefile").val(event.originalEvent.request.responseText);
	var json = $.parseJSON(event.originalEvent.request.responseText);
	 /* editor = ace.edit("screenmapxml");
  	 var XmlMode = require("ace/mode/xml").Mode;
     editor.getSession().setMode(new XmlMode()); */ 
	 editor5.setValue(json.data);
     
});

function beforefilesave(){
	$("#fefile").val(editor5.getValue());
}
</script>
</head>
<body>
<%@include file="../index.jsp" %>
Result: <pre id="resultdiv" style="height:40px;width:900px">d</pre>
 

Edit File: <s:form action="bloadfefile.action" id="fefileloadform">
<input name="filepath" id="filepath" value="${param.filepath}" size="100" />
<input name="filename" id="filename" value="${param.filename}"  size="70" />
<sj:submit targets="resultdiv" formIds="fefileloadform" onCompleteTopics="pageloaded" button="true" value="load BE file"></sj:submit>

</s:form> 

<s:url action="bcreatefefile.action" var="createfefile1" />
<sj:a button="true" href="%{createfefile1}" targets="resultdiv" formIds="fefileloadform" onCompleteTopics="pageloaded">create BE file</sj:a> 

<s:form id="fefilesavefrm">
<s:url action="bsavefefile.action" var="vsavefefile"/> 
<sj:submit button="true" href="%{vsavefefile}" targets="resultdiv" onclick="beforefilesave()" formIds="fefilesavefrm,fefileloadform" value="Save  file"></sj:submit><br/>
FE file:<textarea rows="10" cols="80" name="fefile" id="fefile"></textarea>
</s:form>
<s:form id="bfiledeletefrm">
<s:url action="bdeletefile.action" var="vdeletefile"/> 
<sj:submit button="true" href="%{vdeletefile}" targets="resultdiv" formIds="bfiledeletefrm,fefileloadform" value="Delete  file"></sj:submit><br/>
</s:form>

 
</body>
</html>