package com.lerx.dao.iface;

import java.util.List;

import com.lerx.entities.VisitorIPRecord;
import com.lerx.hql.entities.Rs;

public interface IVisitorIPRecordDao {
	
	public VisitorIPRecord add(VisitorIPRecord vipr);
	public void modify(VisitorIPRecord vipr);
	public VisitorIPRecord findByID(long id);
	public VisitorIPRecord findLast(long bid);
	public List<VisitorIPRecord> findSameIPAfterDatetime(long bid,long datetime,String ip);
	public Rs findSameIPAfterDatetime(long bid,long datetime,String ip,int page, int pagesize);
	public Rs find(long bid,int page, int pagesize);
	public long currPeriod(long bid,int stamp);

}
