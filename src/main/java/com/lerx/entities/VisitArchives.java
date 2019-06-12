package com.lerx.entities;

public class VisitArchives {
	
	private long id;
	private VisitorsBook vbook;
	private int dayKey;
	private long views;
	private long ips;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public VisitorsBook getVbook() {
		return vbook;
	}
	public void setVbook(VisitorsBook vbook) {
		this.vbook = vbook;
	}
	public int getDayKey() {
		return dayKey;
	}
	public void setDayKey(int dayKey) {
		this.dayKey = dayKey;
	}
	public long getViews() {
		return views;
	}
	public void setViews(long views) {
		this.views = views;
	}
	public long getIps() {
		return ips;
	}
	public void setIps(long ips) {
		this.ips = ips;
	}
	
	

}
