package com.lerx.dao.iface;

import org.springframework.orm.hibernate5.HibernateTemplate;

public interface IBaseDao {

	
	public HibernateTemplate getHT();
	public int excuteBySql(String sql);
	
}
