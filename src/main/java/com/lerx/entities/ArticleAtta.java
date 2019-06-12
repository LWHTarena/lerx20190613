package com.lerx.entities;

public class ArticleAtta {
	
	private long id;
	private Article article;
	private UploadedFile uf;
	private String title;
	private long tmpID;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Article getArticle() {
		return article;
	}
	public void setArticle(Article article) {
		this.article = article;
	}
	public UploadedFile getUf() {
		return uf;
	}
	public void setUf(UploadedFile uf) {
		this.uf = uf;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getTmpID() {
		return tmpID;
	}
	public void setTmpID(long tmpID) {
		this.tmpID = tmpID;
	}

}
