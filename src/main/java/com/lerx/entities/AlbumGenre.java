package com.lerx.entities;

public class AlbumGenre {
	
	private long id;
	private String name;
	private String description;
	private boolean status;
	private boolean open;			//是否公开
	private boolean free;			//是否可以自己申请
	private boolean comm;
	private boolean poll;
	private int quota;
	private long createTime;
	private String markLogoUrl;
	private String folder;
	private User creator;
	private String agreement;
	
	private TempletAlbumGenreMain templet;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public boolean isFree() {
		return free;
	}
	public void setFree(boolean free) {
		this.free = free;
	}
	public boolean isComm() {
		return comm;
	}
	public void setComm(boolean comm) {
		this.comm = comm;
	}
	public int getQuota() {
		return quota;
	}
	public void setQuota(int quota) {
		this.quota = quota;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getMarkLogoUrl() {
		return markLogoUrl;
	}
	public void setMarkLogoUrl(String markLogoUrl) {
		this.markLogoUrl = markLogoUrl;
	}
	public User getCreator() {
		return creator;
	}
	public void setCreator(User creator) {
		this.creator = creator;
	}
	public boolean isPoll() {
		return poll;
	}
	public void setPoll(boolean poll) {
		this.poll = poll;
	}
	public String getFolder() {
		return folder;
	}
	public void setFolder(String folder) {
		this.folder = folder;
	}
	public TempletAlbumGenreMain getTemplet() {
		return templet;
	}
	public void setTemplet(TempletAlbumGenreMain templet) {
		this.templet = templet;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAgreement() {
		return agreement;
	}
	public void setAgreement(String agreement) {
		this.agreement = agreement;
	}

}
