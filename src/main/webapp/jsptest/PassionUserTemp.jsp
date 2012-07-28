<!DOCTYPE script PUBLIC "-//W3C//DTD XHTML 1.1 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s"  uri="/struts-tags" %>
<%@taglib prefix="sj"  uri="/struts-jquery-tags" %>
<%--@taglib prefix="sjg"  uri="/struts-jquery-grid-tags" --%>
<html>
<head>
<title>Passion User Temp</title> 
<sj:head jqueryui="true" jquerytheme="redmond"  />
<link rel="stylesheet" type="text/css" href="../css/ui.jqgrid.css">
<script src="../js/jquery.validate.js" > </script>
<script src="../js/additional-methods.js" > </script>
<script src="../js/i18n/grid.locale-en.js" > </script>
<script src="../js/jquery.jqGrid.min.js" > </script>
<script src="../js/json2.js" > </script>

<script>
	var rulesframework = {}; 
	<s:if test = "jsrule != null" >
		 rulesframework =  ${jsrule};
	</s:if>
        	var fieldlist = "cardno, idvalue, idtype, actionflag, validcard, alreadyregistered, registrationcompleted, inputfilename, returnfilecreated, errorcode, fileprocessedflag, nationality, errormsg".split(",");
   $(document).ready(function(){
	//iadt.setFieldlist(fieldlist);
	globalAjaxErrorSetup();
	$("#form1").validate($.extend(rulesframework,{debug: true}));
	calljqgrid();		
   });
	function showAjaxError(request, type, errorThrown)
   {
       var message = "There was an error with the AJAX request.\n";
       switch (type) {
           case 'timeout':
               message += "The request timed out.";
               break;
           case 'notmodified':
               message += "The request was not modified but was not retrieved from the cache.";
               break;
           case 'parseerror':
               message += "XML/Json format is bad.";
               break;
           default:
               message += "HTTP Error (" +type+" "+ request.status + " " + request.statusText + ").";
       }
       message += "\n";
       alert(message);
   }
   function globalAjaxErrorSetup(){
	   $( "#globalajaxerror" ).ajaxError(function(e, jqxhr, settings, exception) {
		     alert( "Triggered ajaxError handler." +exception);
			 if(window.console){
				 console.log("Ajax ecxcetion:");
				 console.log(exception);
				 }  
		 });
	}
  var lastsel= {};
  function calljqgrid(formdata){
   jQuery("#listid").jqGrid( {

      	url:'<%= request.getContextPath() %>/jqgrid.action?command=true&screenName=PassionUserTemp&submitdata={bulkcmd="gridonload"}',
      	datatype: "json",
      	height:350, 
      	colNames:['Card No','Id Value','Id Type','Action Flag','Valid Card','Already Registered','Registration Completed','Input File Name','Return File Created','Error Code','File Processed Flag','Nationality','Error Msg'      	],
      	colModel:[
	{name: 'cardno', index: 'cardno' , width:176, editable:true },
	{name: 'idvalue', index: 'idvalue' , width:175, editable:true },
	{name: 'idtype', index: 'idtype' , width:110, editable:true },
	{name: 'actionflag', index: 'actionflag' , width:140, editable:true },
	{name: 'validcard', index: 'validcard' , width:11, editable:true },
	{name: 'alreadyregistered', index: 'alreadyregistered' , width:11, editable:true },
	{name: 'registrationcompleted', index: 'registrationcompleted' , width:11, editable:true },
	{name: 'inputfilename', index: 'inputfilename' , width:250, editable:true },
	{name: 'returnfilecreated', index: 'returnfilecreated' , width:11, editable:true },
	{name: 'errorcode', index: 'errorcode' , width:110, editable:true },
	{name: 'fileprocessedflag', index: 'fileprocessedflag' , width:11, editable:true },
	{name: 'nationality', index: 'nationality' , width:55, editable:true },
	{name: 'errormsg', index: 'errormsg' , width:210, editable:true }
      	],
      	rowNum: 15,
      	rowList: [ 15, 25, 50],
      	pager: '#pagerid',
      	sortname: 'cardno',
        viewrecords: true,
        sortorder: "desc",
        jsonReader: {
    		repeatitems : false,
    		userdata: 'userdata',
            id: "0"
    	},
       onSelectRow: function(id){
			    		if(id && id!==lastsel){
			    			//jQuery('#listid').jqGrid('restoreRow',lastsel);
			    			//jQuery('#listid').jqGrid('editRow',id,true);
			    			jQuery("#listid").jqGrid('GridToForm',id,"#form1");
			    			lastsel=id;
			    		}
			    	},
	    loadComplete: function(){
			    		var ret;
 						$("#messagegrid").text(JSON.stringify(jQuery("#listid").getGridParam('userData'), null, 2));
			    	},
			       editurl: "${pageContext.request.contextPath}/html/simpleform.action?screenName=PassionUserTemp&bulkcmd=grid",
       caption: "Type the Caption here"
   } ).navGrid('#pagerid',{edit:true,add:true,del:true},{},{},{},{multipleSearch:true, multipleGroup:true});
   jQuery("#listid").jqGrid('navButtonAdd','#pagerid',{caption:"Edit",
		onClickButton:function(){
			var gsr = jQuery("#listid").jqGrid('getGridParam','selrow');
			if(gsr){
				jQuery("#listid").jqGrid('GridToForm',gsr,"#form1");
			} else {
				alert("Please select Row");
			}							
		} 
	}); 
  } //end calljqgrid
  function ajaxSubmit(){
	jQuery.post("${pageContext.request.contextPath}/html/simpleform.action?screenName=PassionUserTemp", 
			$("#form1").serialize(),
			function(data){
		var json = jQuery.parseJSON(data);
		jQuery("#listid").trigger("reloadGrid");
      }).error(showAjaxError);
  }
