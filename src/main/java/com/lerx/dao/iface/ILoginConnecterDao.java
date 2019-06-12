package com.lerx.dao.iface;

import com.lerx.entities.LoginConnecter;
import com.lerx.entities.User;
import com.lerx.hql.entities.Rs;

public interface ILoginConnecterDao {
	
	public LoginConnecter add(LoginConnecter lc,int otype);
	public LoginConnecter findByID(long id);
	public LoginConnecter findByUID(long uid);
	public LoginConnecter findByOpenID(String openID,int otype);
	public User findUserByOpenID(String openID,int otype);
	public void modify(LoginConnecter lc);
	public boolean delByID(long id);
	public Rs find(int page,int pagesize,int otype);

}
