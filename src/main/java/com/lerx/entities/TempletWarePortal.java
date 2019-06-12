package com.lerx.entities;

public class TempletWarePortal {
	
	private long id;
	private TempletPortalMain templet;
	private Poll poll;
	private VisitorsBook vbook;
	private CommentBridge cb;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public TempletPortalMain getTemplet() {
		return templet;
	}
	public void setTemplet(TempletPortalMain templet) {
		this.templet = templet;
	}
	public Poll getPoll() {
		return poll;
	}
	public void setPoll(Poll poll) {
		this.poll = poll;
	}
	public VisitorsBook getVbook() {
		return vbook;
	}
	public void setVbook(VisitorsBook vbook) {
		this.vbook = vbook;
	}
	public CommentBridge getCb() {
		return cb;
	}
	public void setCb(CommentBridge cb) {
		this.cb = cb;
	}
	
	

}
