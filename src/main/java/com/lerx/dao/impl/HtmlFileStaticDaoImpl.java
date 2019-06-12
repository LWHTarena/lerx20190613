package com.lerx.dao.impl;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.lerx.dao.iface.IHtmlFileStaticDao;
import com.lerx.entities.HtmlFileStatic;

public class HtmlFileStaticDaoImpl extends HibernateDaoSupport implements IHtmlFileStaticDao {

	@Override
	public void modify(HtmlFileStatic hfs) {
		this.getHibernateTemplate().update(hfs);

	}

	@Override
	public HtmlFileStatic findByID(long id) {
		return this.getHibernateTemplate().get(HtmlFileStatic.class, id);
	}

}
