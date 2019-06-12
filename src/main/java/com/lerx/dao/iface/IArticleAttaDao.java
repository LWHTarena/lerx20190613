package com.lerx.dao.iface;

import java.util.List;

import com.lerx.entities.ArticleAtta;

public interface IArticleAttaDao {
	
	public long add(ArticleAtta aa);
	public void modify(ArticleAtta aa);
	public ArticleAtta findByID(long id);
	public boolean delByID(long id);
	public List<ArticleAtta> findByArticleID(long aid);
	public List<ArticleAtta> findByAidOrTmpID(long aid,long tid);
	public List<ArticleAtta> findByFidAndTmpID(long fid,long tid);
	public List<ArticleAtta> findByArticleAndFileID(long aid,long uid);

}
