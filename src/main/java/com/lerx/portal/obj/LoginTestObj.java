package com.lerx.portal.obj;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.support.ResourceBundleMessageSource;

import com.lerx.dao.iface.IUserDao;
import com.lerx.entities.User;

public class LoginTestObj {
	

	private User user;
	private String vcode;
	private IUserDao userDaoImpl;
	private boolean fromConnecter;
	private boolean w;
	private ResourceBundleMessageSource messageSource;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getVcode() {
		return vcode;
	}
	public void setVcode(String vcode) {
		this.vcode = vcode;
	}
	public IUserDao getUserDaoImpl() {
		return userDaoImpl;
	}
	public void setUserDaoImpl(IUserDao userDaoImpl) {
		this.userDaoImpl = userDaoImpl;
	}
	public boolean isFromConnecter() {
		return fromConnecter;
	}
	public void setFromConnecter(boolean fromConnecter) {
		this.fromConnecter = fromConnecter;
	}
	public boolean isW() {
		return w;
	}
	public void setW(boolean w) {
		this.w = w;
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
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	public HttpSession getSession() {
		return session;
	}
	public void setSession(HttpSession session) {
		this.session = session;
	}
	


}
