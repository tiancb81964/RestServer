package com.asiainfo.ocmanager.rest.bean;

import java.util.List;
import java.util.Set;

import com.asiainfo.ocmanager.rest.resource.utils.ServiceType;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Tenant quota of certain tenant, including tenant resouces limitations and its resource usage.
 * @author EthanWang
 *
 */
public class TenantQuota {
	private String id;
	private Set<ServiceQuota> services;

	public String getId() {
		return id;
	}
	
	public Set<ServiceQuota> getServices() {
		return services;
	}
	
	public TenantQuota(String id) {
		this.id = id;
		init();
	}
	
	public void appendLimitation(QuotaBean2 limitations) {
		ServiceQuota service = new ServiceQuota(limitations.getService());
		service.setLimitations(limitations);
		if (services.contains(service)) {
			throw new RuntimeException("Service already exist: " + service);
		}
		this.services.add(service);
	}
	
	private void init() {
		this.services = Sets.newHashSet();
	}
	
	public void appendUsage(ServiceType type, String instanceId, List<QuotaBean> usage) {
		for(ServiceQuota s : services) {
			if (s.getService().equals(type)) {
				s.appendUsage(instanceId, usage);
				return;
			}
		}
	}

	public String toString() {
		return "tenant:" + this.id;
	}
	
	public static class ServiceQuota{
		private ServiceType serviceType;
		private QuotaBean2 limitations;
		private List<ServiceInstanceQuota> instances;
		
		public List<ServiceInstanceQuota> getInstances() {
			return instances;
		}

		public QuotaBean2 getLimitations() {
			return limitations;
		}

		public void setInstances(List<ServiceInstanceQuota> instances) {
			this.instances = instances;
		}

		public void setLimitations(QuotaBean2 limitations) {
			this.limitations = limitations;
		}

		public ServiceQuota (ServiceType type) {
			this.serviceType = type;
			initService();
		}

		private void initService() {
			this.instances = Lists.newArrayList();
		}
		
		public void appendUsage(String instanceId, List<QuotaBean> usage) {
			ServiceInstanceQuota ins = new ServiceInstanceQuota(this.serviceType, instanceId);
			ins.setUsage(usage);
			instances.add(ins);
		}

		public ServiceType getService() {
			return serviceType;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((serviceType == null) ? 0 : serviceType.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ServiceQuota other = (ServiceQuota) obj;
			if (serviceType != other.serviceType)
				return false;
			return true;
		}
	}
}
