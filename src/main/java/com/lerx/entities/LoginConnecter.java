package com.lerx.entities;


public class LoginConnecter {
	
	private long id;
	private String openIDAtQQ;
	private String openIDAtWeChat;
	private String openIDAtWeibo;
	private User user;
	private long createTimstamp;
	
	
	/*
	 * otype
	 * 111
	 * qq 微信 微博 
	 */
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getOpenIDAtQQ() {
		return openIDAtQQ;
	}
	public void setOpenIDAtQQ(String openIDAtQQ) {
		this.openIDAtQQ = openIDAtQQ;
	}
	public String getOpenIDAtWeChat() {
		return openIDAtWeChat;
	}
	public void setOpenIDAtWeChat(String openIDAtWeChat) {
		this.openIDAtWeChat = openIDAtWeChat;
	}
	public String getOpenIDAtWeibo() {
		return openIDAtWeibo;
	}
	public void setOpenIDAtWeibo(String openIDAtWeibo) {
		this.openIDAtWeibo = openIDAtWeibo;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public long getCreateTimstamp() {
		return createTimstamp;
	}
	public void setCreateTimstamp(long createTimstamp) {
		this.createTimstamp = createTimstamp;
	}
	
	

}
