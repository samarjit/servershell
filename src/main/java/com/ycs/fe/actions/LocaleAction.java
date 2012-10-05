package com.ycs.fe.actions;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.struts2.StrutsConstants;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.inject.Inject;

public class LocaleAction extends ActionSupport {

	private static final long serialVersionUID = -1069207141208713092L;
	private String language;
	private String resourceBundle;
	private String request_locale;
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Inject(StrutsConstants.STRUTS_CUSTOM_I18N_RESOURCES)
	public void setResourceBundle(String resourceBundle) {
		this.resourceBundle = resourceBundle;
	}
//	
	
	//@Action(value="locale",results={@Result(name="success",location = "/login.jsp")})
	public String execute(){
		ActionContext context = ActionContext.getContext();
//		if(language != null && language.trim() != ""){
//			context.setLocale(new Locale(language));
////			Map parameters = new HashMap(); 
////			Locale loc = new Locale(language);
////			parameters.put("request_locale", loc);
////			context.setParameters(parameters);
//		}
//		
		context.getSession().put("resourceBundle", resourceBundle);
	
//		Map parameters = new HashedMap();
//		parameters.put("resourceBundle", resourceBundle);
//		context.setApplication(parameters);
//		Map pp = context.getApplication();
//		String resource = (String) pp.get("resourceBundle");
//		context.getValueStack().set("resourceBundle", resourceBundle);
		
		return SUCCESS;
	}

	public void setRequest_locale(String request_locale) {
		this.request_locale = request_locale;
	}

	public String getRequest_locale() {
		return request_locale;
	}
}
