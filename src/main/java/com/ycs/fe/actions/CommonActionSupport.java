package com.ycs.fe.actions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import servershell.be.dto.InputDTO;
import servershell.be.dto.PageReturnType;
import servershell.be.dto.ResultDTO;

import com.opensymphony.xwork2.ActionSupport;
import com.ycs.fe.commandprocessor.CommandProcessor;
import com.ycs.fe.commandprocessor.FrontendException;
import com.ycs.fe.commandprocessor.ReturnCommandProcessor;
import com.ycs.fe.commandprocessor.ValidationException;


public class CommonActionSupport extends ActionSupport {
	private Logger logger = Logger.getLogger(getClass());
	

	private static final long serialVersionUID = 1L;
	
	protected String submitdata;
	@SuppressWarnings("unused")
	private String desc;
	protected InputStream inputStream;
	protected String screenName;
	protected Map<String, Object> session;
	@SuppressWarnings("unused")
	private String submitdatatxncode; 
	protected String resultPage;
	protected ResultDTO aresDTO;
	private String  jsrule;
	
	public InputStream getInputStream() {
		return inputStream;
	}
	
	public String commonExecute() throws Exception{
		String resultHtml = "";
		logger.debug("Start common action submitdata:"+submitdata);
		JSONObject jsonRecord =   JSONObject.fromObject(submitdata);
		@SuppressWarnings("unused")
		InputDTO inputDTO = populateInputDTO(jsonRecord);
		ResultDTO resDTO = null;
	
		try {
			
			jsrule = new FEValidator().createJSRule(screenName);
			if(submitdata != null && !"".equals(submitdata)){
			resDTO = validate(jsonRecord);
			resDTO = commandProcessor(jsonRecord, resDTO);
			
			populateActionErrors(resDTO);
				aresDTO  = resDTO;
			JSONObject resjsonResult = JSONObject.fromObject(resDTO);
			resultHtml = resjsonResult.toString();
			}
			
		}catch (Exception e){
			logger.error("unknown exception",e);
//			throw new Exception("error.global");
		}
		System.out.println("result beginning to process");
		PageReturnType pg = setResult(resultHtml, jsonRecord, resDTO);
		logger.error("Result Page:"+ pg.resultName);
		return pg.resultName;
	}

	public String execute() throws Exception{
		return commonExecute();
	}
	
	/**
	 * This method localises error messages. Populates <b>actionErrors</b> and <b>fieldErrors</b>, also modified existing error 
	 * messages so that proper messages goes in json format response.
	 * <ol>
	 * <li>First find the error messages is a key or not, just by searching against existing resource bundle keys getText(error message).</li>
	 * <li>If the messages is a key(full key must match message), then the error Message is replaced by localised message.</li>
	 * <li>Some error messages can be KEY|default_message, in this case getText(KEY) is assumed primary, if this fails use default_message.</li> 
	 * <li>For putting in the fields errors first try to put in field Label if found, or field name</li> 
	 * </ol>
	 * @param resultDTO
	 */
	protected void populateActionErrors(ResultDTO resultDTO) {
		if(resultDTO!=null && resultDTO.getErrors() != null && resultDTO.getErrors().size() >0){
			for (String errorStr : resultDTO.getErrors()) {
				String[] key = errorStr.split("\\|");
				String localisedMsg = getText(key[0]);
				if(localisedMsg == null && key.length >1)localisedMsg = key[1];
				if(localisedMsg == null || "".equals(localisedMsg))localisedMsg = errorStr;
					addActionError(localisedMsg);
				 
			}
		}
		if(resultDTO!=null && resultDTO.getFieldErrors() != null && resultDTO.getFieldErrors().size() >0){
			for (Entry<String, List<String>> fieldErrEntry : resultDTO.getFieldErrors().entrySet()) {
				String fieldName = fieldErrEntry.getKey();
				List<String> errors = fieldErrEntry.getValue();
				for (String errorMessage : errors) {
					String label = LabelFactory.INSTANCE.getLabel(screenName, fieldName);
					String[] arStr = new String[]{label};
					String[] key = errorMessage.split("\\|");
					String localisedMsg = getText(key[0], arStr);
					if(localisedMsg == null && key.length >1)localisedMsg = key[1];
					if(localisedMsg == null || "".equals(localisedMsg))localisedMsg = errorMessage ;
						addFieldError(fieldName, localisedMsg);	
				}
			}
		}
	}

