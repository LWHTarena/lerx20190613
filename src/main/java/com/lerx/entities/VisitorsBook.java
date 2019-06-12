package com.lerx.entities;

public class VisitorsBook {
	
	private long id;
	private int objType;
	private long objID;
	private String objTitle;
	private long viewsTotal;
	private long viewsSub;
	private long viewsOwn;
	private long ipTotal;
	private long ipSub;
	private long ipOwn;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getObjType() {
		return objType;
	}
	public void setObjType(int objType) {
		this.objType = objType;
	}
	public long getObjID() {
		return objID;
	}
	public void setObjID(long objID) {
		this.objID = objID;
	}
	public String getObjTitle() {
		return objTitle;
	}
	public void setObjTitle(String objTitle) {
		this.objTitle = objTitle;
	}
	public long getViewsTotal() {
		return viewsTotal;
	}
	public void setViewsTotal(long viewsTotal) {
		this.viewsTotal = viewsTotal;
	}
	public long getViewsSub() {
		return viewsSub;
	}
	public void setViewsSub(long viewsSub) {
		this.viewsSub = viewsSub;
	}
	public long getViewsOwn() {
		return viewsOwn;
	}
	public void setViewsOwn(long viewsOwn) {
		this.viewsOwn = viewsOwn;
	}
	public long getIpTotal() {
		return ipTotal;
	}
	public void setIpTotal(long ipTotal) {
		this.ipTotal = ipTotal;
	}
	public long getIpSub() {
		return ipSub;
	}
	public void setIpSub(long ipSub) {
		this.ipSub = ipSub;
	}
	public long getIpOwn() {
		return ipOwn;
	}
	public void setIpOwn(long ipOwn) {
		this.ipOwn = ipOwn;
	}

}
