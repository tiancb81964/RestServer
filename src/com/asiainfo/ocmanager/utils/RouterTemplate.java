package com.asiainfo.ocmanager.utils;

import com.asiainfo.ocmanager.service.broker.BrokerInterface;

/**
 * Broker dc template for creating dc
 * 
 * @author Ethan
 *
 */
public class RouterTemplate {
	private String tmplate;

	public RouterTemplate() {
		init();
	}

	private void init() {
		StringBuilder sb = new StringBuilder();
		// TODO:
		tmplate = sb.toString();
	}

	/**
	 * get request body string
	 * @return
	 */
	public String assembleString(BrokerInterface adaptor) {
		//TODO: assemble string using adaptor
		return tmplate;
	}
}
