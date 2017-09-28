package com.asiainfo.ocmanager.security;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Schedual authentication actions.
 * 
 * @author EthanWang
 *
 */
public class AuthScheduller {
	private static final Logger LOG = LoggerFactory.getLogger(AuthScheduller.class);
	public static final long DELAY_SECONDS = 1l;
	private static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	public static void schedule(long rate, long delay) {
		executor.scheduleAtFixedRate(new AuthScheduller().new AuthRunnable(), delay, rate, TimeUnit.SECONDS);
		LOG.info("AuthScheduller schedulled at period " + rate + ", delay " + delay + " in seconds.");
	}

	public static void stop() {
		executor.shutdown();
		LOG.info("AuthScheduller stopped!");
	}

	private class AuthRunnable implements Runnable {
		private boolean firstTime = true;

		@Override
		public void run() {
			if (this.firstTime) {
				SecurityManager.getInstance().login();
				firstTime = false;
				return;
			}
			SecurityManager.getInstance().relogin();
		}

	}
}
