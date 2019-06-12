package com.lerx.portal.obj;

public class CommOut {
	
	private String html;
	private long count;					//记录总数
	private int page;					//第几页
	private int pageSize;				//页长
	private int pageCount;				//总页数 
	
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
	@Override
	public String toString() {
		return "CommOut [html=" + html + ", count=" + count + ", page=" + page + ", pageSize=" + pageSize
				+ ", pageCount=" + pageCount + "]";
	}
	
	

}
