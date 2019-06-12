package com.lerx.dao.iface;

import com.lerx.entities.VisitorCountInPeriod;

public interface IVisitorCountInPeriodDao {
	
	public VisitorCountInPeriod add(VisitorCountInPeriod vcip);
	public void modify(VisitorCountInPeriod vcip);
	public VisitorCountInPeriod findByID(long id);
	public VisitorCountInPeriod findByPeriod(long vid,int hour);
	public long count(long vid,int hour);
	public long sum(long vid,int hour,int mode);

}
