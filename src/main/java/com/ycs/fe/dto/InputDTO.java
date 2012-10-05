package com.ycs.fe.dto;

import net.sf.json.JSONObject;

public class InputDTO {
	private JSONObject data;
	private PaginationDTO pagination;
	
	public void setData(JSONObject data) {
		this.data = data;
	}

	public JSONObject getData() {
		return data;
	}

	public PaginationDTO getPagination() {
		return pagination;
	}

	public void setPagination(PaginationDTO pagination) {
		this.pagination = pagination;
	}
	
	
}
