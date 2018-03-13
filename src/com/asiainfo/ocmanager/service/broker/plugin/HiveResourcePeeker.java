package com.asiainfo.ocmanager.service.broker.plugin;

import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.service.broker.imp.BaseResourcePeeker;
import com.asiainfo.ocmanager.service.client.v2.ServiceClient;
import com.asiainfo.ocmanager.service.client.v2.ServiceClientInterface;
import com.asiainfo.ocmanager.service.client.v2.ServiceClientPool;
import com.asiainfo.ocmanager.service.client.v2.HDFSClient;
import com.asiainfo.ocmanager.service.client.v2.HiveClient;

/**
 * Hive resource monitor.
 * 
 * @author EthanWang
 *
 */
public class HiveResourcePeeker extends BaseResourcePeeker {
	private static final Logger LOG = Logger.getLogger(HiveResourcePeeker.class);
	private FileSystem hdfs;

	public HiveResourcePeeker(String serviceName) {
		super(serviceName);
		if (serviceName.isEmpty()) return;
		try {
			ServiceClientInterface cli = ServiceClientPool.getInstance().getClient(serviceName);
			if (!(cli instanceof HDFSClient)) {
				LOG.error("Client type error: " + cli.getClass().getName() + " for " + this.getClass().getName());
				throw new RuntimeException("Client type error: " + cli.getClass().getName() + " for " + this.getClass().getName());
			}
			hdfs = ((HDFSClient)cli).getFS();
		} catch (Exception e) {
			LOG.error("Exception when init peeker: ", e);
			throw new RuntimeException("Exception when init peeker: ", e);
		}

	}
	
	@Override
	protected void setup() {
	}

	@Override
	protected void cleanup() {
	}

	@Override
	protected Long fetchTotalQuota(String resourceType, String resourceName) {
		try {
			if (resourceType.equals("storageSpaceQuota")) {
				ContentSummary resource = this.hdfs.getContentSummary(new Path(resourceName));
				return resource.getSpaceQuota();
			} else {
				LOG.error("Unknown resourceType: " + resourceType);
				throw new RuntimeException("Unknown resourceType: " + resourceType);
			}
		} catch (Exception e) {
			LOG.error("Error while fetching totalResource: ", e);
			throw new RuntimeException("Error while fetching totalResource: ", e);
		}
	}

	@Override
	protected Long fetchUsedQuota(String resourceType, String resourceName) {
		try {
			if (resourceType.equals("storageSpaceQuota")) {
				ContentSummary resource = this.hdfs.getContentSummary(new Path(resourceName));
				return resource.getSpaceConsumed();
			} else {
				LOG.error("Unknown resourceType: " + resourceType);
				throw new RuntimeException("Unknown resourceType: " + resourceType);
			}
		} catch (Exception e) {
			LOG.error("Error while fetching totalResource: ", e);
			throw new RuntimeException("Error while fetching totalResource: ", e);
		}
	}

	@Override
	public List<String> resourceTypes() {
		return Arrays.asList("storageSpaceQuota");
	}

	@Override
	public Class<? extends ServiceClient> getClientClass() {
		return HiveClient.class;
	}

}
