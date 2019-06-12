package com.lerx.portal.obj;

public class CommentThreadHref {
	
	private long cid;
	private long bid;
	private long oid;
	private String subject;
	private String content;
	private String ip;
	private String ipfrom;
	private String href;
	private long agrees;			//支持者数
	private long antis;				//反对者数
	private long passbys;			//中立者数
	private long occurDatetime;
	public long getCid() {
		return cid;
	}
	public void setCid(long cid) {
		this.cid = cid;
	}
	public long getBid() {
		return bid;
	}
	public void setBid(long bid) {
		this.bid = bid;
	}
	public long getOid() {
		return oid;
	}
	public void setOid(long oid) {
		this.oid = oid;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public long getAgrees() {
		return agrees;
	}
	public void setAgrees(long agrees) {
		this.agrees = agrees;
	}
	public long getAntis() {
		return antis;
	}
	public void setAntis(long antis) {
		this.antis = antis;
	}
	public long getPassbys() {
		return passbys;
	}
	public void setPassbys(long passbys) {
		this.passbys = passbys;
	}
	public long getOccurDatetime() {
		return occurDatetime;
	}
	public void setOccurDatetime(long occurDatetime) {
		this.occurDatetime = occurDatetime;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getIpfrom() {
		return ipfrom;
	}
	public void setIpfrom(String ipfrom) {
		this.ipfrom = ipfrom;
	}

}
