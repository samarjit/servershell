package com.ycs.fe.commandprocessor;

import java.util.Map;

import org.apache.log4j.Logger;

import com.ycs.fe.businesslogic.BaseBL;
import com.ycs.fe.cache.BusinessLogicFactory;
import com.ycs.fe.dto.InputDTO;
import com.ycs.fe.dto.ResultDTO;


public class BlCommandProcessor implements BaseCommandProcessor {
	private static Logger logger = Logger.getLogger(BlCommandProcessor.class);

	@Override
	public ResultDTO processCommand(String screenName, String querynodeXpath, Map<String,Object> jsonRecord, InputDTO inputDTO, ResultDTO resultDTO) {
		logger.debug("Currently processing record:");
		BaseBL bl = BusinessLogicFactory.getBusinessLogic(screenName);
		ResultDTO resDTO = bl.executeCommand(screenName, querynodeXpath, jsonRecord, inputDTO, resultDTO /*previous result*/);
		return resDTO;
	}

}
