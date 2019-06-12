package com.lerx.dao.iface;

import com.lerx.entities.UploadedFile;
import com.lerx.hql.entities.Rs;

public interface IUploadedFileDao {

	public long add(UploadedFile uf);
	public boolean delByID(long id);
	public boolean delByURL(String url);
	public void modify(UploadedFile uf);
	public UploadedFile findByID(long id);
	public UploadedFile findByMD5(String md5,long excludedID);
	public UploadedFile findByURL(String url);
	public Rs query(int page,int pagesize);
	public long countByUid(long uid);
}
