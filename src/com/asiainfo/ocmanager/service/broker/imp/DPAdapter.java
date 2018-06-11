package com.asiainfo.ocmanager.service.broker.imp;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.asiainfo.ocmanager.persistence.model.Cluster;
import com.asiainfo.ocmanager.service.broker.BrokerInterface;
import com.asiainfo.ocmanager.utils.BrokersIni;
import com.google.common.collect.Maps;

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
		Properties props = BrokersIni.getInstance().getBroker(getType());
		return props.getProperty("image");
	}

	@Override
	public Map<String, String> getEnv() {
		// TODO Auto-generated method stub
		HashMap<String, String> map = Maps.newHashMap();
		map.put("EVN_alpha", "ABCD");
		map.put("EVN_beta", "XYZ$");
		return map;
	}

	@Override
	public Cluster getCluster() {
		return this.cluster;
	}

}
