package com.lerx.entities;


public class AlbumArticleContent {
	private long id;
	private AlbumArticle art;
	private User editor;
	private long cutOffTime;
	private String subject;
	private String subjectShort;
	private String secondTitle;
	private String shortTitle;
	private String author;
	private String synopsis;
	private String mediaUrl;
	private String content;
	private String titleImg;
	private String thumbnail; // 缩略图
	private String titleImgTxt;
	private String jumpUrl;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public AlbumArticle getArt() {
		return art;
	}
	public void setArt(AlbumArticle art) {
		this.art = art;
	}
	public User getEditor() {
		return editor;
	}
	public void setEditor(User editor) {
		this.editor = editor;
	}
	public long getCutOffTime() {
		return cutOffTime;
	}
	public void setCutOffTime(long cutOffTime) {
		this.cutOffTime = cutOffTime;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getSubjectShort() {
		return subjectShort;
	}
	public void setSubjectShort(String subjectShort) {
		this.subjectShort = subjectShort;
	}
	public String getSecondTitle() {
		return secondTitle;
	}
	public void setSecondTitle(String secondTitle) {
		this.secondTitle = secondTitle;
	}
	public String getShortTitle() {
		return shortTitle;
	}
	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getSynopsis() {
		return synopsis;
	}
	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}
	public String getMediaUrl() {
		return mediaUrl;
	}
	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTitleImg() {
		return titleImg;
	}
	public void setTitleImg(String titleImg) {
		this.titleImg = titleImg;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getTitleImgTxt() {
		return titleImgTxt;
	}
	public void setTitleImgTxt(String titleImgTxt) {
		this.titleImgTxt = titleImgTxt;
	}
	public String getJumpUrl() {
		return jumpUrl;
	}
	public void setJumpUrl(String jumpUrl) {
		this.jumpUrl = jumpUrl;
	}
	
	
	
}
