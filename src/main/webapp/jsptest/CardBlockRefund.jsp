<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s"  uri="/struts-tags" %>
<%@taglib prefix="sj"  uri="/struts-jquery-tags" %>
<%--@taglib prefix="sjg"  uri="/struts-jquery-grid-tags" --%>
<html>
<head>
 <title>CardBlockRefund</title>
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
        	var fieldlist = "cardno, userid, balatreporting, balatblocking, totalmisusedamount, reportingtime, blockingtime, cardinsureavail, insamountclaimed, replacecardno, refundexpirydate, refundstatus, policereportnumber, policereportduedate, policereportfilename, sawfilerefid, sawresult, insurfilerefid, insurresult, mtrfilerefid, emailnotified, sawtxnref, insurfilerecvdate, sawfilerecvdate, mtrfilerecvdate, replacecardnotmp, refundmode, refundchannel".split(",");
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
   jQuery("#gridtableid").jqGrid( {

      	url:'<%= request.getContextPath() %>/jqgrid.action?command=true&screenName=CardBlockRefund&submitdata={bulkcmd="gridonload"}',
      	datatype: "json",
      	height:350, 
      	colNames:['Card No','User Id','Bal At Reporting','Bal At Blocking','Total Misused Amount','Reporting Time','Blocking Time','Card Insure Avail','Ins Amount Claimed','Replace Card No','Refund Expiry Date','Refund Status','Police Report Number','Police Report Due Date','Police Report File Name','Saw File Ref Id','Saw Result','Insur File Ref Id','Insur Result','Mtr File Ref Id','Email Notified','Saw Txn Ref','Insur File Recv Date','Saw File Recv Date','Mtr File Recv Date','Replace Card No Tmp','Refund Mode','Refund Channel'      	],
      	colModel:[
      	{name: 'cardno', index: 'cardno' , width:128, editable:true },
      	{name: 'userid', index: 'userid' , width:160, editable:true },
      	{name: 'balatreporting', index: 'balatreporting' , width:64, editable:true },
      	{name: 'balatblocking', index: 'balatblocking' , width:64, editable:true },
      	{name: 'totalmisusedamount', index: 'totalmisusedamount' , width:64, editable:true },
      	{name: 'reportingtime', index: 'reportingtime' , width:110, editable:true },
      	{name: 'blockingtime', index: 'blockingtime' , width:110, editable:true },
      	{name: 'cardinsureavail', index: 'cardinsureavail' , width:8, editable:true },
      	{name: 'insamountclaimed', index: 'insamountclaimed' , width:64, editable:true },
      	{name: 'replacecardno', index: 'replacecardno' , width:128, editable:true },
      	{name: 'refundexpirydate', index: 'refundexpirydate' , width:110, editable:true },
      	{name: 'refundstatus', index: 'refundstatus' , width:240, editable:true },
      	{name: 'policereportnumber', index: 'policereportnumber' , width:160, editable:true },
      	{name: 'policereportduedate', index: 'policereportduedate' , width:110, editable:true },
      	{name: 'policereportfilename', index: 'policereportfilename' , width:400, editable:true },
      	{name: 'sawfilerefid', index: 'sawfilerefid' , width:160, editable:true },
      	{name: 'sawresult', index: 'sawresult' , width:400, editable:true },
      	{name: 'insurfilerefid', index: 'insurfilerefid' , width:160, editable:true },
      	{name: 'insurresult', index: 'insurresult' , width:110, editable:true },
      	{name: 'mtrfilerefid', index: 'mtrfilerefid' , width:160, editable:true },
      	{name: 'emailnotified', index: 'emailnotified' , width:8, editable:true },
      	{name: 'sawtxnref', index: 'sawtxnref' , width:160, editable:true },
      	{name: 'insurfilerecvdate', index: 'insurfilerecvdate' , width:110, editable:true },
      	{name: 'sawfilerecvdate', index: 'sawfilerecvdate' , width:110, editable:true },
      	{name: 'mtrfilerecvdate', index: 'mtrfilerecvdate' , width:110, editable:true },
      	{name: 'replacecardnotmp', index: 'replacecardnotmp' , width:128, editable:true },
      	{name: 'refundmode', index: 'refundmode' , width:160, editable:true },
      	{name: 'refundchannel', index: 'refundchannel' , width:160, editable:true }
      	],
      	rowNum: 15,
      	rowList: [ 15, 25, 50],
      	pager: '#gridpagerid',
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
			    			//jQuery('#gridtableid').jqGrid('restoreRow',lastsel);
			    			//jQuery('#gridtableid').jqGrid('editRow',id,true);
			    			jQuery("#gridtableid").jqGrid('GridToForm',id,"#form1");
			    			lastsel=id;
			    		}
			    	},
	    loadComplete: function(){
			    		var ret;
 						$("#messagegrid").text(JSON.stringify(jQuery("#gridtableid").getGridParam('userData'), null, 2));
			    	},
			       editurl: "${pageContext.request.contextPath}/html/simpleform.action?screenName=CardBlockRefund&bulkcmd=grid",
       caption: "CardBlockRefund"
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
	jQuery.post("${pageContext.request.contextPath}/html/simpleform.action?screenName=CardBlockRefund", 
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
<form name="form1" id="form1" method="post" action="${pageContext.request.contextPath}/html/simpleform.action?screenName=CardBlockRefund">
        	 <table>
        	   <tr><td>Card No </td><td><input name="cardno" id="cardno" value="${resultDTO.data.formonload[0].cardno}"/></td></tr>
        	   <tr><td>User Id </td><td><input name="userid" id="userid" value="${resultDTO.data.formonload[0].userid}"/></td></tr>
        	   <tr><td>Bal At Reporting </td><td><input name="balatreporting" id="balatreporting" value="${resultDTO.data.formonload[0].balatreporting}"/></td></tr>
        	   <tr><td>Bal At Blocking </td><td><input name="balatblocking" id="balatblocking" value="${resultDTO.data.formonload[0].balatblocking}"/></td></tr>
        	   <tr><td>Total Misused Amount </td><td><input name="totalmisusedamount" id="totalmisusedamount" value="${resultDTO.data.formonload[0].totalmisusedamount}"/></td></tr>
        	   <tr><td>Reporting Time </td><td><input name="reportingtime" id="reportingtime" value="${resultDTO.data.formonload[0].reportingtime}"/></td></tr>
        	   <tr><td>Blocking Time </td><td><input name="blockingtime" id="blockingtime" value="${resultDTO.data.formonload[0].blockingtime}"/></td></tr>
        	   <tr><td>Card Insure Avail </td><td><input name="cardinsureavail" id="cardinsureavail" value="${resultDTO.data.formonload[0].cardinsureavail}"/></td></tr>
        	   <tr><td>Ins Amount Claimed </td><td><input name="insamountclaimed" id="insamountclaimed" value="${resultDTO.data.formonload[0].insamountclaimed}"/></td></tr>
        	   <tr><td>Replace Card No </td><td><input name="replacecardno" id="replacecardno" value="${resultDTO.data.formonload[0].replacecardno}"/></td></tr>
        	   <tr><td>Refund Expiry Date </td><td><input name="refundexpirydate" id="refundexpirydate" value="${resultDTO.data.formonload[0].refundexpirydate}"/></td></tr>
        	   <tr><td>Refund Status </td><td><input name="refundstatus" id="refundstatus" value="${resultDTO.data.formonload[0].refundstatus}"/></td></tr>
        	   <tr><td>Police Report Number </td><td><input name="policereportnumber" id="policereportnumber" value="${resultDTO.data.formonload[0].policereportnumber}"/></td></tr>
        	   <tr><td>Police Report Due Date </td><td><input name="policereportduedate" id="policereportduedate" value="${resultDTO.data.formonload[0].policereportduedate}"/></td></tr>
        	   <tr><td>Police Report File Name </td><td><input name="policereportfilename" id="policereportfilename" value="${resultDTO.data.formonload[0].policereportfilename}"/></td></tr>
        	   <tr><td>Saw File Ref Id </td><td><input name="sawfilerefid" id="sawfilerefid" value="${resultDTO.data.formonload[0].sawfilerefid}"/></td></tr>
        	   <tr><td>Saw Result </td><td><input name="sawresult" id="sawresult" value="${resultDTO.data.formonload[0].sawresult}"/></td></tr>
        	   <tr><td>Insur File Ref Id </td><td><input name="insurfilerefid" id="insurfilerefid" value="${resultDTO.data.formonload[0].insurfilerefid}"/></td></tr>
        	   <tr><td>Insur Result </td><td><input name="insurresult" id="insurresult" value="${resultDTO.data.formonload[0].insurresult}"/></td></tr>
        	   <tr><td>Mtr File Ref Id </td><td><input name="mtrfilerefid" id="mtrfilerefid" value="${resultDTO.data.formonload[0].mtrfilerefid}"/></td></tr>
        	   <tr><td>Email Notified </td><td><input name="emailnotified" id="emailnotified" value="${resultDTO.data.formonload[0].emailnotified}"/></td></tr>
        	   <tr><td>Saw Txn Ref </td><td><input name="sawtxnref" id="sawtxnref" value="${resultDTO.data.formonload[0].sawtxnref}"/></td></tr>
        	   <tr><td>Insur File Recv Date </td><td><input name="insurfilerecvdate" id="insurfilerecvdate" value="${resultDTO.data.formonload[0].insurfilerecvdate}"/></td></tr>
        	   <tr><td>Saw File Recv Date </td><td><input name="sawfilerecvdate" id="sawfilerecvdate" value="${resultDTO.data.formonload[0].sawfilerecvdate}"/></td></tr>
        	   <tr><td>Mtr File Recv Date </td><td><input name="mtrfilerecvdate" id="mtrfilerecvdate" value="${resultDTO.data.formonload[0].mtrfilerecvdate}"/></td></tr>
        	   <tr><td>Replace Card No Tmp </td><td><input name="replacecardnotmp" id="replacecardnotmp" value="${resultDTO.data.formonload[0].replacecardnotmp}"/></td></tr>
        	   <tr><td>Refund Mode </td><td><input name="refundmode" id="refundmode" value="${resultDTO.data.formonload[0].refundmode}"/></td></tr>
        	   <tr><td>Refund Channel </td><td><input name="refundchannel" id="refundchannel" value="${resultDTO.data.formonload[0].refundchannel}"/></td></tr>
        	   


        	  
        	 </table>
        	 bulkcmd: <input name="bulkcmd" value="frmgridedit"/>
        	 <button >submit</button>
        	 <button type="button" onclick="ajaxSubmit()">Ajax Submit</button></form>
</body>

</html>
