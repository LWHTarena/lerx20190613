package com.lerx.dao.iface;

import com.lerx.entities.Poll;

public interface IPollDao {
	
	public Poll modifyPoll(Poll poll,int status);
	public Poll findByID(long id);
	

}
