package com.ycs.fe.util;

import java.util.Locale;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.LocaleProvider;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.TextProviderFactory;
import com.ycs.fe.cache.ScreenDetails;
import com.ycs.fe.cache.ScreenDetails.Label;

import com.ycs.fe.exception.FrontendException;

public class LabelFactory implements LocaleProvider{
	private static Logger logger = Logger.getLogger(LabelFactory.class);
	private  TextProvider textProvider;
	
	public static LabelFactory INSTANCE = new LabelFactory();
	
	//Singleton 
	private LabelFactory(){}
	
	/**
	 * @param screenName
	 * @param fieldName "name" attribute of the FieldElement forname in XML
	 * @return label from screen map xml or fieldName if label not found 
	 */
	public String getLabel(String screenName, String fieldName){
		String retlabel = null;
		try {
			ScreenDetails screenDetails = ScreenMapRepo.findScreenDetails(screenName);
			Label label = screenDetails.nameLabelMap.get(fieldName);
			String key;
			String value = null;
			
			if(label == null){
				logger.debug("Label not defined in xml; label"+fieldName + "screen "+screenName);
				retlabel = fieldName;
			}else{
				key = label.key;
				value = label.value;
				retlabel = getTextProvider().getText(key, value);
			}
//			AppCacheManager.putElementInCache(screenName+"_label", "label", labelList);
			
			if(!(retlabel != null && !"".equals(retlabel))){
				//label not found
				retlabel = value;
			}
		} catch (Exception e) {
			logger.debug("Label retrieval failed for label"+fieldName + "screen "+screenName);
			retlabel = fieldName;
		}
			
		return retlabel;
	}
	
	
	private TextProvider getTextProvider()
	{
	  if (this.textProvider == null) {
	    TextProviderFactory tpf = new TextProviderFactory();
	     
	    this.textProvider = tpf.createInstance(super.getClass(), this);
	  }
	  return   this.textProvider;
	}

	@Override
	public Locale getLocale() {
		 ActionContext ctx = ActionContext.getContext();
		  if (ctx != null) {
		    return ctx.getLocale();
		  }
		  System.out.println("Action context not initialized");
		  return null;
	}
}
