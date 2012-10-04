<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
<%@taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@taglib prefix="s" uri="/struts-tags" %>



<style>
.ui-widget{
font-size: 12px;
}

#menu a {
padding-right:2px;
padding-left: 0px;
border-right: 1px dotted black;
text-decoration: none;
}
#menu{
font-family: verdana; 
font-size: 11px;
border:1px dotted black;
padding:2px;
}
</style>
 
<div  style="font-family: verdana"><span style="font-weight: bold;">Index servershell</span> <label>Welcome "${session.name}"</label></div>
<div id="logindiv" style="display:none">
<form action="${pageContext.request.contextPath}/monitoring.action">
<input type="text" name="cmd" id="cmd" value="login"/>
Name: <input name="name" /> Password: <input type="password" name="password" />
<sj:submit button="true" targets="index_divlogin" value="login" />
</form>
<div id="index_divlogin"></div>
<sj:submit button="true" href="#" value="hide" onclick="javascript:$('#logindiv').hide();"></sj:submit>
</div>

<script type="text/javascript">
function showconf(){
	var pagekey = document.title.replace(/\s/g,"");
	  
		$("#userconfig").load("${pageContext.request.contextPath}/jsp/showconfig.jsp", function (){
			$("#userconfig").show();
			$("#userconfig").dialog();
		});
		
	 
}
</script>

<div id="menu"  >
<a href="#" onclick="javascript:document.getElementById('logindiv').style.display='block';" >login</a>
<a href="${pageContext.request.contextPath}/jsp/monitoring.jsp" >monitoring</a>
<a href="${pageContext.request.contextPath}/jsp/logviewer.jsp" >logviewer</a>
<a href="${pageContext.request.contextPath}/jsp/query.jsp" >query</a>
<a href="${pageContext.request.contextPath}/jsp/fileuploaddownload.jsp" >fileuploaddownload</a>
<a href="${pageContext.request.contextPath}/jsp/berunlog.jsp" >berunlog</a>
<a href="${pageContext.request.contextPath}/config-browser/index.action" >config browser</a>
<a href="${pageContext.request.contextPath}/jsp/jvmMemoryMonitor.jsp" >jvm</a>
<a href="${pageContext.request.contextPath}/jsp/bescrolllog.jsp" >bescrolllog</a>
<a href="${pageContext.request.contextPath}/jsp/beshell.jsp" >beshell</a>
<a href="${pageContext.request.contextPath}/jsp/feshell.jsp" >feshell</a>
<a href="${pageContext.request.contextPath}/jsp/screendesignedit.jsp" >screendesignedit</a>
<a href="${pageContext.request.contextPath}/jsp/revenggquery.jsp" >revenggquery</a>
<a href="${pageContext.request.contextPath}/jsp/fefileeditor.jsp" >fefileeditor</a>
<a href="${pageContext.request.contextPath}/jsp/befileeditor.jsp" >befileeditor</a>
<a href="javascript:showconf();">Save config</a>
<a href="${pageContext.request.contextPath}/bconfig.action">Designate FE/BE</a>
<a href="${pageContext.request.contextPath}/logout.action">Logout</a>
</div>

<div id="userconfig" style="display:none"></div>