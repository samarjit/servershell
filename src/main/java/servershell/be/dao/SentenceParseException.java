package servershell.be.dao;

import ognl.OgnlException;

public class SentenceParseException extends Exception {

	String message;
	
	public SentenceParseException(String parsedresult) {
		message = parsedresult;
	}

	public SentenceParseException(String string, OgnlException e) {
		message = string + " " + e.getMessage();
	}

	public String toString(){
		return super.toString() +" "+message;
	}
	
	private static final long serialVersionUID = 1L;

}
