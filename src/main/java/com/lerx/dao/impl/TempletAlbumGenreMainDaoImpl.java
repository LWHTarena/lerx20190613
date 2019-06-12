package com.lerx.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.lerx.dao.iface.ITempletAlbumGenreMainDao;
import com.lerx.entities.TempletAlbumGenreMain;
import com.lerx.hql.entities.Rs;
import com.lerx.hql.util.HibernateCallbackUtil;

public class TempletAlbumGenreMainDaoImpl extends HibernateDaoSupport implements ITempletAlbumGenreMainDao {

	@Override
	public long add(TempletAlbumGenreMain templet) {
		List<TempletAlbumGenreMain> findedResult=findByTitle(templet.getName());
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
	public void modify(TempletAlbumGenreMain templet) {
		this.getHibernateTemplate().update(templet);

	}

	@Override
	public TempletAlbumGenreMain findByID(long id) {
		return this.getHibernateTemplate().get(TempletAlbumGenreMain.class, id);
	}

	@Override
	public List<TempletAlbumGenreMain> findByTitle(String title) {
		String hql="from TempletAlbumGenreMain t where t.name=?0";
		
		@SuppressWarnings("unchecked")
		List<TempletAlbumGenreMain> list = (List<TempletAlbumGenreMain>) this.getHibernateTemplate().find(hql,title);
		return list;
	}

	/*
	 * 分页查询
	 */
	
	@Override
	public Rs query(int page, int pagesize) {
		String hql;
		hql="from TempletAlbumGenreMain t order by t.def desc,t.orderNum asc,t.id desc";
		
		int firstPlace=0;
		return HibernateCallbackUtil.getRs(this.getHibernateTemplate(), hql,firstPlace, page, pagesize);
	}
	
	@Override
	public Rs queryByAllow(int page, int pagesize) {
		String hql;
		hql="from TempletAlbumGenreMain t where t.state is true order by t.orderNum asc,t.id desc";
		
		int firstPlace=0;
		return HibernateCallbackUtil.getRs(this.getHibernateTemplate(), hql,firstPlace, page, pagesize);
	}

	@Override
	public List<TempletAlbumGenreMain> queryAll() {
		String hql;
		hql="from TempletAlbumGenreMain t order by t.orderNum asc,t.id desc";
//		String hql="from TerminalShowTemplet t where t.market is null or t.market.id="+mid+" order by t.orderNum asc,t.id desc";
		@SuppressWarnings("unchecked")
		List<TempletAlbumGenreMain> list = (List<TempletAlbumGenreMain>) this.getHibernateTemplate().find(hql);
		
		return list;
	}

	@Override
	public void setDef(long id) {
		// TODO Auto-generated method stub
		
		String hql="update TempletAlbumGenreMain t set t.def=false";
		this.getHibernateTemplate().bulkUpdate(hql);
		hql="update TempletAlbumGenreMain t set t.def=true where t.id="+id;
		this.getHibernateTemplate().bulkUpdate(hql);
	}

	@Override
	public TempletAlbumGenreMain findDef() {
		String hql;
		hql="from TempletAlbumGenreMain t where t.def is true order by t.orderNum asc,t.id desc";
		@SuppressWarnings("unchecked")
		List<TempletAlbumGenreMain> list = (List<TempletAlbumGenreMain>) this.getHibernateTemplate().find(hql);
		if (list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public TempletAlbumGenreMain findByUuid(String uuid) {
		String hql;
		hql="from TempletAlbumGenreMain t where t.uuid=?0";
		@SuppressWarnings("unchecked")
		List<TempletAlbumGenreMain> list = (List<TempletAlbumGenreMain>) this.getHibernateTemplate().find(hql,uuid);
		if (list.size()>0){
			return list.get(0);
		}
		return null;
	}

	

	

}
