package com.asiainfo.ocmanager.tenant.management;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.persistence.model.Tenant;
import com.asiainfo.ocmanager.rest.resource.persistence.TenantPersistenceWrapper;
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
	private ExecutorService service = Executors.newSingleThreadExecutor();

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
		service.execute(new Runnable() {

			@Override
			public void run() {
				Thread.currentThread().setName("Tenant-LifetimeDetector-Thread");
				while (true) {
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
								LOG.info("Tenant [{}] exceeded lifetime [{}]", t.getName(), dueTime.getTime());
								dueTenants.add(TenantEvent.create(t, LifetimeFlag.DUE));
							} else if (diff > 0 && diff <= 604800000) {
								// 604800000 mills = 7 days
								LOG.info("Tenant [{}] is approaching lifetime [{}] within 7 days",
										 t.getName(), dueTime.getTime());
								dueTenants.add(TenantEvent.create(t, LifetimeFlag.ABT_DUE));
							}
						});
					} catch (Exception e) {
						LOG.error("Exception during running Tenant-LifetimeDetector-Thread: ", e);
					}
					holdUntillMidnight();
				}
			}

			private void holdUntillMidnight() {
				while (daytime()) {
					try {
						Thread.sleep(1800000);// sleep for 30 mins
					} catch (InterruptedException e) {
						e.printStackTrace();
					}	
				}
				LOG.info("Tenant-LifetimeDetector-Thread has been triggered.");
			}

			private boolean daytime() {
				return Calendar.getInstance().get(Calendar.HOUR_OF_DAY) != 0;
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
		});
		LOG.info("LifetimeDetector thread initialized and started.");
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
			return "TenantEvent [tenantName=" + tenant.getName() + ", createTime=" + tenant.getCreateTime() + ", dueTime="
					+ tenant.getDueTime() + ", status=" + tenant.getStatus() + ", flag=" + flag + "]";
		}
	}

	public static enum LifetimeFlag {
		/** tenant is due on lifetime */
		DUE, 
		/** tenant is about to due on lifetime */
		ABT_DUE
	}
}
