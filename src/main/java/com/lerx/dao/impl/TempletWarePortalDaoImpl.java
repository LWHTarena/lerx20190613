package com.lerx.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.lerx.dao.iface.ITempletWarePortalDao;
import com.lerx.entities.CommentBridge;
import com.lerx.entities.Poll;
import com.lerx.entities.TempletWarePortal;
import com.lerx.entities.VisitorsBook;
import com.lerx.hql.entities.Rs;
import com.lerx.hql.util.HibernateCallbackUtil;

public class TempletWarePortalDaoImpl extends HibernateDaoSupport implements ITempletWarePortalDao {

	@Override
	public TempletWarePortal add(TempletWarePortal ware) {
		CommentBridge cb=new CommentBridge();
		Poll poll=new Poll();
		poll.setStatus(true);
		cb.setStatus(true);
		VisitorsBook vbook = new VisitorsBook();
		
		this.getHibernateTemplate().save(vbook);
		this.getHibernateTemplate().save(cb);
		this.getHibernateTemplate().save(poll);
		ware.setCb(cb);
		ware.setPoll(poll);
		ware.setVbook(vbook);
		
		this.getHibernateTemplate().save(ware);
		return ware;
	}

	@Override
	public void modify(TempletWarePortal ware) {
		this.getHibernateTemplate().update(ware);
		this.getHibernateTemplate().flush();

	}

	@Override
	public TempletWarePortal findByID(long id) {
		return this.getHibernateTemplate().get(TempletWarePortal.class, id);
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
	public Rs query(int firstResult, int page, int pagesize, int orderby) {
		String orderCmd;
		switch (orderby) {
		case 1:
			orderCmd=" t.cb.total desc ";
			break;
		case 2:
			orderCmd=" t.poll.agrees desc ";
			break;
			
		default:
			orderCmd=" t.templet.downs desc ";	
		}
		String hql = "from TempletWarePortal t  where t.templet.state is true order by "+orderCmd;
		return HibernateCallbackUtil.getRs(this.getHibernateTemplate(), hql, firstResult, page, pagesize);
	}

	@Override
	public TempletWarePortal findByTempletID(long tid) {
		String hql = "from TempletWarePortal t  where t.templet.id =?0";
		@SuppressWarnings("unchecked")
		List<TempletWarePortal> lu=(List<TempletWarePortal>) this.getHibernateTemplate().find(hql,tid);
		if (lu.size() > 0) { // 如果有子对象，则拒绝删除
			return lu.get(0);
		}else{
			return null;
		}
	}

}
