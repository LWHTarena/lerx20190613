package com.lerx.dao.iface;

import com.lerx.entities.VisitorsBook;

public interface IVisitorsBookDao {
	
	public void modify(VisitorsBook vbook);
	public VisitorsBook findByID(long id);

}
