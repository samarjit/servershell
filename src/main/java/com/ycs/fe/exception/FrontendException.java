package com.ycs.fe.exception;

public class FrontendException extends Exception{

	private static final long serialVersionUID = 1L;

	public FrontendException() {
		super();
	}

	public FrontendException(String message, Throwable cause) {
		super(message, cause);
	}

	public FrontendException(String message) {
		super(message);
	}

	public FrontendException(Throwable cause) {
		super(cause);
	}

}
