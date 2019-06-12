package com.lerx.dao.iface;

import com.lerx.entities.Portal;

public interface IPortalDao {
	
	public Portal add(Portal portal);
	public Portal findByID(long id);
	public void modify(Portal portal);
	public Portal query_update();

}
