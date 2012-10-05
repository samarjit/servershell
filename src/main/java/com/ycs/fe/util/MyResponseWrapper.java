package com.ycs.fe.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;


public class MyResponseWrapper extends HttpServletResponseWrapper {
private StringWriter strw;
	public MyResponseWrapper(HttpServletResponse response) {
		super(response);
		strw = new StringWriter();
	}
	public String toString(){
		return strw.toString();
	}
	public PrintWriter getWriter(){
		return new PrintWriter(strw);
	}
}
