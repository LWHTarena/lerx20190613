package com.lerx.dao.impl;

import org.hibernate.Session;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.lerx.dao.iface.IBaseDao;

public class BaseDaoImpl extends HibernateDaoSupport implements IBaseDao {

	@Override
	public HibernateTemplate getHT() {
		return this.getHibernateTemplate();
	}
	
    
	@Override
	public int excuteBySql(String sql) {
		
		Session session = this.getSessionFactory().getCurrentSession();
		return session.createSQLQuery(sql).executeUpdate();
		
		/*SQLQuery<?> sqlQuery=session.createSQLQuery(sql);
		return sqlQuery.executeUpdate();*/
		
	}

}
