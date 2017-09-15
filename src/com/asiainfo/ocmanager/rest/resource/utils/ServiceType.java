package com.asiainfo.ocmanager.rest.resource.utils;

/**
 * Service type.
 * @author EthanWang
 *
 */
public enum ServiceType {
	HDFS("hdfs", "nameSpaceQuota", "storageSpaceQuota"),
	HBASE("hbase", "maximumTablesQuota", "maximumRegionsQuota"),
	MAPREDUCE("mapreduce", "yarnQueueQuota"),
	HIVE("hive", "storageSpaceQuota", "yarnQueueQuota"),
	KAFKA("kafka", "topicTTL", "topicQuota", "partitionSize"),
	SPARK("spark", "yarnQueueQuota"),
	STORM("storm"),
	REDIS("redis");
	
	private String name;
	private String[] quota;
	private ServiceType(String name, String... quotakeys ) {
		this.name = name;
		this.quota = quotakeys;
	}
	
	public String serviceType() {
		return this.name;
	}
	
	public String[] quotaKeys() {
		return this.quota;
	}
}
