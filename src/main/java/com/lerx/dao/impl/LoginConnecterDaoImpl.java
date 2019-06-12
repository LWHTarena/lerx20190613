package com.lerx.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.lerx.dao.iface.ILoginConnecterDao;
import com.lerx.entities.LoginConnecter;
import com.lerx.entities.User;
import com.lerx.hql.entities.Rs;
import com.lerx.hql.util.HibernateCallbackUtil;

public class LoginConnecterDaoImpl extends HibernateDaoSupport implements ILoginConnecterDao {

	@Override
	public LoginConnecter add(LoginConnecter lc,int otype) {
		this.getHibernateTemplate().save(lc);
		return lc;
		
	}

	@Override
	public LoginConnecter findByID(long id) {
		return this.getHibernateTemplate().get(LoginConnecter.class, id);
	}

	@Override
	public LoginConnecter findByUID(long uid) {
		String hql = "from LoginConnecter l where l.user is not null and l.user.id=?0";
		List<?> lu =  this.getHibernateTemplate().find(hql, uid);
		if (lu.size() > 0) { // 如果有子对象，则拒绝删除
			return (LoginConnecter) lu.get(0);
		} else {
			return null;
		}
	}

	@Override
	public LoginConnecter findByOpenID(String openID, int otype) {
		if (openID==null || openID.trim().equals("")) {
			return null;
		}
		String hql;
		switch (otype) {
		case 1:
			hql = "from LoginConnecter l where l.openIDAtWeChat=?0";
			break;
		case 2:
			hql = "from LoginConnecter l where l.openIDAtWeibo=?0";
			break;
		default:
			hql = "from LoginConnecter l where l.openIDAtQQ=?0";

		}

		@SuppressWarnings("unchecked")
		List<LoginConnecter> lu = (List<LoginConnecter>) this.getHibernateTemplate().find(hql, openID);
		if (lu.size() > 0) { // 如果有子对象，则拒绝删除
			return lu.get(0);
		} else {
			return null;
		}
	}
	
	@Override
	public User findUserByOpenID(String openID, int otype) {
		LoginConnecter lc = findByOpenID(openID, otype);
		if (lc!=null) {
			return lc.getUser();
		}else {
			return null;
		}
		
	}

	@Override
	public void modify(LoginConnecter lc) {
		this.getHibernateTemplate().update(lc);

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
	public Rs find(int page, int pagesize, int otype) {
		String hql = "from LoginConnecter l where l.otype="+otype+" order by l.id desc";
		int firstPlace = 0;
		return HibernateCallbackUtil.getRs(this.getHibernateTemplate(), hql, firstPlace, page, pagesize);
	}


}
