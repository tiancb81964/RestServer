package com.asiainfo.ocmanager.rest.bean;

import java.util.Arrays;
import java.util.List;

public class TenantQuotaResponse {
	private String tenantId;
	private ServiceInstanceQuotaResponse[] instances;

	public TenantQuotaResponse(String tenantId) {
		this.tenantId = tenantId;
	}
	
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public ServiceInstanceQuotaResponse[] getInstances() {
		return instances;
	}

	public void setInstances(ServiceInstanceQuotaResponse[] instances) {
		this.instances = instances;
	}
	
	public void addInstance(ServiceInstanceQuotaResponse ins) {
		List<ServiceInstanceQuotaResponse> list = Arrays.asList(instances);
		list.add(ins);
		this.instances = list.toArray(new ServiceInstanceQuotaResponse[list.size()]);
	}

	// Quota response of a certain instance
	public static class ServiceInstanceQuotaResponse{
		private String instanceId;
		private List<QuotaBean> items;
		
		public ServiceInstanceQuotaResponse(String instanceId) {
			this.instanceId = instanceId;
		}
		
		public String getInstanceId() {
			return instanceId;
		}
		public void setInstanceId(String instanceId) {
			this.instanceId = instanceId;
		}
		public List<QuotaBean> getItems() {
			return items;
		}
		public void setItems(List<QuotaBean> items) {
			this.items = items;
		}
	}
}