	/**
	 * @param jsonRecord
	 * @return
	 */
	protected InputDTO populateInputDTO(JSONObject jsonRecord) {
		InputDTO inputDTO = new InputDTO();
		inputDTO.setData((JSONObject) jsonRecord);
		return inputDTO;
	}
	
	/**
	 * @depends screenName
	 * @param jsonRecord
	 * @param validationResultDTO if error is there it will not process just return the same resultDTO, suitable to chain 
	 * on top of validation resultDTO
	 * @return resultDTO with error, message, pagination
	 */
	protected ResultDTO commandProcessor(JSONObject jsonRecord, ResultDTO validationResultDTO) {
		if (validationResultDTO != null && validationResultDTO.getErrors() != null && validationResultDTO.getErrors().size() > 0) {
			return validationResultDTO;
		}
		CommandProcessor cmdpr = new CommandProcessor();
		ResultDTO resDTO = cmdpr.commandProcessor(jsonRecord, screenName);
		
		if(resDTO == null) {
			resDTO = new ResultDTO();
			resDTO.addError("error.commandprocessing");
		}
		
		return resDTO;
	}

	/**
	 * @param resultHtml to be converted into inputStream for ajax
	 * @param jsonRecord
	 * @param resDTO TODO
	 * @return nextScreenName *.page, resultName = struts result name to be returned by execute(), resultPage= *.ftl,*.jsp,*.vm 
	 * @throws FrontendException
	 * @throws Exception
	 */
	protected PageReturnType setResult(String resultHtml, JSONObject jsonRecord, ResultDTO resDTO) throws FrontendException, Exception {
		PageReturnType pg = null;
		try{
			pg = new ReturnCommandProcessor().getReturnType(screenName, jsonRecord, resDTO);
			screenName = pg.nextScreenName;
			resultPage = pg.resultPage;
		
			if("ajax".equals(pg.resultName)){
				inputStream = new ByteArrayInputStream(resultHtml.getBytes() );
			}
		}catch(FrontendException e){
			logger.error("error.processingresult",e);
			throw new FrontendException("error.nextpagenotfound",e);
		}catch (Exception e){
			logger.error("error.processingresult",e);
			throw new Exception("error.global",e);
		}
		logger.debug("resultName = "+pg.resultName);
		logger.debug("screenName = "+pg.nextScreenName);
		logger.debug("resultPage = "+pg.resultPage);
		
		return pg;
	}

	/**
	 * It will populate the errors in returned resultDTO. For subsequent validation check for errors in resultDTO
	 * @param jsonRecord
	 * @return
	 * @throws ValidationException
	 */
	protected ResultDTO validate(JSONObject jsonRecord) throws ValidationException {
		FEValidator validator = new FEValidator();
		ResultDTO validatorDTO = validator.validate(screenName, jsonRecord);
//		if (validatorDTO != null && validatorDTO.getErrors() != null && validatorDTO.getErrors().size() > 0) {
//				return false;
//		}
		return validatorDTO;
	}

	public String getSubmitdata() {
		return submitdata;
	}

	public void setSubmitdata(String submitdata) {
		this.submitdata = submitdata;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getResultPage() {
		return resultPage;
	}

	public void setResultPage(String resultPage) {
		this.resultPage = resultPage;
	}
	
	public ResultDTO getAresDTO() {
		return aresDTO;
	}

	public void setAresDTO(ResultDTO aresDTO) {
		this.aresDTO = aresDTO;
	}

	public String getJsrule() {
		return jsrule;
	}

	public void setJsrule(String jsrule) {
		this.jsrule = jsrule;
	}
	
}
