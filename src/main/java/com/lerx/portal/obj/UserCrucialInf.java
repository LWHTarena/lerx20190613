package com.lerx.portal.obj;

public class UserCrucialInf {
	
	private long uid;
	private String firstMail;
	private String regDatetime;
	private String regIP;
	private String lastLoginDatetime;
	private String lastLoginIP;
	
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getFirstMail() {
		return firstMail;
	}
	public void setFirstMail(String firstMail) {
		this.firstMail = firstMail;
	}
	public String getRegIP() {
		return regIP;
	}
	public void setRegIP(String regIP) {
		this.regIP = regIP;
	}
	public String getLastLoginIP() {
		return lastLoginIP;
	}
	public void setLastLoginIP(String lastLoginIP) {
		this.lastLoginIP = lastLoginIP;
	}
	public String getRegDatetime() {
		return regDatetime;
	}
	public void setRegDatetime(String regDatetime) {
		this.regDatetime = regDatetime;
	}
	public String getLastLoginDatetime() {
		return lastLoginDatetime;
	}
	public void setLastLoginDatetime(String lastLoginDatetime) {
		this.lastLoginDatetime = lastLoginDatetime;
	}

}
