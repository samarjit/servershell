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
<sj:head jqueryui="true" loadAtOnce="true" defaultIndicator="indicator" compressed="false" defaultLoadingText="Loading..." />


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
var pagexmleditor,htmlscreenview,htmlscreen,editor4,screenmap;
var initialized = false;
$(document).ready(function (){
	  	screenmap = ace.edit("screenmapxmlxml");
		 var XmlMode3 = require("ace/mode/html").Mode;
		 screenmap.getSession().setMode(new XmlMode3());
		 
		 pagexmleditor = ace.edit("pagexmldiv");
	  	 var XmlMode = require("ace/mode/xml").Mode;
	  	 pagexmleditor.getSession().setMode(new XmlMode());
	     
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
	$("#pagexmlfilename").val(json.screenname+".xml");
	
	pagexmleditor.setValue(json.pagexml);
	
	htmlscreenview.setValue(json.htmlscreenview);
	htmlscreen.setValue(json.htmlscreen);
	
});

 
function beforeXmlSave (event, data){
	$("#pagexml").val(pagexmleditor.getValue());
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

function editXml(){
	window.open('fefileeditor.jsp?filepath='+$(":input[name='fepagexmlpath']").val()+'&filename='+$("#pagexmlfilename").val(),'_blank');	
	window.open('befileeditor.jsp?filepath='+$(":input[name='bepagexmlpath']").val()+'&filename='+$("#pagexmlfilename").val(),'_blank');	
}
function editFtl(){
	window.open('fefileeditor.jsp?filepath='+$(":input[name='ftlbasepath']").val()+'&filename='+$("#ftlfilename").val(),'_blank');	
}


</script>
</head>
<body  >
<%@include file="../index.jsp" %>
Reverse Engg. Query: <form action="${pageContext.request.contextPath}/brevengg.action" method="POST"> 
<textarea name="sqlstring" id="sqlstring"  cols="80" rown="5">select * from alert_queue</textarea>
<sj:submit button="true" buttonText="Rev Engg."   onSuccessTopics="onsuccesRevEng"  targets="revenggresult" value="Rev Engg."
indicator="indicator"
 />

</form>
<div id="indicator" style="display:none">Loading ... </div>

<br/>
Reverse Engg. Add Column: <form> <input name="revenggqry" id="revenggqry" /><button type="button">Rev engg.</button></form><br/>
<div class="ui-widget-header">Reverse Engg. Result:</div>
<textarea id="revenggresult" style="height:40px;width:99%;overflow:auto"></textarea>
 
<div class="ui-widget-header">Tabledetails:</div> <pre id="tabledetails">g</pre>

<!--  screenmap -->

<div class="ui-widget-header">screenmapxml (Add to existing	screenmap.xml):</div>
<s:form id="frmscreenmap">
FE screenmappath:  <input name="fescreenmappath"    id="fescreenmappath" 			value="target/classes/map" size="100" />    <br />
BE screenmappath:  <input name="bescreenmappath"			value="target/classes/map/jsptest" size="100" />
screenmapfilename: <input name="screenmapfilename" 	id="screenmapfilename" value="screenmap.xml" /> <textarea name="screenmapxml" id="screenmapxml" rows="1" cols="1" style="display:none"></textarea>
		
		<s:url var="loadscreenmap" action="loadscreenmap.action"></s:url>
		<sj:a targets="revenggresult" href="%{loadscreenmap}" 	formIds="frmscreenmap" onSuccessTopics="screenMapLoaded"	button="true">load screenmap</sj:a>
		
		<s:url var="screenmapsave" action="screenmapsave.action"></s:url>	
		<sj:a targets="revenggresult" href="%{screenmapsave}"	formIds="frmscreenmap" onclick="screenMapSave()"   button="true">save screenmap</sj:a>
</s:form>
<pre id="screenmapxmlShow">g</pre>
Existing FE screenmap.xml:
<div style="width: 1024px; height: 300px; position: relative">		<div id="screenmapxmlxml" style="width: 1024px; height: 300px;">g</div>	</div>

<!--  screenmap end -->

<br/>

<!--  pagexml -->

<div class="ui-widget-header">pagexml:</div>
<s:form action="createxml.action" id="createxmlfrm">
FE xmlpath:<input name="fepagexmlpath" value="target/classes/map/jsptest"  size="100" /><br/>
BE xmlpath:<input name="bepagexmlpath" value="target/classes/map/jsptest"  size="100" />
           <input name="pagexmlfilename" id="pagexmlfilename"  size="70" /> 
	<textarea rows="1" cols="1" id="pagexml" name="pagexml" style="display:none">ss</textarea>
	<sj:a button="true" formIds="createxmlfrm" targets="revenggresult" onclick="beforeXmlSave()" >save pagexml</sj:a>
	<sj:a button="true" type="button" onclick="editXml()" > edit</sj:a>
</s:form>
<div style="width: 1024px; height: 400px;position:relative" ><div id="pagexmldiv" style="width: 1024px; height: 400px;">g</div></div>

<!--  pagexml end-->

<br/>

<!--  htmlscreen -->

<div class="ui-widget-header">htmlscreen1:</div> 
<s:form action="createhtml.action" id="pp" method="post" > 
	Once confirmed copy to: src/main/webapp/jsptest: 
							<input name="ftlbasepath" value="target/classes/map/jsptest"  size="100" />
	JSP/FTL Filename:       <input name="ftlfilename" id="ftlfilename"  size="70" /> 
		                    <textarea name="htmlscreen1" id="htmlscreen1" rows="10" cols="80" style="width:1024px;display:none" >g</textarea>
 		
 	<sj:a button="true"   onclick="beforeHtmlSave()" targets="revenggresult" formIds="pp"  > Save jsp/ftl</sj:a>
 	<sj:a button="true" type="button" onclick="editFtl()" >edit</sj:a>
</s:form>
htmlscreen: 
	<div style="width: 1024px; height: 400px;position:relative" > 		<div id="htmlscreen" style="width: 1024px; height: 400px;">g</div>	</div>
<br/>
<!--  htmlscreen end -->

<div class="ui-widget-header">htmlscreenview:</div><div style="width: 1024px; height: 400px;position:relative" > <div id="htmlscreenview" style="width: 1024px; height: 400px;">g</div></div>
<div class="ui-widget-header">aliasquery:</div> <pre id="aliasquery">g</pre>
<div class="ui-widget-header">SQLs:</div><pre id="sqls">g</pre>
<div class="ui-widget-header">miscellaneous:</div><pre id="miscellaneous">g</pre>

</body>
</html>