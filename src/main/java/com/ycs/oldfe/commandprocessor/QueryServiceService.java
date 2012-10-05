package com.ycs.oldfe.commandprocessor;

import java.net.URL;

import javax.xml.namespace.QName;

public class QueryServiceService {

	public QueryServiceService(URL url, QName qName) {
		// TODO Auto-generated constructor stub
	}

	public QueryServiceService() {
		// TODO Auto-generated constructor stub
	}

	public QueryService getQueryServicePort() {
		return new QueryService();
	}

}
