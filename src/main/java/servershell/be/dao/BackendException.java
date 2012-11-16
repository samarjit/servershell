package servershell.be.dao;

import java.sql.SQLException;
import java.text.ParseException;

import ognl.OgnlException;

public class BackendException extends Exception {

	public String message;
	
	public BackendException(String string, SQLException e) {
		message = string+ " SQL exception "+e.getMessage()+" "+e.getErrorCode();
	}

	public BackendException(String string, ParseException e) {
		message = string + " parse exception "+e.getMessage();
	}

	public BackendException(String string, ClassNotFoundException e) {
		message = string + " class not found "+e.getMessage();
	}

	public BackendException(String string) {
		message = string;
	}

	public BackendException(String string, BackendException e) {
		message = string +" "+e.getMessage();
	}

//	public BackendException(String string, QueryParseException e) {
//		message = string +" "+e.getMessage();
//	}

	public BackendException(String string, DataTypeException e) {
		message = string +" "+e.getMessage();
	}

	public BackendException(String string, OgnlException e) {
		message = string +" "+e.getMessage();
	}

	public String toString(){
		return super.toString() +" " +message;
	}
	private static final long serialVersionUID = 1L;
	
}
