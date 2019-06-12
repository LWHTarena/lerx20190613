package com.lerx.dao.impl;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.lerx.dao.iface.IPollDao;
import com.lerx.entities.Poll;

public class PollDaoImpl extends HibernateDaoSupport implements IPollDao {

	@Override
	public Poll modifyPoll(Poll poll, int status) {
		switch (status) {
		case -1:
			poll.setAntis(poll.getAntis()+1);
			break;
		case 1:
			poll.setAgrees(poll.getAgrees()+1);
			break;
		default:
			poll.setPassbys(poll.getPassbys()+1);
		}
		this.getHibernateTemplate().update(poll);
		return poll;
	}

	@Override
	public Poll findByID(long id) {
		return this.getHibernateTemplate().get(Poll.class, id);
	}

}
