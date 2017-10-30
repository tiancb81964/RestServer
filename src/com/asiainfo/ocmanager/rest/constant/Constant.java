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
	public final static String _TOBIND = "_ToBind";
	public final static String _TOUNBIND = "_ToUnbind";
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
	public static final List<String> canCreateUserList = Arrays.asList("system.admin", "subsidiary.admin",
			"project.admin");

	public static final String KRB_KEYTAB = "oc.hadoop.krb.keytab";
	public static final String KRB_PRINCIPAL = "oc.hadoop.krb.pricipal";
	public static final String KRB_CONF = "oc.hadoop.krb.conf";

	public static final String ADMIN = "admin";
	public static final String ADMINID = "2ef26018-003d-4b2b-b786-0481d4ee9fa8";
	public static final String ADMINROLEID = "a10170cb-524a-11e7-9dbb-fa163ed7d0ae";

	public static final String ROOTTENANTID = "ae783b6d-655a-11e7-aa10-fa163ed7d0ae";

	public static final String SYSADMIN = "system.admin";
	public static final String TENANTADMIN = "tenant.admin";

	public static final String ZOOKEEPER = "oc.zookeeper.quorum";
	public static final String ZOOKEEPER_PORT = "oc.zookeeper.port";
	public static final String SECURITY_MODULE = "oc.server.security.mudule.class";
	public static final String SECURITY_PERIOD = "oc.server.security.scheduler.period.seconds";
	
	// request utf-8
	public static final String CHARSET_EQUAL_UTF_8 = "charset=utf-8";
	public static final String SEMICOLON = ";";

	/*****************************************************************/
	/******************* authentication const ************************/
	/*****************************************************************/
	public static final List<String> SHIROINIPATHS = Arrays.asList("shiroLdap.ini", "shiroJdbc.ini");
	public static final Long AUTHTOKENTTL = new Long(60 * 60 * 1000);
	public static final String AUTHTYPE = "oc.server.user.source";

	/*****************************************************************/
	/***************** kerberos properties const *********************/
	/*****************************************************************/
	public static final String KERBEROS_USER_PRINCIPAL="kerberos.user.principal";
	public static final String KERBEROS_KEYTAB_LOCATION="kerberos.krb5.location";
	public static final String KERBEROS_ADMIN_PASSWORD="kerberos.admin.password";
	public static final String KERBEROS_KDC_HOST="kerberos.kdc.host";
	public static final String KERBEROS_REALM="kerberos.realm";

}
