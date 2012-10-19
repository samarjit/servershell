<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s"  uri="/struts-tags" %>
<%@taglib prefix="sj"  uri="/struts-jquery-tags" %>
<%--@taglib prefix="sjg"  uri="/struts-jquery-grid-tags" --%>
<html>
<head>
 <title>EzlRetnAuditTrail</title>
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
        	var fieldlist = "audittrailid, nric, cin, cardno, modulename, pagename, oldvalue, newvalue, description, makerid, makerdate".split(",");
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
  var lastsel= {};var lastsel3;
  function calljqgrid(formdata){
   jQuery("#gridtableid").jqGrid( {

      	url:'<%= request.getContextPath() %>/jqgrid.action?command=true&screenName=EzlRetnAuditTrail&submitdata={bulkcmd="gridonload"}',
      	datatype: "json",
      	height:350, 
      	colNames:['Audit Trail Id','Nric','Cin','Card No','Module Name','Page Name','Old Value','New Value','Description','Maker Id','Maker Date'      	],
      	colModel:[
      	{name: 'audittrailid', index: 'audittrailid' , width:160, editable:false },
      	{name: 'nric', index: 'nric' , width:160, editable:true },
      	{name: 'cin', index: 'cin' , width:160, editable:true },
      	{name: 'cardno', index: 'cardno' , width:128, editable:true },
      	{name: 'modulename', index: 'modulename' , width:200, editable:true },
      	{name: 'pagename', index: 'pagename' , width:400, editable:true },
      	{name: 'oldvalue', index: 'oldvalue' , width:400, editable:true },
      	{name: 'newvalue', index: 'newvalue' , width:400, editable:true },
      	{name: 'description', index: 'description' , width:400, editable:true },
      	{name: 'makerid', index: 'makerid' , width:200, editable:true },
      	{name: 'makerdate', index: 'makerdate' , width:110, editable:true }
      	],
      	rowNum: 15,
      	rowList: [ 15, 25, 50],
      	pager: '#gridpagerid',
      	sortname: 'audittrailid',
        viewrecords: true,
        sortorder: "desc",
        jsonReader: {
    		repeatitems : false,
    		userdata: 'userdata',
            id: "0"
    	},
        afterShowForm: function (){
            alert('hi');
        },
      /* onSelectRow: function(id){
			    		if(id && id!==lastsel){
			    			//jQuery('#gridtableid').jqGrid('restoreRow',lastsel);
			    			//jQuery('#gridtableid').jqGrid('editRow',id,true);
			    			jQuery("#gridtableid").jqGrid('GridToForm',id,"#form1");
			    			lastsel=id;
			    		}
			    	},*/
	    loadComplete: function(){
			    		var ret;
 						$("#messagegrid").text(JSON.stringify(jQuery("#gridtableid").getGridParam('userData'), null, 2));
			    	},
                   onSelectRow: function(id){
                    	if(id && id!==lastsel3){
                			jQuery('#gridtableid').jqGrid('restoreRow',lastsel3);
                			jQuery('#gridtableid').jqGrid('editRow',id,true, null , function(data){console.log(data);});
                			lastsel3=id;
                		}
                	},
			       editurl: "${pageContext.request.contextPath}/html/simpleform.action?screenName=EzlRetnAuditTrail&bulkcmd=grid",
       caption: "EzlRetnAuditTrail"
   } ).navGrid('#gridpagerid',{edit:true,add:true,del:true},{},{},{},{multipleSearch:true, multipleGroup:true});
   jQuery("#gridtableid").jqGrid('navButtonAdd','#gridpagerid',{caption:"Edit",
		onClickButton:function(){
			var gsr = jQuery("#gridtableid").jqGrid('getGridParam','selrow');
			if(gsr){
				jQuery("#gridtableid").jqGrid('GridToForm',gsr,"#form1");
			} else {
				alert("Please select Row");
			}							
		} 
	}); 
  } //end calljqgrid
  function ajaxSubmit(){
	jQuery.post("${pageContext.request.contextPath}/html/simpleform.action?screenName=EzlRetnAuditTrail", 
			$("#form1").serialize(),
			function(data){
		var json = jQuery.parseJSON(data);
         var errors = 'errors:'+json['errors'];
        var messages = ' message:'+json['messages'];
        $("#editmessageresult").text(messages+" "+errors);		jQuery("#gridtableid").trigger("reloadGrid");
      }).error(showAjaxError);
  }
</script>

</head>

<body>
  <table id="gridtableid" ></table>  <div id="gridpagerid"></div>
  <div id="messagegrid"></div>
  <div id="editmessageresult"></div>
<!--Submit Form -->
<form name="form1" id="form1" method="post" action="${pageContext.request.contextPath}/html/simpleform.action?screenName=EzlRetnAuditTrail">
        	 <table>
        	   <tr><td>Audit Trail Id </td><td><input name="audittrailid" id="audittrailid" value="${resultDTO.data.formonload[0].audittrailid}"/></td></tr>
        	   <tr><td>Nric </td><td><input name="nric" id="nric" value="${resultDTO.data.formonload[0].nric}"/></td></tr>
        	   <tr><td>Cin </td><td><input name="cin" id="cin" value="${resultDTO.data.formonload[0].cin}"/></td></tr>
        	   <tr><td>Card No </td><td><input name="cardno" id="cardno" value="${resultDTO.data.formonload[0].cardno}"/></td></tr>
        	   <tr><td>Module Name </td><td><input name="modulename" id="modulename" value="${resultDTO.data.formonload[0].modulename}"/></td></tr>
        	   <tr><td>Page Name </td><td><input name="pagename" id="pagename" value="${resultDTO.data.formonload[0].pagename}"/></td></tr>
        	   <tr><td>Old Value </td><td><input name="oldvalue" id="oldvalue" value="${resultDTO.data.formonload[0].oldvalue}"/></td></tr>
        	   <tr><td>New Value </td><td><input name="newvalue" id="newvalue" value="${resultDTO.data.formonload[0].newvalue}"/></td></tr>
        	   <tr><td>Description </td><td><input name="description" id="description" value="${resultDTO.data.formonload[0].description}"/></td></tr>
        	   <tr><td>Maker Id </td><td><input name="makerid" id="makerid" value="${resultDTO.data.formonload[0].makerid}"/></td></tr>
        	   <tr><td>Maker Date </td><td><input name="makerdate" id="makerdate" value="${resultDTO.data.formonload[0].makerdate}"/></td></tr>
        	   


        	    
        	 </table>
        	 bulkcmd: <input name="bulkcmd" value="frmgridedit"/>
        	 <button >submit</button>
        	 <button type="button" onclick="ajaxSubmit()">Ajax Submit</button></form>
</body>

</html>
