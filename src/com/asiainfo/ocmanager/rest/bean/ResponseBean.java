package com.asiainfo.ocmanager.rest.bean;

/**
 * HTTP response bean
 * @author Ethan
 *
 */
public class ResponseBean {
	private Object operand;
	private String msgs;
	
	public ResponseBean(Object operand, String msgs) {
		this.operand = operand;
		this.msgs = msgs;
	}

	public Object getOperand() {
		return operand;
	}

	public String getMsgs() {
		return msgs;
	}

	@Override
	public String toString() {
		return msgs;
	}
}
