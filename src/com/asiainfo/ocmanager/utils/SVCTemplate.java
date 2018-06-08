package com.asiainfo.ocmanager.utils;

import com.asiainfo.ocmanager.service.broker.BrokerInterface;

/**
 * Broker dc template for creating dc
 * 
 * @author Ethan
 *
 */
public class SVCTemplate {
	private BrokerInterface adaptor;
	private String tmplate;

	public SVCTemplate() {
		assemble();
	}

	public SVCTemplate(BrokerInterface adaptor) {
		assemble();
		this.adaptor = adaptor;
	}

	private void assemble() {
		StringBuilder sb = new StringBuilder();
		// TODO:
		tmplate = sb.toString();
	}

	/**
	 * get request body string
	 * @return
	 */
	public String assembleString() {
		//TODO: assemble string using adaptor
		return null;
	}
}
