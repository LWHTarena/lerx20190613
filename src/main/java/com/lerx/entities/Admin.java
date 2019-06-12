package com.lerx.entities;

public class Admin extends com.lerx.user.entities.User{

	@Override
	public String toString() {
		return "Admin [getId()=" + getId() + ", getUsername()=" + getUsername() + ", getUuid()=" + getUuid()
				+ ", getAvatarUrl()=" + getAvatarUrl() + ", getTruename()=" + getTruename() + ", getPassword()="
				+ getPassword() + ", getSalt()=" + getSalt() + ", getPwdAtCreate()=" + getPwdAtCreate()
				+ ", getPwdChangeTime()=" + getPwdChangeTime() + ", getLastLoginTime()=" + getLastLoginTime()
				+ ", getLastLoginIP()=" + getLastLoginIP() + ", isState()=" + isState() + ", getEmail()=" + getEmail()
				+ ", getCreateTime()=" + getCreateTime() + ", getCreateIP()=" + getCreateIP() + ", getEntitiesName()="
				+ getEntitiesName() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
	

}
