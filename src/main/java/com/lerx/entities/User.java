package com.lerx.entities;


public class User extends com.lerx.user.entities.User{
	
	private Role role;
	private String regCodeSendTarget;
	private long artsPassed;
	private long artsTotal;
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getRegCodeSendTarget() {
		return regCodeSendTarget;
	}

	public void setRegCodeSendTarget(String regCodeSendTarget) {
		this.regCodeSendTarget = regCodeSendTarget;
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
