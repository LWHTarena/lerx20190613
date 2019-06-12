package com.lerx.handlers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lerx.annotation.Token;
import com.lerx.dao.iface.IArticleDao;
import com.lerx.dao.iface.IHtmlFileStaticDao;
import com.lerx.entities.Article;
import com.lerx.entities.HtmlFileStatic;
import com.lerx.portal.obj.FileEl;
import com.lerx.portal.obj.LongPollingReturn;
import com.lerx.sys.util.FileUtil;
import com.lerx.sys.util.MavUtil;
import com.lerx.v5.util.ArticleUtil;

@RequestMapping("/action_static")
@Controller
public class HtmlHandler {
	
	@Autowired
	private IArticleDao articleDaoImpl;
	
	@Autowired
	private IHtmlFileStaticDao htmlFileStaticDaoImpl;
	
	@Autowired
	private ResourceBundleMessageSource messageSource;

	private static final String ADMINFORBID = "_admin.forbid_";
	
	@RequestMapping(value = "/start")
	@Token(ajax = false, admin = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
  	public ModelAndView start(Map<String, Object> map) {
		map.put("spacePercent", 0);
		
		return MavUtil.mav("jsp/article/static", "");
		
	}
  	
		
	
	
  	//静态化
  	@ResponseBody
  	@RequestMapping(value = "/art/{lastid}/{finished}/{total}")
  	@Token(ajax = false, admin = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
  	public LongPollingReturn staticFiles(@PathVariable(value = "lastid", required = false) Long lastid,@PathVariable(value = "finished", required = false) Integer finished,@PathVariable(value = "total", required = false) Integer total,HttpServletRequest request,HttpSession session) {
  		LongPollingReturn lpr = new LongPollingReturn();
  		if (total==null) {
  			total=0;
  		}
  		
  		if (lastid==null) {
  			lastid=0L;
  		}
  		
  		if (finished==null) {
  			finished=0;
  		}
  		
  		List<Long> list;
  		if (lastid==0L) {
  			list=articleDaoImpl.queryAllPassedID(0);
  			total=list.size();
  		}else {
  			list=articleDaoImpl.queryAllPassedID(lastid);
  		}
  		lpr.setTag(1);
  		lpr.setTotal(total);
  		long startT=System.currentTimeMillis();
  		boolean jump;
  		for (long aid:list) {
  			finished++;
  			Article art = articleDaoImpl.findByID(aid);
  			jump=false;
  			if (art.getJumpUrl()!=null && !art.getJumpUrl().trim().equals("")) {
  				jump=true;
  			}
  			if (art==null || jump) {
  				continue;
  			}else {
  			}
  			if (messageSource.getMessage("file.html.art.folder.rebulid", null, "false", null).trim().equalsIgnoreCase("true")) {
  				FileEl fe=ArticleUtil.feBuild(messageSource, request, art,false);
  				HtmlFileStatic hfs=art.getHfs();
  				hfs=art.getHfs();
  				hfs.setRealPath(fe.getRealPath());
  				hfs.setUrl(fe.getUrl());
  				hfs.setFilename(FileUtil.getFileFromPath(fe.getRealPath()));
  				htmlFileStaticDaoImpl.modify(hfs);
  			}
  			
  			ArticleUtil.htmlCreate(request, messageSource, art);
  			art.getHfs().setStatus(true);
  			articleDaoImpl.modify(art);
  			lpr.setLastID(aid);
  			lpr.setFinished(finished);
  			lpr.setMsg(art.getSubject());
  			long endT=System.currentTimeMillis();
  			if (endT - startT >= 3000 ) {
  				break;
  			}
  		}
  		return lpr;
  	}

}
