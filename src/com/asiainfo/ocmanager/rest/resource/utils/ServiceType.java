package com.asiainfo.ocmanager.rest.resource.utils;

/**
 * Service type.
 * @author EthanWang
 *
 */
public enum ServiceType {
	hdfs("hdfs", "nameSpaceQuota", "storageSpaceQuota"),
	hbase("hbase", "maximumTablesQuota", "maximumRegionsQuota"),
	mapreduce("mapreduce", "yarnQueueQuota"),
	hive("hive", "storageSpaceQuota", "yarnQueueQuota"),
	kafka("kafka", "topicTTL", "topicQuota", "partitionSize"),
	spark("spark", "yarnQueueQuota"),
	storm("storm"),
	redis("redis");
	
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
