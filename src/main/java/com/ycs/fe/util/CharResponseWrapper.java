package com.ycs.fe.util;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class CharResponseWrapper extends HttpServletResponseWrapper {

	static class ServletOutputStreamWrapper extends ServletOutputStream {
		CharArrayWriter writer;

		ServletOutputStreamWrapper(CharArrayWriter aWriter) {
			writer = aWriter;
		}

		public void write(int aByte) {
			writer.write(aByte);
		}
	}

	// private CharArrayWriter output;

	CharArrayWriter strout;
	PrintWriter writer;
	ServletOutputStream soswrapper;

	private boolean getWriterCalled;

	private boolean getOutputStreamCalled;

	public CharResponseWrapper(HttpServletResponse response) {
		super(response);

		strout = new CharArrayWriter();
		// sout = new ServletOutputStreamWrapper(strout);
		// output = new CharArrayWriter();
		// writer = new PrintWriter(strout);
	}

	public String getData() {
		writer.flush();
		return strout.toString();
	}

	public String toString() {
		return strout.toString();
	}

	public ServletOutputStream getOutputStream() {
		if (getWriterCalled) {
			throw new IllegalStateException("getWriter already called");
		}

		getOutputStreamCalled = true;
		if (soswrapper == null)
			soswrapper = new ServletOutputStreamWrapper(strout);
		return soswrapper;
	}

	public PrintWriter getWriter() {
		if (writer != null) {
			return writer;
		}
		if (getOutputStreamCalled) {
			throw new IllegalStateException("getOutputStream already called");
		}
		getWriterCalled = true;
		writer = new PrintWriter(strout);

		return writer;
	}
}