</script>

</head>

<body>
  <table id="listid" ></table>
  <div id="pagerid"></div>  <div id="messagegrid"></div>
<!--Submit Form -->
<form name="form1" id="form1" method="post" action="${pageContext.request.contextPath}/html/simpleform.action?screenName=PassionUserTemp">
        	 <table>
        	   <tr><td>Card No </td><td><input name="cardno" id="cardno" value="${resultDTO.data.formonload[0].cardno}"/></td></tr>
        	   <tr><td>Id Value </td><td><input name="idvalue" id="idvalue" value="${resultDTO.data.formonload[0].idvalue}"/></td></tr>
        	   <tr><td>Id Type </td><td><input name="idtype" id="idtype" value="${resultDTO.data.formonload[0].idtype}"/></td></tr>
        	   <tr><td>Action Flag </td><td><input name="actionflag" id="actionflag" value="${resultDTO.data.formonload[0].actionflag}"/></td></tr>
        	   <tr><td>Valid Card </td><td><input name="validcard" id="validcard" value="${resultDTO.data.formonload[0].validcard}"/></td></tr>
        	   <tr><td>Already Registered </td><td><input name="alreadyregistered" id="alreadyregistered" value="${resultDTO.data.formonload[0].alreadyregistered}"/></td></tr>
        	   <tr><td>Registration Completed </td><td><input name="registrationcompleted" id="registrationcompleted" value="${resultDTO.data.formonload[0].registrationcompleted}"/></td></tr>
        	   <tr><td>Input File Name </td><td><input name="inputfilename" id="inputfilename" value="${resultDTO.data.formonload[0].inputfilename}"/></td></tr>
        	   <tr><td>Return File Created </td><td><input name="returnfilecreated" id="returnfilecreated" value="${resultDTO.data.formonload[0].returnfilecreated}"/></td></tr>
        	   <tr><td>Error Code </td><td><input name="errorcode" id="errorcode" value="${resultDTO.data.formonload[0].errorcode}"/></td></tr>
        	   <tr><td>File Processed Flag </td><td><input name="fileprocessedflag" id="fileprocessedflag" value="${resultDTO.data.formonload[0].fileprocessedflag}"/></td></tr>
        	   <tr><td>Nationality </td><td><input name="nationality" id="nationality" value="${resultDTO.data.formonload[0].nationality}"/></td></tr>
        	   <tr><td>Error Msg </td><td><input name="errormsg" id="errormsg" value="${resultDTO.data.formonload[0].errormsg}"/></td></tr>
        	   
        	 </table>
        	 bulkcmd: <input name="bulkcmd" value="frmgridedit"/>
        	 <button >submit</button>
        	 <button type="button" onclick="ajaxSubmit()">Ajax Submit</button></form>
</body>

</html>