package com.lerx.entities;

public class VisitorCountInPeriod {
	
	private long id;
	private VisitArchives va;
	private int hour;
	private long totalView;
	private long totalIP;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public VisitArchives getVa() {
		return va;
	}
	public void setVa(VisitArchives va) {
		this.va = va;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public long getTotalView() {
		return totalView;
	}
	public void setTotalView(long totalView) {
		this.totalView = totalView;
	}
	public long getTotalIP() {
		return totalIP;
	}
	public void setTotalIP(long totalIP) {
		this.totalIP = totalIP;
	}
	
	

}
