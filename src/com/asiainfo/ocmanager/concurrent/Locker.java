package com.asiainfo.ocmanager.concurrent;

import java.util.Date;
import java.util.UUID;

public class Locker extends Object {
	private boolean busy = false;
	private long epoch = -1l;
	private String id;
	
	public Locker() {
		super();
		this.id = UUID.randomUUID().toString();
	}
	
	public void lock() {
		this.busy = true;
		this.epoch = new Date().getTime();
	}
	
	public void unlock() {
		this.busy = false;
		this.epoch = -1l;
	}
	
	public boolean isOccupied() {
		return this.busy;
	}
	
	public String toString() {
		return "id:" +this.id + "busy:" + String.valueOf(this.busy) + "#" + "epoch:" + String.valueOf(this.epoch);
	}
}
