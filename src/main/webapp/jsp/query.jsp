<html>
<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="sj" uri="/struts-jquery-tags" %>
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title>Query</title>
	<s:head />
	<sj:head/>
	<script type="text/javascript">
		function frmsubmit(){
			$("#log").append("<div class='query'>"+$("#query").val()+"</div>");
			$("#log").append("<div class='res'>"+$("#queryresult").html()+"</div>");
				$("#queryresult").text("Loading .. ");	
				$.post("${pageContext.request.contextPath}/query.action",{query: $("#query").val(), cmd: 'query'},function (data){
					$("#queryresult").html(data);		
				});
		}
		$(document).ready(function (){
			$('#query').keydown(function (e) {
				  if (e.ctrlKey && e.keyCode == 13) {
	 					frmsubmit();
				  }
				});
		});
		
	</script>
	<style type="text/css">
	.grid{
	  overflow: hidden;
	  border: 1px solid lightgrey;
	  border-collapse: collapse;
	  font-face: verdana;
	  font-size: .8em;
	}
	.grid th{
		background-color: grey;
	}
	
	</style>
</head>
<body>
<%@ include file="../index.jsp" %>
<form action="query.action" id="frm1">
<!-- <button type="button" onclick="frmsubmit()">Submit Query</button> -->
	<table style="width:100%;height:100%">
		<tr height="160px">
			<td>
				<div class="ui-widget-header">Query ${pageContext.request.contextPath}</div>
				<textarea name="query" id="query" cols="30" value="select * from alert_queue where rownum <5;" rows="10" style="width:100%">select * from alert_queue where rownum <5;</textarea>
			</td>
		</tr>
		<tr valign="top">
			<td><div class="ui-widget-header">Result</div>
				<div name="queryresult" id="queryresult" cols="30" rows="10" style="width:100%"></div>
			</td>
		</tr>
	
	</table>
	
</form>	
  
 Log:
 <div id="log">
 </div> 
</body>
</html>



