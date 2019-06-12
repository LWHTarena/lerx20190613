package com.lerx.dao.impl;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.lerx.dao.iface.IVisitorsBookDao;
import com.lerx.entities.VisitorsBook;

public class VisitorsBookDaoImpl extends HibernateDaoSupport implements IVisitorsBookDao {

	@Override
	public void modify(VisitorsBook vbook) {
		this.getHibernateTemplate().update(vbook);

	}

	@Override
	public VisitorsBook findByID(long id) {
		return this.getHibernateTemplate().get(VisitorsBook.class, id);
	}

}
