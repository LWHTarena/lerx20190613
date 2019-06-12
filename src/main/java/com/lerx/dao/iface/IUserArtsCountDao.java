package com.lerx.dao.iface;

import com.lerx.entities.User;
import com.lerx.entities.UserArtsCount;
import com.lerx.hql.entities.Rs;

public interface IUserArtsCountDao {
	public boolean modify(UserArtsCount uac);
	public UserArtsCount findByUK(User user,int timeKey);
	public Rs findTopByUKAndGroup(int groupId,int timeKey,int mod,int page,
			int pageSize);
	public Rs findTopByUKAndGroup(String gidStr,int timeKey,int mod,int page,
			int pageSize);
	public long countByUid(long uid);
}
