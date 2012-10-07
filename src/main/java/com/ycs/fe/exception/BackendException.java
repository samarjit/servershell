package com.ycs.fe.exception;

public class BackendException extends Exception {

	private static final long serialVersionUID = 1L;

	public BackendException() {
		super();
	}

	public BackendException(String message, Throwable cause) {
		super(message, cause);
	}

	public BackendException(String message) {
		super(message);
	}

	public BackendException(Throwable cause) {
		super(cause);
	}

	 

}
