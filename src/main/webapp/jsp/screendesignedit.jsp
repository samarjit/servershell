<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="sj" uri="/struts-jquery-tags" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Reverse Engineering Tool</title>
<s:head/>
<sj:head compressed="false" loadAtOnce="true"/>

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



var editor2 = null,editor4=null, editor5=null;
$(document).ready(function(){

	 /* editor = ace.edit("screenmapxml");
 	 var XmlMode = require("ace/mode/xml").Mode;
    editor.getSession().setMode(new XmlMode()); */ 
	
    editor4 = CodeMirror.fromTextArea(document.getElementById("screenmapxml"), {
        mode:  "xml", tabMode: "indent" , lineNumbers: true,        matchBrackets: true
     });
    editor4.setSize("80%", "200");
    
    editor5 = CodeMirror.fromTextArea(document.getElementById("screenxml"), {
        mode:  "xml", tabMode: "indent" ,  lineNumbers: true,        matchBrackets: true
     });
    editor5.setSize("80%", "400");
    
    editor2 = CodeMirror.fromTextArea(document.getElementById("ftlfile"), {
        mode:  "text/html", tabMode: "indent" ,  lineNumbers: true,        matchBrackets: true
     });
     editor2.setSize("80%", "400");
	
});

$.subscribe("screenMapLoaded",function (event,data){
	var json = $.parseJSON(event.originalEvent.request.responseText);
	 $("#screenmapxml").val(json.data);
	 editor4.setValue(json.data);
});

$.subscribe("pagexmlloaded",function (event,data){
	var json = $.parseJSON(event.originalEvent.request.responseText);
	$("#screenxml").val(json.data);
	 editor5.setValue(json.data);
});

$.subscribe("pageftlloaded",function (event,data){
	var json = $.parseJSON(event.originalEvent.request.responseText); 
	$("#ftlfile").val(json.data);
	 editor2.setValue($("#ftlfile").val());	  
});


function beforescrmapsave(){
	$("#screenmapxml").val(editor4.getValue());
}
function beforescrxmlesave(){
	$("#screenxml").val(editor5.getValue());
}

function beforefilesave(){
	$("#ftlfile").val(editor2.getValue());
}

</script>
</head>
<body>
<%@include file="../index.jsp" %>
Result: <div id="resultdiv" style="height:40px;width:900px;overflow:hidden">d</div>
Edit screenmap:

<s:form id="frmscreenmap" action="loadscreenmap"> 
screenmappath:<input name="screenmappath" id="screenmappath" value="target/classes/map" size="50"/> 
screenmapfilename: <input name="screenmapfilename" id="screenmapfilename" value="screenmap.xml" />
<sj:a targets="resultdiv" formIds="frmscreenmap" onSuccessTopics="screenMapLoaded" button="true">load screenmap</sj:a>
<!-- <button type="button"  onclick="screenMapLoad">load screenmap</button> -->
</s:form><br/>

Edit Screen xml: <s:form id="xmlscr" action="loadscreenmap"> 
<input name="screenmappath" id="filepath" value="target/classes/map" size="50"/>
<input name="screenmapfilename" id="filename" />

<sj:a targets="resultdiv" formIds="xmlscr" onCompleteTopics="pagexmlloaded" button="true">load pagexml</sj:a>

</s:form><br/>

Edit Screen ftl: <s:form action="loadfefile" id="ftlform">
<input name="filepath" id="ftlbasepath" value="src\main\webapp\jsptest" size="50"/>
<input name="filename" id="ftlfilename" />
<sj:a targets="resultdiv" formIds="ftlform" onCompleteTopics="pageftlloaded" button="true">load pageftl</sj:a>

</s:form><br/>


screenmap.xml:
<s:form action="screenmapsave" id="screenmapsave">
<s:url action="screenmapsave.action" var="screenmapsave"/> 
<sj:submit type="button" href="%{screenmapsave}" targets="resultdiv" onclick="beforescrmapsave()" formIds="screenmapsave,frmscreenmap" button="true">Save screenmap</sj:submit><br/>
<textarea rows="10" cols="80" name="screenmapxml" id="screenmapxml" style="width:200px"></textarea>
</s:form>

<s:form  id="screenxmlsave">
<s:url action="screenmapsave.action" var="screenxmlsave"/> 
<sj:submit type="button" href="%{screenxmlsave}" targets="resultdiv" onclick="beforescrxmlesave()" formIds="screenxmlsave,xmlscr" button="true">Save screenmap</sj:submit><br/>
XML file:<textarea rows="10" cols="80" name="screenmapxml" id="screenxml"></textarea>
</s:form>

<s:form id="fefilesavefrm">
<s:url action="savefefile.action" var="savefefile"/> 
Ftl file:<sj:submit type="button" href="%{savefefile}" targets="resultdiv" onclick="beforefilesave()" formIds="fefilesavefrm,ftlform" button="true">Save ftl file</sj:submit><br/>

<textarea rows="10" cols="80" name="fefile" id="ftlfile"></textarea>
</s:form>

 
</body>
</html>