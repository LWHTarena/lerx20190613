package com.lerx.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.lerx.dao.iface.IGroupDao;
import com.lerx.entities.ArticleGroup;
import com.lerx.entities.TempletPortalMain;
import com.lerx.entities.VisitorsBook;
import com.lerx.rank.util.RankUnitUtil;

public class GroupDaoImpl extends HibernateDaoSupport implements IGroupDao {

	@Override
	public long add(ArticleGroup g) {
		VisitorsBook vbook = new VisitorsBook();
		vbook.setObjType(1);
		vbook.setObjTitle(g.getName());
		this.getHibernateTemplate().save(vbook);
		g.setVbook(vbook);
		g = (ArticleGroup) RankUnitUtil.add(this.getHibernateTemplate(), g);
		modifyChanged(g);
		return g.getId();
	}
	
	@Override
	public void modify(ArticleGroup g) {
		TempletPortalMain templet=g.getTemplet();
		if (templet==null) {
			g.setTemplet(null);
		}else {
			g.setTemplet(this.getHibernateTemplate().get(TempletPortalMain.class, templet.getId()));
		}
		RankUnitUtil.modify(this.getHibernateTemplate(), g);
		
		ArticleGroup changed=findByID(g.getId());
		
		changed.setCh(g.getCh());
		changed.setChanged(g.isChanged());
		changed.setCoerce(g.isCoerce());
		changed.setComm(g.isComm());
		changed.setCw(g.getCw());
		changed.setFolder(g.getFolder());
		changed.setGather(g.isGather());
		changed.setHotn(g.getHotn());
		changed.setHtmlOwn(g.getHtmlOwn());
		changed.setIcoUrl(g.getIcoUrl());
		changed.setImgIntact(g.isImgIntact());
		changed.setIpVisitAllow(g.getIpVisitAllow());
		changed.setJumpToUrl(g.getJumpToUrl());
		changed.setName(g.getName());
		changed.setOpen(g.isOpen());
		changed.setClogging(g.isClogging());
		changed.setPoll(g.isPoll());
		changed.setStaticPage(g.isStaticPage());
		changed.setStatus(g.isStatus());
		if (g.getTemplet()==null || g.getTemplet().getId()<=0) {
			changed.setTemplet(null);
		}else {
			changed.setTemplet(g.getTemplet());
		}
		
		changed.setTitle(g.getTitle());
		changed.setTmp(g.getTmp());
		changed.setUrl(g.getUrl());
		
		
		/*g.setFootLeft(changed.getFootLeft());
		g.setFootRight(changed.getFootRight());
		g.setParent(changed.getParent());
		g.setOrderStr(changed.getOrderStr());
		g.setVbook(changed.getVbook());*/
		
		
		this.getHibernateTemplate().update(changed);
		this.getHibernateTemplate().flush();
	
		VisitorsBook vbook = g.getVbook();
		if (g!=null && vbook==null) {
			vbook = new VisitorsBook();
			vbook.setObjType(1);
			vbook.setObjTitle(g.getName());
			this.getHibernateTemplate().save(vbook);
			g.setVbook(vbook);
		}
		vbook.setObjTitle(g.getName());
		
		this.getHibernateTemplate().update(vbook);
		modifyChanged(changed);
		
	}
	
	@Override
	public void modifySimple(ArticleGroup g) {
		this.getHibernateTemplate().update(g);
		
	}

	@Override
	public ArticleGroup findByID(long id) {
		return this.getHibernateTemplate().get(ArticleGroup.class, id);
	}

