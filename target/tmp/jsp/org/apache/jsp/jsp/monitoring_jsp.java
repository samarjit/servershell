package org.apache.jsp.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class monitoring_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_sj_head_ajaxcache_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_s_head_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_sj_submit_value_timeout_targets_onErrorTopics_onCompleteTopics_onBeforeTopics_indicator_id_formId_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_s_form_id_action;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_sj_head_ajaxcache_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_s_head_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_sj_submit_value_timeout_targets_onErrorTopics_onCompleteTopics_onBeforeTopics_indicator_id_formId_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_s_form_id_action = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_sj_head_ajaxcache_nobody.release();
    _jspx_tagPool_s_head_nobody.release();
    _jspx_tagPool_sj_submit_value_timeout_targets_onErrorTopics_onCompleteTopics_onBeforeTopics_indicator_id_formId_nobody.release();
    _jspx_tagPool_s_form_id_action.release();
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("<html>\r\n");

response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<head>\r\n");
      out.write("\t<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\" />\r\n");
      out.write("\t<title>Servershell</title>\r\n");
      out.write("\t<style>\r\n");
      out.write("\tform{\r\n");
      out.write("\tborder: 1px solid black;\r\n");
      out.write("\t}\r\n");
      out.write("\t#result{\r\n");
      out.write("\t\tfont-family: courier;\r\n");
      out.write("\t}\r\n");
      out.write("\t</style>\r\n");
      out.write("\t");
      if (_jspx_meth_s_head_0(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("\t");
      if (_jspx_meth_sj_head_0(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("\t<script type=\"text/javascript\" src=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${pageContext.request.contextPath}", java.lang.String.class, (PageContext)_jspx_page_context, null));
      out.write("/struts/js/plugins/jquery.form.min.js\" ></script>\r\n");
      out.write("\t<script type=\"text/javascript\">\r\n");
      out.write("\t$.fn.serializeObject = function()\r\n");
      out.write("\t{\r\n");
      out.write("\t    var o = {};\r\n");
      out.write("\t    var a = this.serializeArray();\r\n");
      out.write("\t    $.each(a, function() {\r\n");
      out.write("\t        if (o[this.name]) {\r\n");
      out.write("\t            if (!o[this.name].push) {\r\n");
      out.write("\t                o[this.name] = [o[this.name]];\r\n");
      out.write("\t            }\r\n");
      out.write("\t            o[this.name].push(this.value || '');\r\n");
      out.write("\t        } else {\r\n");
      out.write("\t            o[this.name] = this.value || '';\r\n");
      out.write("\t        }\r\n");
      out.write("\t    });\r\n");
      out.write("\t    return o;\r\n");
      out.write("\t};\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\r\n");
      out.write("\t\tfunction frmsubmit(){\r\n");
      out.write("\t\t\t\t$.post(\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${pageContext.request.contextPath}", java.lang.String.class, (PageContext)_jspx_page_context, null));
      out.write("/monitoring.action\",{cmd: $(\"#cmd1\").val()},function (data){\r\n");
      out.write("\t\t\t\t\t\t$(\"#result\").html(data);\t\t\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tfunction login(){\r\n");
      out.write("\t\t\t$('#frm0').ajaxSubmit({target: '#result', beforeSubmit:showRequest , success: showResponse });\r\n");
      out.write("\t\t\t/*$.post(\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${pageContext.request.contextPath}", java.lang.String.class, (PageContext)_jspx_page_context, null));
      out.write("/monitoring.action\",$('#frm0').serializeObject(),function (data){\r\n");
      out.write("\t\t\t\t$(\"#queryresult\").text(data);\t\t\r\n");
      out.write("\t\t\t});*/\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t$.subscribe(\"complete\", function(event, data){\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\talert(data)\r\n");
      out.write("\t\t});\r\n");
      out.write("\t\t\r\n");
      out.write("\t\r\n");
      out.write("\t\t$.subscribe('before', function(event,data) {\r\n");
      out.write("\t\t      var fData = event.originalEvent.formData;\r\n");
      out.write("\t\t   \r\n");
      out.write("\t\t        // alert('About to submit: \\n\\n' + fData[0].value + ' to target '+event.originalEvent.options.target+' with timeout '+event.originalEvent.options.timeout );\r\n");
      out.write("\t\t      var form = event.originalEvent.form[0];\r\n");
      out.write("\t\t      try{\r\n");
      out.write("\t\t    \t  if (form.cmd.value.length < 2) {\r\n");
      out.write("\t\t          alert('Please enter a value with min 2 characters');\r\n");
      out.write("\t\t          // Cancel Submit comes with 1.8.0\r\n");
      out.write("\t\t          event.originalEvent.options.submit = false;\r\n");
      out.write("\t\t\t      }\r\n");
      out.write("\t\t\t\t}catch(e){\r\n");
      out.write("\t\t\t\t\t//alert(e);\r\n");
      out.write("\t\t\t\t}\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t    });\r\n");
      out.write("\t\t$.subscribe('errorState', function(event,data) {\r\n");
      out.write("\t        alert('status: ' + event.originalEvent.status + '\\n\\nrequest status: ' +event.originalEvent.request.status);\r\n");
      out.write("\t    });\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tfunction showRequest(formData, jqForm, options) { \r\n");
      out.write("\t\t    // formData is an array; here we use $.param to convert it to a string to display it \r\n");
      out.write("\t\t    // but the form plugin does this for you automatically when it submits the data \r\n");
      out.write("\t\t    var queryString = $.param(formData); \r\n");
      out.write("\t\t \r\n");
      out.write("\t\t    // jqForm is a jQuery object encapsulating the form element.  To access the \r\n");
      out.write("\t\t    // DOM element for the form do this: \r\n");
      out.write("\t\t    // var formElement = jqForm[0]; \r\n");
      out.write("\t\t \r\n");
      out.write("\t\t    alert('About to submit: \\n\\n' + queryString); \r\n");
      out.write("\t\t \r\n");
      out.write("\t\t    // here we could return false to prevent the form from being submitted; \r\n");
      out.write("\t\t    // returning anything other than false will allow the form submit to continue \r\n");
      out.write("\t\t    return true; \r\n");
      out.write("\t\t} \r\n");
      out.write("\t\t \r\n");
      out.write("\t\t// post-submit callback \r\n");
      out.write("\t\tfunction showResponse(responseText, statusText, xhr, $form)  { \r\n");
      out.write("\t\t    // for normal html responses, the first argument to the success callback \r\n");
      out.write("\t\t    // is the XMLHttpRequest object's responseText property \r\n");
      out.write("\t\t \r\n");
      out.write("\t\t    // if the ajaxForm method was passed an Options Object with the dataType \r\n");
      out.write("\t\t    // property set to 'xml' then the first argument to the success callback \r\n");
      out.write("\t\t    // is the XMLHttpRequest object's responseXML property \r\n");
      out.write("\t\t \r\n");
      out.write("\t\t    // if the ajaxForm method was passed an Options Object with the dataType \r\n");
      out.write("\t\t    // property set to 'json' then the first argument to the success callback \r\n");
      out.write("\t\t    // is the json data object returned by the server \r\n");
      out.write("\t\t \r\n");
      out.write("\t\t    alert('status: ' + statusText + '\\n\\nresponseText: \\n' + responseText + \r\n");
      out.write("\t\t        '\\n\\nThe output div should have already been updated with the responseText.'); \r\n");
      out.write("\t\t} \r\n");
      out.write("\t\t\r\n");
      out.write("\t</script>\r\n");
      out.write("</head>\r\n");
      out.write("<body>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("   \r\n");
      out.write("\r\n");
      out.write("\r\n");
      if (_jspx_meth_s_form_0(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write(" \r\n");
      if (_jspx_meth_s_form_1(_jspx_page_context))
        return;
      out.write("\t\r\n");
      out.write("  \r\n");
      out.write("</body>\r\n");
      out.write("</html>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_s_head_0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:head
    org.apache.struts2.views.jsp.ui.HeadTag _jspx_th_s_head_0 = (org.apache.struts2.views.jsp.ui.HeadTag) _jspx_tagPool_s_head_nobody.get(org.apache.struts2.views.jsp.ui.HeadTag.class);
    _jspx_th_s_head_0.setPageContext(_jspx_page_context);
    _jspx_th_s_head_0.setParent(null);
    int _jspx_eval_s_head_0 = _jspx_th_s_head_0.doStartTag();
    if (_jspx_th_s_head_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_s_head_nobody.reuse(_jspx_th_s_head_0);
      return true;
    }
    _jspx_tagPool_s_head_nobody.reuse(_jspx_th_s_head_0);
    return false;
  }

  private boolean _jspx_meth_sj_head_0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  sj:head
    com.jgeppert.struts2.jquery.views.jsp.ui.HeadTag _jspx_th_sj_head_0 = (com.jgeppert.struts2.jquery.views.jsp.ui.HeadTag) _jspx_tagPool_sj_head_ajaxcache_nobody.get(com.jgeppert.struts2.jquery.views.jsp.ui.HeadTag.class);
    _jspx_th_sj_head_0.setPageContext(_jspx_page_context);
    _jspx_th_sj_head_0.setParent(null);
    _jspx_th_sj_head_0.setAjaxcache("false");
    int _jspx_eval_sj_head_0 = _jspx_th_sj_head_0.doStartTag();
    if (_jspx_th_sj_head_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_sj_head_ajaxcache_nobody.reuse(_jspx_th_sj_head_0);
      return true;
    }
    _jspx_tagPool_sj_head_ajaxcache_nobody.reuse(_jspx_th_sj_head_0);
    return false;
  }

  private boolean _jspx_meth_s_form_0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:form
    org.apache.struts2.views.jsp.ui.FormTag _jspx_th_s_form_0 = (org.apache.struts2.views.jsp.ui.FormTag) _jspx_tagPool_s_form_id_action.get(org.apache.struts2.views.jsp.ui.FormTag.class);
    _jspx_th_s_form_0.setPageContext(_jspx_page_context);
    _jspx_th_s_form_0.setParent(null);
    _jspx_th_s_form_0.setAction("monitoring");
    _jspx_th_s_form_0.setId("frm0");
    int _jspx_eval_s_form_0 = _jspx_th_s_form_0.doStartTag();
    if (_jspx_eval_s_form_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_s_form_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_s_form_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_s_form_0.doInitBody();
      }
      do {
        out.write("\r\n");
        out.write("<input type=\"text\" name=\"cmd\" id=\"cmd\" value=\"login\"/>\r\n");
        out.write("User: <input type=\"text\" name=\"name\" id=\"name\">\r\n");
        out.write("Pass: <input type=\"password\" name=\"password\" id=\"password\"/>\r\n");
        out.write("<button type=\"button\" onclick=\"login()\" id=\"btn\">login..</button>\t\r\n");
        if (_jspx_meth_sj_submit_0((javax.servlet.jsp.tagext.JspTag) _jspx_th_s_form_0, _jspx_page_context))
          return true;
        out.write('\r');
        out.write('\n');
        int evalDoAfterBody = _jspx_th_s_form_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_s_form_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_s_form_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_s_form_id_action.reuse(_jspx_th_s_form_0);
      return true;
    }
    _jspx_tagPool_s_form_id_action.reuse(_jspx_th_s_form_0);
    return false;
  }

  private boolean _jspx_meth_sj_submit_0(javax.servlet.jsp.tagext.JspTag _jspx_th_s_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  sj:submit
    com.jgeppert.struts2.jquery.views.jsp.ui.SubmitTag _jspx_th_sj_submit_0 = (com.jgeppert.struts2.jquery.views.jsp.ui.SubmitTag) _jspx_tagPool_sj_submit_value_timeout_targets_onErrorTopics_onCompleteTopics_onBeforeTopics_indicator_id_formId_nobody.get(com.jgeppert.struts2.jquery.views.jsp.ui.SubmitTag.class);
    _jspx_th_sj_submit_0.setPageContext(_jspx_page_context);
    _jspx_th_sj_submit_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_form_0);
    _jspx_th_sj_submit_0.setDynamicAttribute(null, "formId", new String("frm0"));
    _jspx_th_sj_submit_0.setId("pp");
    _jspx_th_sj_submit_0.setTargets("result");
    _jspx_th_sj_submit_0.setValue("Submit Form");
    _jspx_th_sj_submit_0.setOnBeforeTopics("before");
    _jspx_th_sj_submit_0.setOnCompleteTopics("complete");
    _jspx_th_sj_submit_0.setOnErrorTopics("errorState");
    _jspx_th_sj_submit_0.setTimeout("2500");
    _jspx_th_sj_submit_0.setIndicator("indicator");
    int _jspx_eval_sj_submit_0 = _jspx_th_sj_submit_0.doStartTag();
    if (_jspx_th_sj_submit_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_sj_submit_value_timeout_targets_onErrorTopics_onCompleteTopics_onBeforeTopics_indicator_id_formId_nobody.reuse(_jspx_th_sj_submit_0);
      return true;
    }
    _jspx_tagPool_sj_submit_value_timeout_targets_onErrorTopics_onCompleteTopics_onBeforeTopics_indicator_id_formId_nobody.reuse(_jspx_th_sj_submit_0);
    return false;
  }

  private boolean _jspx_meth_s_form_1(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:form
    org.apache.struts2.views.jsp.ui.FormTag _jspx_th_s_form_1 = (org.apache.struts2.views.jsp.ui.FormTag) _jspx_tagPool_s_form_id_action.get(org.apache.struts2.views.jsp.ui.FormTag.class);
    _jspx_th_s_form_1.setPageContext(_jspx_page_context);
    _jspx_th_s_form_1.setParent(null);
    _jspx_th_s_form_1.setAction("monitoring.action");
    _jspx_th_s_form_1.setId("frm1");
    int _jspx_eval_s_form_1 = _jspx_th_s_form_1.doStartTag();
    if (_jspx_eval_s_form_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_s_form_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_s_form_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_s_form_1.doInitBody();
      }
      do {
        out.write("\r\n");
        out.write("\r\n");
        out.write("\t<table style=\"width:100%;height:100%\">\r\n");
        out.write("\t\t<tr>\r\n");
        out.write("\t\t\t<td width=\"100%\">\r\n");
        out.write("\t\t\t\tQuery ");
        out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${pageContext.request.contextPath}", java.lang.String.class, (PageContext)_jspx_page_context, null));
        out.write("\r\n");
        out.write("\t\t\t\t<textarea name=\"cmd\" id=\"cmd1\" cols=\"3\" rows=\"2\" style=\"width:100%\"></textarea><button type=\"button\" onclick=\"frmsubmit()\">Submit Query</button>\r\n");
        out.write("\t\t        Result\r\n");
        out.write("\t\t\t\t<div name=\"result\" id=\"result\" cols=\"30\" rows=\"10\" style=\"width:100%\"></div>\r\n");
        out.write("\t\t\t</td>\r\n");
        out.write("\t\t</tr>\r\n");
        out.write("\t\r\n");
        out.write("\t</table>\r\n");
        out.write("\t\r\n");
        int evalDoAfterBody = _jspx_th_s_form_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_s_form_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_s_form_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_s_form_id_action.reuse(_jspx_th_s_form_1);
      return true;
    }
    _jspx_tagPool_s_form_id_action.reuse(_jspx_th_s_form_1);
    return false;
  }
}
