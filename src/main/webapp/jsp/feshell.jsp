<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>FE Shell</title>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>

<s:head/>

<sj:head debug="true" defaultLoadingText="loading..." defaultErrorText="Error occurred.." loadAtOnce="true" compressed="false" />
<style>
.ui-autocomplete {
        max-height: 400px;
        overflow-y: auto;
        /* prevent horizontal scrollbar */
        overflow-x: hidden;
    }
* html .ui-autocomplete {
        height: 400px;
    }    
</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/json2.js"></script>
<script type="text/javascript">
var seq = 0;
var lasteol = false;
var lastline = "";
function refresh(cmd){
	$.get("${pageContext.request.contextPath}/bescrolllog.action?prevpos="+$("#prevpos").val()+"&belogpath="+$("#belogpath").val()+"&cmd="+cmd+"&pagesize="+$("#pagesize").val(),function(data){
		$("#result").text(data);
		
		var json = $.parseJSON(data);
		//$("#runlogdiv").text(json.message);
		 
		if(json.jobj.message != ""){
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
			*/$("#runlogdiv").append("<div id='lastline"+seq+"' style='color:green'>"+json.jobj.message+"</div>");
			seq++;
			
			$("#prevpos").val(json.jobj.prevpos);
			
		}
	}); 
	
}
var refreshinterval = null;
function autorefresh(){
	refreshinterval = setInterval('refresh("getalluptoend")', 5000);
}
function stopautorefresh(){
	clearInterval(refreshinterval );
}
function clearresult(){
	$("#runlogdiv div").remove();
}
function gotoHome(){
	 $.get("${pageContext.request.contextPath}/behome.action",function (data){$("#rootpath").val(data);});
}
function showHomeTopic(event, data){
	alert(event.originalEvent.request.responseText);
}
function showRootPath(){
	$("#rootpath").load("${pageContext.request.contextPath}/bepwd.action");
}
function changedir(){
	var json={};
	json.rootPath = escape($('#rootpath').val());
	json.cdpath = $('#cdpath').val();
	
	$.get("${pageContext.request.contextPath}/becd.action?data="+$('#rootpath').val()+"/"+$('#cdpath').val(),
//	$.get("${pageContext.request.contextPath}/becd.action?rootPath="+$('#rootpath').val()+"&cdpath="+$('#cdpath').val(),
	 function(data){
		$("#result").text(data);
		if(!(data.indexOf("File") >0))
			$("#rootpath").val(data);
		 	$('#cdpath').val('');
	 });
}
function ls(){
	var jsonls  = {};
	jsonls.rootPath = $('#rootpath').val();
	jsonls.relPath =  $('#relPath').val();
	
	$("#result").load("${pageContext.request.contextPath}/bels.action?data="+JSON.stringify(jsonls));
}
function grep(){
	var json={};
	json.rootPath = $('#rootpath').val().trim();
	json.filename = $('#grepfile').val().trim();
	json.expression = $('#exp').val();
//	$("#result").load("${pageContext.request.contextPath}/begrep.action?rootPath="+$('#rootpath').val()+"&filename="+$("#grepfile").val()+"&expression="+$("#exp").val());
	$("#result").load("${pageContext.request.contextPath}/begrep.action?data="+JSON.stringify(json));
}
function copytoLogpath(){
	$("#belogpath").val($("#rootpath").val());
}


function changedirdyn(){
	var jsonls  = {};
	jsonls.rootPath = $('#rootpath').val();
	jsonls.relPath =  $('#cdpath').val();
	$.get("${pageContext.request.contextPath}/flsjson.action?rootPath="+jsonls.rootPath+"&relPath="+jsonls.relPath+"", function (data){
		
		$("#result").text(data);
		var json = $.parseJSON(data);
		dt = json.jobj;
		var localar = [];	
		for (var x in dt){
			localar.push(x);
			}
        
		$("#cdpath").autocomplete({
			source: function(request,response){
				var splt = request.term.split(/[\/\\]/);
				var srch = splt.pop();
				var ar = $.grep(localar, function (a){
					return (a.indexOf(srch) == 0);
				} );
				
				response(
						
					ar	
				);
			},
			minLengthType: 0,
		/*	search: function (event, ui){
				var terms = this.value.split( "/" );
			},*/
			focus: function() {
                // prevent value inserted on focus
                return false;
            },
			select: function( event, ui ) {
                var terms = this.value.split( "/" );
                // remove the current input
                terms.pop();
                // add the selected item
                terms.push( ui.item.value );
                // add placeholder to get the comma-and-space at the end
                terms.push( "" );
                this.value = terms.join( "/" );
                return false;
            }
			
			});
		 
	});
}

$(document).ready(function(){
	$("#cdpath").keydown(function(e){
		 if (e.keyCode == 9 ) {
			 changedirdyn();
			 return false;
		 }
	});
});
</script>
</head>
<body>
<%@include file="../index.jsp" %>
<br/>
<button type="button" onclick="gotoHome()">goto home dir</button>
<s:url  action="behome.action" var="bhome1" />
<sj:submit value="home" type="button" href="%{#bhome1}" onCompleteTopics="showHomeTopic()" targets="result" >js:home</sj:submit>
<br/>
pwd:<textarea id="rootpath" name="rootpath" rows="2" cols="100"></textarea>
<button onclick="copytoLogpath()">copy to logpath</button>
<br/>
List:<input name="relPath" id="relPath" size="100"/><button type="button" onclick="ls()">ls</button>

<br/>
CD Path:<input name="cdpath" id="cdpath" size="100"><button type="button" onclick="changedir()">cd</button>
<br/> 
grep exp <input name="exp" id="exp" size="50" >file<input name="grepfile" id="grepfile" size="50" /><button type="button" onclick="grep()">grep</button>
<br/>
<button type="button" onclick="autorefresh()">autorefresh</button>
<button type="button" onclick="stopautorefresh()">stopautorefresh</button>
<button type="button" onclick="clearresult()">clear</button>

<form action="bescrolllog.action" id="form1">
BE Logpath<input name="belogpath" id="belogpath" size="100" value="C:/Users/Samarjit/Desktop/Book1.txt" />
Prevpos: <input type="text" name="prevpos" id="prevpos" value="0" /> 
Page Size: <input type="text" name="pagesize" id="pagesize" value="10" />

<button type="button" id="ref" onclick="refresh('pageup')">Up</button>
<button type="button" id="ref" onclick="refresh('pagedown')">Down</button>
<button type="button" id="ref" onclick="refresh('getalluptoend')">Get all</button>
</form>
<div id="res"></div>
<pre id="result" style="font-family:courier new;font-size:.7em">

</pre>
<pre id="runlogdiv"></pre>
</body>
</html>