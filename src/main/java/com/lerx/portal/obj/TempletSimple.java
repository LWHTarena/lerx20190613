package com.lerx.portal.obj;

import com.lerx.entities.CommentBridge;
import com.lerx.entities.Poll;

public class TempletSimple {
	
	private long id;
	private String name;
	private String author;
	private String description;
	private int downs;
	private int free;
	private String designTime;
	private String modifyTime;
	private String demo;
	private String resFolder;
	private Poll poll;
	private CommentBridge cb;
	
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
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getDowns() {
		return downs;
	}
	public void setDowns(int downs) {
		this.downs = downs;
	}
	public int getFree() {
		return free;
	}
	public void setFree(int free) {
		this.free = free;
	}
	public String getDesignTime() {
		return designTime;
	}
	public void setDesignTime(String designTime) {
		this.designTime = designTime;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getDemo() {
		return demo;
	}
	public void setDemo(String demo) {
		this.demo = demo;
	}
	public String getResFolder() {
		return resFolder;
	}
	public void setResFolder(String resFolder) {
		this.resFolder = resFolder;
	}
	public Poll getPoll() {
		return poll;
	}
	public void setPoll(Poll poll) {
		this.poll = poll;
	}
	public CommentBridge getCb() {
		return cb;
	}
	public void setCb(CommentBridge cb) {
		this.cb = cb;
	}

}
