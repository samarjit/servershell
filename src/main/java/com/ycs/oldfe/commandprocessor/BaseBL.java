package com.ycs.oldfe.commandprocessor;

import java.io.Serializable;
import java.util.HashMap;

import net.sf.json.JSONObject;
import servershell.be.dto.InputDTO;
import servershell.be.dto.ResultDTO;

import com.opensymphony.xwork2.ActionInvocation;

/**
 * This class must be thread safe. Classes implementing this must be 
 * @author Samarjit
 *
 */
public interface BaseBL extends Serializable {
	
HashMap preJsRPCListerner(ActionInvocation invocation);
HashMap postJsRPCListerner(ActionInvocation invocation);
HashMap preCrudListener(ActionInvocation invocation);
HashMap postCrudListener(ActionInvocation invocation);
HashMap preWorkflowListener(ActionInvocation invocation);
HashMap postWorkflowListener(ActionInvocation invocation);
ResultDTO executeCommand(String screenName, String querynodeXpath, JSONObject jsonRecord, InputDTO inputDTO, ResultDTO resultDTO);
}
