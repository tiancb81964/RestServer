package com.asiainfo.ocmanager.tenant.management.listener;

import com.asiainfo.ocmanager.tenant.management.LifetimeDetector.TenantEvent;

public class EmailNotifyListener implements Listener {

	@Override
	public void handleDue(TenantEvent e) {
		// TODO Auto-generated method stub
		System.out.println("EmailNotifyListener handling tenant: " + e.getTenant() + ", flag: " + e.getFlag());
	}
}
