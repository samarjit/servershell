package com.ycs.fe.dto;

public class PagingFilterRule {
	private String field;
	private String op;
	private String data;
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "PagingFilterRule [field=" + field + ", op=" + op + ", data=" + data + "]";
	} 
	
	
}