	@Override
	public boolean delByID(long id) {
		ArticleGroup g = findByID(id);
		if (g==null) {
			return false;
		}
		VisitorsBook vbook=g.getVbook();
		modifyChanged(g);
		int r=RankUnitUtil.del(this.getHibernateTemplate(), g);
		if (r<0) {
			return false;
		}else {
			this.getHibernateTemplate().delete(vbook);
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ArticleGroup> queryByParentID(long parentID,boolean showPosterity,boolean gather,int status) {
		List<ArticleGroup> list;
		ArticleGroup parent=findByID(parentID);
		String hql;
		
		String statusSql;
		switch (status) {
		case -1:
			statusSql=" g.status is false ";
			break;
		case 0:
			statusSql="";
			break;
		default:
			statusSql=" g.status is true ";
		}
		
		if (gather) {
			if (statusSql!=null && !statusSql.trim().equals("")){
				statusSql = statusSql + " and g.gather is true ";
			}else {
				statusSql =  " g.gather is true ";
			}
		}
		if (parentID > 0L && findByID(parentID)!=null) {
			if (statusSql!=null && !statusSql.trim().equals("")){
				statusSql = statusSql + " and ";
			}
			if (showPosterity) {
				hql="from ArticleGroup g where " + statusSql + " g.footLeft > "+parent.getFootLeft() + " and g.footRight < " + parent.getFootRight()+" order by g.orderStr asc";
			}else {
				
				hql="from ArticleGroup g where " + statusSql + " g.parent.id = " + parentID + " order by g.orderStr asc";
			}
			
		}else {
			
			if (showPosterity) {
				if (statusSql!=null && !statusSql.trim().equals("")){
					statusSql = " where " + statusSql;
				}
				hql="from ArticleGroup g " + statusSql + " order by g.orderStr asc";
			}else {
				if (statusSql!=null && !statusSql.trim().equals("")){
					statusSql = " and " + statusSql;
				}
				hql="from ArticleGroup g  where (g.parent is null) " + statusSql + " order by g.orderStr asc";
			}
		}
		this.getHibernateTemplate().clear();
		list = (List<ArticleGroup>) this.getHibernateTemplate().find(hql);
		
		return list;
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ArticleGroup> queryGatherByParentID(long parentID) {
		List<ArticleGroup> list;
		String hql;
		
		if (parentID > 0L && findByID(parentID)!=null) {
			hql="from ArticleGroup g where g.status is true and g.gather is true  and g.parent.id = " + parentID + " order by g.orderStr asc";
			
		}else {
			hql="from ArticleGroup g  where (g.parent is null) and g.status is true and g.gather is true order by g.orderStr asc";
		}
		this.getHibernateTemplate().clear();
		list = (List<ArticleGroup>) this.getHibernateTemplate().find(hql);
		return list;
	}

	@Override
	public HibernateTemplate ht() {
		return this.getHibernateTemplate();
	}

	@Override
	public void move(ArticleGroup g, int offset) {
		modifyChanged(g);
		RankUnitUtil.move(this.getHibernateTemplate(), g, null, offset);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ArticleGroup> queryParentBySubID(long subID, int status) {
		ArticleGroup currGroup=findByID(subID);
		String statusSql;
		switch (status) {
		case -1:
			statusSql=" g.status is false ";
			break;
		case 0:
			statusSql="";
			break;
		default:
			statusSql=" g.status is true ";
		}
		
		if (statusSql!=null && !statusSql.trim().equals("")){
			statusSql = statusSql + " and ";
		}
		
		String hql="from ArticleGroup g where " + statusSql + " g.footLeft < "+currGroup.getFootLeft() + " and g.footRight > " + currGroup.getFootRight()+" order by g.orderStr asc";
		List<ArticleGroup> list;
		list = (List<ArticleGroup>) this.getHibernateTemplate().find(hql);
		
		return list;
	}

	@Override
	public void modifyChanged(ArticleGroup g) {
		String hql="update ArticleGroup g set g.changed=true where g.footLeft <= "+g.getFootLeft() + " and g.footRight >= " + g.getFootRight();
		this.getHibernateTemplate().bulkUpdate(hql);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ArticleGroup> queryChanged() {
		String hql="from ArticleGroup g where g.status is true and g.open is true and staticPage is true and g.changed is true";
		List<ArticleGroup> list;
		list = (List<ArticleGroup>) this.getHibernateTemplate().find(hql);
		return list;
	}


}
