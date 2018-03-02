package com.asiainfo.ocmanager.service.broker.plugin;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.asiainfo.ocmanager.service.broker.imp.BaseResourcePeeker;
import com.asiainfo.ocmanager.service.client.v2.ServiceClient;
import com.asiainfo.ocmanager.service.client.v2.ServiceClientInterface;
import com.asiainfo.ocmanager.service.client.v2.ServiceClientPool;
import com.asiainfo.ocmanager.service.client.v2.GPClient;

public class GPResourcePeeker extends BaseResourcePeeker {
	private static final Logger LOG = Logger.getLogger(GPResourcePeeker.class);
	private GPClient client;
	
	public GPResourcePeeker(String serviceName) {
		super(serviceName);
		try {
			ServiceClientInterface cli = ServiceClientPool.getInstance().getClient(serviceName);
			if (!(cli instanceof GPClient)) {
				LOG.error("Client type error for serviceName: " + serviceName + ", error type: " + cli.getClass().getName());
				throw new RuntimeException("Client type error for serviceName: " + serviceName + ", error type: " + cli.getClass().getName());
			}
			client = (GPClient)cli;
		} catch (Exception e) {
			LOG.error("Exception when init peeker: ", e);
			throw new RuntimeException("Exception when init peeker: ", e);
		}
	}
	
	@Override
	protected void setup() {
	}

	@Override
	protected void cleanup() {
	}

	@Override
	protected Long fetchTotalQuota(String resourceType, String dbName) {
		return -1l; // no max volumeSize for greenplum.
	}

	@Override
	protected Long fetchUsedQuota(String resourceType, String dbName) {
		if (!resourceTypes().contains(resourceType)) {
			LOG.error("ResourceType not defined: " + resourceType);
			throw new RuntimeException("ResourceType not defined: " + resourceType);
		}
		try (Statement state = client.getConnection(dbName).createStatement();
				ResultSet rs = state.executeQuery("select sum((pg_relation_size(relid)))from pg_stat_user_tables");) {
			long used = 0;
			while (rs.next()) {
				used = Long.valueOf(rs.getString(1));
			}
			return used;
		} catch (Exception e) {
			LOG.error("Error while fetching resouce usage of greenplum: " + dbName, e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<String> resourceTypes() {
		return Arrays.asList("volumeSize");
	}

	@Override
	public Class<? extends ServiceClient> getClientClass() {
		return GPClient.class;
	}

}
