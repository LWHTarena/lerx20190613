package com.lerx.entities;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.lerx.interceptor.ArticleIndexStatusInterceptor;

@Indexed(interceptor=ArticleIndexStatusInterceptor.class)
@Analyzer(impl = IKAnalyzer.class)
public class AlbumArticle {
	
	@DocumentId
	private long id;
	private Album album;
	@Field (index=Index.YES, analyze=Analyze.YES, store=Store.YES)
	private String subject;
	private String subjectShort;
	private long views;
	private long creationTime;
	private long lastModifyTime;
	private long lastViewTime;
	private boolean status;
	private boolean soul;
	private boolean topOne;
	private int hotn;
	private Poll poll;
	private VisitorsBook vbook;
	private CommentBridge cb;
	private HtmlFileStatic hfs;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Album getAlbum() {
		return album;
	}
	public void setAlbum(Album album) {
		this.album = album;
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
	public long getViews() {
		return views;
	}
	public void setViews(long views) {
		this.views = views;
	}
	public long getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}
	public long getLastModifyTime() {
		return lastModifyTime;
	}
	public void setLastModifyTime(long lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	public long getLastViewTime() {
		return lastViewTime;
	}
	public void setLastViewTime(long lastViewTime) {
		this.lastViewTime = lastViewTime;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public boolean isSoul() {
		return soul;
	}
	public void setSoul(boolean soul) {
		this.soul = soul;
	}
	public boolean isTopOne() {
		return topOne;
	}
	public void setTopOne(boolean topOne) {
		this.topOne = topOne;
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
	public CommentBridge getCb() {
		return cb;
	}
	public void setCb(CommentBridge cb) {
		this.cb = cb;
	}
	public HtmlFileStatic getHfs() {
		return hfs;
	}
	public void setHfs(HtmlFileStatic hfs) {
		this.hfs = hfs;
	}
	


}
