package com.lerx.dao.iface;

import java.util.List;

import com.lerx.entities.TempletAlbumMain;
import com.lerx.hql.entities.Rs;

public interface ITempletAlbumMainDao {
	
	public long add(TempletAlbumMain templet);
	public boolean delByID(long id);
	public void modify(TempletAlbumMain templet);
	public TempletAlbumMain findByID(long id);
	public TempletAlbumMain findByUuid(String uuid);
	public List<TempletAlbumMain> findByTitle(String title);
	public Rs query(int page,int pagesize);
	public Rs queryByAllow(int page,int pagesize);
	public List<TempletAlbumMain> queryAll();
	public void setDef(long id);
	public TempletAlbumMain findDef();

}
