package com.lerx.entities;

public class Album {
	
	private long id;
	private String name;
	private String description;
	private String mail;
	private String logo;
	private String banner;
	private String uuid;
	private String logoSmall;
	private String logo8831;
	private String bannerSmall;
	private boolean status;
	private boolean open;
	private long createTime;
	private long lastEditTime;
	private long lastViewTime;
	private String lastViewIP;
	private long lastModifyTime;
	private long expiredDate;				//结束时间
	private int hotn;
	private Poll poll;
	private VisitorsBook vbook;
	private int totalOfArts;
	private AlbumGenre genre;
	private User leader;
	private User passer;
	private TempletAlbumMain templet;
	
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getBanner() {
		return banner;
	}
	public void setBanner(String banner) {
		this.banner = banner;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getLogoSmall() {
		return logoSmall;
	}
	public void setLogoSmall(String logoSmall) {
		this.logoSmall = logoSmall;
	}
	public String getLogo8831() {
		return logo8831;
	}
	public void setLogo8831(String logo8831) {
		this.logo8831 = logo8831;
	}
	public String getBannerSmall() {
		return bannerSmall;
	}
	public void setBannerSmall(String bannerSmall) {
		this.bannerSmall = bannerSmall;
	}
	public AlbumGenre getGenre() {
		return genre;
	}
	public void setGenre(AlbumGenre genre) {
		this.genre = genre;
	}
	public User getLeader() {
		return leader;
	}
	public void setLeader(User leader) {
		this.leader = leader;
	}
	public User getPasser() {
		return passer;
	}
	public void setPasser(User passer) {
		this.passer = passer;
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
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getLastEditTime() {
		return lastEditTime;
	}
	public void setLastEditTime(long lastEditTime) {
		this.lastEditTime = lastEditTime;
	}
	public long getLastViewTime() {
		return lastViewTime;
	}
	public void setLastViewTime(long lastViewTime) {
		this.lastViewTime = lastViewTime;
	}
	public String getLastViewIP() {
		return lastViewIP;
	}
	public void setLastViewIP(String lastViewIP) {
		this.lastViewIP = lastViewIP;
	}
	public long getLastModifyTime() {
		return lastModifyTime;
	}
	public void setLastModifyTime(long lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	public long getExpiredDate() {
		return expiredDate;
	}
	public void setExpiredDate(long expiredDate) {
		this.expiredDate = expiredDate;
	}
	public int getTotalOfArts() {
		return totalOfArts;
	}
	public void setTotalOfArts(int totalOfArts) {
		this.totalOfArts = totalOfArts;
	}
	public int getHotn() {
		return hotn;
	}
	public void setHotn(int hotn) {
		this.hotn = hotn;
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
	public TempletAlbumMain getTemplet() {
		return templet;
	}
	public void setTemplet(TempletAlbumMain templet) {
		this.templet = templet;
	}

}
