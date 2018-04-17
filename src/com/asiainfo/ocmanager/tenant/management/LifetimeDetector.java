package com.asiainfo.ocmanager.tenant.management;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.persistence.model.Tenant;
import com.asiainfo.ocmanager.rest.constant.Constant;
import com.asiainfo.ocmanager.rest.resource.persistence.TenantPersistenceWrapper;
import com.asiainfo.ocmanager.utils.ServerConfiguration;
import com.google.common.collect.Lists;

/**
 * Detect out-of-date tenants
 * 
 * @author Ethan
 *
 */
public class LifetimeDetector {
	private static final Logger LOG = LoggerFactory.getLogger(LifetimeDetector.class);
	private static LifetimeDetector instance;
	private TenantSync tenants;
	private BlockingQueue<TenantEvent> dueTenants;
	private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

	public static LifetimeDetector getInstance() {
		if (instance == null) {
			synchronized (LifetimeDetector.class) {
				if (instance == null) {
					instance = new LifetimeDetector();
				}
			}
		}
		return instance;
	}

	public BlockingQueue<TenantEvent> getDueTenants() {
		return dueTenants;
	}

	private LifetimeDetector() {
		dueTenants = new ArrayBlockingQueue<>(200);
		tenants = new TenantSync();
		updateTenants();
		String period = ServerConfiguration.getConf().getProperty(Constant.LIFETIME_PERIOD, "86400");
		service.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				Thread.currentThread().setName("Tenant-LifetimeDetector-Thread");
				try {
					LOG.debug("Starting sync up tenants: " + tenants.getTenants());
					tenants.syncTenants();
					LOG.debug("Sync up tenants finished: " + tenants.getTenants());
					tenants.getTenants().forEach(t -> {
						Calendar dueTime;
						try {
							dueTime = tenantDueTime(t);
						} catch (ParseException e) {
							LOG.error("Exception while parse tenant dueTime: " + t.getDueTime() + ", tenant: " + t.getName(), e);
							return;
						}
						Calendar nowTime = dbNowTime();
						long diff = dueTime.getTimeInMillis() - nowTime.getTimeInMillis();
						if (diff <= 0) {
							LOG.info("Tenant [{}] exceeded lifetime [{}]", t.getId() + "->" + t.getName(), dueTime.getTime());
							dueTenants.add(TenantEvent.create(t, LifetimeFlag.DUE));
						} else if (diff > 0 && diff <= 604800000) {
							// 604800000 mills = 7 days
							LOG.info("Tenant [{}] is about to exceed lifetime [{}] within 7 days",
									 t.getId() + "->" + t.getName(), dueTime.getTime());
							dueTenants.add(TenantEvent.create(t, LifetimeFlag.ABT_DUE));
						}
					});
				} catch (Exception e) {
					LOG.error("Exception during running Tenant-LifetimeDetector-Thread: ", e);
				}
			}

			private Calendar dbNowTime() {
				return Calendar.getInstance();
			}

			private Calendar tenantDueTime(Tenant t) throws ParseException {
				Calendar c = Calendar.getInstance();
				if (t.getDueTime() == null) {
					// no lifetime limit for current tenant
					c.set(2099, 11, 31);
					return c;
				}
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date due = null;
				due = df.parse(t.getDueTime());
				c.setTime(due);
				return c;
			}
		}, 3, Integer.valueOf(period), TimeUnit.SECONDS);
		LOG.info("Period of tenant lifetime manager is set to: " + period + " seconds");
	}

	private void updateTenants() {
		tenants.syncTenants();
	}

	/**
	 * To sync up db tenant.
	 * 
	 * @author Ethan
	 *
	 */
	private static class TenantSync {
		private static final Logger LOG = LoggerFactory.getLogger(TenantSync.class);
		private java.util.List<Tenant> tenants = Lists.newArrayList();

		public TenantSync() {
			loadTenants();
		}

		private void loadTenants() {
			List<Tenant> all = TenantPersistenceWrapper.getAllTenants();
			this.tenants.addAll(all);
			if (LOG.isDebugEnabled()) {
				LOG.debug("Sync up tenants finished, new tenants: " + tenants);
			}
		}

		public void syncTenants() {
			if (LOG.isDebugEnabled()) {
				LOG.debug("To sync up tenants, old tenants: " + tenants);
			}
			tenants.clear();
			loadTenants();
		}

		public List<Tenant> getTenants() {
			return tenants;
		}

	}

	public static class TenantEvent {
		private Tenant tenant;
		private LifetimeFlag flag;

		public static TenantEvent create(Tenant t, LifetimeFlag flag) {
			return new TenantEvent(t, flag);
		}

		public Tenant getTenant() {
			return tenant;
		}

		public LifetimeFlag getFlag() {
			return flag;
		}

		private TenantEvent(Tenant t, LifetimeFlag flag) {
			this.tenant = t;
			this.flag = flag;
		}

		@Override
		public String toString() {
			return "TenantEvent [tenantID=" + tenant.getId() + ", createTime=" + tenant.getCreateTime() + ", dueTime="
					+ tenant.getDueTime() + ", status=" + tenant.getStatus() + ", flag=" + flag + "]";
		}
	}

	public enum LifetimeFlag {
		DUE, // tenant is due on tenant lifetime
		ABT_DUE// tenant is about to due on tenant lifetime
	}
}
