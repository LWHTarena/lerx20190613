package com.lerx.dao.iface;

import java.util.List;

import com.lerx.entities.PollIPRecord;
import com.lerx.hql.entities.Rs;

public interface IPollIPRecordDao {
	
	public PollIPRecord add(PollIPRecord pr);
	public PollIPRecord findLast(long pid);
	public List<PollIPRecord> findSameIPAfterDatetime(long pid,long datetime,String ip);
	public Rs findSameIPAfterDatetime(long pid,long datetime,String ip,int page, int pagesize);
	public Rs find(long pid,int page, int pagesize);

}
