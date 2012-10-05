package com.ycs.oldfe.actions;

import java.util.Locale;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.LocaleProvider;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.TextProviderFactory;
import com.ycs.oldfe.commandprocessor.FrontendException;
import com.ycs.oldfe.commandprocessor.ScreenDetails;
import com.ycs.oldfe.commandprocessor.ScreenMapRepo;
import com.ycs.oldfe.commandprocessor.ScreenDetails.Label;

public class LabelFactory implements LocaleProvider{
	private Logger logger = Logger.getLogger(LabelFactory.class);
	private  TextProvider textProvider;
	
	public static LabelFactory INSTANCE = new LabelFactory();
	
	//Singleton 
	private LabelFactory(){}
	
	/**
	 * @param screenName
	 * @param fieldName "name" attribute of the FieldElement
	 * @return label from screen map xml or fieldName if label not found 
	 */
	public String getLabel(String screenName, String fieldName){
		String label = null;
		try {
			ScreenDetails screenDetails = new ScreenMapRepo().findScreenDetails(screenName);
			Label label2 = screenDetails.nameLabelMap.get(fieldName);
			String key;
			String value = null;
				key = label2.key;
				value = label2.value;
				label = getTextProvider().getText(key);
			
//			AppCacheManager.putElementInCache(screenName+"_label", "label", labelList);
			
			if(!(label != null && !"".equals(label))){
				//label not found
				label = value;
			}
		} catch (FrontendException e) {
			logger.debug("Label retrieval failed for label"+fieldName + "screen "+screenName);
			label = fieldName;
		}
			
		return label;
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
