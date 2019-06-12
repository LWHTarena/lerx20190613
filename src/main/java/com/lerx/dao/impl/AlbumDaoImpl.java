package com.lerx.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.lerx.dao.iface.IAlbumDao;
import com.lerx.entities.Album;
import com.lerx.entities.Poll;
import com.lerx.entities.VisitorsBook;
import com.lerx.hql.entities.Rs;
import com.lerx.hql.util.HibernateCallbackUtil;

public class AlbumDaoImpl extends HibernateDaoSupport implements IAlbumDao {

	@Override
	public Album add(Album album) {
		VisitorsBook vbook = album.getVbook();
		Poll poll=album.getPoll();
		
		this.getHibernateTemplate().save(vbook);
		this.getHibernateTemplate().save(poll);
		
		this.getHibernateTemplate().save(album);
		return album;
	}

	@Override
	public void modify(Album album) {
		this.getHibernateTemplate().update(album);

	}

	@Override
	public Album findByID(long id) {
		return this.getHibernateTemplate().get(Album.class, id);
	}

	@Override
	public Album findByName(String name) {
		String hql = "from Album a where a.name=?0";
		@SuppressWarnings("unchecked")
		List<Album> lu = (List<Album>) this.getHibernateTemplate().find(hql, name);
		if (lu.size() > 0) { // 如果有子对象，则拒绝删除
			return lu.get(0);
		} else {
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
	public Rs query(int page, int pagesize, int status, int open, int orderBy, int orderDirection) {
		String sqladd = "", statusSql, openSql;
		switch (status) {
		case -1:
			statusSql = " a.status is false ";
			break;
		case 0:
			statusSql = "";
			break;
		default:
			statusSql = " a.status is true ";
		}
		sqladd += statusSql;

		switch (open) {
		case -1:
			openSql = " a.open is false ";
			break;
		case 0:
			openSql = "";
			break;
		default:
			openSql = " a.open is true ";
		}

		if (!sqladd.trim().equals("") && !openSql.trim().equals("")) {
			sqladd += " and ";
		}

		sqladd += openSql;

		if (!sqladd.trim().equals("")) {
			sqladd = " where " + sqladd;
		}
		
		String orderSqlAdd;

		switch (orderBy) {
		
		case 1:
			orderSqlAdd=" order by a.totalOfArts ";
			break;
		case 2:
			orderSqlAdd=" order by a.vbook.ipTotal ";
			break;
		case 3:
			
			orderSqlAdd=" order by (a.hotn/( TO_DAYS(NOW()) - (TO_DAYS(a.createTime)-1) )) ";
			break;
		default:
			orderSqlAdd=" order by a.id ";
		}
		
		if (orderDirection==0) {
			orderSqlAdd += "desc";
		}else {
			orderSqlAdd += "asc";
		}

		String hql = "from Album a " + sqladd + orderSqlAdd;
		int firstPlace = 0;
		return HibernateCallbackUtil.getRs(this.getHibernateTemplate(), hql, firstPlace, page, pagesize);
	}

	@Override
	public List<Album> findByUid(long uid,long gid, int status) {
		String sqladd = "", statusSql;
		switch (status) {
		case -1:
			statusSql = " and a.status is false ";
			break;
		case 0:
			statusSql = "";
			break;
		default:
			statusSql = " and a.status is true ";
		}
		sqladd += statusSql;
		
		if (gid>0) {
			sqladd += " and a.genre.id=" + gid;
		}
		String hql = "from Album a where a.leader.id=?0 " + sqladd + " order by a.id desc ";
		@SuppressWarnings("unchecked")
		List<Album> list = (List<Album>) this.getHibernateTemplate().find(hql,uid);
		
		return list;
	}

}
