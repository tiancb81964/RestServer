package com.asiainfo.ocmanager.rest.constant;

import java.util.Arrays;
import java.util.List;

public class Constant {

	/*****************************************************************/
	/********************* data foundry const ************************/
	/*****************************************************************/
	public final static String DATAFOUNDRY_URL = "dataFoundry.url";
	public final static String DATAFOUNDRY_TOKEN = "dataFoundry.token";

	public final static String PROVISIONING = "Provisioning";
	public final static String FAILURE = "Failure";
	public final static String UNBOUND = "Unbound";
	public final static String BOUND = "Bound";

	public final static String UPDATE = "Update";
	public final static String _TODELETE = "_ToDelete";
	public final static String PROJECTMANAGERROLE = "a12a84d0-524a-11e7-9dbb-fa163ed7d0ae";

	/*****************************************************************/
	/************************** monitor const ************************/
	/*****************************************************************/
	
	public final static String TENANT_MONITOR_ENABLE = "tenant.monitor.enable";
	public final static String TENANT_MONITOR_PERIOD = "tenant.monitor.period";
	public final static String TENANT_MONITOR_URL = "tenant.monitor.url";

	/*****************************************************************/
	/************************** adapter const ************************/
	/*****************************************************************/
	// ocdp service name list
	public static final List<String> list = Arrays.asList("hdfs", "hbase", "hive", "mapreduce", "spark", "kafka");
	
	public static final String KRB_ENABLED = "hadoop.krb.enabled";
	public static final String KRB_KEYTAB = "hadoop.krb.keytab";
	public static final String KRB_PRINCIPAL = "hadoop.krb.pricipal";
	public static final String KRB_CONF = "hadoop.krb.conf";

}
