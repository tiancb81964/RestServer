package com.asiainfo.ocmanager.rest.resource.utils;

/**
 * Service type.
 * @author EthanWang
 *
 */
public enum ServiceType {
	HDFS("HDFS", "nameSpaceQuota", "storageSpaceQuota"),
	HBASE("HBase", "maximumTablesQuota", "maximumRegionsQuota"),
	MAPREDUCE("MapReduce", "yarnQueueQuota"),
	HIVE("Hive", "storageSpaceQuota", "yarnQueueQuota"),
	KAFKA("Kafka", "topicTTL", "topicQuota", "partitionSize"),
	SPARK("Spark", "yarnQueueQuota"),
	STORM("Storm"),
	REDIS("Redis");
	
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
