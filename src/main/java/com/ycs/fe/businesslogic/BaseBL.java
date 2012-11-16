package com.ycs.fe.businesslogic;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.opensymphony.xwork2.ActionInvocation;
import com.ycs.fe.dto.InputDTO;
import com.ycs.fe.dto.ResultDTO;

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
ResultDTO executeCommand(String screenName, String querynodeXpath, Map<String,Object> jsonRecord, InputDTO inputDTO, ResultDTO resultDTO);
}
