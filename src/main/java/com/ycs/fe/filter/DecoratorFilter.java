package com.ycs.fe.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.StrutsResultSupport;


import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.ycs.fe.util.MyResponseWrapper;

public final class DecoratorFilter extends StrutsResultSupport implements Filter {
	private Logger logger = Logger.getLogger(this.getClass());
	
	private FilterConfig filterConfig = null;
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		logger.debug("DecoratorFilter: called"+request.getQueryString());
		if (filterConfig == null)
	         return;
		 
		long startTime = System.currentTimeMillis();
		logger.debug("DecoratorFilter:Starting filter.."+request.getParameter("retrieveName"));
		
		MyResponseWrapper wrapper = new MyResponseWrapper(response);
		PrintWriter out = response.getWriter();
		chain.doFilter(request, wrapper); //If this line is commented only then out.println()/dispatcher.forward/include() will work.
		 
		response.setContentType("text/html");
		response.setContentLength(31);
		//logger.debug("DecoratorFilter: resultinghtml="+wrapper.toString());
		StringBuffer strnw = new StringBuffer();
		strnw.append(wrapper.toString());
		 
		strnw.append("<strong>hello from DecoratorFilter</strong>\n<a href='index.jsp'>index.jsp</a>");
		response.setContentLength(strnw.toString().length());
		out.write(strnw.toString());
		out.flush(); //do not flush or else dispatcher.forward/include() statements will fail
//		out.close(); 
		//1 if flush is not commented and close is commented and wrapper is sent then include is working fine & out.write also prints
		//2 if both flush and close are commented and wrapper is sent then forward is working fine
		//3 if flush is commented and close is not commented then include gets called but it doesn't work, out.write works
		//4 if both flush and close are not commented then include gets called but does not work but out.write works
		//result if either(flush, close) of them is done then response is committed to include is called
		//result if the stream is flushed then include does not work
		//result for include to work flush must be called, but if the stream is closed then include does not work
		
//		String finalLocation =  "/second.jsp";
//		RequestDispatcher dispatcher = request.getRequestDispatcher(finalLocation);
//		if (  !response.isCommitted() && (request.getAttribute("javax.servlet.include.servlet_path") == null)) {
//            request.setAttribute("struts.view_uri", finalLocation);
//            request.setAttribute("struts.request_uri", request.getRequestURI());
//            logger.debug("DecoratorFilter:forward: ");
//            dispatcher.forward(request, response); //The out.println().. contents will be lost in this case
//        } else {
//        	logger.debug("DecoratorFilter:include: ");
//        	dispatcher.include(request, response);
//        }
		
		long stopTime = System.currentTimeMillis();
		logger.debug("DecoratorFilter:Time to execute request: " + (stopTime - startTime) + " milliseconds");
		logger.debug("DecoratorFilter:Ending filter"+request.getParameter("retrieveName")+" "+request.getParameter("name"));
		 
		
		
		
	}

	 
	@Override
	protected void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {
		ActionContext ctx = invocation.getInvocationContext();
        HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) ctx.get(ServletActionContext.HTTP_RESPONSE);
        logger.debug("doing filter");
		((HttpServletResponse) response).setStatus(200);
        ((HttpServletResponse) response).setHeader("Location", "somelocation");
        response.getWriter().write("somelocation");
        response.getWriter().close();
        response.sendError(900, "Unable to show problem report: ");
	}


	@Override
	public void destroy() {
		 this.filterConfig = null;
		
	}


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		
	}
}