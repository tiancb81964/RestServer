package com.asiainfo.ocmanager.service.broker.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.persistence.model.Cluster;
import com.asiainfo.ocmanager.rest.bean.CustomEvnBean;
import com.asiainfo.ocmanager.service.broker.BrokerAdapterInterface;
import com.asiainfo.ocmanager.service.broker.imp.DPAdapter;
import com.asiainfo.ocmanager.service.broker.imp.GBaseAdapter;

/**
 * Broker utils
 * @author Ethan
 *
 */
public class BrokerAdaptorUtils {
	private static final Logger LOG = LoggerFactory.getLogger(BrokerAdaptorUtils.class);
	
	public static BrokerAdapterInterface getAdapter(Cluster cluster, List<CustomEvnBean> customEnvs) {
		if (cluster == null) {
			LOG.error("Cluster is null");
			throw new RuntimeException("Cluster is null");
		}
		if (cluster.getCluster_type().equals(new DPAdapter().getType())) {
			return new DPAdapter(cluster, customEnvs);
		}
		else if (cluster.getCluster_type().equals(new GBaseAdapter().getType())) {
			return new GBaseAdapter(cluster, customEnvs);
		}
		else {
			LOG.error("Unknow broker type: " + cluster);
			throw new RuntimeException("Unknow broker type: " + cluster);
		}
	}

}
