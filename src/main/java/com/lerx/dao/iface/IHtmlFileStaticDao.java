package com.lerx.dao.iface;

import com.lerx.entities.HtmlFileStatic;

public interface IHtmlFileStaticDao {
	
	public void modify(HtmlFileStatic hfs);
	public HtmlFileStatic findByID(long id);

}
