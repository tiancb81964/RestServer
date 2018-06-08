package com.asiainfo.ocmanager.persistence.model;

import java.sql.Timestamp;

/**
 * 
 * @author zhaoyim
 *
 */
public class ApplyBill {

	private String id;
	private String applyUser;
	private String applyDate;
	private String ApplyQuota;
	private String ApplyReason;
	private int status;

	// default construct function is needed by mybatis
	// if not have default construct will hit error
	public ApplyBill() {

	}

	public ApplyBill(String id, String applyUser, String applyDate, String ApplyQuota, String ApplyReason,
			int status) {
		this.id = id;
		this.applyUser = applyUser;
		this.applyDate = applyDate;
		this.ApplyQuota = ApplyQuota;
		this.ApplyReason = ApplyReason;
		this.status = status;
	}

	public ApplyBill(String id, String applyUser, String ApplyQuota, String ApplyReason, int status) {
		this.id = id;
		this.applyUser = applyUser;
		this.ApplyQuota = ApplyQuota;
		this.ApplyReason = ApplyReason;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplyUser() {
		return applyUser;
	}

	public void setApplyUser(String applyUser) {
		this.applyUser = applyUser;
	}

	public String getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}

	public String getApplyQuota() {
		return ApplyQuota;
	}

	public void setApplyQuota(String applyQuota) {
		ApplyQuota = applyQuota;
	}

	public String getApplyReason() {
		return ApplyReason;
	}

	public void setApplyReason(String applyReason) {
		ApplyReason = applyReason;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
