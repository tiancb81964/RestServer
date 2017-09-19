package com.asiainfo.ocmanager.concurrent;

public interface LockerPool <T> {
	
	public void register(T t);
	
	public void unregister(T t);
	
	public Locker getLocker(T t);

}
