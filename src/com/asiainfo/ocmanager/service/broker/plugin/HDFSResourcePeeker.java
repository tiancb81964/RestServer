package com.asiainfo.ocmanager.service.broker.plugin;

import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.rest.resource.utils.ServiceType;
import com.asiainfo.ocmanager.service.broker.imp.BaseResourcePeeker;
import com.asiainfo.ocmanager.service.client.HDFSClient;

public class HDFSResourcePeeker extends BaseResourcePeeker {
	private static final Logger LOG = Logger.getLogger(HDFSResourcePeeker.class);
	private FileSystem fs;

	@Override
	protected void setup() {
		this.fs = HDFSClient.getFileSystem();
	}

	@Override
	protected Long fetchTotalQuota(String resourceType, String path) {
		switch (resourceType) {
		case "nameSpaceQuota":
			return totalNamespace(path);

		case "storageSpaceQuota":
			return totalStoragespace(path);

		default:
			LOG.error("Unknown key: " + resourceType + "=" + path);
			throw new RuntimeException("Unknown key: " + resourceType + "=" + path);
		}
	}

	@Override
	protected Long fetchUsedQuota(String resourceType, String pathName) {
		switch (resourceType) {
		case "nameSpaceQuota":
			return usedNamespace(pathName);

		case "storageSpaceQuota":
			return usedStoragespace(pathName);

		default:
			LOG.error("Unknown key: " + resourceType + "=" + pathName);
			throw new RuntimeException("Unknown key: " + "=" + pathName);
		}

	}

	private Long usedStoragespace(String resource) {
		try {
			ContentSummary summary = this.fs.getContentSummary(new Path(resource));
			return summary.getSpaceConsumed();
		} catch (Exception e) {
			LOG.error("Error while get storagespace quota by : " + resource);
			throw new RuntimeException(e);
		}
	}

	private Long usedNamespace(String resource) {
		try {
			ContentSummary summary = this.fs.getContentSummary(new Path(resource));
			return summary.getFileCount();
		} catch (Exception e) {
			LOG.error("Error while get namespace quota by : " + resource);
			throw new RuntimeException(e);
		}
	}

	private Long totalStoragespace(String resourceName) {
		try {
			ContentSummary summary = this.fs.getContentSummary(new Path(resourceName));
			return summary.getSpaceQuota();
		} catch (Exception e) {
			LOG.error("Error while get storagespace quota by : " + resourceName);
			throw new RuntimeException(e);
		}
	}

	private Long totalNamespace(String resourceName) {
		try {
			ContentSummary summary = this.fs.getContentSummary(new Path(resourceName));
			return summary.getQuota();
		} catch (Exception e) {
			LOG.error("Error while get namespace quota by : " + resourceName, e);
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void cleanup() {

	}

	@Override
	public List<String> resourceTypes() {
		return Arrays.asList("nameSpaceQuota", "storageSpaceQuota");
	}

	@Override
	public ServiceType getType() {
		return ServiceType.hdfs;
	}

}
