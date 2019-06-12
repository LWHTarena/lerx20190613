package com.lerx.dao.iface;

import java.util.List;

import org.springframework.orm.hibernate5.HibernateTemplate;

import com.lerx.entities.ArticleGroup;

public interface IGroupDao {
	
	public long add (ArticleGroup g);
	public void modify (ArticleGroup g);
	public void modifySimple (ArticleGroup g);
	public ArticleGroup findByID(long id);
	public boolean delByID(long id);
	public List<ArticleGroup> queryByParentID(long parentID,boolean showPosterity,boolean gather,int status);
	public List<ArticleGroup> queryGatherByParentID(long parentID);
	public List<ArticleGroup> queryParentBySubID(long subID,int status);
	public List<ArticleGroup> queryChanged();
	public HibernateTemplate ht();
	public void move(ArticleGroup g,int offset);
	public void modifyChanged(ArticleGroup g);

}
