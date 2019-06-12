package com.lerx.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.lerx.dao.iface.IAdminDao;
import com.lerx.entities.Admin;
import com.lerx.hql.entities.Rs;
import com.lerx.hql.util.HibernateCallbackUtil;
import com.lerx.login.util.LoginUtil;

public class AdminDaoImpl extends HibernateDaoSupport implements IAdminDao {

	@Override
	public Admin add(Admin admin) {
		if (findByUsername(admin.getUsername())!=null) {
			return null;
		}
		admin = (Admin) LoginUtil.create(admin, true);
		this.getHibernateTemplate().save(admin);
		return admin;
	}

	@Override
	public void modify(Admin admin) {
		this.getHibernateTemplate().update(admin);
	}
	
	@Override
	public void modifySafely(Admin admin) {
		LoginUtil.safeSave(this.getHibernateTemplate(), admin);
		
	}

	@Override
	public Admin findByID(long id) {
		return this.getHibernateTemplate().get(Admin.class, id);
	}
	
	@Override
	public Admin findByUsername(String username) {
		String hql = "from Admin a where a.username=?0";
		@SuppressWarnings("unchecked")
		List<Admin> lu=(List<Admin>) this.getHibernateTemplate().find(hql,username);
		if (lu.size() > 0) { // 如果有子对象，则拒绝删除
			return lu.get(0);
		}else{
			return null;
		}
	}

	@Override
	public boolean delByID(long id) {
		this.getHibernateTemplate().delete(findByID(id));

		if (findByID(id) == null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Rs find(int page, int pagesize) {
		String hql = "from Admin a order by a.id desc";
		int firstPlace = 0;
		return HibernateCallbackUtil.getRs(this.getHibernateTemplate(), hql, firstPlace, page, pagesize);
	}

	@Override
	public Admin login(Admin admin) {
		if (findByUsername(admin.getUsername())==null){
			return null;
		}
		admin = (Admin) LoginUtil.login(this.getHibernateTemplate(), admin);
		return admin;
	}

	@Override
	public void modifypw(Admin admin, String newpw) {
		admin.setSalt(null);
		admin.setPassword(newpw);
		admin = (Admin) LoginUtil.passwordMD5Create(admin);
		this.getHibernateTemplate().update(admin);
		
	}


}
