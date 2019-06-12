package com.lerx.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.lerx.dao.iface.IArticleAttaDao;
import com.lerx.entities.ArticleAtta;

public class ArticleAttaDaoImpl extends HibernateDaoSupport implements IArticleAttaDao {

	@Override
	public long add(ArticleAtta aa) {
		List<ArticleAtta> list;
		if (aa.getArticle()!=null) {
			list=findByArticleAndFileID(aa.getArticle().getId(),aa.getUf().getId());
			if (list.isEmpty() || list.size()==0) {
				this.getHibernateTemplate().save(aa);
				return aa.getId();
			}else {
				return -8;
			}
		}else {
			if (aa.getUf()==null){
				return -13;
			}
			list=findByFidAndTmpID(aa.getUf().getId(),aa.getTmpID());
			if (list.isEmpty() || list.size()==0) {
				this.getHibernateTemplate().save(aa);
				return aa.getId();
			}else {
				return -8;
			}
		}
		
	}
	
	@Override
	public void modify(ArticleAtta aa) {
		this.getHibernateTemplate().update(aa);
	}

	@Override
	public ArticleAtta findByID(long id) {
		return this.getHibernateTemplate().get(ArticleAtta.class, id);
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
	public List<ArticleAtta> findByArticleID(long aid) {
		String hql="from ArticleAtta a where a.article.id="+aid;
		@SuppressWarnings("unchecked")
		List<ArticleAtta> list = (List<ArticleAtta>) this.getHibernateTemplate().find(hql);
		return list;
	}
	
	@Override
	public List<ArticleAtta> findByAidOrTmpID(long aid, long tid) {
		String hqlAdd="";
		if (tid>0L) {
			hqlAdd=" or a.tmpID="+tid;
		}
		String hql="from ArticleAtta a where a.article.id="+aid +hqlAdd;
		@SuppressWarnings("unchecked")
		List<ArticleAtta> list = (List<ArticleAtta>) this.getHibernateTemplate().find(hql);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ArticleAtta> findByFidAndTmpID(long fid, long tid) {
		List<ArticleAtta> list;
		if (fid<=0L || tid<=0L){
			list=new ArrayList<ArticleAtta>	();
			return list;
		}
		String hql="from ArticleAtta a where a.uf.id="+fid+" and a.tmpID="+tid;
		list = (List<ArticleAtta>) this.getHibernateTemplate().find(hql);
		return list;
	}

	@Override
	public List<ArticleAtta> findByArticleAndFileID(long aid, long uid) {
		String hql="from ArticleAtta a where a.article.id="+aid + " and a.uf.id="+uid;
		@SuppressWarnings("unchecked")
		List<ArticleAtta> list = (List<ArticleAtta>) this.getHibernateTemplate().find(hql);
		return list;
	}

	

	

	

}
