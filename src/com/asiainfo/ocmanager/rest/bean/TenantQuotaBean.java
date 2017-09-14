package com.asiainfo.ocmanager.rest.bean;

import java.util.Map;

import com.asiainfo.ocmanager.persistence.model.Tenant;
import com.asiainfo.ocmanager.rest.resource.utils.TenantQuotaUtils;

/**
 * 
 * @author zhaoyim
 *
 */
public class TenantQuotaBean {

	public String tenantId;
	public String tenantName;

	public long nameSpaceQuotaHdfs;
	public long storageSpaceQuotaHdfs;

	public long maximumTablesQuotaHbase;
	public long maximumRegionsQuotaHbase;

	public long storageSpaceQuotaHive;
	public long yarnQueueQuotaHive;

	public long yarnQueueQuotaMapreduce;

	public long yarnQueueQuotaSpark;

	public long topicTTLKafka;
	public long topicQuotaKafka;
	public long partitionSizeKafka;

	public TenantQuotaBean() {

	}

	public TenantQuotaBean(Tenant tenant) {
		this.tenantId = tenant.getId();
		this.tenantName = tenant.getName();
		this.tenantQuotaParser(tenant.getQuota());
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public long getNameSpaceQuotaHdfs() {
		return nameSpaceQuotaHdfs;
	}

	public void setNameSpaceQuotaHdfs(long nameSpaceQuotaHdfs) {
		this.nameSpaceQuotaHdfs = nameSpaceQuotaHdfs;
	}

	public long getStorageSpaceQuotaHdfs() {
		return storageSpaceQuotaHdfs;
	}

	public void setStorageSpaceQuotaHdfs(long storageSpaceQuotaHdfs) {
		this.storageSpaceQuotaHdfs = storageSpaceQuotaHdfs;
	}

	public long getMaximumTablesQuotaHbase() {
		return maximumTablesQuotaHbase;
	}

	public void setMaximumTablesQuotaHbase(long maximumTablesQuotaHbase) {
		this.maximumTablesQuotaHbase = maximumTablesQuotaHbase;
	}

	public long getMaximumRegionsQuotaHbase() {
		return maximumRegionsQuotaHbase;
	}

	public void setMaximumRegionsQuotaHbase(long maximumRegionsQuotaHbase) {
		this.maximumRegionsQuotaHbase = maximumRegionsQuotaHbase;
	}

	public long getStorageSpaceQuotaHive() {
		return storageSpaceQuotaHive;
	}

	public void setStorageSpaceQuotaHive(long storageSpaceQuotaHive) {
		this.storageSpaceQuotaHive = storageSpaceQuotaHive;
	}

	public long getYarnQueueQuotaHive() {
		return yarnQueueQuotaHive;
	}

	public void setYarnQueueQuotaHive(long yarnQueueQuotaHive) {
		this.yarnQueueQuotaHive = yarnQueueQuotaHive;
	}

	public long getYarnQueueQuotaMapreduce() {
		return yarnQueueQuotaMapreduce;
	}

	public void setYarnQueueQuotaMapreduce(long yarnQueueQuotaMapreduce) {
		this.yarnQueueQuotaMapreduce = yarnQueueQuotaMapreduce;
	}

	public long getYarnQueueQuotaSpark() {
		return yarnQueueQuotaSpark;
	}

	public void setYarnQueueQuotaSpark(long yarnQueueQuotaSpark) {
		this.yarnQueueQuotaSpark = yarnQueueQuotaSpark;
	}

	public long getTopicTTLKafka() {
		return topicTTLKafka;
	}

	public void setTopicTTLKafka(long topicTTLKafka) {
		this.topicTTLKafka = topicTTLKafka;
	}

	public long getTopicQuotaKafka() {
		return topicQuotaKafka;
	}

	public void setTopicQuotaKafka(long topicQuotaKafka) {
		this.topicQuotaKafka = topicQuotaKafka;
	}

	public long getPartitionSizeKafka() {
		return partitionSizeKafka;
	}

	public void setPartitionSizeKafka(long partitionSizeKafka) {
		this.partitionSizeKafka = partitionSizeKafka;
	}

	/**
	 * parser the quota str to tenant quota bean
	 * 
	 * @param quotaStr
	 */
	public void tenantQuotaParser(String quotaStr) {

		Map<String, String> mapHdfs = TenantQuotaUtils.getTenantQuotaByService("hdfs", quotaStr);
		this.nameSpaceQuotaHdfs = mapHdfs.get("nameSpaceQuota") == null ? 0
				: Long.parseLong(mapHdfs.get("nameSpaceQuota"));
		this.storageSpaceQuotaHdfs = mapHdfs.get("storageSpaceQuota") == null ? 0
				: Long.parseLong(mapHdfs.get("storageSpaceQuota"));

		Map<String, String> mapHbase = TenantQuotaUtils.getTenantQuotaByService("hbase", quotaStr);
		this.maximumTablesQuotaHbase = mapHbase.get("maximumTablesQuota") == null ? 0
				: Long.parseLong(mapHbase.get("maximumTablesQuota"));
		this.maximumRegionsQuotaHbase = mapHbase.get("maximumRegionsQuota") == null ? 0
				: Long.parseLong(mapHbase.get("maximumRegionsQuota"));

		Map<String, String> mapHive = TenantQuotaUtils.getTenantQuotaByService("hive", quotaStr);
		this.storageSpaceQuotaHive = mapHive.get("storageSpaceQuota") == null ? 0
				: Long.parseLong(mapHive.get("storageSpaceQuota"));
		this.yarnQueueQuotaHive = mapHive.get("yarnQueueQuota") == null ? 0
				: Long.parseLong(mapHive.get("yarnQueueQuota"));

		Map<String, String> mapMapreduce = TenantQuotaUtils.getTenantQuotaByService("mapreduce", quotaStr);
		this.yarnQueueQuotaMapreduce = mapMapreduce.get("yarnQueueQuota") == null ? 0
				: Long.parseLong(mapMapreduce.get("yarnQueueQuota"));

		Map<String, String> mapSpark = TenantQuotaUtils.getTenantQuotaByService("spark", quotaStr);
		this.yarnQueueQuotaSpark = mapSpark.get("yarnQueueQuota") == null ? 0
				: Long.parseLong(mapSpark.get("yarnQueueQuota"));

		Map<String, String> mapkafka = TenantQuotaUtils.getTenantQuotaByService("kafka", quotaStr);
		this.topicTTLKafka = mapkafka.get("topicTTL") == null ? 0 : Long.parseLong(mapkafka.get("topicTTL"));
		this.topicQuotaKafka = mapkafka.get("topicQuota") == null ? 0 : Long.parseLong(mapkafka.get("topicQuota"));
		this.partitionSizeKafka = mapkafka.get("partitionSize") == null ? 0
				: Long.parseLong(mapkafka.get("partitionSize"));

	}

	/**
	 * add the 2 tenants quota together
	 * 
	 * @param otherTenantQuota
	 */
	public void plusOtherTenantQuota(TenantQuotaBean otherTenantQuota) {
		this.nameSpaceQuotaHdfs = this.nameSpaceQuotaHdfs + otherTenantQuota.getNameSpaceQuotaHdfs();
		this.storageSpaceQuotaHdfs = this.storageSpaceQuotaHdfs + otherTenantQuota.getStorageSpaceQuotaHdfs();
		this.maximumTablesQuotaHbase = this.maximumTablesQuotaHbase + otherTenantQuota.getMaximumTablesQuotaHbase();
		this.maximumRegionsQuotaHbase = this.maximumRegionsQuotaHbase + otherTenantQuota.getMaximumRegionsQuotaHbase();
		this.storageSpaceQuotaHive = this.storageSpaceQuotaHive + otherTenantQuota.getStorageSpaceQuotaHive();
		this.yarnQueueQuotaHive = this.yarnQueueQuotaHive + otherTenantQuota.getYarnQueueQuotaHive();
		this.yarnQueueQuotaMapreduce = this.yarnQueueQuotaMapreduce + otherTenantQuota.getYarnQueueQuotaMapreduce();
		this.yarnQueueQuotaSpark = this.yarnQueueQuotaSpark + otherTenantQuota.getYarnQueueQuotaSpark();
		this.topicTTLKafka = this.topicTTLKafka + otherTenantQuota.getTopicTTLKafka();
		this.topicQuotaKafka = this.topicQuotaKafka + otherTenantQuota.getTopicQuotaKafka();
		this.partitionSizeKafka = this.partitionSizeKafka + otherTenantQuota.getPartitionSizeKafka();
	}

	/**
	 * minus other tenant quota
	 * 
	 * @param otherTenantQuota
	 */
	public void minusOtherTenantQuota(TenantQuotaBean otherTenantQuota) {
		this.nameSpaceQuotaHdfs = this.nameSpaceQuotaHdfs - otherTenantQuota.getNameSpaceQuotaHdfs();
		this.storageSpaceQuotaHdfs = this.storageSpaceQuotaHdfs - otherTenantQuota.getStorageSpaceQuotaHdfs();
		this.maximumTablesQuotaHbase = this.maximumTablesQuotaHbase - otherTenantQuota.getMaximumTablesQuotaHbase();
		this.maximumRegionsQuotaHbase = this.maximumRegionsQuotaHbase - otherTenantQuota.getMaximumRegionsQuotaHbase();
		this.storageSpaceQuotaHive = this.storageSpaceQuotaHive - otherTenantQuota.getStorageSpaceQuotaHive();
		this.yarnQueueQuotaHive = this.yarnQueueQuotaHive - otherTenantQuota.getYarnQueueQuotaHive();
		this.yarnQueueQuotaMapreduce = this.yarnQueueQuotaMapreduce - otherTenantQuota.getYarnQueueQuotaMapreduce();
		this.yarnQueueQuotaSpark = this.yarnQueueQuotaSpark - otherTenantQuota.getYarnQueueQuotaSpark();
		this.topicTTLKafka = this.topicTTLKafka - otherTenantQuota.getTopicTTLKafka();
		this.topicQuotaKafka = this.topicQuotaKafka - otherTenantQuota.getTopicQuotaKafka();
		this.partitionSizeKafka = this.partitionSizeKafka - otherTenantQuota.getPartitionSizeKafka();
	}

	@Override
	public String toString() {
		return "TenantQuotaBean [tenantId: " + tenantId + ", tenantName: " + tenantName + ", nameSpaceQuotaHdfs: "
				+ nameSpaceQuotaHdfs + ", storageSpaceQuotaHdfs: " + storageSpaceQuotaHdfs
				+ ", maximumTablesQuotaHbase: " + maximumTablesQuotaHbase + ", maximumRegionsQuotaHbase: "
				+ maximumRegionsQuotaHbase + ", storageSpaceQuotaHive: " + storageSpaceQuotaHive
				+ ", yarnQueueQuotaHive: " + yarnQueueQuotaHive + ", yarnQueueQuotaMapreduce: "
				+ yarnQueueQuotaMapreduce + ", yarnQueueQuotaSpark: " + yarnQueueQuotaSpark + ", topicTTLKafka: "
				+ topicTTLKafka + ", topicQuotaKafka: " + topicQuotaKafka + ", partitionSizeKafka: "
				+ partitionSizeKafka + "]";
	}

}
