package com.asiainfo.ocmanager.rest.bean;

import java.util.List;

import com.google.common.collect.Lists;

public class CreateBrokerBean {
	private int status = -1;
	private String message;
	private List<Phase> phases = Lists.newArrayList();

	public CreateBrokerBean(int status, String message) {
		this.status = status;
		this.message = message;
	}
	
	public CreateBrokerBean() {
	}

	public CreateBrokerBean withPhase(Phase phase) {
		this.phases.add(phase);
		return this;
	}

	public static class Phase {
		private String phase;
		private int status;
		private String message;

		public Phase(String phase, int status, String message) {
			super();
			this.phase = phase;
			this.status = status;
			this.message = message;
		}

		public String getPhase() {
			return phase;
		}

		public void setPhase(String phase) {
			this.phase = phase;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Phase> getPhases() {
		return phases;
	}

	public void setPhases(List<Phase> phases) {
		this.phases = phases;
	}
}
