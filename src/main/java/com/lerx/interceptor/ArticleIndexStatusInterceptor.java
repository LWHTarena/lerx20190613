package com.lerx.interceptor;

import org.hibernate.search.indexes.interceptor.EntityIndexingInterceptor;
import org.hibernate.search.indexes.interceptor.IndexingOverride;

import com.lerx.entities.Article;


/*
 * 配置一个拦截器，当文章的审核状态为true时，加入结果列表，否则剔除
 */
public class ArticleIndexStatusInterceptor implements EntityIndexingInterceptor<Article>{

	@Override
	public IndexingOverride onAdd(Article entity) {
		if(entity.isStatus()) {
            return IndexingOverride.APPLY_DEFAULT;
        }
        return IndexingOverride.SKIP;
	}

	@Override
	public IndexingOverride onCollectionUpdate(Article entity) {
		return onUpdate(entity);
	}

	@Override
	public IndexingOverride onDelete(Article entity) {
		return IndexingOverride.APPLY_DEFAULT;
	}

	@Override
	public IndexingOverride onUpdate(Article entity) {
		if(entity.isStatus()) {
            return IndexingOverride.UPDATE;
        } else {
            return IndexingOverride.REMOVE;
        }
	}
	
	

}
