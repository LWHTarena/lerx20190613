package com.lerx.dao.iface;

import com.lerx.entities.User;
import com.lerx.hql.entities.Rs;
import com.lerx.portal.obj.PortalStatInfo;

public interface IUserDao {
	
	public User add(User user);
	public void modify(User user);
	public void modifySafely(User user);
	public User findByKeywords(String keywords);
	public User findByID(long id);
	public User findByUsername(String username);
	public User findByMobile(String mobile,long excludeID);
	public User findByEmail(String email,long excludeID);
	public boolean delByID(long id);
	public Rs find(int page,int pagesize);
	public User login(User user);
	public void modifypw(User user, String newpw) ;
	public PortalStatInfo stat(PortalStatInfo psi);
	public Rs artsPassedRank(int page,int pagesize);

}
