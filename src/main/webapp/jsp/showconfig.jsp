<script>
if(jQuery.struts2_jquery != null){
	jQuery.struts2_jquery.require("js/base/jquery.ui.dialog.js");
	jQuery.struts2_jquery.require("js/base/jquery.ui.widget.js");
	jQuery.struts2_jquery.require("js/base/jquery.ui.mouse.js");
	jQuery.struts2_jquery.require("js/base/jquery.ui.position.js");
	jQuery.struts2_jquery.require("js/base/jquery.bgiframe.js");
	jQuery.struts2_jquery.require("js/base/jquery.ui.resizable.js");
	jQuery.struts2_jquery.require("js/base/jquery.ui.draggable.js");
	jQuery.struts2_jquery.require("../js/json2.js");
	document.getElementById("configname").value = "d"+Math.ceil(Math.random()*10000000);
}

function showallconf(){
	var s="";
	$(":input").each(function(i,v){ 
		if(v.type == 'text' && v.name != 'name' && v.name != null && v.name != ""){
			s+= document.title.replace(/\s/g,"")+"_"+$("#configname").val()+"."+v.name+"="+v.value+"\r\n<br/>";
		}
	});
	
	$("#show").html(s);
}

function saveconf(){
	if($("#show").text().length < 3){
		alert('Please use show first to make the configname setting');return;
	}
	$.post("${pageContext.request.contextPath}/saveuserpref.action",'fefile='+$("#show").text(),function(data){
		$("#errordiv").text(data);
	});
}

function fetchconf(){
	$.get("${pageContext.request.contextPath}/finduserpref.action",'fefile='+document.title.replace(/\s/g,""),function(data){
		var json = $.parseJSON(data);
		
		jsonProps = json.jobj.props;
		var tabl = $("<table id='datatable' border='1'></table>");
		$("#fetchresult").append(tabl);
		for(var x in jsonProps){
			tabl.append("<tr><td><input type='button' value='restore' onclick='restoreconf(this)' /><input type='button' value='x' onclick='removeconf(this)' /></td><td>"+x+"</td><td>"+JSON.stringify(jsonProps[x])+"</td></tr>");
		}
		$("#fetchresult").empty();
		//$("#fetchresult").text(data);
		$("#fetchresult").append(tabl);
		
		$("#userconfig").dialog('open',{width: 'auto'})
	});
}

function restoreconf(elm){
	//console.log('restoring with '+$(elm).parent().parent().find('td:eq(2)').text());
	var jsonstr = $(elm).parent().parent().find('td:eq(2)').text();
	json = $.parseJSON(jsonstr);
	for(var x in json){
		document.getElementsByName(x)[0].value= json[x];
	}
}

function removeconf(elm){
	console.log('removing with '+$(elm).parent().parent().find('td:eq(1)').text());
	$.get("${pageContext.request.contextPath}/removeuserpref.action",'fefile='+$(elm).parent().parent().find('td:eq(1)').text(),function(data){
		alert(data);
	});
}
 
</script>

<form >

<input type="button" value="fetch" onclick="fetchconf();"/>
<input type="button" value="show" onclick="showallconf();"/>
<input type="button" value="save" onclick="saveconf();"/>
Config name:<input type="text" id="configname" value="default" />
<div id="show">

</div><br/>
<div id="fetchresult">

</div>
<br/>
<div id="errordiv" ></div>
</form>