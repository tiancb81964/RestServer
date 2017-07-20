package com.asiainfo.ocmanager.service.broker.plugins;

import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.service.broker.imp.BaseResourcePeeker;
import com.asiainfo.ocmanager.service.client.HDFSClient;

public class HDFSResourcePeeker extends BaseResourcePeeker {
	private static final Logger LOG = Logger.getLogger(HDFSResourcePeeker.class);
	private static final String NAMESPACE_QUOTA = "nameSpaceQuota";
	private static final String STORAGESPACE_QUOTA = "storageSpaceQuota";
	private FileSystem fs ;

	@Override
	protected void init() {
		this.fs = HDFSClient.getFileSystem();
	}
	
	@Override
	protected Long fetchTotalQuota(String resourceType, String resourceName) {
		switch (resourceType) {
		case NAMESPACE_QUOTA:
			return totalNamespace(resourceName);
			
		case STORAGESPACE_QUOTA:
			return totalStoragespace(resourceName);

		default:
			LOG.error("Unknown key: " + resourceType + "=" + resourceName);
			throw new RuntimeException("Unknown key: " + "=" + resourceName);
		}
	}
	
	@Override
	protected Long fetchUsedQuota(String resourceType, String resourceName) {
		switch (resourceType) {
		case NAMESPACE_QUOTA:
			return usedNamespace(resourceName);
			
		case STORAGESPACE_QUOTA:
			return usedStoragespace(resourceName);

		default:
			LOG.error("Unknown key: " + resourceType + "=" + resourceName);
			throw new RuntimeException("Unknown key: " + "=" + resourceName);
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

	private Long totalNamespace(String resourceName){
		try {
			ContentSummary summary = this.fs.getContentSummary(new Path(resourceName));
			return summary.getQuota();
		} catch (Exception e) {
			LOG.error("Error while get namespace quota by : " + resourceName);
			throw new RuntimeException(e);
		}
	}

}
