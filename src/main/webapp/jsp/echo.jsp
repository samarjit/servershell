<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<html>
  <head>
    <sj:head jqueryui="true"/>
    <script type="text/javascript">
    $.subscribe('before', function(event,data) {
      var fData = event.originalEvent.formData;
         alert('About to submit: \n\n' + fData[0].value + ' to target '+event.originalEvent.options.target+' with timeout '+event.originalEvent.options.timeout );
      var form = event.originalEvent.form[0];
      if (form.echo.value.length < 2) {
          alert('Please enter a value with min 2 characters');
          // Cancel Submit comes with 1.8.0
          event.originalEvent.options.submit = false;
      }
    });
    $.subscribe('complete', function(event,data) {
         alert('status: ' + event.originalEvent.status + '\n\nresponseText: \n' + event.originalEvent.request.responseText + 
     '\n\nThe output div should have already been updated with the responseText.');
    });
    $.subscribe('errorState', function(event,data) {
        alert('status: ' + event.originalEvent.status + '\n\nrequest status: ' +event.originalEvent.request.status);
    });
    </script>       
  </head>
  <body>
    <s:form id="formevent" action="monitoring" theme="simple" cssClass="yform">
        <fieldset>
            <legend>AJAX Form</legend>
                <div class="type-text">
                    <label for="echo">Echo: </label>
                    <s:textfield id="echo" name="echo" value="Hello World!!!"/>
                </div>
                <div class="type-button">
                    <sj:submit  targets="result" 
                                        value="AJAX Submit" 
                                        timeout="2500" 
                                        indicator="indicator" 
                                       	onBeforeTopics="before" 
                                        onCompleteTopics="complete" 
                                        onErrorTopics="errorState"
                                        effect="highlight" 
                                        effectOptions="{ color : '#222222' }" 
                                        effectDuration="3000"/>
                </div>
        </fieldset>
    </s:form>

    <img id="indicator" src="images/indicator.gif" alt="Loading..." style="display:none"/>    
    
    <s:form id="formeventerror" action="file-does-not-exist.html" theme="simple" cssClass="yform">
        <fieldset>
            <legend>AJAX Form with Error Result</legend>
            <div class="type-text">
                <label for="echo">Echo: </label>
                <s:textfield id="echo" name="echo" value="Hello World!!!"/>
            </div>
            <div class="type-button">
                <sj:submit      targets="result" 
                                        value="AJAX Submit with Error" 
                                        timeout="2500" 
                                        indicator="indicator" 
                                        onBeforeTopics="before" 
                                        onCompleteTopics="complete" 
                                        onErrorTopics="errorState" 
                                        effect="highlight" 
                                        effectOptions="{ color : '#222222' }" 
                                        effectDuration="3000"/>
            </div>
        </fieldset>
    </s:form>
  </body>
</html>