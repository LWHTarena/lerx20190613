package com.lerx.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.lerx.dao.iface.ITempletAlbumMainDao;
import com.lerx.entities.TempletAlbumMain;
import com.lerx.hql.entities.Rs;
import com.lerx.hql.util.HibernateCallbackUtil;
import com.lerx.sys.util.StringUtil;

public class TempletAlbumMainDaoImpl extends HibernateDaoSupport implements ITempletAlbumMainDao {

	@Override
	public long add(TempletAlbumMain templet) {
		List<TempletAlbumMain> findedResult=findByTitle(templet.getName());
		if (findedResult.size()==0){
			this.getHibernateTemplate().save(templet);
			return templet.getId();
		}else{
			return -1;
		}
		
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
	public void modify(TempletAlbumMain templet) {
		this.getHibernateTemplate().update(templet);

	}

	@Override
	public TempletAlbumMain findByID(long id) {
		TempletAlbumMain main=this.getHibernateTemplate().get(TempletAlbumMain.class, id);
		if (main!=null && (main.getUuid()==null || main.getUuid().trim().equals(""))) {
			main.setUuid(StringUtil.uuidStr());
			modify(main);
		}
		return main;
	}

	@Override
	public List<TempletAlbumMain> findByTitle(String title) {
		String hql="from TempletAlbumMain t where t.name=?0";
		
		@SuppressWarnings("unchecked")
		List<TempletAlbumMain> list = (List<TempletAlbumMain>) this.getHibernateTemplate().find(hql,title);
		return list;
	}

	/*
	 * 分页查询
	 */
	
	@Override
	public Rs query(int page, int pagesize) {
		String hql;
		hql="from TempletAlbumMain t order by t.def desc,t.orderNum asc,t.id desc";
		
		int firstPlace=0;
		return HibernateCallbackUtil.getRs(this.getHibernateTemplate(), hql,firstPlace, page, pagesize);
	}
	
	@Override
	public Rs queryByAllow(int page, int pagesize) {
		String hql;
		hql="from TempletAlbumMain t where t.state is true order by t.orderNum asc,t.id desc";
		
		int firstPlace=0;
		return HibernateCallbackUtil.getRs(this.getHibernateTemplate(), hql,firstPlace, page, pagesize);
	}

	@Override
	public List<TempletAlbumMain> queryAll() {
		String hql;
		hql="from TempletAlbumMain t order by t.orderNum asc,t.id desc";
//		String hql="from TerminalShowTemplet t where t.market is null or t.market.id="+mid+" order by t.orderNum asc,t.id desc";
		@SuppressWarnings("unchecked")
		List<TempletAlbumMain> list = (List<TempletAlbumMain>) this.getHibernateTemplate().find(hql);
		
		return list;
	}

	@Override
	public void setDef(long id) {
		// TODO Auto-generated method stub
		
		String hql="update TempletAlbumMain t set t.def=false";
		this.getHibernateTemplate().bulkUpdate(hql);
		hql="update TempletAlbumMain t set t.def=true where t.id="+id;
		this.getHibernateTemplate().bulkUpdate(hql);
	}

	@Override
	public TempletAlbumMain findDef() {
		String hql;
		hql="from TempletAlbumMain t where t.def is true order by t.orderNum asc,t.id desc";
		@SuppressWarnings("unchecked")
		List<TempletAlbumMain> list = (List<TempletAlbumMain>) this.getHibernateTemplate().find(hql);
		if (list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public TempletAlbumMain findByUuid(String uuid) {
		String hql;
		hql="from TempletAlbumMain t where t.uuid=?0";
		@SuppressWarnings("unchecked")
		List<TempletAlbumMain> list = (List<TempletAlbumMain>) this.getHibernateTemplate().find(hql,uuid);
		if (list.size()>0){
			return list.get(0);
		}
		return null;
	}

	

	

}
