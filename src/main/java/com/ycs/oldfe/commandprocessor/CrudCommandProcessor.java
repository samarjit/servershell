package com.ycs.oldfe.commandprocessor;

import net.sf.json.JSONObject;
import servershell.be.dto.InputDTO;
import servershell.be.dto.ResultDTO;

public class CrudCommandProcessor implements BaseCommandProcessor {

	@Override
	public ResultDTO processCommand(String screenName, String querynodeXpath, JSONObject jsonRecord, InputDTO jsonInput, ResultDTO resultDTO) {
		JsrpcPojo rpc = new JsrpcPojo();
		return rpc.selectData(  screenName,   null, querynodeXpath ,   (JSONObject)jsonRecord,   jsonInput,   resultDTO);
	}

}
