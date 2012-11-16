package com.ycs.be.commandprocessor;

import java.util.Map;

import com.ycs.be.crud.JsrpcPojo;
import com.ycs.be.dto.InputDTO;
import com.ycs.be.dto.ResultDTO;

public class CrudCommandProcessor implements BaseCommandProcessor {

	@Override
	public ResultDTO processCommand(String screenName, String querynodeXpath, Map<String,Object> jsonRecord, InputDTO jsonInput, ResultDTO resultDTO) {
		JsrpcPojo rpc = new JsrpcPojo();
		return rpc.selectData(  screenName,   null, querynodeXpath ,   (Map<String,Object>)jsonRecord,   jsonInput,   resultDTO);
	}

}
