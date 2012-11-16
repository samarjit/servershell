package com.ycs.fe.actions;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;
import com.ycs.fe.commandprocessor.CommandProcessor;
import com.ycs.fe.dto.InputDTO;
import com.ycs.fe.dto.ResultDTO;
import com.ycs.fe.exception.ValidationException;
import com.ycs.fe.util.FEValidator;

@ParentPackage(value = "default")
public class JavascriptRpc extends ActionSupport {
	 
	private static final long serialVersionUID = -623830420192157346L;


	private Logger logger = Logger.getLogger(JavascriptRpc.class);
	
	
	private String command;
	private String screenName;
	private InputStream inputStream;
	private String panelName;
	private String submitdata = "{}";
    private String data;
	
	public JavascriptRpc() {
		super();
		screenName="ProgramSetup";
		panelName="form1";
		command="selectonload";
	}
	
	
	
	@Action(value="jsrpc",params={"configxml","ProductSetup.xml"},
			results={@Result(name="success",type="stream",params={"contentType","text/html","inputName","inputStream","resultxml","ProductSetup.xml"})}
//	results={@Result(name="success",location="/test.jsp")}
	)
	public String execute(){
		System.out.println("js RPC called with command:"+command+" for screen:"+screenName);
//		BaseBL bl = BusinessLogicFactory.getBusinessLogic(screenName);
//		
//		if(bl != null)
//		  bl.preJsRPCListerner(ActionContext.getContext().getActionInvocation());
		
		String parsedquery = "";
		ResultDTO resDTO = new ResultDTO();
		
		ValueStack stack = ActionContext.getContext().getValueStack();
		try {
				//Element root = ScreenMapRepo.findMapXMLRoot(screenName);
				FEValidator validator = new FEValidator();
				logger.debug("JsonRPC with submitdata="+submitdata);
				Map<String,Object> submitdataObj =  new Gson().fromJson(submitdata, Map.class);
				
				ResultDTO validatorDTO = validator.validate(screenName,submitdataObj);
				if(validatorDTO!=null && validatorDTO.getErrors() != null){
					if(validatorDTO.getErrors().size() >0){
						throw new ValidationException();
					}
				}
		
				InputDTO inputDTO = new InputDTO();
				inputDTO.setData(submitdataObj);
				ActionContext.getContext().getValueStack().getContext().put("inputDTO", inputDTO);
				
				CommandProcessor cmdpr = new CommandProcessor();
				resDTO = cmdpr.commandProcessor(submitdataObj, screenName);  
				
				if(resDTO!=null && resDTO.getErrors() != null){
					if(resDTO.getErrors().size() >0){
						throw new ValidationException();
					}
				}
				
			 
		} catch (ValidationException e) {
			resDTO.addError("error.validation");
		}catch(Exception e){
			resDTO.addError("error.global");
		}

		logger.debug(stack.getContext().get("resultDTO"));
		
//		if(bl !=null)
//		 bl.postJsRPCListerner(ActionContext.getContext().getActionInvocation());
//		
	//		Gson gson = new Gson();
	//		String json1 = gson.toJson(stack.getContext().get("resultDTO"));
	//		logger.debug("Gson result(not sent back to client):"+json1);
//		setResultDTO((ResultDTO)stack.getContext().get("resultDTO"));
		
//		try {
//			OgnlContext context = new OgnlContext();
//			Object expression = Ognl.parseExpression("resultDTO.data.form1[0].txtnewprogname");
//			logger.debug(Ognl.getValue(expression,stack.getContext()));
			logger.debug(stack.findString("#resultDTO.data.form1[0].countryofissue" ));
//		} catch (OgnlException e1) {
//			e1.printStackTrace();
//		}
		
		String jobj =  new Gson().toJson(resDTO);
//		try {
//			jobj.put("data",resDTO.getData());
//			jobj.put("pagination",resDTO.getPagination());
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		String json = jobj.toString();
		logger.debug("Sent back to client:"+json);
		inputStream = new StringBufferInputStream(json);
		 
		return "success";
	}
	public static void main(String[] args) {
	}
	
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getCommand() {
		return command;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setPanelName(String panelName) {
		this.panelName = panelName;
	}
	public String getPanelName() {
		return panelName;
	}
	public void setSubmitdata(String submitdata) {
		this.submitdata = submitdata;
	}
	public String getSubmitdata() {
		return submitdata;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getData() {
		return data;
	}
 
	
	
}
