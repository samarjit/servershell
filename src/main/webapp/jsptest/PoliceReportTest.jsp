<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s"  uri="/struts-tags" %>
<%@taglib prefix="sj"  uri="/struts-jquery-tags" %>
<%--@taglib prefix="sjg"  uri="/struts-jquery-grid-tags" --%>
<html>
<head>
 <title>PoliceReportTest</title>
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
        	var fieldlist = "cardno, userid, balatreporting, spentaftreporttrans, spentaftreportnontrans, balatblocking, cardstatus, cardinsureavail, inspolicyno, refundmode, replacecardno, refundexpirydate, refundstatus, blockedby, reportingtime, blockingtime, blockreason, policereportnumber, policereportduedate, insamountclaimed, refundchannel, policereportfilename, totalmisusedamount, unsyncsettled, sawfilerefid, sawresult, insurfilerefid, insurresult, mtrfilerefid, prevrefundstatus, prevrefundchannel, updatedate, updatedby, rejectreason, emailnotified, sawtxnref, insurfilerecvdate, sawfilerecvdate, mtrfilerecvdate, replacecardnotmp".split(",");
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
  
  //CardMaster
  jQuery("#listidCM").jqGrid( {

      	url:'<%= request.getContextPath() %>/jqgrid.action?command=true&screenName=CardMaster&submitdata={bulkcmd="gridonload"}',
      	datatype: "json",
      	height:350, 
      	colNames:['Card No','Cin','Card Status'      	],
      	colModel:[
      	{name: 'cardno', index: 'cardno' , width:200, editable:true },
      	{name: 'cin', index: 'cin' , width:400, editable:true },
      	{name: 'cardstatus', index: 'cardstatus' , width:16, editable:true }
      	],
      	rowNum: 15,
      	rowList: [ 15, 25, 50],
      	pager: '#pageridCM',
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
			    			jQuery("#listidCM").jqGrid('GridToForm',id,"#form1");
			    			lastsel=id;
			    		}
			    	},
	    loadComplete: function(){
			    		var ret;
 						$("#messagegrid").text(JSON.stringify(jQuery("#listidCM").getGridParam('userData'), null, 2));
			    	},
			       editurl: "${pageContext.request.contextPath}/html/simpleform.action?screenName=CardMaster&bulkcmd=grid",
       caption: "CardMaster"
   } ).navGrid('#pageridCM',{edit:true,add:true,del:true},{},{},{},{multipleSearch:true, multipleGroup:true});
  //jQuery("#listidCM").jqGrid('gridResize',{minWidth:350,maxWidth:800,minHeight:80, maxHeight:350});
  
  jQuery("#gridtableid").jqGrid( {

      	url:'<%= request.getContextPath() %>/jqgrid.action?command=true&screenName=CustomerMaster&submitdata={bulkcmd="gridonload"}',
      	datatype: "json",
      	height:350, 
      	colNames:['Cin','Nric','F Name','L Name','Email Id','Mobile No'      	],
      	colModel:[
      	{name: 'cin', index: 'cin' , width:128, editable:true },
      	{name: 'nric', index: 'nric' , width:160, editable:true },
      	{name: 'fname', index: 'fname' , width:400, editable:true },
      	{name: 'lname', index: 'lname' , width:400, editable:true },
      	{name: 'emailid', index: 'emailid' , width:400, editable:true },
      	{name: 'mobileno', index: 'mobileno' , width:200, editable:true }
      	],
      	rowNum: 15,
      	rowList: [ 15, 25, 50],
      	pager: '#gridpagerid',
      	sortname: 'cin',
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
			       editurl: "${pageContext.request.contextPath}/html/simpleform.action?screenName=CustomerMaster&bulkcmd=grid",
       caption: "CustomerMaster"
   } ).navGrid('#gridpagerid',{edit:true,add:true,del:true},{},{},{},{multipleSearch:true, multipleGroup:true});
  
   jQuery("#listid").jqGrid( {

      	url:'<%= request.getContextPath() %>/jqgrid.action?command=true&screenName=PoliceReportTest&submitdata={bulkcmd="gridonload"}',
      	datatype: "json",
      	height:350, 
      	colNames:['Card No','User Id','Bal At Reporting','Spent Aft Report Trans','Spent Aft Report Nontrans','Bal At Blocking','Card Status','Card Insure Avail','Ins Policy No','Refund Mode','Replace Card No','Refund Expiry Date','Refund Status','Blocked By','Reporting Time','Blocking Time','Block Reason','Police Report Number','Police Report Due Date','Ins Amount Claimed','Refund Channel','Police Report File Name','Total Misused Amount','Unsync Settled','Saw File Ref Id','Saw Result','Insur File Ref Id','Insur Result','Mtr File Ref Id','Prev Refund Status','Prev Refund Channel','Update Date','Updated By','Reject Reason','Email Notified','Saw Txn Ref','Insur File Recv Date','Saw File Recv Date','Mtr File Recv Date','Replace Card No Tmp'      	],
      	colModel:[
      	{name: 'cardno', index: 'cardno' , width:128, editable:true },
      	{name: 'userid', index: 'userid' , width:160, editable:true },
      	{name: 'balatreporting', index: 'balatreporting' , width:64, editable:true },
      	{name: 'spentaftreporttrans', index: 'spentaftreporttrans' , width:64, editable:true },
      	{name: 'spentaftreportnontrans', index: 'spentaftreportnontrans' , width:64, editable:true },
      	{name: 'balatblocking', index: 'balatblocking' , width:64, editable:true },
      	{name: 'cardstatus', index: 'cardstatus' , width:160, editable:true },
      	{name: 'cardinsureavail', index: 'cardinsureavail' , width:8, editable:true },
      	{name: 'inspolicyno', index: 'inspolicyno' , width:110, editable:true },
      	{name: 'refundmode', index: 'refundmode' , width:160, editable:true },
      	{name: 'replacecardno', index: 'replacecardno' , width:128, editable:true },
      	{name: 'refundexpirydate', index: 'refundexpirydate' , width:110, editable:true },
      	{name: 'refundstatus', index: 'refundstatus' , width:240, editable:true },
      	{name: 'blockedby', index: 'blockedby' , width:200, editable:true },
      	{name: 'reportingtime', index: 'reportingtime' , width:110, editable:true },
      	{name: 'blockingtime', index: 'blockingtime' , width:110, editable:true },
      	{name: 'blockreason', index: 'blockreason' , width:400, editable:true },
      	{name: 'policereportnumber', index: 'policereportnumber' , width:160, editable:true },
      	{name: 'policereportduedate', index: 'policereportduedate' , width:110, editable:true },
      	{name: 'insamountclaimed', index: 'insamountclaimed' , width:64, editable:true },
      	{name: 'refundchannel', index: 'refundchannel' , width:160, editable:true },
      	{name: 'policereportfilename', index: 'policereportfilename' , width:400, editable:true },
      	{name: 'totalmisusedamount', index: 'totalmisusedamount' , width:64, editable:true },
      	{name: 'unsyncsettled', index: 'unsyncsettled' , width:8, editable:true },
      	{name: 'sawfilerefid', index: 'sawfilerefid' , width:160, editable:true },
      	{name: 'sawresult', index: 'sawresult' , width:400, editable:true },
      	{name: 'insurfilerefid', index: 'insurfilerefid' , width:160, editable:true },
      	{name: 'insurresult', index: 'insurresult' , width:110, editable:true },
      	{name: 'mtrfilerefid', index: 'mtrfilerefid' , width:160, editable:true },
      	{name: 'prevrefundstatus', index: 'prevrefundstatus' , width:240, editable:true },
      	{name: 'prevrefundchannel', index: 'prevrefundchannel' , width:160, editable:true },
      	{name: 'updatedate', index: 'updatedate' , width:110, editable:true },
      	{name: 'updatedby', index: 'updatedby' , width:160, editable:true },
      	{name: 'rejectreason', index: 'rejectreason' , width:400, editable:true },
      	{name: 'emailnotified', index: 'emailnotified' , width:8, editable:true },
      	{name: 'sawtxnref', index: 'sawtxnref' , width:160, editable:true },
      	{name: 'insurfilerecvdate', index: 'insurfilerecvdate' , width:110, editable:true },
      	{name: 'sawfilerecvdate', index: 'sawfilerecvdate' , width:110, editable:true },
      	{name: 'mtrfilerecvdate', index: 'mtrfilerecvdate' , width:110, editable:true },
      	{name: 'replacecardnotmp', index: 'replacecardnotmp' , width:128, editable:true }
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
  		cellEdit: true,
       onSelectRow: function(id){
			    		if(id && id!==lastsel){
			    			//jQuery('#listid').jqGrid('restoreRow',lastsel);
			    			//jQuery('#listid').jqGrid('editRow',id,true);
			    			jQuery("#listid").jqGrid('GridToForm',id,"#form1");
			    			lastsel=id;
			    		}
			    	},
  		afterSubmit: function(data){
  						alert(data);
  					},	
	    loadComplete: function(){
			    		var ret;
 						$("#messagegrid").text(JSON.stringify(jQuery("#listid").getGridParam('userData'), null, 2));
			    	},
			       editurl: "${pageContext.request.contextPath}/html/simpleform.action?screenName=PoliceReportTest&bulkcmd=grid",
       caption: "PoliceReportTest"
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
	jQuery.post("${pageContext.request.contextPath}/html/simpleform.action?screenName=PoliceReportTest", 
			$("#form1").serialize(),
			function(data){
		var json = jQuery.parseJSON(data);
         var errors = 'errors:'+json['errors'];
        var messages = ' message:'+json['messages'];
        $("#editmessageresult").text(messages+" "+errors);		jQuery("#listid").trigger("reloadGrid");
      }).error(showAjaxError);
  }
</script>

</head>

<body>
  <table id="listidCM" ></table>  <div id="pageridCM"></div>
  <table id="gridtableid" ></table>  <div id="gridpagerid"></div>
  <table id="listid" ></table>  <div id="pagerid"></div>
  <div id="messagegrid"></div>
  <div id="editmessageresult"></div>
<!--Submit Form -->
<form name="form1" id="form1" method="post" action="${pageContext.request.contextPath}/html/simpleform.action?screenName=PoliceReportTest">
        	 <table>
        	   <tr><td>Card No </td><td><input name="cardno" id="cardno" value="${resultDTO.data.formonload[0].cardno}"/></td></tr>
        	   <tr><td>User Id </td><td><input name="userid" id="userid" value="${resultDTO.data.formonload[0].userid}"/></td></tr>
        	   <tr><td>Bal At Reporting </td><td><input name="balatreporting" id="balatreporting" value="${resultDTO.data.formonload[0].balatreporting}"/></td></tr>
        	   <tr><td>Spent Aft Report Trans </td><td><input name="spentaftreporttrans" id="spentaftreporttrans" value="${resultDTO.data.formonload[0].spentaftreporttrans}"/></td></tr>
        	   <tr><td>Spent Aft Report Nontrans </td><td><input name="spentaftreportnontrans" id="spentaftreportnontrans" value="${resultDTO.data.formonload[0].spentaftreportnontrans}"/></td></tr>
        	   <tr><td>Bal At Blocking </td><td><input name="balatblocking" id="balatblocking" value="${resultDTO.data.formonload[0].balatblocking}"/></td></tr>
        	   <tr><td>Card Status </td><td><input name="cardstatus" id="cardstatus" value="${resultDTO.data.formonload[0].cardstatus}"/></td></tr>
        	   <tr><td>Card Insure Avail </td><td><input name="cardinsureavail" id="cardinsureavail" value="${resultDTO.data.formonload[0].cardinsureavail}"/></td></tr>
        	   <tr><td>Ins Policy No </td><td><input name="inspolicyno" id="inspolicyno" value="${resultDTO.data.formonload[0].inspolicyno}"/></td></tr>
        	   <tr><td>Refund Mode </td><td><input name="refundmode" id="refundmode" value="${resultDTO.data.formonload[0].refundmode}"/></td></tr>
        	   <tr><td>Replace Card No </td><td><input name="replacecardno" id="replacecardno" value="${resultDTO.data.formonload[0].replacecardno}"/></td></tr>
        	   <tr><td>Refund Expiry Date </td><td><input name="refundexpirydate" id="refundexpirydate" value="${resultDTO.data.formonload[0].refundexpirydate}"/></td></tr>
        	   <tr><td>Refund Status </td><td><input name="refundstatus" id="refundstatus" value="${resultDTO.data.formonload[0].refundstatus}"/></td></tr>
        	   <tr><td>Blocked By </td><td><input name="blockedby" id="blockedby" value="${resultDTO.data.formonload[0].blockedby}"/></td></tr>
        	   <tr><td>Reporting Time </td><td><input name="reportingtime" id="reportingtime" value="${resultDTO.data.formonload[0].reportingtime}"/></td></tr>
        	   <tr><td>Blocking Time </td><td><input name="blockingtime" id="blockingtime" value="${resultDTO.data.formonload[0].blockingtime}"/></td></tr>
        	   <tr><td>Block Reason </td><td><input name="blockreason" id="blockreason" value="${resultDTO.data.formonload[0].blockreason}"/></td></tr>
        	   <tr><td>Police Report Number </td><td><input name="policereportnumber" id="policereportnumber" value="${resultDTO.data.formonload[0].policereportnumber}"/></td></tr>
        	   <tr><td>Police Report Due Date </td><td><input name="policereportduedate" id="policereportduedate" value="${resultDTO.data.formonload[0].policereportduedate}"/></td></tr>
        	   <tr><td>Ins Amount Claimed </td><td><input name="insamountclaimed" id="insamountclaimed" value="${resultDTO.data.formonload[0].insamountclaimed}"/></td></tr>
        	   <tr><td>Refund Channel </td><td><input name="refundchannel" id="refundchannel" value="${resultDTO.data.formonload[0].refundchannel}"/></td></tr>
        	   <tr><td>Police Report File Name </td><td><input name="policereportfilename" id="policereportfilename" value="${resultDTO.data.formonload[0].policereportfilename}"/></td></tr>
        	   <tr><td>Total Misused Amount </td><td><input name="totalmisusedamount" id="totalmisusedamount" value="${resultDTO.data.formonload[0].totalmisusedamount}"/></td></tr>
        	   <tr><td>Unsync Settled </td><td><input name="unsyncsettled" id="unsyncsettled" value="${resultDTO.data.formonload[0].unsyncsettled}"/></td></tr>
        	   <tr><td>Saw File Ref Id </td><td><input name="sawfilerefid" id="sawfilerefid" value="${resultDTO.data.formonload[0].sawfilerefid}"/></td></tr>
        	   <tr><td>Saw Result </td><td><input name="sawresult" id="sawresult" value="${resultDTO.data.formonload[0].sawresult}"/></td></tr>
        	   <tr><td>Insur File Ref Id </td><td><input name="insurfilerefid" id="insurfilerefid" value="${resultDTO.data.formonload[0].insurfilerefid}"/></td></tr>
        	   <tr><td>Insur Result </td><td><input name="insurresult" id="insurresult" value="${resultDTO.data.formonload[0].insurresult}"/></td></tr>
        	   <tr><td>Mtr File Ref Id </td><td><input name="mtrfilerefid" id="mtrfilerefid" value="${resultDTO.data.formonload[0].mtrfilerefid}"/></td></tr>
        	   <tr><td>Prev Refund Status </td><td><input name="prevrefundstatus" id="prevrefundstatus" value="${resultDTO.data.formonload[0].prevrefundstatus}"/></td></tr>
        	   <tr><td>Prev Refund Channel </td><td><input name="prevrefundchannel" id="prevrefundchannel" value="${resultDTO.data.formonload[0].prevrefundchannel}"/></td></tr>
        	   <tr><td>Update Date </td><td><input name="updatedate" id="updatedate" value="${resultDTO.data.formonload[0].updatedate}"/></td></tr>
        	   <tr><td>Updated By </td><td><input name="updatedby" id="updatedby" value="${resultDTO.data.formonload[0].updatedby}"/></td></tr>
        	   <tr><td>Reject Reason </td><td><input name="rejectreason" id="rejectreason" value="${resultDTO.data.formonload[0].rejectreason}"/></td></tr>
        	   <tr><td>Email Notified </td><td><input name="emailnotified" id="emailnotified" value="${resultDTO.data.formonload[0].emailnotified}"/></td></tr>
        	   <tr><td>Saw Txn Ref </td><td><input name="sawtxnref" id="sawtxnref" value="${resultDTO.data.formonload[0].sawtxnref}"/></td></tr>
        	   <tr><td>Insur File Recv Date </td><td><input name="insurfilerecvdate" id="insurfilerecvdate" value="${resultDTO.data.formonload[0].insurfilerecvdate}"/></td></tr>
        	   <tr><td>Saw File Recv Date </td><td><input name="sawfilerecvdate" id="sawfilerecvdate" value="${resultDTO.data.formonload[0].sawfilerecvdate}"/></td></tr>
        	   <tr><td>Mtr File Recv Date </td><td><input name="mtrfilerecvdate" id="mtrfilerecvdate" value="${resultDTO.data.formonload[0].mtrfilerecvdate}"/></td></tr>
        	   <tr><td>Replace Card No Tmp </td><td><input name="replacecardnotmp" id="replacecardnotmp" value="${resultDTO.data.formonload[0].replacecardnotmp}"/></td></tr>
        	   

 
        	 </table>
        	 bulkcmd: <input name="bulkcmd" value="frmgridedit"/>
        	 <button >submit</button>
        	 <button type="button" onclick="ajaxSubmit()">Ajax Submit</button></form>
</body>

</html>
