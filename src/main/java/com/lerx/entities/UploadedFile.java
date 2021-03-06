package com.lerx.entities;

public class UploadedFile {
	
	/*
	 * 上传文件库
	 */
	
	private long id;
	private String url;
	private String fullPath;
	private String realPath;
	private String nameNoExt;
	private String ext;
	private long size;
	private String md5;
	private long uploadDatetime;
	private User user;
	private String 	remark;	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFullPath() {
		return fullPath;
	}
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}
	public long getUploadDatetime() {
		return uploadDatetime;
	}
	public void setUploadDatetime(long uploadDatetime) {
		this.uploadDatetime = uploadDatetime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRealPath() {
		return realPath;
	}
	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}
	public String getNameNoExt() {
		return nameNoExt;
	}
	public void setNameNoExt(String nameNoExt) {
		this.nameNoExt = nameNoExt;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

}
