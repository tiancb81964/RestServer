package com.asiainfo.ocmanager.utils;

import com.asiainfo.ocmanager.service.broker.BrokerInterface;

/**
 * Broker dc template for creating dc
 * 
 * @author Ethan
 *
 */
public class DCTemplate {
	private BrokerInterface adaptor;
	private String tmplate;

	public DCTemplate() {
		assemble();
	}

	public DCTemplate(BrokerInterface adaptor) {
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
