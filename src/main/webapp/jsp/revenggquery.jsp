<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="sj" uri="/struts-jquery-tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Reverse Engg</title>
<s:head />
<sj:head jqueryui="true" defaultIndicator="indicator" compressed="false" defaultLoadingText="Loading..." />


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
#div {overflow: auto}

</style> 

<script type="text/javascript">
var pagexml,htmlscreenview,htmlscreen,editor4,screenmap;
var initialized = false;
$(document).ready(function (){
	  	screenmap = ace.edit("screenmapxmlxml");
		 var XmlMode3 = require("ace/mode/html").Mode;
		 screenmap.getSession().setMode(new XmlMode3());
		 
		 pagexml = ace.edit("pagexml");
	  	 var XmlMode = require("ace/mode/xml").Mode;
	  	 pagexml.getSession().setMode(new XmlMode());
	     
	     htmlscreenview = ace.edit("htmlscreenview");
		 var XmlMode2 = require("ace/mode/xml").Mode;
		 htmlscreenview.getSession().setMode(new XmlMode2());    
		     
		 htmlscreen = ace.edit("htmlscreen");
		 var XmlMode3 = require("ace/mode/html").Mode;
		 htmlscreen.getSession().setMode(new XmlMode3());    
		
});
$.subscribe('onsuccesRevEng',function(event,data){
	var json = $.parseJSON(event.originalEvent.request.responseText);
		
	if(!initialized){
		initialized = true;

         
	
    /*    editor4 = CodeMirror.fromTextArea(document.getElementById("htmlscreen1"), {
         mode:  "text/html", tabMode: "indent" ,         matchBrackets: true
      }); */
       
	}
	
	
	$("#tabledetails").text(json.tabledetails);
	//$("#htmlscreen1").val(json.htmlscreen);
	//$("#htmlscreen").text(json.htmlscreen);
	//$("#htmlscreenview").text(json.htmlscreenview);
	$("#aliasquery").text(json.aliasquery);
	//$("#pagexml").text(json.pagexml);
	$("#screenmapxmlShow").text(json.screenmapxml);
	$("#sqls").text(json.sqls);
	$("#miscellaneous").text(json.miscellaneous);
	$("#ftlfilename").val(json.screenname+".jsp");
	$("#xmlscreenname").val(json.screenname+".xml");
	
	pagexml.setValue(json.pagexml);
	
	htmlscreenview.setValue(json.htmlscreenview);
	htmlscreen.setValue(json.htmlscreen);
	
});

 
function beforeXmlSave (event, data){
	$("#screenxml").val(pagexml.getValue());
}
function beforeHtmlSave (event, data){
	$("#htmlscreen1").val(htmlscreen.getValue());
}

$.subscribe("screenMapLoaded",function(event, data){
	var json = $.parseJSON(event.originalEvent.request.responseText);
	$("#revenggresult").text(event.originalEvent.request.responseText);
	screenmap.setValue(json.data);
});
function screenMapSave(){
	$("#screenmapxml").val(screenmap.getValue());
}

</script>
</head>
<body  >
<%@include file="../index.jsp" %>
Reverse Engg. Query: <form action="${pageContext.request.contextPath}/bcreatescreen.action" method="POST"> 
<textarea name="sqlstring" id="sqlstring"  cols="80" rown="5">select * from alert_queue</textarea>
<sj:submit button="true" buttonText="Rev Engg."   onSuccessTopics="onsuccesRevEng"  targets="revenggresult" value="Rev Engg."
indicator="indicator"
 />
<button type="submit">submit</button>
</form>
<div id="indicator" style="display:none">Loading ... </div>

<br/>
Reverse Engg. Add Column: <form> <input name="revenggqry" id="revenggqry" /><button type="button">Rev engg.</button></form><br/>
<div class="ui-widget-header">Reverse Engg. Result:</div>
<div    name="revenggresult" id="revenggresult" style="height:40px;width:100%; overflow:auto"></div>
 
<div class="ui-widget-header">Tabledetails:</div> <pre id="tabledetails">g</pre>
<div class="ui-widget-header">screenmapxml (Add to existing screenmap.xml):
</div>
<s:form   id="frmscreenmap">
FE screenmappath:<input name="screenmappath" id="screenmappath" value="target/classes/map" size="100"/> <br/>
BE screenmappath:<input name="filepath" value="target/classes/map/jsptest"  size="100" />
screenmapfilename: <input name="screenmapfilename" id="screenmapfilename" value="screenmap.xml" />
<textarea name="screenmapxml" id="screenmapxml" rows="1" cols="1"></textarea><br/>
<s:url var="loadscreenmap" action="loadscreenmap"></s:url>
<sj:a targets="revenggresult" href="%{loadscreenmap}" formIds="frmscreenmap" onSuccessTopics="screenMapLoaded" button="true">load screenmap</sj:a>
<s:url var="screenmapsave" action="screenmapsave"></s:url>
<sj:a targets="revenggresult" href="%{screenmapsave}" formIds="frmscreenmap"  onclick="screenMapSave()" button="true">save screenmap</sj:a>
</s:form>
<pre id="screenmapxmlShow">g</pre>
screenmap.xml: <div style="width: 1024px; height: 300px;position:relative" > <div id="screenmapxmlxml" style="width: 1024px; height: 300px;">g</div></div>  
<br/>

<div class="ui-widget-header">htmlscreen1:</div> 
Path:<s:form action="createhtml.action" id="pp" method="post" > 
Once confirmed copy to: src/main/webapp/jsptest
<input name="ftlbasepath" value="target/classes/map/jsptest"  size="100" />
<input name="ftlfilename" id="ftlfilename"  size="70" /> 
<sj:a button="true"   onclick="beforeHtmlSave()" targets="revenggresult" formIds="pp"  > Save</sj:a>
 <textarea name="htmlscreen1" id="htmlscreen1" rows="10" cols="80" style="width:1024px;display:none" >g</textarea></s:form>
htmlscreen: <div style="width: 1024px; height: 400px;position:relative" > <div id="htmlscreen" style="width: 1024px; height: 400px;">g</div></div>


<div class="ui-widget-header">htmlscreenview:</div><div style="width: 1024px; height: 400px;position:relative" > <div id="htmlscreenview" style="width: 1024px; height: 400px;">g</div></div>
<div class="ui-widget-header">aliasquery:</div> <pre id="aliasquery">g</pre>
<div class="ui-widget-header">pagexml:</div>
<s:form action="createxml.action">
FE xmlpath:<input name="xmlscreenpath" value="target/classes/map/jsptest"  size="100" /><br/>
BE xmlpath:<input name="filepath" value="target/classes/map/jsptest"  size="100" />
<input name="xmlscreenname" id="xmlscreenname"  size="70" /> 
<textarea rows="1" cols="1" id="screenxml" name="screenxml">ss</textarea>
<sj:submit type="button" targets="revenggresult" onclick="beforeXmlSave()" button="true">save xml</sj:submit>
</s:form>
<div style="width: 1024px; height: 400px;position:relative" ><div id="pagexml" style="width: 1024px; height: 400px;">g</div></div>

<div class="ui-widget-header">SQLs:</div><pre id="sqls">g</pre>
<div class="ui-widget-header">miscellaneous:</div><pre id="miscellaneous">g</pre>

</body>
</html>