package com.lerx.portal.obj;

public class LongPollingReturn {
	
	private int total;
	private int finished;
	private long lastID;
	private long startTime;
	private int tag;
	private String msg;
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getFinished() {
		return finished;
	}
	public void setFinished(int finished) {
		this.finished = finished;
	}
	public long getLastID() {
		return lastID;
	}
	public void setLastID(long lastID) {
		this.lastID = lastID;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public int getTag() {
		return tag;
	}
	public void setTag(int tag) {
		this.tag = tag;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	

}
