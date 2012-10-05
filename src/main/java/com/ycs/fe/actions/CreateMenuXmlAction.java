package com.ycs.fe.actions;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.SessionAware;
import org.dom4j.Document;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.ycs.fe.dao.UserRoleHelperDAO;
import com.ycs.fe.exception.BackendException;
import com.ycs.fe.exception.FrontendException;
import com.ycs.fe.util.MenuParser;
import com.ycs.user.Role;
import com.ycs.user.RoleRightsMap;

public class CreateMenuXmlAction extends ActionSupport implements SessionAware {
	private static Logger logger = Logger.getLogger(CreateMenuXmlAction.class);
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;

	public String execute() throws FrontendException{
		try{
		session = (Map<String, Object>) ActionContext.getContext().getSession();
		String userid = (String) session.get("userid");
		UserRoleHelperDAO urh = new UserRoleHelperDAO();
		List<Role> listRole = urh.getRolesForUser(userid);
		logger.debug("Role:" + listRole);

		session.put("roles", listRole);

		// get tasklist
		List<RoleRightsMap> roletasklist = new ArrayList<RoleRightsMap>();

		for (Role role : listRole) {
			String roleid = role.getRoleId();
			RoleRightsMap roleAndTask = urh.getTaskList(roleid);
			roletasklist.add(roleAndTask);
		}
		logger.debug("RolesAndTask : " + roletasklist);
		session.put("rolesAndTask", roletasklist);

		// menuXml
		MenuParser menuParser = new MenuParser();
		String menuXml = menuParser.getMenuXml(roletasklist);
		logger.debug("Menu XML : " + menuXml);
		session.put("menuXml", menuXml);
		System.out.println(menuXml);
		} catch (BackendException e) {
			throw new FrontendException("error.menunotfound",e);
		}
		return SUCCESS;
	}

	@Override
	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub

	}

//	
	
}
