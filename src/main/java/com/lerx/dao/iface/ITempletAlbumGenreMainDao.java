package com.lerx.dao.iface;

import java.util.List;

import com.lerx.entities.TempletAlbumGenreMain;
import com.lerx.hql.entities.Rs;

public interface ITempletAlbumGenreMainDao {
	
	public long add(TempletAlbumGenreMain templet);
	public boolean delByID(long id);
	public void modify(TempletAlbumGenreMain templet);
	public TempletAlbumGenreMain findByID(long id);
	public TempletAlbumGenreMain findByUuid(String uuid);
	public List<TempletAlbumGenreMain> findByTitle(String title);
	public Rs query(int page,int pagesize);
	public Rs queryByAllow(int page,int pagesize);
	public List<TempletAlbumGenreMain> queryAll();
	public void setDef(long id);
	public TempletAlbumGenreMain findDef();

}
