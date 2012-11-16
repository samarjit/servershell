package com.ycs.fe.dto;

import java.util.Map;

public class InputDTO {
	private Map<String,Object> data;
	private PaginationDTO pagination;
	
	public void setData(Map<String,Object> data) {
		this.data = data;
	}

	public Map<String,Object> getData() {
		return data;
	}

	public PaginationDTO getPagination() {
		return pagination;
	}

	public void setPagination(PaginationDTO pagination) {
		this.pagination = pagination;
	}
	
	
}
