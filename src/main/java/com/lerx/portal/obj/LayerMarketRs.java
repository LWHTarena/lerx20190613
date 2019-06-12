package com.lerx.portal.obj;

import java.util.List;

public class LayerMarketRs {
	
	private int code;
	private String msg;
	private long count;
	private List<TempletSimple> data;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public List<TempletSimple> getData() {
		return data;
	}
	public void setData(List<TempletSimple> data) {
		this.data = data;
	}
	
	

}
