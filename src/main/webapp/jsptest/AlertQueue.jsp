<!DOCTYPE script PUBLIC "-//W3C//DTD XHTML 1.1 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s"  uri="/struts-tags" %>
<%@taglib prefix="sj"  uri="/struts-jquery-tags" %>
<%--@taglib prefix="sjg"  uri="/struts-jquery-grid-tags" --%>
<html>
<head>
 <title>AlertQueue</title>
<sj:head jqueryui="true" jquerytheme="redmond"  />
<s:set var="ctx"  >${pageContext.request.contextPath}</s:set><link rel="stylesheet" type="text/css" href="${ctx}/css/ui.jqgrid.css">
<!--script src="../js/jquery.validate.js" > </script>
<script src="../js/additional-methods.js" > </script-->
<script src="../js/i18n/grid.locale-en.js" > </script>
<script src="../js/jquery.jqGrid.min.js" > </script>
<!--script src="../js/json2.js" > </script-->

<script>
	var rulesframework = {}; 
	<s:if test = "jsrule != null" >
		 rulesframework =  ${jsrule};
	</s:if>
        	var fieldlist = "alertqid, alerttype, userid, smstext, emailtext, createtime, scheduledtime, deliveredstatus, retrycount, mobileno, emailto, emailtemplateid, bulkemailmemberno".split(",");
   $(document).ready(function(){
	//iadt.setFieldlist(fieldlist);
	globalAjaxErrorSetup();
	//$("#form1").validate($.extend(rulesframework,{debug: true}));
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

      	url:'<%= request.getContextPath() %>/jqgrid.action?command=true&screenName=AlertQueue&submitdata={bulkcmd="gridonload"}',
      	datatype: "json",
      	height:350, 
      	colNames:['Alert Q Id','Alert Type','User Id','Sms Text','Email Text','Create Time','Scheduled Time','Delivered Status','Retry Count','Mobile No','Email To','Email Template Id','Bulk Email Member No'      	],
      	colModel:[
      	{name: 'alertqid', index: 'alertqid' , width:88, editable:true },
      	{name: 'alerttype', index: 'alerttype' , width:160, editable:true },
      	{name: 'userid', index: 'userid' , width:160, editable:true },
      	{name: 'smstext', index: 'smstext' , width:400, editable:true },
      	{name: 'emailtext', index: 'emailtext' , width:400, editable:true },
      	{name: 'createtime', index: 'createtime' , width:138, editable:true },
      	{name: 'scheduledtime', index: 'scheduledtime' , width:138, editable:true },
      	{name: 'deliveredstatus', index: 'deliveredstatus' , width:400, editable:true },
      	{name: 'retrycount', index: 'retrycount' , width:22, editable:true },
      	{name: 'mobileno', index: 'mobileno' , width:200, editable:true },
      	{name: 'emailto', index: 'emailto' , width:400, editable:true },
      	{name: 'emailtemplateid', index: 'emailtemplateid' , width:400, editable:true },
      	{name: 'bulkemailmemberno', index: 'bulkemailmemberno' , width:400, editable:true }
      	],
      	rowNum: 15,
      	rowList: [ 15, 25, 50],
      	pager: '#pagerid',
      	sortname: 'alertqid',
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
			       editurl: "${pageContext.request.contextPath}/html/simpleform.action?screenName=AlertQueue&bulkcmd=grid",
       caption: "AlertQueue"
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
	jQuery.post("${pageContext.request.contextPath}/html/simpleform.action?screenName=AlertQueue", 
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
<form name="form1" id="form1" method="post" action="${pageContext.request.contextPath}/html/simpleform.action?screenName=AlertQueue">
        	 <table>
        	   <tr><td>Alert Q Id </td><td><input name="alertqid" id="alertqid" value="${resultDTO.data.formonload[0].alertqid}"/></td></tr>
        	   <tr><td>Alert Type </td><td><input name="alerttype" id="alerttype" value="${resultDTO.data.formonload[0].alerttype}"/></td></tr>
        	   <tr><td>User Id </td><td><input name="userid" id="userid" value="${resultDTO.data.formonload[0].userid}"/></td></tr>
        	   <tr><td>Sms Text </td><td><input name="smstext" id="smstext" value="${resultDTO.data.formonload[0].smstext}"/></td></tr>
        	   <tr><td>Email Text </td><td><input name="emailtext" id="emailtext" value="${resultDTO.data.formonload[0].emailtext}"/></td></tr>
        	   <tr><td>Create Time </td><td><input name="createtime" id="createtime" value="${resultDTO.data.formonload[0].createtime}"/></td></tr>
        	   <tr><td>Scheduled Time </td><td><input name="scheduledtime" id="scheduledtime" value="${resultDTO.data.formonload[0].scheduledtime}"/></td></tr>
        	   <tr><td>Delivered Status </td><td><input name="deliveredstatus" id="deliveredstatus" value="${resultDTO.data.formonload[0].deliveredstatus}"/></td></tr>
        	   <tr><td>Retry Count </td><td><input name="retrycount" id="retrycount" value="${resultDTO.data.formonload[0].retrycount}"/></td></tr>
        	   <tr><td>Mobile No </td><td><input name="mobileno" id="mobileno" value="${resultDTO.data.formonload[0].mobileno}"/></td></tr>
        	   <tr><td>Email To </td><td><input name="emailto" id="emailto" value="${resultDTO.data.formonload[0].emailto}"/></td></tr>
        	   <tr><td>Email Template Id </td><td><input name="emailtemplateid" id="emailtemplateid" value="${resultDTO.data.formonload[0].emailtemplateid}"/></td></tr>
        	   <tr><td>Bulk Email Member No </td><td><input name="bulkemailmemberno" id="bulkemailmemberno" value="${resultDTO.data.formonload[0].bulkemailmemberno}"/></td></tr>
        	   


        	   <tr><td>Alert Q Id </td><td><s:property value="#resultDTO.data.formonload[0].alertqid"  /></td></tr>
        	   <tr><td>Alert Type </td><td><s:property value="#resultDTO.data.formonload[0].alerttype"  /></td></tr>
        	   <tr><td>User Id </td><td><s:property value="#resultDTO.data.formonload[0].userid"  /></td></tr>
        	   <tr><td>Sms Text </td><td><s:property value="#resultDTO.data.formonload[0].smstext"  /></td></tr>
        	   <tr><td>Email Text </td><td><s:property value="#resultDTO.data.formonload[0].emailtext"  /></td></tr>
        	   <tr><td>Create Time </td><td><s:property value="#resultDTO.data.formonload[0].createtime"  /></td></tr>
        	   <tr><td>Scheduled Time </td><td><s:property value="#resultDTO.data.formonload[0].scheduledtime"  /></td></tr>
        	   <tr><td>Delivered Status </td><td><s:property value="#resultDTO.data.formonload[0].deliveredstatus"  /></td></tr>
        	   <tr><td>Retry Count </td><td><s:property value="#resultDTO.data.formonload[0].retrycount"  /></td></tr>
        	   <tr><td>Mobile No </td><td><s:property value="#resultDTO.data.formonload[0].mobileno"  /></td></tr>
        	   <tr><td>Email To </td><td><s:property value="#resultDTO.data.formonload[0].emailto"  /></td></tr>
        	   <tr><td>Email Template Id </td><td><s:property value="#resultDTO.data.formonload[0].emailtemplateid"  /></td></tr>
        	   <tr><td>Bulk Email Member No </td><td><s:property value="#resultDTO.data.formonload[0].bulkemailmemberno"  /></td></tr>
        	 </table>
        	 bulkcmd: <input name="bulkcmd" value="frmgridedit"/>
        	 <button >submit</button>
        	 <button type="button" onclick="ajaxSubmit()">Ajax Submit</button></form>
</body>

</html>
