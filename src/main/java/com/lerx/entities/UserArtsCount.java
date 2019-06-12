package com.lerx.entities;

public class UserArtsCount {
	
	private Long id;
	private User user;
	private int timeKey;
	private String artGids;
	private long artsPassed;
	private long artsTotal;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getTimeKey() {
		return timeKey;
	}
	public void setTimeKey(int timeKey) {
		this.timeKey = timeKey;
	}
	public String getArtGids() {
		return artGids;
	}
	public void setArtGids(String artGids) {
		this.artGids = artGids;
	}
	public long getArtsPassed() {
		return artsPassed;
	}
	public void setArtsPassed(long artsPassed) {
		this.artsPassed = artsPassed;
	}
	public long getArtsTotal() {
		return artsTotal;
	}
	public void setArtsTotal(long artsTotal) {
		this.artsTotal = artsTotal;
	}
	
}
