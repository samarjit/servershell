package com.ycs.fe.exception;

public class ProcessorNotFoundException extends Exception {


	private static final long serialVersionUID = 1L;

	public ProcessorNotFoundException(){
		super();
	}
	
	public ProcessorNotFoundException(String message,Throwable cause){
		super(message,cause);
	}
	
	public ProcessorNotFoundException(String message){
		super(message);
	}
	
	public ProcessorNotFoundException(Throwable cause){
		super(cause);
	}
}
