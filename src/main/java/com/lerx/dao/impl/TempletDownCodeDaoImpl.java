package com.lerx.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.lerx.dao.iface.ITempletDownCodeDao;
import com.lerx.entities.TempletDownCode;
import com.lerx.hql.entities.Rs;
import com.lerx.hql.util.HibernateCallbackUtil;

public class TempletDownCodeDaoImpl extends HibernateDaoSupport implements ITempletDownCodeDao {

	@Override
	public TempletDownCode add(TempletDownCode tdc) {
		this.getHibernateTemplate().save(tdc);
		return tdc;
	}

	@Override
	public void modify(TempletDownCode tdc) {
		this.getHibernateTemplate().update(tdc);

	}

	@Override
	public TempletDownCode findByID(long id) {
		TempletDownCode tdc=this.getHibernateTemplate().get(TempletDownCode.class, id);
		return tdc;
	}

	@Override
	public TempletDownCode findByCode(String code) {
		String hql;
		hql="from TempletDownCode t where t.code=?0";
		@SuppressWarnings("unchecked")
		List<TempletDownCode> list = (List<TempletDownCode>) this.getHibernateTemplate().find(hql,code);
		if (list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public boolean delByID(long id) {
		this.getHibernateTemplate().delete(findByID(id));
		
		if (findByID(id)==null){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Rs find(int typeNum, long tid, int page, int pagesize) {
		String hql;
		hql="from TempletDownCode t where t.typeNum="+typeNum+ " and t.templetID="+tid+" order by t.id desc";
		
		int firstPlace=0;
		return HibernateCallbackUtil.getRs(this.getHibernateTemplate(), hql,firstPlace, page, pagesize);
	}

}
