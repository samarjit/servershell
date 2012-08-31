<html>
<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="sj" uri="/struts-jquery-tags" %>
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	<title>Upload Download</title>
	<style>
	form{
	border: 1px solid black;
	}
	#result{
		font-family: courier;
	}
	</style>
	<s:head />
	<sj:head ajaxcache="false"  compressed="false" defaultLoadingText="Loading..." />
	<script type="text/javascript" src="${pageContext.request.contextPath}/struts/js/plugins/jquery.form.min.js" ></script>
	<script type="text/javascript">
	$.fn.serializeObject = function()
	{
	    var o = {};
	    var a = this.serializeArray();
	    $.each(a, function() {
	        if (o[this.name]) {
	            if (!o[this.name].push) {
	                o[this.name] = [o[this.name]];
	            }
	            o[this.name].push(this.value || '');
	        } else {
	            o[this.name] = this.value || '';
	        }
	    });
	    return o;
	};
			
	
		function frmsubmit(){
				$.post("${pageContext.request.contextPath}/monitoring.action",{cmd: $("#cmd1").val()},function (data){
						$("#result").html(data);		
				});
		}
		
		function login(){
			$('#frm0').ajaxSubmit({target: '#result', beforeSubmit:showRequest , success: showResponse });
			/*$.post("${pageContext.request.contextPath}/monitoring.action",$('#frm0').serializeObject(),function (data){
				$("#queryresult").text(data);		
			});*/
		}
		
		$.subscribe("complete", function(event, data){
		//	alert('status: ' + event.originalEvent.status + '\n\nresponseText: \n' + event.originalEvent.request.responseText + 
		  //   '\n\nThe output div should have already been updated with the responseText.');
		});
		
	
		$.subscribe('before', function(event,data) {
		      var fData = event.originalEvent.formData;
		   
		        // alert('About to submit: \n\n' + fData[0].value + ' to target '+event.originalEvent.options.target+' with timeout '+event.originalEvent.options.timeout );
		      var form = event.originalEvent.form[0];
		      try{
		    	  if (form.cmd.value.length < 2) {
		          alert('Please enter a value with min 2 characters');
		          // Cancel Submit comes with 1.8.0
		          event.originalEvent.options.submit = false;
			      }
				}catch(e){
					//alert(e);
				}
			
		    });
		$.subscribe('errorState', function(event,data) {
	        alert('status: ' + event.originalEvent.status + '\n\nrequest status: ' +event.originalEvent.request.status);
	    });
		
		
		
		function showRequest(formData, jqForm, options) { 
		    // formData is an array; here we use $.param to convert it to a string to display it 
		    // but the form plugin does this for you automatically when it submits the data 
		    var queryString = $.param(formData); 
		 
		    // jqForm is a jQuery object encapsulating the form element.  To access the 
		    // DOM element for the form do this: 
		    // var formElement = jqForm[0]; 
		 
		    alert('About to submit: \n\n' + queryString); 
		 
		    // here we could return false to prevent the form from being submitted; 
		    // returning anything other than false will allow the form submit to continue 
		    return true; 
		} 
		 
		// post-submit callback 
		function showResponse(responseText, statusText, xhr, $form)  { 
		    // for normal html responses, the first argument to the success callback 
		    // is the XMLHttpRequest object's responseText property 
		 
		    // if the ajaxForm method was passed an Options Object with the dataType 
		    // property set to 'xml' then the first argument to the success callback 
		    // is the XMLHttpRequest object's responseXML property 
		 
		    // if the ajaxForm method was passed an Options Object with the dataType 
		    // property set to 'json' then the first argument to the success callback 
		    // is the json data object returned by the server 
		 
		    alert('status: ' + statusText + '\n\nresponseText: \n' + responseText + 
		        '\n\nThe output div should have already been updated with the responseText.'); 
		} 
		
	</script>
</head>
<body>

<%@ include file="../index.jsp" %>

<hr/>
<div style="color:red">   
<s:actionerror/>
<s:actionmessage/>
</div>
<hr/>

<s:form action="feupload.action" id="frm0"  enctype="multipart/form-data">
Upload FE Dir: <input type="text" name="feUploadDir" id="feUploadDir" size="50"/>
File: <input type="file" name="fileUpload" id="fileUpload"/>
<input type="submit" value="upload file"/>
<sj:submit formId="frm0" id="pp"   targets="result" value="Upload FE" 
 onBeforeTopics="before"
 onCompleteTopics="complete"
 onErrorTopics="errorState"  
 timeout="2500" 
 indicator="indicator" 
  />
</s:form>
<br/>



<s:form action="fedownload.action" id="frm1">
Download FE filename: <input type="text" name="filename" id="filename" size="50">

<sj:submit formId="frm1" id="pp1"   targets="result" value="Download FE" 
 onBeforeTopics="before"
 onCompleteTopics="complete"
 onErrorTopics="errorState"  
 timeout="2500" 
 indicator="indicator" 
  />
</s:form>


<s:form action="beupload.action" id="frm2"  enctype="multipart/form-data">
Upload BE Dir: <input type="text" name="beUploadDir" id="beUploadDir" size="50"/>
BE File: <input type="file" name="fileUpload" id="fileUpload"/>
<input type="submit" value="upload file"/>
<sj:submit formId="frm2" id="pp2"   targets="result" value="Upload BE" 
 onBeforeTopics="before"
 onCompleteTopics="complete"
 onErrorTopics="errorState"  
 timeout="2500" 
 indicator="indicator" 
  />
</s:form>
<br/>



<s:form action="bedownload.action" id="frm3">
Download BE filename: <input type="text" name="filename" id="filename" size="50">

<sj:submit formId="frm3" id="pp3"   targets="result" value="Download BE" 
 onBeforeTopics="before"
 onCompleteTopics="complete"
 onErrorTopics="errorState"  
 timeout="2500" 
 indicator="indicator" 
  />
</s:form>
 
<s:form action="upload.action" id="frm1">

	<table >
		<tr>
			<td width="100%">
				Query ${pageContext.request.contextPath}
				<textarea name="cmd" id="cmd1" cols="3" rows="2" style="width:100%"></textarea><button type="button" onclick="frmsubmit()">Submit Query</button>
		        
				
			</td>
		</tr>
	
	</table>
	
</s:form>	
Result  <div name="result" id="result" cols="30" rows="10" style="width:100%"></div>
</body>
</html>



