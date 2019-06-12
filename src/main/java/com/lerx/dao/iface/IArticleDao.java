package com.lerx.dao.iface;

import java.util.List;

import com.lerx.entities.Article;
import com.lerx.hql.entities.Rs;
import com.lerx.portal.obj.PortalStatInfo;

public interface IArticleDao {
	
	public Article add (Article art);
	public void modify (Article g);
	public Article findByID(long id);
	public boolean delByID(long id);
	public Rs queryByGid(long gid,int firstResult,int page,int pagesize,boolean asc,int status,int img);
	public Rs queryByUid(long uid,long gid,int firstResult,int page,int pagesize,boolean asc,int status);
	public Rs hotByGid(long gid,int firstResult,int page,int pagesize);
	public Rs clicksByGid(long gid,int firstResult,int page,int pagesize);
	public Rs randByGid(long gid,int page,int pagesize);
	public Rs hotByUid(long uid,long gid,int firstResult,int page,int pagesize);
	public Rs clicksByUid(long uid,long gid,int firstResult,int page,int pagesize);
	public Rs search(long gid,String key,boolean fulltext,int firstResult,int page,int pagesize);
	public Rs statByUser(long gid,int page,int pagesize);
	public PortalStatInfo stat(PortalStatInfo psi);
	public long countByUid(long uid);
	public void modifyTopOne(long id,boolean state);
	public List<Long> queryAllPassedID(long startID);

}
