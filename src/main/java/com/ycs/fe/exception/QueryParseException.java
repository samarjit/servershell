package com.ycs.fe.exception;

public class QueryParseException extends Exception {

	private static final long serialVersionUID = 6176305249277513272L;
	private String queryError;
	
	public QueryParseException() {
		super();
		queryError = "unknown";
	}


	public QueryParseException(String message, Throwable cause) {
		super(message, cause);
		queryError = message;
	}


	public QueryParseException(String strQuery) {
		super(strQuery);
		queryError = strQuery;
	}


	public QueryParseException(Throwable cause) {
		super(cause);
		queryError = cause.getLocalizedMessage();
	}


	
	
	public String getError(){
		return queryError;
	}
}
