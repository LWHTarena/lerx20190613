package com.lerx.dao.iface;

import java.util.List;

import com.lerx.entities.Album;
import com.lerx.hql.entities.Rs;

public interface IAlbumDao {
	
	public Album add(Album album);
	public void modify (Album album);
	public Album findByID(long id);
	public Album findByName(String name);
	public boolean delByID(long id);
	public Rs query(int page,int pagesize,int status, int open,int orderBy,int orderDirection);
	public List<Album> findByUid(long uid,long gid,int status);

}
