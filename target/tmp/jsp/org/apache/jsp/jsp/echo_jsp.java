package org.apache.jsp.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class echo_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_sj_head_jqueryui_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_s_form_theme_id_cssClass_action;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_sj_submit_value_timeout_targets_onErrorTopics_onCompleteTopics_onBeforeTopics_indicator_effectOptions_effectDuration_effect_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_s_textfield_value_name_id_nobody;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_sj_head_jqueryui_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_s_form_theme_id_cssClass_action = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_sj_submit_value_timeout_targets_onErrorTopics_onCompleteTopics_onBeforeTopics_indicator_effectOptions_effectDuration_effect_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_s_textfield_value_name_id_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_sj_head_jqueryui_nobody.release();
    _jspx_tagPool_s_form_theme_id_cssClass_action.release();
    _jspx_tagPool_sj_submit_value_timeout_targets_onErrorTopics_onCompleteTopics_onBeforeTopics_indicator_effectOptions_effectDuration_effect_nobody.release();
    _jspx_tagPool_s_textfield_value_name_id_nobody.release();
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

      out.write("\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("  <head>\r\n");
      out.write("    ");
      if (_jspx_meth_sj_head_0(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("    <script type=\"text/javascript\">\r\n");
      out.write("    $.subscribe('before', function(event,data) {\r\n");
      out.write("      var fData = event.originalEvent.formData;\r\n");
      out.write("         alert('About to submit: \\n\\n' + fData[0].value + ' to target '+event.originalEvent.options.target+' with timeout '+event.originalEvent.options.timeout );\r\n");
      out.write("      var form = event.originalEvent.form[0];\r\n");
      out.write("      if (form.echo.value.length < 2) {\r\n");
      out.write("          alert('Please enter a value with min 2 characters');\r\n");
      out.write("          // Cancel Submit comes with 1.8.0\r\n");
      out.write("          event.originalEvent.options.submit = false;\r\n");
      out.write("      }\r\n");
      out.write("    });\r\n");
      out.write("    $.subscribe('complete', function(event,data) {\r\n");
      out.write("         alert('status: ' + event.originalEvent.status + '\\n\\nresponseText: \\n' + event.originalEvent.request.responseText + \r\n");
      out.write("     '\\n\\nThe output div should have already been updated with the responseText.');\r\n");
      out.write("    });\r\n");
      out.write("    $.subscribe('errorState', function(event,data) {\r\n");
      out.write("        alert('status: ' + event.originalEvent.status + '\\n\\nrequest status: ' +event.originalEvent.request.status);\r\n");
      out.write("    });\r\n");
      out.write("    </script>       \r\n");
      out.write("  </head>\r\n");
      out.write("  <body>\r\n");
      out.write("    ");
      if (_jspx_meth_s_form_0(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("\r\n");
      out.write("    <img id=\"indicator\" src=\"images/indicator.gif\" alt=\"Loading...\" style=\"display:none\"/>    \r\n");
      out.write("    \r\n");
      out.write("    ");
      if (_jspx_meth_s_form_1(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("  </body>\r\n");
      out.write("</html>");
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

  private boolean _jspx_meth_sj_head_0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  sj:head
    com.jgeppert.struts2.jquery.views.jsp.ui.HeadTag _jspx_th_sj_head_0 = (com.jgeppert.struts2.jquery.views.jsp.ui.HeadTag) _jspx_tagPool_sj_head_jqueryui_nobody.get(com.jgeppert.struts2.jquery.views.jsp.ui.HeadTag.class);
    _jspx_th_sj_head_0.setPageContext(_jspx_page_context);
    _jspx_th_sj_head_0.setParent(null);
    _jspx_th_sj_head_0.setJqueryui("true");
    int _jspx_eval_sj_head_0 = _jspx_th_sj_head_0.doStartTag();
    if (_jspx_th_sj_head_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_sj_head_jqueryui_nobody.reuse(_jspx_th_sj_head_0);
      return true;
    }
    _jspx_tagPool_sj_head_jqueryui_nobody.reuse(_jspx_th_sj_head_0);
    return false;
  }

  private boolean _jspx_meth_s_form_0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:form
    org.apache.struts2.views.jsp.ui.FormTag _jspx_th_s_form_0 = (org.apache.struts2.views.jsp.ui.FormTag) _jspx_tagPool_s_form_theme_id_cssClass_action.get(org.apache.struts2.views.jsp.ui.FormTag.class);
    _jspx_th_s_form_0.setPageContext(_jspx_page_context);
    _jspx_th_s_form_0.setParent(null);
    _jspx_th_s_form_0.setId("formevent");
    _jspx_th_s_form_0.setAction("monitoring");
    _jspx_th_s_form_0.setTheme("simple");
    _jspx_th_s_form_0.setCssClass("yform");
    int _jspx_eval_s_form_0 = _jspx_th_s_form_0.doStartTag();
    if (_jspx_eval_s_form_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_s_form_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_s_form_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_s_form_0.doInitBody();
      }
      do {
        out.write("\r\n");
        out.write("        <fieldset>\r\n");
        out.write("            <legend>AJAX Form</legend>\r\n");
        out.write("                <div class=\"type-text\">\r\n");
        out.write("                    <label for=\"echo\">Echo: </label>\r\n");
        out.write("                    ");
        if (_jspx_meth_s_textfield_0((javax.servlet.jsp.tagext.JspTag) _jspx_th_s_form_0, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("                </div>\r\n");
        out.write("                <div class=\"type-button\">\r\n");
        out.write("                    ");
        if (_jspx_meth_sj_submit_0((javax.servlet.jsp.tagext.JspTag) _jspx_th_s_form_0, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("                </div>\r\n");
        out.write("        </fieldset>\r\n");
        out.write("    ");
        int evalDoAfterBody = _jspx_th_s_form_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_s_form_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_s_form_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_s_form_theme_id_cssClass_action.reuse(_jspx_th_s_form_0);
      return true;
    }
    _jspx_tagPool_s_form_theme_id_cssClass_action.reuse(_jspx_th_s_form_0);
    return false;
  }

  private boolean _jspx_meth_s_textfield_0(javax.servlet.jsp.tagext.JspTag _jspx_th_s_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:textfield
    org.apache.struts2.views.jsp.ui.TextFieldTag _jspx_th_s_textfield_0 = (org.apache.struts2.views.jsp.ui.TextFieldTag) _jspx_tagPool_s_textfield_value_name_id_nobody.get(org.apache.struts2.views.jsp.ui.TextFieldTag.class);
    _jspx_th_s_textfield_0.setPageContext(_jspx_page_context);
    _jspx_th_s_textfield_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_form_0);
    _jspx_th_s_textfield_0.setId("echo");
    _jspx_th_s_textfield_0.setName("echo");
    _jspx_th_s_textfield_0.setValue("Hello World!!!");
    int _jspx_eval_s_textfield_0 = _jspx_th_s_textfield_0.doStartTag();
    if (_jspx_th_s_textfield_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_s_textfield_value_name_id_nobody.reuse(_jspx_th_s_textfield_0);
      return true;
    }
    _jspx_tagPool_s_textfield_value_name_id_nobody.reuse(_jspx_th_s_textfield_0);
    return false;
  }

  private boolean _jspx_meth_sj_submit_0(javax.servlet.jsp.tagext.JspTag _jspx_th_s_form_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  sj:submit
    com.jgeppert.struts2.jquery.views.jsp.ui.SubmitTag _jspx_th_sj_submit_0 = (com.jgeppert.struts2.jquery.views.jsp.ui.SubmitTag) _jspx_tagPool_sj_submit_value_timeout_targets_onErrorTopics_onCompleteTopics_onBeforeTopics_indicator_effectOptions_effectDuration_effect_nobody.get(com.jgeppert.struts2.jquery.views.jsp.ui.SubmitTag.class);
    _jspx_th_sj_submit_0.setPageContext(_jspx_page_context);
    _jspx_th_sj_submit_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_form_0);
    _jspx_th_sj_submit_0.setTargets("result");
    _jspx_th_sj_submit_0.setValue("AJAX Submit");
    _jspx_th_sj_submit_0.setTimeout("2500");
    _jspx_th_sj_submit_0.setIndicator("indicator");
    _jspx_th_sj_submit_0.setOnBeforeTopics("before");
    _jspx_th_sj_submit_0.setOnCompleteTopics("complete");
    _jspx_th_sj_submit_0.setOnErrorTopics("errorState");
    _jspx_th_sj_submit_0.setEffect("highlight");
    _jspx_th_sj_submit_0.setEffectOptions("{ color : '#222222' }");
    _jspx_th_sj_submit_0.setEffectDuration("3000");
    int _jspx_eval_sj_submit_0 = _jspx_th_sj_submit_0.doStartTag();
    if (_jspx_th_sj_submit_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_sj_submit_value_timeout_targets_onErrorTopics_onCompleteTopics_onBeforeTopics_indicator_effectOptions_effectDuration_effect_nobody.reuse(_jspx_th_sj_submit_0);
      return true;
    }
    _jspx_tagPool_sj_submit_value_timeout_targets_onErrorTopics_onCompleteTopics_onBeforeTopics_indicator_effectOptions_effectDuration_effect_nobody.reuse(_jspx_th_sj_submit_0);
    return false;
  }

  private boolean _jspx_meth_s_form_1(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:form
    org.apache.struts2.views.jsp.ui.FormTag _jspx_th_s_form_1 = (org.apache.struts2.views.jsp.ui.FormTag) _jspx_tagPool_s_form_theme_id_cssClass_action.get(org.apache.struts2.views.jsp.ui.FormTag.class);
    _jspx_th_s_form_1.setPageContext(_jspx_page_context);
    _jspx_th_s_form_1.setParent(null);
    _jspx_th_s_form_1.setId("formeventerror");
    _jspx_th_s_form_1.setAction("file-does-not-exist.html");
    _jspx_th_s_form_1.setTheme("simple");
    _jspx_th_s_form_1.setCssClass("yform");
    int _jspx_eval_s_form_1 = _jspx_th_s_form_1.doStartTag();
    if (_jspx_eval_s_form_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_s_form_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_s_form_1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_s_form_1.doInitBody();
      }
      do {
        out.write("\r\n");
        out.write("        <fieldset>\r\n");
        out.write("            <legend>AJAX Form with Error Result</legend>\r\n");
        out.write("            <div class=\"type-text\">\r\n");
        out.write("                <label for=\"echo\">Echo: </label>\r\n");
        out.write("                ");
        if (_jspx_meth_s_textfield_1((javax.servlet.jsp.tagext.JspTag) _jspx_th_s_form_1, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("            </div>\r\n");
        out.write("            <div class=\"type-button\">\r\n");
        out.write("                ");
        if (_jspx_meth_sj_submit_1((javax.servlet.jsp.tagext.JspTag) _jspx_th_s_form_1, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("            </div>\r\n");
        out.write("        </fieldset>\r\n");
        out.write("    ");
        int evalDoAfterBody = _jspx_th_s_form_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_s_form_1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
        out = _jspx_page_context.popBody();
    }
    if (_jspx_th_s_form_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_s_form_theme_id_cssClass_action.reuse(_jspx_th_s_form_1);
      return true;
    }
    _jspx_tagPool_s_form_theme_id_cssClass_action.reuse(_jspx_th_s_form_1);
    return false;
  }

  private boolean _jspx_meth_s_textfield_1(javax.servlet.jsp.tagext.JspTag _jspx_th_s_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:textfield
    org.apache.struts2.views.jsp.ui.TextFieldTag _jspx_th_s_textfield_1 = (org.apache.struts2.views.jsp.ui.TextFieldTag) _jspx_tagPool_s_textfield_value_name_id_nobody.get(org.apache.struts2.views.jsp.ui.TextFieldTag.class);
    _jspx_th_s_textfield_1.setPageContext(_jspx_page_context);
    _jspx_th_s_textfield_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_form_1);
    _jspx_th_s_textfield_1.setId("echo");
    _jspx_th_s_textfield_1.setName("echo");
    _jspx_th_s_textfield_1.setValue("Hello World!!!");
    int _jspx_eval_s_textfield_1 = _jspx_th_s_textfield_1.doStartTag();
    if (_jspx_th_s_textfield_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_s_textfield_value_name_id_nobody.reuse(_jspx_th_s_textfield_1);
      return true;
    }
    _jspx_tagPool_s_textfield_value_name_id_nobody.reuse(_jspx_th_s_textfield_1);
    return false;
  }

  private boolean _jspx_meth_sj_submit_1(javax.servlet.jsp.tagext.JspTag _jspx_th_s_form_1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  sj:submit
    com.jgeppert.struts2.jquery.views.jsp.ui.SubmitTag _jspx_th_sj_submit_1 = (com.jgeppert.struts2.jquery.views.jsp.ui.SubmitTag) _jspx_tagPool_sj_submit_value_timeout_targets_onErrorTopics_onCompleteTopics_onBeforeTopics_indicator_effectOptions_effectDuration_effect_nobody.get(com.jgeppert.struts2.jquery.views.jsp.ui.SubmitTag.class);
    _jspx_th_sj_submit_1.setPageContext(_jspx_page_context);
    _jspx_th_sj_submit_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_form_1);
    _jspx_th_sj_submit_1.setTargets("result");
    _jspx_th_sj_submit_1.setValue("AJAX Submit with Error");
    _jspx_th_sj_submit_1.setTimeout("2500");
    _jspx_th_sj_submit_1.setIndicator("indicator");
    _jspx_th_sj_submit_1.setOnBeforeTopics("before");
    _jspx_th_sj_submit_1.setOnCompleteTopics("complete");
    _jspx_th_sj_submit_1.setOnErrorTopics("errorState");
    _jspx_th_sj_submit_1.setEffect("highlight");
    _jspx_th_sj_submit_1.setEffectOptions("{ color : '#222222' }");
    _jspx_th_sj_submit_1.setEffectDuration("3000");
    int _jspx_eval_sj_submit_1 = _jspx_th_sj_submit_1.doStartTag();
    if (_jspx_th_sj_submit_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_sj_submit_value_timeout_targets_onErrorTopics_onCompleteTopics_onBeforeTopics_indicator_effectOptions_effectDuration_effect_nobody.reuse(_jspx_th_sj_submit_1);
      return true;
    }
    _jspx_tagPool_sj_submit_value_timeout_targets_onErrorTopics_onCompleteTopics_onBeforeTopics_indicator_effectOptions_effectDuration_effect_nobody.reuse(_jspx_th_sj_submit_1);
    return false;
  }
}
