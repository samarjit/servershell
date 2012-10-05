<!DOCTYPE script PUBLIC "-//W3C//DTD XHTML 1.1 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s"  uri="/struts-tags" %>
<%@taglib prefix="sj"  uri="/struts-jquery-tags" %>
<%--@taglib prefix="sjg"  uri="/struts-jquery-grid-tags" --%>
<html>
<head>
 <title>AlertTemplateMaster</title>
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
        	var fieldlist = "alerttemplateid, alerttype, alertcontent, smspassword, smsuserid, techstudiocampaignid, emailsubject, contenttype, variablenames, createdby, templatefilename, createddate".split(",");
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

      	url:'<%= request.getContextPath() %>/jqgrid.action?command=true&screenName=AlertTemplateMaster&submitdata={bulkcmd="gridonload"}',
      	datatype: "json",
      	height:350, 
      	colNames:['Alert Template Id','Alert Type','Alert Content','Sms Password','Sms Userid','Tech Studio Campaign Id','Email Subject','Content Type','Variable Names','Created By','Template File Name','Created Date'      	],
      	colModel:[
      	{name: 'alerttemplateid', index: 'alerttemplateid' , width:400, editable:true },
      	{name: 'alerttype', index: 'alerttype' , width:400, editable:true },
      	{name: 'alertcontent', index: 'alertcontent' , width:400, editable:true },
      	{name: 'smspassword', index: 'smspassword' , width:160, editable:true },
      	{name: 'smsuserid', index: 'smsuserid' , width:160, editable:true },
      	{name: 'techstudiocampaignid', index: 'techstudiocampaignid' , width:160, editable:true },
      	{name: 'emailsubject', index: 'emailsubject' , width:400, editable:true },
      	{name: 'contenttype', index: 'contenttype' , width:138, editable:true },
      	{name: 'variablenames', index: 'variablenames' , width:400, editable:true },
      	{name: 'createdby', index: 'createdby' , width:200, editable:true },
      	{name: 'templatefilename', index: 'templatefilename' , width:400, editable:true },
      	{name: 'createddate', index: 'createddate' , width:138, editable:true }
      	],
      	rowNum: 15,
      	rowList: [ 15, 25, 50],
      	pager: '#pagerid',
      	sortname: 'alerttemplateid',
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
			       editurl: "${pageContext.request.contextPath}/html/simpleform.action?screenName=AlertTemplateMaster&bulkcmd=grid",
       caption: "AlertTemplateMaster"
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
	jQuery.post("${pageContext.request.contextPath}/html/simpleform.action?screenName=AlertTemplateMaster", 
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
<form name="form1" id="form1" method="post" action="${pageContext.request.contextPath}/html/simpleform.action?screenName=AlertTemplateMaster">
        	 <table>
        	   <tr><td>Alert Template Id </td><td><input name="alerttemplateid" id="alerttemplateid" value="${resultDTO.data.formonload[0].alerttemplateid}"/></td></tr>
        	   <tr><td>Alert Type </td><td><input name="alerttype" id="alerttype" value="${resultDTO.data.formonload[0].alerttype}"/></td></tr>
        	   <tr><td>Alert Content </td><td><input name="alertcontent" id="alertcontent" value="${resultDTO.data.formonload[0].alertcontent}"/></td></tr>
        	   <tr><td>Sms Password </td><td><input name="smspassword" id="smspassword" value="${resultDTO.data.formonload[0].smspassword}"/></td></tr>
        	   <tr><td>Sms Userid </td><td><input name="smsuserid" id="smsuserid" value="${resultDTO.data.formonload[0].smsuserid}"/></td></tr>
        	   <tr><td>Tech Studio Campaign Id </td><td><input name="techstudiocampaignid" id="techstudiocampaignid" value="${resultDTO.data.formonload[0].techstudiocampaignid}"/></td></tr>
        	   <tr><td>Email Subject </td><td><input name="emailsubject" id="emailsubject" value="${resultDTO.data.formonload[0].emailsubject}"/></td></tr>
        	   <tr><td>Content Type </td><td><input name="contenttype" id="contenttype" value="${resultDTO.data.formonload[0].contenttype}"/></td></tr>
        	   <tr><td>Variable Names </td><td><input name="variablenames" id="variablenames" value="${resultDTO.data.formonload[0].variablenames}"/></td></tr>
        	   <tr><td>Created By </td><td><input name="createdby" id="createdby" value="${resultDTO.data.formonload[0].createdby}"/></td></tr>
        	   <tr><td>Template File Name </td><td><input name="templatefilename" id="templatefilename" value="${resultDTO.data.formonload[0].templatefilename}"/></td></tr>
        	   <tr><td>Created Date </td><td><input name="createddate" id="createddate" value="${resultDTO.data.formonload[0].createddate}"/></td></tr>
        	   


        	   <tr><td>Alert Template Id </td><td><s:property value="#resultDTO.data.formonload[0].alerttemplateid"  /></td></tr>
        	   <tr><td>Alert Type </td><td><s:property value="#resultDTO.data.formonload[0].alerttype"  /></td></tr>
        	   <tr><td>Alert Content </td><td><s:property value="#resultDTO.data.formonload[0].alertcontent"  /></td></tr>
        	   <tr><td>Sms Password </td><td><s:property value="#resultDTO.data.formonload[0].smspassword"  /></td></tr>
        	   <tr><td>Sms Userid </td><td><s:property value="#resultDTO.data.formonload[0].smsuserid"  /></td></tr>
        	   <tr><td>Tech Studio Campaign Id </td><td><s:property value="#resultDTO.data.formonload[0].techstudiocampaignid"  /></td></tr>
        	   <tr><td>Email Subject </td><td><s:property value="#resultDTO.data.formonload[0].emailsubject"  /></td></tr>
        	   <tr><td>Content Type </td><td><s:property value="#resultDTO.data.formonload[0].contenttype"  /></td></tr>
        	   <tr><td>Variable Names </td><td><s:property value="#resultDTO.data.formonload[0].variablenames"  /></td></tr>
        	   <tr><td>Created By </td><td><s:property value="#resultDTO.data.formonload[0].createdby"  /></td></tr>
        	   <tr><td>Template File Name </td><td><s:property value="#resultDTO.data.formonload[0].templatefilename"  /></td></tr>
        	   <tr><td>Created Date </td><td><s:property value="#resultDTO.data.formonload[0].createddate"  /></td></tr>
        	 </table>
        	 bulkcmd: <input name="bulkcmd" value="frmgridedit"/>
        	 <button >submit</button>
        	 <button type="button" onclick="ajaxSubmit()">Ajax Submit</button></form>
</body>

</html>
