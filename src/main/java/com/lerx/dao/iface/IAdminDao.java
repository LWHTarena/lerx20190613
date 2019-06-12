package com.lerx.dao.iface;

import com.lerx.entities.Admin;
import com.lerx.hql.entities.Rs;

public interface IAdminDao {
	
	public Admin add(Admin admin);
	public void modify(Admin admin);
	public void modifySafely(Admin admin);
	public Admin findByID(long id);
	public Admin findByUsername(String username);
	public boolean delByID(long id);
	public Rs find(int page,int pagesize);
	public Admin login(Admin admin);
	public void modifypw(Admin admin, String newpw) ;

}
