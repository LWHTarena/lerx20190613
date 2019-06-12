package com.lerx.handlers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lerx.analyze.util.AnalyzeUtil;
import com.lerx.dao.iface.IAlbumDao;
import com.lerx.dao.iface.IAlbumGenreDao;
import com.lerx.dao.iface.IHtmlFileStaticDao;
import com.lerx.dao.iface.IPortalDao;
import com.lerx.dao.iface.IRoleDao;
import com.lerx.dao.iface.ITempletAlbumGenreMainDao;
import com.lerx.dao.iface.IUserDao;
import com.lerx.dao.iface.IVisitArchivesDao;
import com.lerx.dao.iface.IVisitorCountInPeriodDao;
import com.lerx.dao.iface.IVisitorIPRecordDao;
import com.lerx.dao.iface.IVisitorsBookDao;
import com.lerx.entities.Album;
import com.lerx.entities.AlbumGenre;
import com.lerx.entities.TempletAlbumGenreMain;
import com.lerx.entities.TempletSubElement;
import com.lerx.entities.User;
import com.lerx.portal.obj.EnvirSet;
import com.lerx.portal.obj.GlobalTagsAnalyzeReturn;
import com.lerx.portal.obj.UserPanelCodeArgs;
import com.lerx.sys.util.HttpUtil;
import com.lerx.v5.util.HttpShowUtil;
import com.lerx.v5.util.TempletAlbumGenreUtil;
import com.lerx.v5.util.UserUtil;

@RequestMapping("/show_albgenre")
@Controller
public class HttpShowAlbgenreHandler {
	
	
	@Autowired
	private ResourceBundleMessageSource messageSource;
	
	@Autowired
	private IPortalDao portalDaoImpl;
	
	@Autowired
	private IAlbumGenreDao albumGenreDaoImpl;
	
	@Autowired
	private IAlbumDao albumDaoImpl;
	
	@Autowired
	private IUserDao userDaoImpl;
	
	@Autowired
	private IRoleDao roleDaoImpl;
	
	@Autowired
	private ITempletAlbumGenreMainDao templetAlbumGenreMainDaoImpl;
	
	@Autowired
	private IVisitorIPRecordDao visitorIPRecordDaoImpl;

	@Autowired
	private IVisitorsBookDao visitorsBookDaoImpl;

	@Autowired
	private IVisitArchivesDao visitArchivesDaoImpl;

	@Autowired
	private IVisitorCountInPeriodDao visitorCountInPeriodDaoImpl;

	@Autowired
	private IHtmlFileStaticDao htmlFileStaticDaoImpl;

	
	private EnvirSet envirInit(long gid, long aid, HttpServletRequest request, HttpServletResponse response) {
		EnvirSet es = new EnvirSet();
		es.setStartTime(System.currentTimeMillis());
		if (request == null) {
			es.setRequest(HttpUtil.currRequest());
		} else {
			es.setRequest(request);
		}
		if (response == null) {
			es.setResponse(HttpUtil.currResponse());
		} else {
			es.setResponse(response);
		}
		es.setGid(gid);
		es.setAid(aid);
		es.setMessageSource(messageSource);
		es.setPortalDaoImpl(portalDaoImpl);
		es.setAlbumGenreDaoImpl(albumGenreDaoImpl);
		es.setTempletAlbumGenreMainDaoImpl(templetAlbumGenreMainDaoImpl);
		es.setUserDaoImpl(userDaoImpl);
		es.setRoleDaoImpl(roleDaoImpl);
		es.setVisitorIPRecordDaoImpl(visitorIPRecordDaoImpl);
		es.setVisitorsBookDaoImpl(visitorsBookDaoImpl);
		es.setVisitArchivesDaoImpl(visitArchivesDaoImpl);
		es.setVisitorCountInPeriodDaoImpl(visitorCountInPeriodDaoImpl);
		es.setHtmlFileStaticDaoImpl(htmlFileStaticDaoImpl);

		return es;
	}
	
	/*
	 * 用户面板
	 */
	@ResponseBody
	@RequestMapping("/panel")
	public UserPanelCodeArgs panel(Long gid, HttpServletRequest request, HttpServletResponse response,
			@CookieValue(value = "username_lerx", required = false) String username_lerx,
			@CookieValue(value = "password_lerx", required = false) String password_lerx,
			@RequestParam(value = "currEl", required = false) String currEl) {
		UserPanelCodeArgs upca=new UserPanelCodeArgs();
		upca.setStatus(0);
		if (gid == null) {
			gid = 0L;
		}
		EnvirSet es = envirInit(gid, 0, request, response);
		AlbumGenre ag=albumGenreDaoImpl.findByID(gid);
		TempletAlbumGenreMain template = ag.getTemplet();
		if (template == null) {
			template = templetAlbumGenreMainDaoImpl.findDef();
		}
		long uid = UserUtil.loginRefresh(es, username_lerx, password_lerx);
		TempletSubElement el = TempletAlbumGenreUtil.elInitByTagStr(template, currEl);
		String panelCode;
		if (uid > 0L) {
			panelCode = el.getUserPanelCodeLogin();
			User user = userDaoImpl.findByID(uid);
			if (user != null) {
				String imgHtmlTemplet = TempletAlbumGenreUtil.sundriesTag(messageSource, template, "htmlTemplet_img");
				panelCode = UserUtil.fmt(panelCode, user, imgHtmlTemplet);
				upca.setStatus(1);
				upca.setUid(user.getId());
				upca.setUsername(user.getUsername());
			}
			String myAlbums;
			
			List<Album> la=albumDaoImpl.findByUid(uid, gid, 0);
			if (la.isEmpty()) {
				myAlbums="";
			}else {
				String myAlbumLoop = TempletAlbumGenreUtil.sundriesTag(messageSource, template, "template_myalbum_loop");
				String tmp,tmpAll="";
				for (Album alb:la) {
					tmp=myAlbumLoop;
					tmp = AnalyzeUtil.replace(tmp, "tag", "id", ""+alb.getId());
					tmp = AnalyzeUtil.replace(tmp, "tag", "name", alb.getName());
					tmpAll += tmp;
				}
				myAlbums=tmpAll;
			}
			
			panelCode = AnalyzeUtil.replace(panelCode, "tag", "myAlbums", myAlbums);

		} else {
			panelCode = el.getUserPanelCodeLogout();
			String token = HttpUtil.buildToken();
			if (panelCode == null) {
				panelCode = "";
			}
			panelCode = AnalyzeUtil.replace(panelCode, "tag", "token", token);
		}
		
		panelCode = TempletAlbumGenreUtil.tagToEnvir(panelCode, es);
		panelCode = AnalyzeUtil.replace(panelCode, "tag", "gid", ""+gid);
		upca.setHtml(panelCode);
		return upca;
	}
	
	/*
	 * 首页
	 */
	@ResponseBody
	@RequestMapping(value = "/index/{id}")
	public String index(@PathVariable(value = "id", required = false) Long id,HttpServletRequest request) {
		if (id == null) {
			id = 0L;
		}
		
		EnvirSet es = envirInit(id, 0, request, null);
		
		GlobalTagsAnalyzeReturn gtar = HttpShowUtil.albgenreGlobalTagsAnalyze(es, "index");
		es = gtar.getEs();
		
		String html = gtar.getHtml();
		html = TempletAlbumGenreUtil.tagToEnvir(html, es);
		if (es == null) {
			return html;
		}
		return html;
	}

}
