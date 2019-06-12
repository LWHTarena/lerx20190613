package com.lerx.portal.obj;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.support.ResourceBundleMessageSource;

import com.lerx.dao.iface.IGroupDao;
import com.lerx.entities.ArticleGroup;

public class ArticleGroupMapCreateArgs {

	
	//Map<String, Object> map,IGroupDao groupDaoImpl,ArticleGroup group,String rootTitle,int status,boolean free,String mask
	
	private Map<String, Object> map;
	private IGroupDao groupDaoImpl;
	private ArticleGroup currRoot;
	private String rootTitle;
	private int status;
	private boolean free;
	private String currRoleMask;
	private ResourceBundleMessageSource messageSource;
	private HttpServletRequest request;
	
	
	public Map<String, Object> getMap() {
		return map;
	}
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	public IGroupDao getGroupDaoImpl() {
		return groupDaoImpl;
	}
	public void setGroupDaoImpl(IGroupDao groupDaoImpl) {
		this.groupDaoImpl = groupDaoImpl;
	}
	public ArticleGroup getCurrRoot() {
		return currRoot;
	}
	public void setCurrRoot(ArticleGroup currRoot) {
		this.currRoot = currRoot;
	}
	public String getRootTitle() {
		return rootTitle;
	}
	public void setRootTitle(String rootTitle) {
		this.rootTitle = rootTitle;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public boolean isFree() {
		return free;
	}
	public void setFree(boolean free) {
		this.free = free;
	}
	public String getCurrRoleMask() {
		return currRoleMask;
	}
	public void setCurrRoleMask(String currRoleMask) {
		this.currRoleMask = currRoleMask;
	}
	public ResourceBundleMessageSource getMessageSource() {
		return messageSource;
	}
	public void setMessageSource(ResourceBundleMessageSource messageSource) {
		this.messageSource = messageSource;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	
	
}
