package com.lerx.handlers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lerx.dao.iface.IArticleDao;
import com.lerx.dao.iface.IBaseDao;
import com.lerx.dao.iface.ITempletPortalMainDao;
import com.lerx.dao.iface.ITempletWarePortalDao;
import com.lerx.entities.Article;
import com.lerx.entities.TempletPortalMain;
import com.lerx.entities.TempletWarePortal;
import com.lerx.sys.util.MavUtil;

@RequestMapping("/action_update")
@Controller
public class UpdateHandler {
	
	@Autowired
	private ITempletPortalMainDao templetPortalMainDaoImpl;
	
	@Autowired
	private ITempletWarePortalDao templetWarePortalDaoImpl;
	
	@Autowired
	private IArticleDao articleDaoImpl;
	
	
	@Autowired
	private IBaseDao baseDaoImpl;
	
	@RequestMapping("/start")
	public ModelAndView start() {
		int prob=0;
		List<TempletPortalMain> list = templetPortalMainDaoImpl.queryAll();
		TempletWarePortal ware;
		
		for (TempletPortalMain templet:list) {
			ware=templetWarePortalDaoImpl.findByTempletID(templet.getId());
			if (ware==null) {
				ware=new TempletWarePortal();
				ware.setTemplet(templet);
				templetWarePortalDaoImpl.add(ware);
				prob++;
			}
		}
		
		String agSQL="update article_group set templetID=0 where templetID is null";
		baseDaoImpl.excuteBySql(agSQL);
		
		List<Long> laid= articleDaoImpl.queryAllPassedID(0);
		for (long aid:laid) {
			Article art=articleDaoImpl.findByID(aid);
			articleDaoImpl.modify(art);
			prob++;
		}
		
		String totalIn6HoursSQL="update visitor_ip_record set totalIn6Hours=1 where totalIn6Hours is null";
		baseDaoImpl.excuteBySql(totalIn6HoursSQL);
		
		/*List<ArticleGroup> listag = groupDaoImpl.queryByParentID(0, false, false, 0);
		
		for (ArticleGroup ag:listag) {
			ag.setCoerce(false);
			groupDaoImpl.modify(ag);
		}*/
		
		return MavUtil.mav("jsp/result/success", prob+" problems(TempletWarePortal and Articles) were successfully fixed!") ;
	}
	

}
