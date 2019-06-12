package com.lerx.dao.iface;

import com.lerx.entities.VisitArchives;

public interface IVisitArchivesDao {
	
	public VisitArchives add(VisitArchives va);
	public void modify(VisitArchives va);
	public VisitArchives findByID(long id);
	public VisitArchives findByDayKey(long vid,int dayKey);

}
