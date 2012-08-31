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
	<title>Logviewer</title>
	<s:head />
	<sj:head  compressed="false" defaultLoadingText="Loading..." />
	<script type="text/javascript">
	var logfilename = "";
		function frmsubmit(){
				$.post("${pageContext.request.contextPath}/monitoring.action",{cmd: $("#cmd").val(),pageup: $("#pageup").val(),pagedown: $("#pagedown").val() },function (data){
						$("#queryresult").html(data);		
				
					if(/runlog.*/.test($("#cmd").val())){
						var json = $.parseJSON(data);
						$("#runlog").append('<div class-"data">'+json.message+'</div>');
					}
		//			$("#logfile").load(logfilename)
					});
				 
		}
		
		function frmsubmitPageup(){
			$.post("${pageContext.request.contextPath}/monitoring.action",{cmd: $("#cmd").val(),pageup: $("#pageup").val(),pagedown:""},function (data){
				$("#queryresult").html(data);		
		
			});
		}
		function frmsubmitPagedown(){
			$.post("${pageContext.request.contextPath}/monitoring.action",{cmd: $("#cmd").val(),pageup:'',pagedown:$("#pagedown").val() },function (data){
				$("#queryresult").html(data);		
		
			});
		}
	</script>
</head>
<body>
<%@ include file="../index.jsp" %>
<form action="monitoring.action" id="frm1">
<button type="button" onclick="frmsubmit()">Submit Query</button>
<button type="button" onclick="frmsubmitPageup()">pageup</button>
<button type="button" onclick="frmsubmitPagedown()">pagedown</button>
	 
				Query ${pageContext.request.contextPath}
				<input name="cmd" id="cmd" cols="30" rows="1" style="width:100%" value="log C:\Users\Samarjit\Desktop\temp\prdlogs\EzLinkBEJob.log.14May"  size="100" /> 
				Pageup: <input type="text" name="pageup" id="pageup"/>
				Pagedown: <input type="text" name="pagedown" id="pagedown" />
			 Result
				<div name="queryresult" id="queryresult" cols="30" rows="10" style="width:100%"></div>
			 
</form>	
  <pre id="logfile"></pre>
  <button type="button" onclick="$('#runlog').empty();" >Clean below log</button>
  <div  id="runlog"></div>
</body>
</html>



