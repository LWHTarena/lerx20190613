package com.lerx.dao.iface;

import java.util.List;

import com.lerx.entities.Role;


public interface IRoleDao {
	
	public Role add(Role role);
	public Role findByID(long id);
	public List<Role> findByName(String name);
	public List<Role> findByNameOutID(String name,long id);
	public boolean delByID(long id);
	public void modify(Role role);
	public int countUserByRoleID(long id);
	public List<Role> queryAll();
	public List<Role> queryAllAndCounts();
	public void setDef(long id);
	public Role findDef();

}
