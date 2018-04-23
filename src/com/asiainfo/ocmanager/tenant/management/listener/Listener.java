package com.asiainfo.ocmanager.tenant.management.listener;

import com.asiainfo.ocmanager.tenant.management.LifetimeDetector.TenantEvent;

/**
 * Listener for tenant lifetime management
 * @author Ethan
 *
 */
public interface Listener {
	
	/**
	 * handle action when tenant lifetime is due
	 */
	public void handleDue(TenantEvent e);
	
}
