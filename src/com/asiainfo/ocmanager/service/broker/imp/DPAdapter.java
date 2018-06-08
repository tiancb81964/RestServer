package com.asiainfo.ocmanager.service.broker.imp;

import java.util.Map;

import com.asiainfo.ocmanager.persistence.model.Cluster;
import com.asiainfo.ocmanager.service.broker.BrokerInterface;

public class DPAdapter implements BrokerInterface {
	private Cluster cluster;
	
	public DPAdapter(Cluster cluster) {
		this.cluster = cluster;
	}
	
	public DPAdapter() {
	}

	@Override
	public String getType() {
		return "hadoop";
	}

	@Override
	public String getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getEnv() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getConfigFileName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cluster getCluster() {
		return this.cluster;
	}

}
