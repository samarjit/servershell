package com.ycs.fe.exception;

public class DataTypeException extends Exception {
 
	private static final long serialVersionUID = 7500407388901429257L;
	private String exceptionstring;
	
	
	public DataTypeException() {
		super();
		exceptionstring = "Spelling Exception";
	}


	public DataTypeException(String message, Throwable cause) {
		super(message, cause);
		exceptionstring =  "Spelling Exception:"+message;
	}


	public DataTypeException(Throwable cause) {
		super(cause);
		exceptionstring = "Spelling Exception:"+cause.getMessage();
	}


	public DataTypeException(String exceptionstring) {
		super(exceptionstring);
		this.exceptionstring = exceptionstring;
	}


	public String getError(){
		return exceptionstring;
	}
}
