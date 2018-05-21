package com.asiainfo.ocmanager.rest.bean.service.instance;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author zhaoyim
 *
 */
public class ServiceInstanceQuotaConst {

	public final static String HDFS = "hdfs";
	public final static String HBASE = "hbase";
	public final static String HIVE = "hive";
	public final static String MAPREDUCE = "mapreduce";
	public final static String SPARK = "spark";
	public final static String KAFKA = "kafka";
	public final static String REDIS = "redis";
	public final static String STORM = "storm";
	public final static String ELASTICSEARCH = "elasticsearch";

	public static final List<String> quotaCheckServices = Arrays.asList("hdfs", "hbase", "hive", "mapreduce", "spark",
			"kafka", "redis", "storm", "elasticsearch");

}
