package com.lerx.dao.iface;

import com.lerx.entities.TempletWarePortal;
import com.lerx.hql.entities.Rs;

public interface ITempletWarePortalDao {
	
	public TempletWarePortal add (TempletWarePortal ware);
	public void modify (TempletWarePortal ware);
	public TempletWarePortal findByID(long id);
	public boolean delByID(long id);
	public Rs query(int firstResult,int page,int pagesize,int orderby);
	public TempletWarePortal findByTempletID(long tid);

}
