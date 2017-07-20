package com.asiainfo.ocmanager.service.broker.utils;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;

/**
 * Service Resource, including all resources of a service and their usage condition.
 * @author EthanWang
 *
 */
public class Resource {
	// resource type/ resource name/ resource ration
	private Table<String, String, Quota> resources = HashBasedTable.create();
	
	public Resource(Table<String, String, Long> totalQuota){
		for (Cell<String, String, Long> type : totalQuota.cellSet()) {
			this.resources.put(type.getRowKey(), type.getColumnKey(), new Quota(type.getValue()));
		}
	}
	
	/**
	 * Set used value of quota.
	 * @param type
	 * @param value
	 * @return
	 */
	public Resource updateUsed(String type, String name, Long value){
		Quota ration = this.resources.get(type, name);
		if (ration == null) {
			throw new RuntimeException("Resource not exist: " + type + "/" + name);
		}
		ration.updateUsed(value);
		return this;
	}
	
	/**
	 * Set total value of quota.
	 * @param type
	 * @param value
	 * @return
	 */
	public Resource updateTotal(String type, String name, Long value){
		Quota ration = this.resources.get(type, name);
		if (ration == null) {
			throw new RuntimeException("Resource not exist: " + type + "/" + name);
		}
		ration.updateTotal(value);
		return this;
	}
	
	/**
	 * Get total values of all the quotas and copy the data into returned table
	 * @return 
	 */
	public Table<String, String, Long> peekTotals(){
		Table<String, String, Long> map = HashBasedTable.create();
		for(Cell<String, String, Quota> en : this.resources.cellSet()){
			map.put(en.getRowKey(), en.getColumnKey(), en.getValue().getTotal());
		}
		return map;
	}
	
	/**
	 * Get used values of all the quotas and copy the data into returned table
	 * @return 
	 */
	public Table<String, String, Long> peekUsed(){
		Table<String, String, Long> map = HashBasedTable.create();
		for(Cell<String, String, Quota> en : this.resources.cellSet()){
			map.put(en.getRowKey(), en.getColumnKey(), en.getValue().getUsed());
		}
		return map;
	}
	
	/**
	 * Get total value by specified key
	 * @param type
	 * @return
	 */
	public Long getTotal(String type, String name){
		Quota ration = this.resources.get(type, name);
		if (ration == null) {
			throw new RuntimeException("Resource not exist: [" + type + "=" + name + "]");
		}
		return ration.getTotal();
	}
	
	/**
	 * Get used value by sepcified key
	 */
	public Long getUsed(String type, String name){
		Quota ration = this.resources.get(type, name);
		if (ration == null) {
			throw new RuntimeException("Resource not exist: " + type + "/" + name);
		}
		return ration.getUsed();
	}
	
	public String toString(){
		return this.resources.toString();
	}
	
	private class Quota{
		private Long total = -1l;
		private Long used = -1l;
		
		public Quota(Long total){
			this.total = total;
		}
		
		public Quota updateUsed(Long used){
			this.used = used;
			return this;
		}
		
		public Quota updateTotal(Long total){
			this.total = total;
			return this;
		}

		public Long getTotal() {
			return total;
		}

		public Long getUsed() {
			return used;
		}
		
		public String toString(){
			return "(" + this.used + "/" + this.total + ")";
		}
	}
}
