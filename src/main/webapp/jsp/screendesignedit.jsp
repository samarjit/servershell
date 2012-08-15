<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Reverse Engineering Tool</title>
</head>
<body>
Edit screenmap <form> <input name="revenggqry" id="revenggqry" /><button type="button">Rev engg.</button></form><br/>
Edit Screen xml: <form> <input name="xmlbasepath" id="xmlbasepath" />
<input name="xmlfilename" id="xmlfilename" /><button type="button">Rev engg.</button></form><br/>
Edit Screen ftl: <form><input name="ftlbasepath" id="ftlbasepath" />
<input name="ftlfilename" id="ftlfilename" /><button type="button">Rev engg.</button></form><br/>

Screenflow.xml:<textarea rows="100" cols="80" name="revenggresult" id="revenggresult"></textarea>
XML file:<textarea rows="100" cols="80" name="revenggresult" id="revenggresult"></textarea>
Ftl file:<textarea rows="100" cols="80" name="revenggresult" id="revenggresult"></textarea>

New Editor<br/>
Browse: <input name="file1" id="file1" size="80" /><button type="button">Open</button><button type="button">Save</button>
<div id="filename1">File1</div><textarea rows="100" cols="80" name="filetext1" id="filetext1"></textarea>
</body>
</html>