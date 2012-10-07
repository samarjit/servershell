package com.ycs.be.commandprocessor;

import net.sf.json.JSONObject;

import com.ycs.be.crud.JsrpcPojo;
import com.ycs.be.dto.InputDTO;
import com.ycs.be.dto.ResultDTO;

public class CrudCommandProcessor implements BaseCommandProcessor {

	@Override
	public ResultDTO processCommand(String screenName, String querynodeXpath, JSONObject jsonRecord, InputDTO jsonInput, ResultDTO resultDTO) {
		JsrpcPojo rpc = new JsrpcPojo();
		return rpc.selectData(  screenName,   null, querynodeXpath ,   (JSONObject)jsonRecord,   jsonInput,   resultDTO);
	}

}
