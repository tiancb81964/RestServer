package com.asiainfo.ocmanager.concurrent;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.exception.OcmanagerRuntimeException;
import com.asiainfo.ocmanager.persistence.model.Tenant;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.utils.TenantTree.TenantTreeNode;
import com.asiainfo.ocmanager.utils.TenantTreeUtil;

/**
 * Lockers used to synchronize Tenant operations.
 * 
 * @author EthanWang
 *
 */
public class TenantLockerPool implements LockerPool<Tenant> {
	private static final Map<String, Locker> POOL = new ConcurrentHashMap<>();
	private static final Logger LOG = LoggerFactory.getLogger(TenantLockerPool.class);
	private static TenantLockerPool instance;

	static {
		try {
			init();
		} catch (Exception e) {
			LOG.error("Init TenantLockerPool failed: ", e);
			throw new OcmanagerRuntimeException("Init exception: ", e);
		}
	}

	public static TenantLockerPool getInstance() {
		if (instance == null) {
			synchronized (TenantLockerPool.class) {
				if (instance == null) {
					instance = new TenantLockerPool();
				}
			}
		}
		return instance;
	}

	private static void init() {
		synchronized (POOL) {
			for (TenantTreeNode node : TenantTreeUtil.constructTree(Constant.ROOTTENANTID).listAllNodes()) {
				POOL.put(node.getId(), new Locker());
				LOG.debug("Tenant registered in LockerPool: " + node.getId());
			}
		}
	}

	private TenantLockerPool() {
	}

	public static class NotTopTenantException extends IOException {
		private static final long serialVersionUID = 1L;

		public NotTopTenantException(String msg) {
			super(msg);
		}
	}

	@Override
	public void register(Tenant tenant) {
		if (!POOL.containsKey(tenant.getId())) {
			synchronized (POOL) {
				POOL.put(tenant.getId(), new Locker());
				LOG.info("Tenant registered in LockerPool: " + tenant.getId());
				return;
			}
		}
		LOG.warn("Tenant already registered in LockerPoll: " + tenant.getId());
	}

	@Override
	public void unregister(Tenant tenant) {
		synchronized (POOL) {
			POOL.remove(tenant.getId());
			LOG.info("Tenant unregistered in LockerPool: " + tenant.getId());
		}
	}

	@Override
	public Locker getLocker(Tenant tenant) {
		if (POOL.containsKey(tenant.getId())) {
			return POOL.get(tenant.getId());
		}
		LOG.error("Tenant not registered in LockerPool: " + tenant.getId());
		throw new OcmanagerRuntimeException("Tenant not registered in LockerPool: " + tenant.getId());
	}

	public Locker getLocker(String tenantID) {
		Tenant t = new Tenant();
		t.setId(tenantID);
		return getLocker(t);
	}

}
