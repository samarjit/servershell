package com.ycs.be.dto;

import net.sf.json.JSONObject;

public class InputDTO {
	private JSONObject data;

	public void setData(JSONObject data) {
		this.data = data;
	}

	public JSONObject getData() {
		return data;
	}
}
