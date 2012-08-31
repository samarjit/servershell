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
var editor,editor2,editor3,editor4;
var initialized = false;
$.subscribe('onsuccesRevEng',function(event,data){
	var json = $.parseJSON(event.originalEvent.request.responseText);
	$("#tabledetails").text(json.tabledetails);
	//$("#htmlscreen1").val(json.htmlscreen);
	$("#htmlscreen").text(json.htmlscreen);
	$("#htmlscreenview").text(json.htmlscreenview);
	$("#aliasquery").text(json.aliasquery);
	$("#pagexml").text(json.pagexml);
	$("#screenmapxml").text(json.screenmapxml);
	$("#sqls").text(json.sqls);
	$("#miscellaneous").text(json.miscellaneous);
	$("#ftlfilename").val(json.screenname+".html");
	$("#xmlscreenname").val(json.screenname+".xml");
	
	if(!initialized){
		initialized = true;
	 editor = ace.edit("pagexml");
  	 var XmlMode = require("ace/mode/xml").Mode;
     editor.getSession().setMode(new XmlMode());
     
       editor2 = ace.edit("htmlscreenview");
	 var XmlMode2 = require("ace/mode/xml").Mode;
	 editor2.getSession().setMode(new XmlMode2());    
	     
       editor3 = ace.edit("htmlscreen");
	 var XmlMode3 = require("ace/mode/html").Mode;
	 editor3.getSession().setMode(new XmlMode3());    
	
    /*    editor4 = CodeMirror.fromTextArea(document.getElementById("htmlscreen1"), {
         mode:  "text/html", tabMode: "indent" ,         matchBrackets: true
      }); */
       
	}
});

function beforeXmlSave (event, data){
	$("#screenxml").val(editor.getValue());
}
function beforeHtmlSave (event, data){
	$("#htmlscreen1").val(editor3.getValue());
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
<div    name="revenggresult" id="revenggresult" style="height:40px;width:100%; overflow:hidden"></div>
 
<div class="ui-widget-header">Tabledetails:</div> <pre id="tabledetails">g</pre>
<div class="ui-widget-header">screenmapxml (Add to existing screenmap.xml):</div><pre id="screenmapxml">g</pre> 
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
<input name="xmlscreenpath" value="src/main/java/map/jsptest"  size="100" />
<input name="xmlscreenname" id="xmlscreenname"  size="70" /> 
<textarea rows="1" cols="1" id="screenxml" name="screenxml">ss</textarea>
<sj:submit type="button" targets="revenggresult" onclick="beforeXmlSave()" button="true">save xml</sj:submit>
</s:form>
<div style="width: 1024px; height: 400px;position:relative" ><div id="pagexml" style="width: 1024px; height: 400px;">g</div></div>

<div class="ui-widget-header">SQLs:</div><pre id="sqls">g</pre>
<div class="ui-widget-header">miscellaneous:</div><pre id="miscellaneous">g</pre>

</body>
</html>