package com.asiainfo.ocmanager.service.broker.imp;

import java.util.Map;
import java.util.Properties;

import com.asiainfo.ocmanager.persistence.model.Cluster;
import com.asiainfo.ocmanager.service.broker.BrokerInterface;
import com.asiainfo.ocmanager.utils.BrokersIni;

public class GBaseAdapter implements BrokerInterface {

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getImage() {
		Properties props = BrokersIni.getInstance().getBroker(getType());
		return props.getProperty("image");
	}

	@Override
	public Map<String, String> getEnv() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cluster getCluster() {
		// TODO Auto-generated method stub
		return null;
	}

}
