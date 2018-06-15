package com.asiainfo.ocmanager.service.broker.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.persistence.model.Cluster;
import com.asiainfo.ocmanager.service.broker.BrokerAdapterInterface;
import com.asiainfo.ocmanager.service.broker.imp.DPAdapter;
import com.asiainfo.ocmanager.service.broker.imp.GBaseAdapter;

/**
 * Broker utils
 * @author Ethan
 *
 */
public class BrokerUtils {
	private static final Logger LOG = LoggerFactory.getLogger(BrokerUtils.class);
	
	public static BrokerAdapterInterface getAdapter(Cluster cluster) {
		if (cluster.getCluster_type().equals(new DPAdapter().getType())) {
			return new DPAdapter(cluster);
		}
		else if (cluster.getCluster_type().equals(new GBaseAdapter().getType())) {
			return new GBaseAdapter();
		}
		else {
			LOG.error("Unknow broker type: " + cluster);
			throw new RuntimeException("Unknow broker type: " + cluster);
		}
	}

}
