package com.asiainfo.ocmanager.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.asiainfo.ocmanager.persistence.model.ApplyBill;

/**
 * 
 * @author zhaoyim
 *
 */
public interface ApplyBillMapper {

	/**
	 * 
	 * @return
	 */
	public List<ApplyBill> selectAllBills();

	/**
	 * 
	 * @param id
	 * @return
	 */
	public ApplyBill selectBillById(@Param("id") String id);

	/**
	 * 
	 * @param applyUser
	 * @return
	 */
	public List<ApplyBill> selectBillsByApplyUser(@Param("applyUser") String applyUser);

	/**
	 * 
	 * @param applyUser
	 * @param applyQuota
	 * @param applyReason
	 * @param status
	 */
	public void insertApplyBill(ApplyBill applyBill);

	/**
	 * 
	 * @param id
	 * @param applyUser
	 * @param applyQuota
	 * @param applyReason
	 * @param status
	 */
	public void updateApplyBillById(ApplyBill applyBill);

	/**
	 * 
	 * @param id
	 * @param status
	 */
	public void updateApplyBillStatusById(@Param("id") String id, @Param("status") int status);

	/**
	 * 
	 * @param id
	 */
	public void deleteApplyBillById(@Param("id") String id);
}
