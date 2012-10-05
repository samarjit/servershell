package com.ycs.oldfe.actions;

import org.apache.log4j.Logger;

import com.ycs.oldfe.commandprocessor.FrontendException;
import com.ycs.oldfe.commandprocessor.ScreenDetails;
import com.ycs.oldfe.commandprocessor.ScreenMapRepo;

public class ReplaceAlias {
	private Logger logger = Logger.getLogger(ReplaceAlias.class);
	
	public  String replaceAlias(String screenName, String fieldName){
		String columnName = null;
		try {
//			Element root = ScreenMapRepo.findMapXMLRoot(screenName);
//			Element elmbyid = (Element) root.selectSingleNode("/root/panels/panel/fields/field/*[@forid='"+fieldName+"']");
//			if(elmbyid!=null){
//				columnName = elmbyid.attributeValue("column");
//			}
			ScreenDetails screenDetails = new ScreenMapRepo().findScreenDetails(screenName);
//			AppCacheManager.putElementInCache(screenName+"_column", "label", labelList);
			columnName = screenDetails.nameColumnMap.get(fieldName);
			if(!(columnName != null && !"".equals(columnName))){
				//label not found
				columnName = fieldName;
			}
		} catch (FrontendException e) {
			logger.debug("Replace Alias failed for fieldname "+fieldName + "screen "+screenName);
			columnName = fieldName;
		}
		return columnName;
	}
}
