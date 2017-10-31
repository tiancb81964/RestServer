package com.asiainfo.ocmanager.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.persistence.model.Tenant;
import com.asiainfo.ocmanager.rest.resource.persistence.TenantPersistenceWrapper;

/**
 * Tenants which are constructed in tree-like format, consisting of
 * {@link TenantTreeNode} in structure.
 * 
 * @author EthanWang
 *
 */
public class TenantTree {
	private static final Logger LOG = Logger.getLogger(TenantTree.class);
	private TenantTreeNode originTenant;

	public static void main(String[] args) {
		TenantTree tree = new TenantTree("23");
		System.out.println(">>> tree: " + tree);
		System.out.println(">>> all names: " + tree.listAllNodes());
	}

	public TenantTree(String tenantId) {
		LOG.debug("Going to construct Tenant-Tree by tenantID: " + tenantId);
		Tenant originalTenant = TenantPersistenceWrapper.getTenantById(tenantId);
		if (originalTenant == null) {
			LOG.error("Tenant not exist: " + tenantId);
			throw new RuntimeException("Tenant not exist: " + tenantId);
		}
		this.originTenant = new TenantTreeNode(originalTenant.getId(), originalTenant.getName(),
				originalTenant.getDescription(), originalTenant.getQuota(), null);
		initTree();
		LOG.debug("Tenant-Tree constructed successful: " + this.originTenant);
	}

	/**
	 * Get the orgin tenantID which was passed to Constructor
	 * {@link #TenantsTree(String)}
	 * 
	 * @return
	 */
	public String getOriginTenantID() {
		return this.originTenant.getId();
	}

	/**
	 * List all the nodes of current tree.
	 * 
	 * @return
	 */
	public List<TenantTreeNode> listAllNodes() {
		List<TenantTreeNode> list = new ArrayList<>();
		list.add(this.originTenant);
		listChildrenNames(list, this.originTenant);
		listParentsNames(list, this.originTenant);
		return list;
	}

	/**
	 * List parents of specified tenant, and put into list.
	 * 
	 * @param list
	 * @param originTenant2
	 */
	private void listParentsNames(List<TenantTreeNode> list, TenantTreeNode tenant) {
		TenantTreeNode parent = tenant.getParent();
		if (parent != null) {
			list.add(parent);
			listParentsNames(list, parent);
		}
	}

	/**
	 * List children of specified tenant, and put into list.
	 * 
	 * @param list
	 * @param tenant
	 */
	private void listChildrenNames(List<TenantTreeNode> list, TenantTreeNode tenant) {
		for (TenantTreeNode node : tenant.getChildren()) {
			list.add(node);
			listChildrenNames(list, node);
		}
	}

	private void initTree() {
		initChildren(originTenant);
		initParent(originTenant);
	}
	
	/**
	 * Get parents of origin tenant.
	 */
	public List<TenantTreeNode> listOriginParents() {
		List<TenantTreeNode> list = new ArrayList<>();
		listParentsNames(list, this.originTenant);
		return list;
	}

	/**
	 * initiate parent node of specified node.
	 * 
	 * @param node
	 */
	@SuppressWarnings("serial")
	private void initParent(TenantTreeNode node) {
		Tenant parent = fetchParent(node.getId());
		if (parent != null) {
			TenantTreeNode pNode = new TenantTreeNode(parent.getId(), parent.getName(), parent.getDescription(),
					parent.getQuota(), null);
			pNode.setChildren(new ArrayList<TenantTreeNode>() {
				{
					add(node);
				}
			});
			node.setParent(pNode);
			initParent(pNode);
		}
	}

	/**
	 * Initiate sub tenant nodes under specified node.
	 * 
	 * @param tenantNode
	 */
	private void initChildren(TenantTreeNode tenantNode) {
		List<Tenant> children = fetchChildren(tenantNode.getId());
		if (children != null && !children.isEmpty()) {
			for (Tenant child : children) {
				TenantTreeNode subNode = new TenantTreeNode(child.getId(), child.getName(), child.getDescription(),
						child.getQuota(), tenantNode);
				tenantNode.addChild(subNode);
				initChildren(subNode);
			}
		}
	}

	private Tenant fetchParent(String tenantID) {
		return TenantPersistenceWrapper.getParent(tenantID);
	}

	/**
	 * Fetch children of specified tenant from Mysql.
	 * 
	 * @param tenantID
	 * @return
	 */
	private List<Tenant> fetchChildren(String tenantID) {
		return TenantPersistenceWrapper.getChildrenTenants(tenantID);
	}

	public String toString() {
		return "origin tenant: " + this.originTenant;
	}

	/**
	 * Node of {@link TenantTree}
	 * 
	 * @author EthanWang
	 *
	 */
	public static class TenantTreeNode {
		private String id;
		private String name;
		private String description;
		private String quota;
		private TenantTreeNode parent;
		private List<TenantTreeNode> children = new ArrayList<>();

		public TenantTreeNode(String id, String name, String description, String quota, TenantTreeNode parent) {
			this.id = id;
			this.name = name;
			this.parent = parent;
			this.description = description;
			this.quota = quota;
		}

		public String getDescription() {
			return description;
		}

		/**
		 * Get the level of current node. eg: root node is in level 1 , 2nd layer of
		 * nodes corresponds to level 2, etc.
		 * 
		 * @return
		 */
		public int getLevel() {
			Counter looper = new Counter(0);
			TenantTreeNode p = parent(this, looper);
			while (p != null) {
				p = parent(p, looper);
			}
			return looper.value();
		}

		private TenantTreeNode parent(TenantTreeNode node, Counter loop) {
			loop.increment();
			return node.getParent();
		}

		public void addChild(TenantTreeNode child) {
			this.children.add(child);
		}

		public TenantTreeNode getParent() {
			return parent;
		}

		public boolean parentExist() {
			return (parent != null);
		}

		public void setParent(TenantTreeNode parent) {
			this.parent = parent;
		}

		public void setChildren(List<TenantTreeNode> children) {
			this.children = children;
		}

		public String getName() {
			return name;
		}

		public String getId() {
			return id;
		}

		public String getQuota() {
			return quota;
		}

		public void setQuota(String quota) {
			this.quota = quota;
		}

		public List<TenantTreeNode> getChildren() {
			return children;
		}

		private List<String> childrenToString() {
			List<String> list = new ArrayList<>();
			for (TenantTreeNode child : this.children) {
				list.add(child.getId());
			}
			return list;
		}

		private String parentToString() {
			if (this.parent == null) {
				return null;
			}
			return this.parent.getId();
		}

		public String toString() {
			return "id:" + this.id + "	name:" + this.name + "	parentID:" + this.parentToString() + "	children:"
					+ this.childrenToString();
		}
	}

	private static class Counter {
		private int count;

		public Counter(int initValue) {
			this.count = initValue;
		}

		public Counter increment() {
			this.count++;
			return this;
		}

		public int value() {
			return this.count;
		}
	}

}
