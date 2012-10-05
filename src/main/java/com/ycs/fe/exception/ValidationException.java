package com.ycs.fe.exception;

public class ValidationException extends Exception {

	private static final long serialVersionUID = 1L;

	public ValidationException() {
		super("unknown validation error");
	}

	public ValidationException(String message) {
		super(message);
	}

	public ValidationException(Throwable cause) {
		super(cause);
	}

	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}


}
