package com.ycs.fe.exception;

public class SentenceParseException extends Exception {

	private static final long serialVersionUID = 6176305249277513272L;
	private String sentenceError;
	
	public SentenceParseException() {
		super();
		sentenceError = "unknown";
	}


	public SentenceParseException(String message, Throwable cause) {
		super(message, cause);
		sentenceError = message;
	}


	public SentenceParseException(String strQuery) {
		super(strQuery);
		sentenceError = strQuery;
	}


	public SentenceParseException(Throwable cause) {
		super(cause);
		sentenceError = cause.getLocalizedMessage();
	}


	
	
	public String getError(){
		return sentenceError;
	}
}
