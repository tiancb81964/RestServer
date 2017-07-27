package com.asiainfo.ocmanager.service.broker.plugin;

import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.service.broker.imp.BaseResourcePeeker;
import com.asiainfo.ocmanager.service.client.HDFSClient;
import com.asiainfo.ocmanager.service.client.YarnClient;
import com.google.gson.JsonObject;

/**
 * Hive resource monitor.
 * 
 * @author EthanWang
 *
 */
public class HiveResourcePeeker extends BaseResourcePeeker {
	private static final Logger LOG = Logger.getLogger(HiveResourcePeeker.class);
	private YarnClient yarn;
	private FileSystem hdfs;

	@Override
	protected void setup() {
		this.hdfs = HDFSClient.getFileSystem();
		this.yarn = YarnClient.getInstance();
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
			} else if (resourceType.equals("yarnQueueQuota")) {
				double capacity = this.yarn.fetchQueueInfoByName(resourceName).get("absoluteCapacity").getAsDouble()
						/ 100;
				double total = Double.valueOf(this.yarn.fetchMetircs().get("totalMB"));
				return new Double(capacity * total).longValue();
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
			} else if (resourceType.equals("yarnQueueQuota")) {
				JsonObject resource = this.yarn.fetchQueueInfoByName(resourceName);
				long used = resource.getAsJsonObject("resourcesUsed").getAsJsonPrimitive("memory").getAsLong();
				return used;
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
		return Arrays.asList("storageSpaceQuota", "yarnQueueQuota");
	}

}
