package com.ycs.be.businesslogic;

import java.util.HashMap;
import java.util.Map;

import com.opensymphony.xwork2.ActionInvocation;
import com.ycs.be.dto.InputDTO;
import com.ycs.be.dto.ResultDTO;

public abstract class BaseBLAdapter implements BaseBL {

	@Override
	public HashMap preJsRPCListerner(ActionInvocation invocation) {
		return null;
	}

	@Override
	public HashMap postJsRPCListerner(ActionInvocation invocation) {
		return null;
	}

	@Override
	public HashMap preCrudListener(ActionInvocation invocation) {
		return null;
	}

	@Override
	public HashMap postCrudListener(ActionInvocation invocation) {
		return null;
	}

	@Override
	public HashMap preWorkflowListener(ActionInvocation invocation) {
		return null;
	}

	@Override
	public HashMap postWorkflowListener(ActionInvocation invocation) {
		return null;
	}

	@Override
	public abstract ResultDTO executeCommand(String screenName, String querynodeXpath, Map<String,Object> jsonRecord, InputDTO inputDTO, ResultDTO resultDTO);
		


	
}
