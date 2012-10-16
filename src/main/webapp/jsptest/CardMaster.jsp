<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s"  uri="/struts-tags" %>
<%@taglib prefix="sj"  uri="/struts-jquery-tags" %>
<%--@taglib prefix="sjg"  uri="/struts-jquery-grid-tags" --%>
<html>
<head>
 <title>CardMaster</title>
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
        	var fieldlist = "cardno, cin, cardstatus".split(",");
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
			       editurl: "${pageContext.request.contextPath}/html/simpleform.action?screenName=CardMaster&bulkcmd=grid",
       afterSubmit: function (data){
  							alert("hello world"+Data);
  					},
       caption: "CardMaster"
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
	jQuery.post("${pageContext.request.contextPath}/html/simpleform.action?screenName=CardMaster", 
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
  <table id="listid" ></table>  <div id="pagerid"></div>
  <div id="messagegrid"></div>
  <div id="editmessageresult"></div>
<!--Submit Form -->
<form name="form1" id="form1" method="post" action="${pageContext.request.contextPath}/html/simpleform.action?screenName=CardMaster">
        	 <table>
        	   <tr><td>Card No </td><td><input name="cardno" id="cardno" value="${resultDTO.data.formonload[0].cardno}"/></td></tr>
        	   <tr><td>Cin </td><td><input name="cin" id="cin" value="${resultDTO.data.formonload[0].cin}"/></td></tr>
        	   <tr><td>Card Status </td><td><input name="cardstatus" id="cardstatus" value="${resultDTO.data.formonload[0].cardstatus}"/></td></tr>
        	   


        	   <tr><td>Card No </td><td><s:property value="#resultDTO.data.formonload[0].cardno"  /></td></tr>
        	   <tr><td>Cin </td><td><s:property value="#resultDTO.data.formonload[0].cin"  /></td></tr>
        	   <tr><td>Card Status </td><td><s:property value="#resultDTO.data.formonload[0].cardstatus"  /></td></tr>
        	 </table>
        	 bulkcmd: <input name="bulkcmd" value="frmgridedit"/>
        	 <button >submit</button>
        	 <button type="button" onclick="ajaxSubmit()">Ajax Submit</button></form>
</body>

</html>
