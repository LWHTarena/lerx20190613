package com.lerx.dao.iface;

import com.lerx.entities.AlbumGenre;
import com.lerx.hql.entities.Rs;

public interface IAlbumGenreDao {
	
	public AlbumGenre add(AlbumGenre ag);
	public void modify (AlbumGenre ag);
	public AlbumGenre findByID(long id);
	public AlbumGenre findByName(String name);
	public AlbumGenre findByFolder(String folder);
	public boolean delByID(long id);
	public Rs query(int page,int pagesize,int status,int open);

}
