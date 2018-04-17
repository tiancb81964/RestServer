package com.asiainfo.ocmanager.tenant.management.listener;

import com.asiainfo.ocmanager.tenant.management.LifetimeDetector.TenantEvent;

/**
 * Default policy for tenant lifetime management, which set tenant.status to
 * <code>deactive</code> and unbound user-list of out-date tenant.
 * 
 * @author Ethan
 *
 */
public class DefaultListener implements Listener {

	@Override
	public void handleDue(TenantEvent e) {
		// TODO Auto-generated method stub
		System.out.println("DefaultListener handling tenant: " + e.getTenant() + ", flag: " + e.getFlag());

	}

}
