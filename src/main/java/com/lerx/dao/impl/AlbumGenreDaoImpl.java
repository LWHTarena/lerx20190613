package com.lerx.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.lerx.dao.iface.IAlbumGenreDao;
import com.lerx.entities.AlbumGenre;
import com.lerx.hql.entities.Rs;
import com.lerx.hql.util.HibernateCallbackUtil;

public class AlbumGenreDaoImpl extends HibernateDaoSupport implements IAlbumGenreDao {

	@Override
	public AlbumGenre add(AlbumGenre ag) {
		if (findByName(ag.getName())!=null) {
			return null;
		}
		this.getHibernateTemplate().save(ag);
		return ag;
	}

	@Override
	public void modify(AlbumGenre ag) {
		this.getHibernateTemplate().update(ag);

	}

	@Override
	public AlbumGenre findByID(long id) {
		return this.getHibernateTemplate().get(AlbumGenre.class, id);
	}
	
	@Override
	public AlbumGenre findByName(String name) {
		String hql = "from AlbumGenre a where a.name=?0";
		@SuppressWarnings("unchecked")
		List<AlbumGenre> lu=(List<AlbumGenre>) this.getHibernateTemplate().find(hql,name);
		if (lu.size() > 0) { // 如果有子对象，则拒绝删除
			return lu.get(0);
		}else{
			return null;
		}
	}
	
	@Override
	public AlbumGenre findByFolder(String folder) {
		String hql = "from AlbumGenre a where a.folder=?0";
		@SuppressWarnings("unchecked")
		List<AlbumGenre> lu=(List<AlbumGenre>) this.getHibernateTemplate().find(hql,folder);
		if (lu.size() > 0) { // 如果有子对象，则拒绝删除
			return lu.get(0);
		}else{
			return null;
		}
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
	public Rs query(int page, int pagesize, int status, int open) {
		String sqladd="",statusSql,openSql;
		switch (status) {
		case -1:
			statusSql=" a.status is false ";
			break;
		case 0:
			statusSql="";
			break;
		default:
			statusSql=" a.status is true ";
		}
		sqladd += statusSql;
		
		switch (open) {
		case -1:
			openSql=" a.open is false ";
			break;
		case 0:
			openSql="";
			break;
		default:
			openSql=" a.open is true ";
		}
		
		if (!sqladd.trim().equals("") && !openSql.trim().equals("")) {
			sqladd += " and ";
		}
		
		sqladd += openSql;
		
		
		if (!sqladd.trim().equals("")) {
			sqladd = " where " + sqladd;
		}
		
		String hql = "from AlbumGenre a " + sqladd + " order by a.id desc";
		int firstPlace = 0;
		return HibernateCallbackUtil.getRs(this.getHibernateTemplate(), hql, firstPlace, page, pagesize);
	}

	

}
