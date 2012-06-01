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
				$.post("${pageContext.request.contextPath}/query.action",{query: $("#query").val()},function (data){
						$("#queryresult").text(data);		
				});
		}
	</script>
</head>
<body>
<form action="query.action" id="frm1">
<button type="button" onclick="frmsubmit()">Submit Query</button>
	<table style="width:100%;height:100%">
		<tr>
			<td>
				Query ${pageContext.request.contextPath}<textarea name="query" id="query" cols="30" rows="10" style="width:100%"></textarea>
			</td>
		</tr>
		<tr>
			<td>Result
				<div name="queryresult" id="queryresult" cols="30" rows="10" style="width:100%"></div>
			</td>
		</tr>
	
	</table>
	
</form>	
  
</body>
</html>



