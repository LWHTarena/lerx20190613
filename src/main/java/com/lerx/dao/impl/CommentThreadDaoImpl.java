package com.lerx.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.lerx.dao.iface.ICommentThreadDao;
import com.lerx.entities.CommentThread;
import com.lerx.hql.entities.Rs;
import com.lerx.hql.util.HibernateCallbackUtil;
import com.lerx.sys.util.StringUtil;

public class CommentThreadDaoImpl extends HibernateDaoSupport implements ICommentThreadDao {

	@Override
	public CommentThread add(CommentThread ct) {
		ct.setContent(StringUtil.htmlFilter(ct.getContent()));
		this.getHibernateTemplate().save(ct.getPoll());
		this.getHibernateTemplate().save(ct);
		return ct;
	}

	@Override
	public CommentThread findByID(long id) {
		return this.getHibernateTemplate().get(CommentThread.class, id);
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
	public void modify(CommentThread ct) {
		this.getHibernateTemplate().update(ct);

	}

	@Override
	public Rs queryByBid(long bid, int page, int pagesize, boolean asc, int status,boolean deletedShow) {
		String statusSql;
		switch (status) {
		case -1:
			statusSql="and c.status is false ";
			break;
		case 0:
			statusSql="";
			break;
		default:
			statusSql="and c.status is true ";
		}
		String deletedSql="";
		if (!deletedShow) {
			deletedSql="and c.deleted=false ";
		}
		String hql;
		if (bid>0) {
			
			hql = "from CommentThread c where c.cb.id="+bid+deletedSql+statusSql+" order by c.id desc";
		}else {
			
			hql = "from CommentThread c where "+deletedSql+statusSql+" order by c.id desc";
			hql=StringUtil.strReplace(hql, "  ", " ");
			hql=StringUtil.strReplace(hql, "where and", "where ");
			
		}
		
		int firstPlace = 0;
		return HibernateCallbackUtil.getRs(this.getHibernateTemplate(), hql, firstPlace, page, pagesize);
	}

	@Override
	public long countByUid(long uid) {
		String hql="select count(*) from CommentThread c where c.user.id=?0";
		@SuppressWarnings("unchecked")
		List<Long> list = (List<Long>) this.getHibernateTemplate().find(hql,uid);
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public long countByBid(long bid) {
		String hql;
		List<Long> list;
		if (bid>0L) {
			hql="select count(*) from CommentThread c where c.status is true and c.deleted=false and c.cb.id=?0";
			list = (List<Long>) this.getHibernateTemplate().find(hql,bid);
		}else {
			hql="select count(*) from CommentThread c where c.status is true and c.deleted=false";
			list = (List<Long>) this.getHibernateTemplate().find(hql);
		}
		
		return list.get(0);
	}

}
