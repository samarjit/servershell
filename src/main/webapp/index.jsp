<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title></title>
<style>
.ui-widget{
font-size: 12px;
}
</style>
</head>
<body>
	<h6>Index servershell</h6>
	<label>Welcome "${session.name}"</label><br/>
	<a href="${pageContext.request.contextPath}/jsp/monitoring.jsp" >monitoring.jsp</a>
	<a href="${pageContext.request.contextPath}/jsp/logview.jsp" >logview.jsp</a>
	<a href="${pageContext.request.contextPath}/jsp/query.jsp" >query.jsp</a>
	<a href="${pageContext.request.contextPath}/jsp/upload.jsp" >upload.jsp</a>
	<a href="${pageContext.request.contextPath}/jsp/fileuploaddownload.jsp" >fileuploaddownload.jsp</a>
	<a href="${pageContext.request.contextPath}/jsp/berunlog.jsp" >berunlog.jsp</a>
	<a href="${pageContext.request.contextPath}/config-browser/index.action" >config browser</a>
	<a href="${pageContext.request.contextPath}/jsp/jvmMemoryMonitor.jsp" >jvm</a>
	<a href="${pageContext.request.contextPath}/jsp/bescrolllog.jsp" >bescrolllog</a>
	<a href="${pageContext.request.contextPath}/jsp/beshell.jsp" >beshell</a>
	<a href="${pageContext.request.contextPath}/jsp/screendesignedit.jsp" >screendesignedit</a>
	<a href="${pageContext.request.contextPath}/jsp/revenggquery.jsp" >revenggquery</a>
	<a href="${pageContext.request.contextPath}/jsp/fefileeditor.jsp" >fefileeditor</a>
	<a href="${pageContext.request.contextPath}/jsp/befileeditor.jsp" >befileeditor</a>
	<a href="http://localhost:8081/servershell/bconfig.action">Designate FE/BE</a>
</body>
</html>