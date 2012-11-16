package servershell.be.dto;

import java.util.Map;



public class InputDTO {
	private Map<String,Object> data;

	public void setData(Map<String,Object> data) {
		this.data = data;
	}

	public Map<String,Object> getData() {
		return data;
	}
}
