package com.lerx.dao.iface;

import com.lerx.entities.TempletDownCode;
import com.lerx.hql.entities.Rs;

public interface ITempletDownCodeDao {
	
	public TempletDownCode add(TempletDownCode tdc);
	public void modify(TempletDownCode tdc);
	public TempletDownCode findByID(long id);
	public TempletDownCode findByCode(String code);
	public boolean delByID(long id);
	public Rs find(int typeNum,long tid,int page,int pagesize);

}
