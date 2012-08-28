<!DOCTYPE html>
<html>
<head>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="sj" uri="/struts-jquery-tags" %>
</head>
<sj:head compressed="false" debug="true" loadAtOnce="true" defaultLoadingText="My Loading text..."/>
<script type="text/javascript">
$.subscribe("handleresult",function(event,data){
	$("#res2").text(event.originalEvent.request.responseText)
});
</script>
<body>
<div id="res"></div>
Result:<div id="res2">res2</div>
<s:form action="logout.action" method="post">
<sj:submit targets="res" value="Logout me" indicator="indicator" onCompleteTopics="handleresult" />
</s:form>
</body>
</html>