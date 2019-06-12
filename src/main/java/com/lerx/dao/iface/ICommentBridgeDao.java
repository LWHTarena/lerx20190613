package com.lerx.dao.iface;

import com.lerx.entities.CommentBridge;

public interface ICommentBridgeDao {
	
	public void modify(CommentBridge cb);
	public CommentBridge findByID(long id);
	public long countByUid(long uid);

}
