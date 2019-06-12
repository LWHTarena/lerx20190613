package com.lerx.entities;

public class CommentBridge {
	
	private long id;
	private long total;
	private int objType;
	private long objID;
	private User user;
	private String objTitle;
	private boolean status;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
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
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

}
