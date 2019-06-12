package com.lerx.v5.util;

import com.lerx.dao.iface.IArticleAttaDao;
import com.lerx.entities.Article;
import com.lerx.entities.ArticleAtta;
import com.lerx.sys.util.StringUtil;

public class ArtAttaUtil {
	
	
	/*
	 * 根据多个附件的id字符串，检查是否有文章（新加的文章中因加之前无id，无法指定附件的文章），特用此方法更新
	 */
	public static void refresh(Article art,String attaStr,IArticleAttaDao articleAttaDaoImpl){
		if (attaStr!=null && !attaStr.trim().equals("")){
			String[] attas = attaStr.split(",");
			for (int i = 0; i < attas.length; i++) {
				if (attas[i]!=null && !attas[i].trim().equals("") && StringUtil.isNumber(attas[i])){
					ArticleAtta aa=articleAttaDaoImpl.findByID(Long.valueOf(attas[i]));
					if (aa.getArticle()==null){
						aa.setArticle(art);
						articleAttaDaoImpl.modify(aa);
					}
				}
				
			}
		}
	}

}
