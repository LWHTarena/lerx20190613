package com.lerx.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.lerx.dao.iface.ICommentBridgeDao;
import com.lerx.entities.CommentBridge;

public class CommentBridgeDaoImpl extends HibernateDaoSupport implements ICommentBridgeDao {

	@Override
	public void modify(CommentBridge cb) {
		this.getHibernateTemplate().update(cb);

	}

	@Override
	public CommentBridge findByID(long id) {
		return this.getHibernateTemplate().get(CommentBridge.class, id);
	}

	@Override
	public long countByUid(long uid) {
		String hql="select count(*) from CommentBridge c where c.user.id=?0";
		@SuppressWarnings("unchecked")
		List<Long> list = (List<Long>) this.getHibernateTemplate().find(hql,uid);
		return list.get(0);
	}

}
