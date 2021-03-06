package com.lerx.entities;

public class CommentThread {
	
	private long id;
	private String subject;
	private String content;
	private String ip;
	private long occurDatetime;
	private long views;
	private long replies;
	private boolean status;
	private boolean deleted;
	private CommentThread parent;
	private User user;
	private Poll poll;
	private CommentBridge cb;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public long getViews() {
		return views;
	}
	public void setViews(long views) {
		this.views = views;
	}
	public long getReplies() {
		return replies;
	}
	public void setReplies(long replies) {
		this.replies = replies;
	}
	public CommentThread getParent() {
		return parent;
	}
	public void setParent(CommentThread parent) {
		this.parent = parent;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public Poll getPoll() {
		return poll;
	}
	public void setPoll(Poll poll) {
		this.poll = poll;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public long getOccurDatetime() {
		return occurDatetime;
	}
	public void setOccurDatetime(long occurDatetime) {
		this.occurDatetime = occurDatetime;
	}
	public CommentBridge getCb() {
		return cb;
	}
	public void setCb(CommentBridge cb) {
		this.cb = cb;
	}

}
