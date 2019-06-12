package com.lerx.dao.iface;

import java.util.List;

import com.lerx.entities.TempletPortalMain;
import com.lerx.hql.entities.Rs;

public interface ITempletPortalMainDao {
	
	public long add(TempletPortalMain templet);
	public boolean delByID(long id);
	public void modify(TempletPortalMain templet);
	public TempletPortalMain findByID(long id);
	public TempletPortalMain findByUuid(String uuid);
	public List<TempletPortalMain> findByTitle(String title);
	public Rs query(int page,int pagesize);
	public Rs queryByAllow(int page,int pagesize);
	public List<TempletPortalMain> queryAll();
	public List<TempletPortalMain> queryAllUsable();
	public void setDef(long id);
	public TempletPortalMain findDef();

}
