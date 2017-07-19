package com.asiainfo.ocmanager.rest.utils;

import java.util.ArrayList;
import java.util.List;

import com.asiainfo.ocmanager.persistence.model.Tenant;
import com.asiainfo.ocmanager.rest.utils.TenantTree.TenantTreeNode;

/**
 * Providing commonly used methods for {@link TenantTree}
 * @author EthanWang
 *
 */
public class TenantTreeUtil {
	
	public static Tenant transform(TenantTreeNode innerNode){
		TenantTreeNode parent = innerNode.getParent();
		if (parent == null) {
			return	new Tenant(innerNode.getId(), innerNode.getName(), innerNode.getDescription(), null, innerNode.getLevel());
		}
		return new Tenant(innerNode.getId(), innerNode.getName(), innerNode.getDescription(), parent.getId(), innerNode.getLevel());
	}

	/**
	 * Transform from inner {@link TenantTreeNode} to {@link Tenant}
	 * @param nodes
	 * @return
	 */
	public static List<Tenant> transform(List<TenantTreeNode> nodes){
		List<Tenant> list = new ArrayList<>();
		for(TenantTreeNode node : nodes){
			list.add(transform(node));
		}
		return list;
	}
	
	/**
	 * Construct TenantTree of the specified tenant.
	 * @param tenantID
	 * @return
	 */
	public static TenantTree constructTree(String tenantID){
		return new TenantTree(tenantID);
	}
}
