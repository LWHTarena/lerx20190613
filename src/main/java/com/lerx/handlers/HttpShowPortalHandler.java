package com.lerx.handlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lerx.analyze.util.AnalyzeUtil;
import com.lerx.dao.iface.IArticleDao;
import com.lerx.dao.iface.ICommentBridgeDao;
import com.lerx.dao.iface.ICommentThreadDao;
import com.lerx.dao.iface.IGroupDao;
import com.lerx.dao.iface.IHtmlFileStaticDao;
import com.lerx.dao.iface.IPortalDao;
import com.lerx.dao.iface.IRoleDao;
import com.lerx.dao.iface.ITempletAlbumGenreMainDao;
import com.lerx.dao.iface.ITempletPortalMainDao;
import com.lerx.dao.iface.IUserArtsCountDao;
import com.lerx.dao.iface.IUserDao;
import com.lerx.dao.iface.IVisitArchivesDao;
import com.lerx.dao.iface.IVisitorCountInPeriodDao;
import com.lerx.dao.iface.IVisitorIPRecordDao;
import com.lerx.dao.iface.IVisitorsBookDao;
import com.lerx.entities.Article;
import com.lerx.entities.ArticleGroup;
import com.lerx.entities.CommentBridge;
import com.lerx.entities.CommentThread;
import com.lerx.entities.Portal;
import com.lerx.entities.TempletComment;
import com.lerx.entities.TempletPortalMain;
import com.lerx.entities.TempletSubElement;
import com.lerx.entities.User;
import com.lerx.entities.UserArtsCount;
import com.lerx.entities.VisitArchives;
import com.lerx.entities.VisitorIPRecord;
import com.lerx.entities.VisitorsBook;
import com.lerx.hql.entities.Rs;
import com.lerx.ip.util.IPUtil;
import com.lerx.login.util.LoginUtil;
import com.lerx.portal.obj.CommOut;
import com.lerx.portal.obj.EnvirSet;
import com.lerx.portal.obj.GlobalTagsAnalyzeReturn;
import com.lerx.portal.obj.LayerUserRankRs;
import com.lerx.portal.obj.UserPanelCodeArgs;
import com.lerx.portal.obj.PortalStatInfo;
import com.lerx.portal.obj.TempletCommentFmtRequisite;
import com.lerx.portal.obj.UserArtsRanking;
import com.lerx.sys.obj.DatetimeSpanSuffixTxt;
import com.lerx.sys.obj.EnvParms;
import com.lerx.sys.util.FileUtil;
import com.lerx.sys.util.HttpUtil;
import com.lerx.sys.util.StringUtil;
import com.lerx.sys.util.TimeUtil;
import com.lerx.v5.util.ArticleUtil;
import com.lerx.v5.util.CommUtil;
import com.lerx.v5.util.ConfigUtil;
import com.lerx.v5.util.GroupUtil;
import com.lerx.v5.util.HttpShowUtil;
import com.lerx.v5.util.TempletPortalUtil;
import com.lerx.v5.util.UserUtil;
import com.lerx.v5.util.VisitUtil;

@RequestMapping("/show_portal")
@Controller
public class HttpShowPortalHandler {

	@Autowired
	private ITempletPortalMainDao templetPortalMainDaoImpl;
	
	@Autowired
	private ITempletAlbumGenreMainDao templetAlbumGenreMainDaoImpl;

	@Autowired
	private ResourceBundleMessageSource messageSource;

	@Autowired
	private IPortalDao portalDaoImpl;

	@Autowired
	private IArticleDao articleDaoImpl;

	@Autowired
	private IGroupDao groupDaoImpl;

	@Autowired
	private IUserDao userDaoImpl;

	@Autowired
	private IUserArtsCountDao userArtsCountDaoImp;

	@Autowired
	private IRoleDao roleDaoImpl;

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

	@Autowired
	private ICommentBridgeDao commentBridgeDaoImpl;

	@Autowired
	private ICommentThreadDao commentThreadDaoImpl;

	private EnvirSet envirInit(long gid, long aid, HttpServletRequest request, HttpServletResponse response) {
		EnvirSet es = new EnvirSet();
		es.setStatus(true);
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
		es.setGroupDaoImpl(groupDaoImpl);
		es.setTempletPortalMainDaoImpl(templetPortalMainDaoImpl);
		es.setTempletAlbumGenreMainDaoImpl(templetAlbumGenreMainDaoImpl);
		es.setUserDaoImpl(userDaoImpl);
		es.setUserArtsCountDaoImp(userArtsCountDaoImp);
		es.setArticleDaoImpl(articleDaoImpl);
		es.setRoleDaoImpl(roleDaoImpl);
		es.setVisitorIPRecordDaoImpl(visitorIPRecordDaoImpl);
		es.setVisitorsBookDaoImpl(visitorsBookDaoImpl);
		es.setVisitArchivesDaoImpl(visitArchivesDaoImpl);
		es.setVisitorCountInPeriodDaoImpl(visitorCountInPeriodDaoImpl);
		es.setHtmlFileStaticDaoImpl(htmlFileStaticDaoImpl);

		return es;
	}


	

	/*
	 * 取当前年份
	 */
	@ResponseBody
	@RequestMapping("/date")
	public String date(@RequestParam(value = "fmt", required = false) String fmt) {
		return TimeUtil.coverLongToStr(System.currentTimeMillis(), fmt);
	}

	/*
	 * 
	 */

	@ResponseBody
	@RequestMapping("/addChk")
	public String artAddChk(Long gid, HttpServletRequest request, HttpServletResponse response) {
		if (gid == null) {
			gid = 0L;
		}
		EnvirSet es = envirInit(gid, 0, request, response);
		return UserUtil.maskChkForArtAdd(es);
	}

	// 栏目IP检测
	@ResponseBody
	@RequestMapping("/navIPChk/{id}")
	public boolean navIPChk(@PathVariable(value = "id", required = false) Long id,HttpServletRequest request) {
		ArticleGroup ag = groupDaoImpl.findByID(id);
		if (ag==null) {
			return false;
		}
		if (ag.getIpVisitAllow()==null || ag.getIpVisitAllow().trim().equals("")) {
			return true;
		}else {
			String ip=IPUtil.getRealRemotIP(request);
			if (IPUtil.isInRange(ip, ag.getIpVisitAllow())) {
				return true;
			}else {
				return false;
			}
		}
	}

	// 返回统计信息psi
	@ResponseBody
	@RequestMapping("/portalStat")
	public PortalStatInfo portalStat() {
		PortalStatInfo psi = new PortalStatInfo();
		psi = articleDaoImpl.stat(psi);
		psi = userDaoImpl.stat(psi);
		Portal portal = portalDaoImpl.query_update();
		psi.setViews(portal.getVbook().getViewsTotal());
		psi.setIps(portal.getVbook().getIpTotal());
		// System.out.println(psi.toString());
		return psi;
	}

	
	// 返回当天或某天的访问记录数 在VisitorHandler中也有同样的
	@ResponseBody
	@RequestMapping("/visitQuery")
	public VisitArchives visitQuery(long vid, int dayKey) {
		return VisitUtil.visitQuery(vid, dayKey, visitArchivesDaoImpl);
	}

	// 返回用户发布排行
	@ResponseBody
	@RequestMapping(value = "/rank/{mode}")
	public LayerUserRankRs rank(@PathVariable(value = "mode", required = false) Integer mode,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pagesize", required = false) Integer pagesize) {
		LayerUserRankRs lurr = new LayerUserRankRs();
		if (mode == null) {
			mode = 0;
		}
		if (page == null) {
			page = 1;
		}
		if (pagesize == null) {
			pagesize = 10;
		}
		User user;
		Rs rs;
		int step = 0;
		List<UserArtsRanking> list = new ArrayList<UserArtsRanking>();
		java.text.SimpleDateFormat formatter;
		int timeKey;
		switch (mode) {
		case 1: // 本年度排行
			formatter = new java.text.SimpleDateFormat("yyyy");
			timeKey = Integer.valueOf(formatter.format(System.currentTimeMillis()));
			rs = userArtsCountDaoImp.findTopByUKAndGroup(0, timeKey, 0, page, pagesize);
			break;
		case 2: // 本季度排行
			timeKey = TimeUtil.quarter(System.currentTimeMillis());
			rs = userArtsCountDaoImp.findTopByUKAndGroup(0, timeKey, 0, page, pagesize);
			break;
		case 3: // 本月
			formatter = new java.text.SimpleDateFormat("yyyyMM");
			timeKey = Integer.valueOf(formatter.format(System.currentTimeMillis()));
			rs = userArtsCountDaoImp.findTopByUKAndGroup(0, timeKey, 0, page, pagesize);
			break;
		case 4: // 本周排行
			Date weekStamp = TimeUtil.firstDayAtWeek(new Date(System.currentTimeMillis()));
			formatter = new java.text.SimpleDateFormat("yyyyMMdd");
			timeKey = Integer.valueOf(formatter.format(weekStamp.getTime()));
			rs = userArtsCountDaoImp.findTopByUKAndGroup(0, timeKey, 0, page, pagesize);
			break;
		default: // 文章总通过数
			rs = userDaoImpl.artsPassedRank(page, pagesize);

		}
		UserArtsCount uac;
		for (Object u : rs.getList()) {
			step++;
			UserArtsRanking uar = new UserArtsRanking();
			if (mode == 0) {
				user = (User) u;
				uar.setNum(user.getArtsPassed());
			} else {
				uac = (UserArtsCount) u;
				user = uac.getUser();
				uar.setNum(uac.getArtsPassed());
			}

			uar.setUid(user.getId());
			uar.setUsername(user.getUsername());
			uar.setTrueName(user.getTruename());

			uar.setSn(step);
			list.add(uar);
		}
		lurr.setCount(rs.getCount());
		lurr.setCode(0);
		lurr.setData(list);
		return lurr;
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
		long uid = UserUtil.loginRefresh(es, username_lerx, password_lerx);
		TempletPortalMain template = templetPortalMainDaoImpl.findDef();
		TempletSubElement el = TempletPortalUtil.elInitByTagStr(template, currEl);
		String panelCode;
		if (uid > 0L) {
			panelCode = el.getUserPanelCodeLogin();
			User user = userDaoImpl.findByID(uid);
			if (user != null) {
				String imgHtmlTemplet = TempletPortalUtil.sundriesTag(messageSource, template, "htmlTemplet_img");
				panelCode = UserUtil.fmt(panelCode, user, imgHtmlTemplet);
				upca.setStatus(1);
				upca.setUid(user.getId());
				upca.setUsername(user.getUsername());
			}

		} else {
			panelCode = el.getUserPanelCodeLogout();
			String token = HttpUtil.buildToken();
			if (panelCode == null) {
				panelCode = "";
			}
			panelCode = AnalyzeUtil.replace(panelCode, "tag", "token", token);
		}
		panelCode = TempletPortalUtil.tagToEnvir(panelCode, es);
		upca.setHtml(panelCode);
		return upca;

	}

	/*
	 * 调查投票区域ajax
	 */
	@ResponseBody
	@RequestMapping(value = "/poll/{id}")
	public String poll(@PathVariable(value = "id", required = true) Long id,
			@RequestParam(value = "titleLen", required = false) Integer titleLen,@RequestParam(value = "overMark", required = false) Integer overMark, HttpServletRequest request,
			HttpServletResponse response) {
		EnvParms ep = HttpUtil.epInit(request, response, null, messageSource);
		if (titleLen == null) {
			titleLen = 0;
		}
		if (overMark == null) {
			overMark=1;
		}
		Article art = articleDaoImpl.findByID(id);
		TempletPortalMain template = templetPortalMainDaoImpl.findDef();
		String html = TempletPortalUtil.sundriesTag(messageSource, template, "htmlTemplet_poll");
		String imgHtmlTemplet = TempletPortalUtil.sundriesTag(messageSource, template, "htmlTemplet_img");

		if (art != null && art.getAgroup() != null && art.getPoll() != null && art.getPoll().isStatus()
				&& GroupUtil.poolchk(art.getAgroup())) {
			html = ArticleUtil.fmt(ep, html, art, imgHtmlTemplet, 0, overMark,false);
		} else {
			html = "";
		}

		html = AnalyzeUtil.replace(html, "tag", "contextPath", request.getContextPath());

		return html;
	}

	/*
	 * 评论表单加载
	 */

	@ResponseBody
	@RequestMapping(value = "/commform/{aid}")
	public String commForm(@PathVariable(value = "aid", required = true) Long aid,
			@CookieValue(value = "uid_lerx", required = false) String uid_lerx, HttpServletRequest request,
			HttpSession session, HttpServletResponse response) {
		Portal portal = portalDaoImpl.query_update();
		if (!portal.isComm()) {
			return "";
		}

		if (aid == null) {
			return "";
		}
		Article art = articleDaoImpl.findByID(aid);
		ArticleGroup ag = art.getAgroup();
		if (!ag.isComm()) {
			return "";
		}

		CommentBridge cb = art.getCb();

		cb = commentBridgeDaoImpl.findByID(cb.getId());

		if (!cb.isStatus()) {
			return "";
		}

		TempletPortalMain template = templetPortalMainDaoImpl.findDef();
		TempletComment templatec = template.getElComment();
		String html = templatec.getFormCode();

		html = AnalyzeUtil.replace(html, "tag", "bid", "" + cb.getId());
		html = AnalyzeUtil.replace(html, "tag", "aid", "" + aid);
		EnvirSet es;
		es = envirInit(ag.getId(), aid, request, null);
		html = TempletPortalUtil.tagToEnvir(html, es);
		return html;

	}

	/*
	 * 评论加载
	 */

	@ResponseBody
	@RequestMapping(value = "/commlist/{id}")
	public CommOut commlist(@PathVariable(value = "id", required = true) Long id,
			@RequestParam(value = "aid", required = false) Long aid,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@CookieValue(value = "uid_lerx", required = false) String uid_lerx, HttpServletRequest request,
			HttpSession session, HttpServletResponse response) {
		CommOut co = new CommOut();
		co.setCount(0);
		co.setPage(1);
		co.setPageCount(0);
		co.setHtml("");
		co.setPageSize(pageSize);
		Portal portal = portalDaoImpl.query_update();
		if (!portal.isComm()) {
			return co;
		}

		CommentBridge cb = null;
		if (id == null) {
			return co;
		}
		cb = commentBridgeDaoImpl.findByID(id);

		if (cb == null || !cb.isStatus()) {
			return co;
		}

		if (aid == null) {
			return co;
		} else {
			Article art = articleDaoImpl.findByID(aid);

			if (art == null) {
				return co;
			} else {
				ArticleGroup ag = art.getAgroup();
				if (ag == null || !ag.isComm()) {
					return co;
				}
			}
		}

		if (page == null) {
			page = 0;
		}

		if (pageSize == null) {
			pageSize = 10;
		}
		boolean deletedShow = false;
		User user = null;
		if (uid_lerx != null && !uid_lerx.trim().equals("") && StringUtil.isNumber(uid_lerx)) {
			long uid = Long.valueOf(uid_lerx);
			user = userDaoImpl.findByID(uid);
		}
		boolean master = false;
		/*
		 * if ((user!=null && cb.getUser()!=null && (user.getId() - cb.getUser().getId()
		 * == 0)) || LoginUtil.admin0Chk(messageSource, session)) { master=true;
		 * 
		 * }
		 */

		if (user != null && cb.getUser() != null) {
			if ((user.getId() - cb.getUser().getId() == 0)) {
				master = true;
			}
			if (UserUtil.isadmin(user)) {
				master = true;
				deletedShow = true;
			}
		}
		int showMode = 1;
		if (master) {
			showMode = 0;
		}

		Rs rs = commentThreadDaoImpl.queryByBid(cb.getId(), page, pageSize, false, showMode, deletedShow);
		TempletPortalMain template = templetPortalMainDaoImpl.findDef();
		TempletComment templatec = template.getElComment();
		String lf =templatec.getItemLoopCode();
		if (lf==null || lf.trim().equals("")) {
			lf = TempletPortalUtil.sundriesTag(messageSource, template, "htmlTemplet_comm_loop");
		}
		String tmp, tmpAll = "";
		String imgHtmlTemplet = TempletPortalUtil.sundriesTag(messageSource, template, "htmlTemplet_img");
		String funHtmlDel = templatec.getActDelCode();
		String funHtmlPass = templatec.getActPassCode();
		String replyAdd = templatec.getActReplyCode();
		/*
		 * String funHtmlDel = TempletUtil.sundriesTag(messageSource,template,
		 * "htmlTemplet_comm_loop_act_del"); String funHtmlPass =
		 * TempletUtil.sundriesTag(messageSource,template,
		 * "htmlTemplet_comm_loop_act_pass"); String replyAdd =
		 * TempletUtil.sundriesTag(messageSource,template,
		 * "htmlTemplet_comm_loop_reply_add");
		 */
		String anonymity = TempletPortalUtil.sundriesTag(messageSource, template, "anonymity");
		String headDef = TempletPortalUtil.sundriesTag(messageSource, template, "headDef");
		DatetimeSpanSuffixTxt dsst = TempletPortalUtil.sundriesTimesLostedTag(template);
		TempletCommentFmtRequisite tcfr = new TempletCommentFmtRequisite();

		tcfr.setFunHtmlPass(funHtmlPass);
		tcfr.setImgHtmlTemplet(imgHtmlTemplet);
		tcfr.setReplyAdd(replyAdd);
		tcfr.setLf(lf);
		tcfr.setMaster(master);
		tcfr.setDsst(dsst);
		tcfr.setAnonymity(anonymity);
		tcfr.setHeadDef(headDef);
		tcfr.setFunHtmlDel(funHtmlDel);
		tcfr.setAvatarNull(ConfigUtil.getAvatarNullFile(messageSource, request));
		for (Object o : rs.getList()) {
			CommentThread ct = (CommentThread) o;
			tcfr.setLf(lf);
			tmp = CommUtil.fmt(tcfr, ct);
			tmpAll += tmp;
		}

		tmpAll = AnalyzeUtil.replace(tmpAll, "tag", "contextPath", request.getContextPath());
		co.setHtml(tmpAll);
		co.setPageCount(rs.getPageCount());
		co.setCount(rs.getCount());
		return co;

	}

	/*
	 * 会员发布排行
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/rank")
	public String rank(@RequestParam(value = "pageSize", required = false) Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		return "";
	}

	/*
	 * 栏目热点文章 byclicks 指按点击量
	 */
	@ResponseBody
	@RequestMapping(value = "/hot/{gid}")
	public String hot(@PathVariable(value = "gid", required = true) Long gid,
			@RequestParam(value = "byclicks", required = false) Integer byclicks,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "titleLen", required = false) Integer titleLen,
			@RequestParam(value = "overMark", required = false) Integer overMark,HttpServletRequest request,
			HttpServletResponse response) {

		EnvParms ep = HttpUtil.epInit(request, response, null, messageSource);
		if (titleLen == null) {
			titleLen = 0;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		if (overMark==null) {
			overMark=1;
		}
		TempletPortalMain template = templetPortalMainDaoImpl.findDef();
		String lf = TempletPortalUtil.sundriesTag(messageSource, template, "htmlTemplet_hot");
		Rs rs;
		if (byclicks == null) {
			byclicks = 0;
		}
		if (byclicks == 1) {
			rs = articleDaoImpl.clicksByGid(gid, 0, 1, pageSize);
		} else {
			rs = articleDaoImpl.hotByGid(gid, 0, 1, pageSize);
		}

		String html = "", tmp;
		@SuppressWarnings("unchecked")
		List<Article> artList = (List<Article>) rs.getList();
		for (Article art : artList) {
			tmp = lf;
			tmp = ArticleUtil.fmt(ep, tmp, art, "", titleLen,overMark, false);
			html += tmp;
		}
		return html;

	}

	/*
	 * 随机取文章
	 */
	@ResponseBody
	@RequestMapping(value = "/rand/{gid}")
	public String rand(@PathVariable(value = "gid", required = true) Long gid,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "titleLen", required = false) Integer titleLen,
			@RequestParam(value = "overMark", required = false) Integer overMark,HttpServletRequest request,
			HttpServletResponse response) {
		EnvParms ep = HttpUtil.epInit(request, response, null, messageSource);
		if (titleLen == null) {
			titleLen = 0;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		if (overMark == null) {
			overMark=1;
		}
		TempletPortalMain template = templetPortalMainDaoImpl.findDef();
		String lf = TempletPortalUtil.sundriesTag(messageSource, template, "htmlTemplet_hot");
		Rs rs;
		rs = articleDaoImpl.randByGid(gid, 1, pageSize);

		String html = "", tmp;
		@SuppressWarnings("unchecked")
		List<Article> artList = (List<Article>) rs.getList();
		for (Article art : artList) {
			tmp = lf;
			tmp = ArticleUtil.fmt(ep, tmp, art, "", titleLen,overMark, false);
			html += tmp;
		}
		return html;

	}

	/*
	 * 个人热点文章 byclicks 指按点击量
	 */
	@ResponseBody
	@RequestMapping(value = "/personalHot/{uid}/{gid}")
	public String personalHot(@PathVariable(value = "uid", required = true) Long uid,
			@PathVariable(value = "gid", required = false) Long gid,
			@RequestParam(value = "byclicks", required = false) Integer byclicks,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "titleLen", required = false) Integer titleLen,
			@RequestParam(value = "overMark", required = false) Integer overMark,HttpServletRequest request,
			HttpServletResponse response) {
		EnvParms ep = HttpUtil.epInit(request, response, null, messageSource);
		if (titleLen == null) {
			titleLen = 0;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		if (gid == null) {
			gid = 0L;
		}
		if (overMark==null) {
			overMark=1;
		}
		TempletPortalMain template = templetPortalMainDaoImpl.findDef();
		String lf = TempletPortalUtil.sundriesTag(messageSource, template, "htmlTemplet_hot");
		Rs rs;
		if (byclicks == null) {
			byclicks = 0;
		}
		if (byclicks == 1) {
			rs = articleDaoImpl.clicksByUid(uid, gid, 0, 1, pageSize);
		} else {
			rs = articleDaoImpl.hotByUid(uid, gid, 0, 1, pageSize);
		}

		String html = "", tmp;
		@SuppressWarnings("unchecked")
		List<Article> artList = (List<Article>) rs.getList();
		for (Article art : artList) {
			tmp = lf;
			tmp = ArticleUtil.fmt(ep, tmp, art, "", titleLen,overMark, false);
			html += tmp;
		}
		return html;

	}

	/*
	 * 首页被阅读信息
	 */
	@ResponseBody
	@RequestMapping(value = "/indexViewShow")
	public String indexVisitfun(@RequestParam(value = "url", required = false) String url,
			@RequestParam(value = "referer", required = false) String referer, HttpServletRequest request,
			HttpServletResponse response) {
		String html = "";
		EnvirSet es = envirInit(0, 0, request, response);
		Portal portal = portalDaoImpl.query_update();
		VisitorsBook vbook = portal.getVbook();
		VisitUtil.visitorRefresh(es, vbook, "111", url, referer); // 更新浏览者信息
		return html;
	}

	/*
	 * 栏目页被阅读信息
	 */
	@ResponseBody
	@RequestMapping(value = "/navViewShow/{id}")
	public String navVisitfun(@PathVariable(value = "id", required = true) Long id,
			@RequestParam(value = "url", required = false) String url,
			@RequestParam(value = "referer", required = false) String referer, HttpServletRequest request,
			HttpServletResponse response) {
		String html = "";
		if (id == null || id == 0) {
			return html;
		}
		int hotnOfVisit,hotnOfIP;
		hotnOfVisit=Integer.valueOf(messageSource.getMessage("vbook.new.visitor.value", null, "0", null));
		hotnOfIP=Integer.valueOf(messageSource.getMessage("vbook.new.ip.value", null, "5", null));
		ArticleGroup agroup = groupDaoImpl.findByID(id);
		long gid = agroup.getId();
		EnvirSet es = envirInit(gid, 0, request, response);
		Portal portal = portalDaoImpl.query_update();
		VisitorsBook vbook = portal.getVbook();
		VisitUtil.visitorRefresh(es, vbook, "101", url, referer); // 更新浏览者信息
		vbook = agroup.getVbook();
		boolean newip=VisitUtil.visitorRefresh(es, vbook, "111", url, referer); // 更新浏览者信息
		HttpShowUtil.updateAgHotn(groupDaoImpl, agroup, newip, hotnOfVisit, hotnOfIP);
		ArticleGroup group = groupDaoImpl.findByID(gid);
		List<ArticleGroup> list = groupDaoImpl.queryParentBySubID(group.getId(), 1);
		for (ArticleGroup g : list) {
			vbook = g.getVbook();
			newip=VisitUtil.visitorRefresh(es, vbook, "110", url, referer); // 更新浏览者信息
			HttpShowUtil.updateAgHotn(groupDaoImpl, g, newip, hotnOfVisit, hotnOfIP);
		}
		html = TempletPortalUtil.sundriesTag(messageSource, templetPortalMainDaoImpl.findDef(), "htmlTemplet_view");
		/*
		 * ,htmlTemplet_view:浏览数：{$tag\58views$} IP数：{$tag\58ips$}
		 * 上次来访IP：{$tag\58lastip$}
		 */
		// html = TempletUtil.escape(html);
		html = AnalyzeUtil.replace(html, "tag", "views", "" + agroup.getVbook().getViewsTotal());
		html = AnalyzeUtil.replace(html, "tag", "ips", "" + agroup.getVbook().getIpTotal());
		html = AnalyzeUtil.replace(html, "tag", "vbID", agroup.getVbook().getId());
		VisitorIPRecord vir = es.getVisitorIPRecordDaoImpl().findLast(agroup.getVbook().getId());
		// IPUtil.ipFilter(vir.getIp(),messageSource.getMessage("ip.filter.mask", null,
		// "1111",null) );
		html = AnalyzeUtil.replace(html, "tag", "lastip",
				IPUtil.ipFilter(vir.getIp(), messageSource.getMessage("ip.filter.mask", null, "1111", null)));
		return html;
	}

	/*
	 * 文章被阅读信息
	 */
	@ResponseBody
	@RequestMapping(value = "/artViewShow/{id}")
	public String artVisitfun(@PathVariable(value = "id", required = true) Long id,
			@RequestParam(value = "url", required = false) String url,
			@RequestParam(value = "referer", required = false) String referer, HttpServletRequest request,
			HttpServletResponse response) {
		String html = "";
		if (id == null || id == 0) {
			return html;
		}
		Article art = articleDaoImpl.findByID(id);
		if (art == null || art.getAgroup() == null) {
			return html;
		}
		long gid = art.getAgroup().getId();
		EnvirSet es = envirInit(gid, 0, request, response);
		Portal portal = portalDaoImpl.query_update();
		int hotnOfVisit,hotnOfIP;
		hotnOfVisit=Integer.valueOf(messageSource.getMessage("vbook.new.visitor.value", null, "0", null));
		hotnOfIP=Integer.valueOf(messageSource.getMessage("vbook.new.ip.value", null, "5", null));
		boolean newip;
		VisitorsBook vbook = portal.getVbook();
		VisitUtil.visitorRefresh(es, vbook, "101", url, referer); // 更新浏览者信息
		vbook = art.getVbook();
		newip=VisitUtil.visitorRefresh(es, vbook, "101", url, referer); // 更新浏览者信息
		HttpShowUtil.updateArtHotn(articleDaoImpl, art, newip, hotnOfVisit, hotnOfIP);
		
		ArticleGroup group = groupDaoImpl.findByID(gid);
		vbook = group.getVbook();
		newip=VisitUtil.visitorRefresh(es, vbook, "111", url, referer); // 更新浏览者信息
		HttpShowUtil.updateAgHotn(groupDaoImpl, group, newip, hotnOfVisit, hotnOfIP);
		
		List<ArticleGroup> list = groupDaoImpl.queryParentBySubID(group.getId(), 1);
		for (ArticleGroup g : list) {
			vbook = g.getVbook();
			newip=VisitUtil.visitorRefresh(es, vbook, "110", url, referer); // 更新浏览者信息
			HttpShowUtil.updateAgHotn(groupDaoImpl, g, newip, hotnOfVisit, hotnOfIP);
		}
		html = TempletPortalUtil.sundriesTag(messageSource, templetPortalMainDaoImpl.findDef(), "htmlTemplet_view");
		/*
		 * ,htmlTemplet_view:浏览数：{$tag\58views$} IP数：{$tag\58ips$}
		 * 上次来访IP：{$tag\58lastip$}
		 */
		// html = TempletUtil.escape(html);
		html = AnalyzeUtil.replace(html, "tag", "views", "" + art.getVbook().getViewsTotal());
		html = AnalyzeUtil.replace(html, "tag", "ips", "" + art.getVbook().getIpTotal());
		html = AnalyzeUtil.replace(html, "tag", "vbID", art.getVbook().getId());
		VisitorIPRecord vir = es.getVisitorIPRecordDaoImpl().findLast(art.getVbook().getId());
		// IPUtil.ipFilter(vir.getIp(),messageSource.getMessage("ip.filter.mask", null,
		// "1111",null) );
		html = AnalyzeUtil.replace(html, "tag", "lastip",
				IPUtil.ipFilter(vir.getIp(), messageSource.getMessage("ip.filter.mask", null, "1111", null)));
		return html;
	}

	/*
	 * 文章编辑功能区域
	 */
	@ResponseBody
	@RequestMapping(value = "/artfun/{id}")
	public String artfun(@PathVariable(value = "id", required = true) Long id, HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@CookieValue(value = "username_lerx", required = false) String username_lerx,
			@CookieValue(value = "password_lerx", required = false) String password_lerx) {

		String areaHtml = "";

		if (id == null || id == 0) {
			return areaHtml;
		}
		Article art = articleDaoImpl.findByID(id);
		if (art == null || art.getAgroup() == null) {
			return areaHtml;
		}

		long gid = art.getAgroup().getId();

		boolean backadmin = LoginUtil.adminChk(messageSource, session);

		EnvirSet es = envirInit(gid, 0, request, response);
		long uid = UserUtil.loginRefresh(es, username_lerx, password_lerx);
		User user = userDaoImpl.findByID(uid);
		boolean forePower = false;
		if (user != null && user.getRole() != null && user.getRole().getMask() != null) {

			forePower = GroupUtil.auditMaskChk(art.getAgroup(), user.getRole().getMask());
		}

		if (!backadmin && (user == null || user.getRole() == null)) {
			return areaHtml;
		}

		if (backadmin || forePower) {

			String msg;
			// 审核
			String passHtmlTemplet = TempletPortalUtil.sundriesTag(messageSource, templetPortalMainDaoImpl.findDef(),
					"htmlTemplet_artpass");
			if (art.isStatus()) {
				msg = TempletPortalUtil.sundriesTag(messageSource, templetPortalMainDaoImpl.findDef(), "unpass");
			} else {
				msg = TempletPortalUtil.sundriesTag(messageSource, templetPortalMainDaoImpl.findDef(), "pass");
			}

			// passHtmlTemplet = TempletUtil.escape(passHtmlTemplet);

			passHtmlTemplet = AnalyzeUtil.replace(passHtmlTemplet, "tag", "status", "" + !art.isStatus());
			passHtmlTemplet = AnalyzeUtil.replace(passHtmlTemplet, "tag", "title", msg);

			passHtmlTemplet = passHtmlTemplet.replaceAll("\r|\n", "");
			areaHtml += passHtmlTemplet;

		}

		if (UserUtil.isadmin(user)) {
			String msg;
			String topOneHtmlTemplet = TempletPortalUtil.sundriesTag(messageSource, templetPortalMainDaoImpl.findDef(),
					"htmlTemplet_topOne");
			if (art.isTopOne()) {
				msg = TempletPortalUtil.sundriesTag(messageSource, templetPortalMainDaoImpl.findDef(), "unTopOne");
			} else {
				msg = TempletPortalUtil.sundriesTag(messageSource, templetPortalMainDaoImpl.findDef(), "topOne");
			}

			topOneHtmlTemplet = AnalyzeUtil.replace(topOneHtmlTemplet, "tag", "status", "" + !art.isTopOne());
			topOneHtmlTemplet = AnalyzeUtil.replace(topOneHtmlTemplet, "tag", "title", msg);

			topOneHtmlTemplet = topOneHtmlTemplet.replaceAll("\r|\n", "");
			areaHtml += topOneHtmlTemplet;

		}

		if (!forePower && (art.getUser() != null && art.getUser().getId() - uid == 0)) {
			forePower = true;
		}

		if (forePower) {
			// 编辑
			String editHtmlTemplet = TempletPortalUtil.sundriesTag(messageSource, templetPortalMainDaoImpl.findDef(),
					"htmlTemplet_edit");
			editHtmlTemplet = TempletPortalUtil.escape(editHtmlTemplet);
			String msg = TempletPortalUtil.sundriesTag(messageSource, templetPortalMainDaoImpl.findDef(), "edit");
			editHtmlTemplet = AnalyzeUtil.replace(editHtmlTemplet, "tag", "title", msg);
			areaHtml += editHtmlTemplet;

		}

		areaHtml = AnalyzeUtil.replace(areaHtml, "tag", "aid", "" + art.getId());
		areaHtml = AnalyzeUtil.replace(areaHtml, "tag", "subject", art.getSubject());

		return areaHtml;
	}

	/*
	 * private String artTagsAnalyze(String html,Article art,EnvirSet es) { String
	 * uidStr=HttpUtil.getCookie(messageSource, es.getRequest(), "uid_lerx"); String
	 * passHtmlTemplet;
	 * 
	 * long uid; User user=null; if (uidStr!=null && StringUtil.isNumber(uidStr)){
	 * uid = Long.valueOf(uidStr); }else{ uid = 0; } if (uid>0L) { user =
	 * userDaoImpl.findByID(uid); } if (user!=null &&
	 * GroupUtil.auditMaskChk(art.getAgroup(), user.getRole().getMask())) { String
	 * msg; passHtmlTemplet=TempletUtil.sundriesTag(
	 * templetPortalMainDaoImpl.findDef().getSundriesTag(), "htmlTemplet_artpass");
	 * if (art.isStatus()) { msg=TempletUtil.sundriesTag(
	 * templetPortalMainDaoImpl.findDef().getSundriesTag(), "unpass"); }else {
	 * msg=TempletUtil.sundriesTag(
	 * templetPortalMainDaoImpl.findDef().getSundriesTag(), "pass"); }
	 * 
	 * passHtmlTemplet =AnalyzeUtil.replace(passHtmlTemplet, "tag", "aid",
	 * ""+art.getId()); passHtmlTemplet =AnalyzeUtil.replace(passHtmlTemplet, "tag",
	 * "msg", msg);
	 * 
	 * }else { passHtmlTemplet=""; } html =AnalyzeUtil.replace(html, "tag",
	 * "auditArea", passHtmlTemplet); return html; }
	 */
	/*private GlobalTagsAnalyzeReturn globalTagsAnalyze(EnvirSet es, String station) {
		GlobalTagsAnalyzeReturn gtar = new GlobalTagsAnalyzeReturn();
		gtar.setEs(es);
		TempletPortalMain template = templetPortalMainDaoImpl.findDef();
		if (template == null) {
			gtar.setHtml(HttpShowUtil.nullTemplate(messageSource));

			return gtar;
		}
		TempletPortalSubElement elTemplate = TempletUtil.elInitByTagStr(template, station);
		String html = HttpShowUtil.htmlInit(template, elTemplate,messageSource);
		html = StringUtil.clear65279(html);
		String imgHtmlTemplet = TempletUtil.sundriesTag(messageSource, template, "htmlTemplet_img");
		// 优先处理文章
		Article artCurr;
		EnvParms ep = HttpUtil.epInit(es.getRequest(), es.getResponse(), null, messageSource);
		if (es.getAid() > 0L) {
			artCurr = es.getArticleDaoImpl().findByID(es.getAid());
			// 这个地方要完整替换标签
			html = ArticleUtil.fmt(ep, html, artCurr, imgHtmlTemplet, 0,1, true);
		}
		ArticleGroup agCurr = null;
		if (es.getGid() > 0L) {
			html = AnalyzeUtil.replace(html, "tag", "gid", "" + es.getGid());
			agCurr = es.getGroupDaoImpl().findByID(es.getGid());
			if (agCurr != null) {
				html = GroupUtil.fmt(html, agCurr);
			}
		} else {
			html = AnalyzeUtil.replace(html, "tag", "gid", "0");
		}
		String delimiter = TempletUtil.sundriesTag(messageSource, template, "delimiter");
		Set<FindedDataAnalyzeResult> fdarSet = AnalyzeUtil.find(html);
		if (fdarSet != null && fdarSet.size() > 0) {
			for (FindedDataAnalyzeResult fdar : fdarSet) {
				if (fdar != null) {
					fdar = TempletUtil.fmt(fdar, template, elTemplate); // 此处的fdar中的dsp中的loopStr中必定有格式化字符串
					
					 * if (fdar != null && fdar.getDsp() != null &&
					 * fdar.getDsp().getDataSource().trim().equalsIgnoreCase(
					 * "articles_from_nav_curr")) {
					 * 
					 * }
					 
					
					 * if (fdar.getDsp() != null) { if (es.getPage() > 1) {
					 * fdar.getDsp().setCurpage(es.getPage()); } if (es.getPageSize() > 0) {
					 * fdar.getDsp().setPagesize(es.getPageSize()); } }
					 

					if (fdar == null || fdar.getDsp() == null || fdar.getDsp().getLoopFormatStr() == null) {
						continue;
					}
					html = TempletUtil.tagToData(html, es, fdar);

					// 栏目处理
					if (es.getGid() > 0L) {
						if (fdar.getDsp().getGid() == 0) {
							fdar.getDsp().setGid(es.getGid());
						}

						if (agCurr != null) {
							if (agCurr.getHtmlOwn() != null && !agCurr.getHtmlOwn().trim().equals("")) {
								html = AnalyzeUtil.replace(html, "code", "htmlOwn", agCurr.getHtmlOwn());
							} else {
								html = AnalyzeUtil.replace(html, "code", "htmlOwn", "");
							}

						}

						html = TempletUtil.tagToNavs(html, es, fdar);
					}

				}

			}
		}

		if (es.getGid() > 0L) {
			if (es.getAid() == 0) {
				html = AnalyzeUtil.replace(html, "tag", "station",
						GroupUtil.locationStr(es, "<a href=\"{$tag:href$}\" >{$tag:name$}</a>", delimiter, false));
			} else {
				html = AnalyzeUtil.replace(html, "tag", "station",
						GroupUtil.locationStr(es, "<a href=\"{$tag:href$}\" >{$tag:name$}</a>", delimiter, true));
			}

		}

		html = AnalyzeUtil.replace(html, "tag", "pageCurr", TempletUtil.sundriesTag(template, station));
		html = AnalyzeUtil.replace(html, "tag", "delimiter", delimiter);
		html = AnalyzeUtil.replace(html, "tag", "hrefTarget", StringUtil.nullFilter2(elTemplate.getTargetStr()));
		gtar.setHtml(html);
		gtar.setEs(es);
		return gtar;
	}*/
	
	private class HtmlInit{
		
		private boolean status;
		private String html;
		private TempletPortalMain template;
		public boolean isStatus() {
			return status;
		}
		public void setStatus(boolean status) {
			this.status = status;
		}
		public String getHtml() {
			return html;
		}
		public void setHtml(String html) {
			this.html = html;
		}
		public TempletPortalMain getTemplate() {
			return template;
		}
		public void setTemplate(TempletPortalMain template) {
			this.template = template;
		}
		
	}
	
	private HtmlInit envChk(EnvirSet es,GlobalTagsAnalyzeReturn gtar) {
		HtmlInit hi=new HtmlInit();
		String html = gtar.getHtml();
		es = gtar.getEs();
		html = TempletPortalUtil.tagToEnvir(html, es);
		hi.setHtml(html);
		hi.setStatus(true);
		if (es == null) {
			hi.setStatus(false);
			return hi;
		}
		TempletPortalMain template = templetPortalMainDaoImpl.findDef();
		hi.setTemplate(template);
		if (template==null) {
			hi.setStatus(false);
		}
		return hi;
	}

	/*
	 * 首页
	 */
	@ResponseBody
	@RequestMapping("/index")
	public String index(HttpServletRequest request) {
		
		EnvirSet es = envirInit(0, 0, request, null);
		GlobalTagsAnalyzeReturn gtar = HttpShowUtil.portalGlobalTagsAnalyze(es, "index");
		String html;
		HtmlInit hi = envChk(es,gtar);
		if (!hi.isStatus()) {
			return hi.getHtml();
		}else {
			html = hi.getHtml();
		}
		Portal portal = portalDaoImpl.query_update();
		VisitorsBook vbook = portal.getVbook();
		html = AnalyzeUtil.replace(html, "tag", "vbookID", "" + vbook.getId());
		
		html = AnalyzeUtil.replace(html, "tag", "station",
				TempletPortalUtil.sundriesTag(messageSource, hi.getTemplate(), "index"));
		return html;
	}

	/*
	 * 分类页
	 */
	@ResponseBody
	@RequestMapping(value = "/nav/{id}")
	public String nav(@PathVariable(value = "id", required = false) Long id, HttpServletRequest request,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {
		if (id == null) {
			id = 0L;
		}
		if (page == null) {
			page = 0;
		}

		if (pageSize == null) {
			pageSize = 0;
		}

		EnvirSet es = envirInit(id, 0, request, null);
		es.setPage(page);
		es.setPageSize(pageSize);
		GlobalTagsAnalyzeReturn gtar = HttpShowUtil.portalGlobalTagsAnalyze(es, "nav");
		
		String html;
		HtmlInit hi = envChk(es,gtar);
		if (!hi.isStatus()) {
			return hi.getHtml();
		}else {
			html = hi.getHtml();
		}
		
		ArticleGroup ag = es.getGroupDaoImpl().findByID(id);
		if (ag==null) {
			html = TempletPortalUtil.tagToEnvir(FileUtil.readRes(messageSource, "template_res404"), es);
			return html;
		}
		
		//
		long currUid;
		String uidStr = HttpUtil.getCookie(messageSource, request, "uid_lerx");
		if (uidStr != null && !uidStr.trim().equals("") && StringUtil.isNumber(uidStr)) {
			currUid = Long.valueOf(uidStr);
		} else {
			currUid = 0L;
		}
		boolean forbid=false;
		if (currUid==0L) {
			forbid=true;
		}else {
			User user=userDaoImpl.findByID(currUid);
			if (user==null || !user.isState() || (user.getRole()!=null && !user.getRole().isStatus())) {
				forbid=true;
			}
		}
		es.setUid(currUid);
		
		if (!GroupUtil.openChk(ag) && forbid ) {
			html = TempletPortalUtil.tagToEnvir(FileUtil.readRes(messageSource, "template_forbid"), es);
			return html;
		}
		//
		
		if (ag.getJumpToUrl()!=null && !ag.getJumpToUrl().trim().equals("")) {
			html = TempletPortalUtil.tagToEnvir(FileUtil.readRes(messageSource, "htmlTemplet_forward"), es);
			html =  AnalyzeUtil.replace(html, "tag", "returnUrl", ag.getJumpToUrl());
			return html;
		}
		
		if (ag != null && ag.getVbook() != null) {
			VisitorsBook vbook = ag.getVbook();
			html = AnalyzeUtil.replace(html, "tag", "vbookID", "" + vbook.getId());
		} else {
			html = AnalyzeUtil.replace(html, "tag", "vbookID", "0");
		}

		html = AnalyzeUtil.replace(html, "tag", "mappCurr",
				es.getRequest().getContextPath() + "/show_portal/nav/" + id);
		return html;
	}

	/*
	 * 文章编辑页
	 */
	@ResponseBody
	@RequestMapping(value = "/edit/{id}")
	public String edit(@PathVariable(value = "id", required = false) Long id, HttpServletRequest request) {
		Article art = null;
		long gid = 0L;
		if (id == null) {
			id = 0L;
		} else {
			art = articleDaoImpl.findByID(id);
			if (art != null && art.getAgroup() != null) {
				gid = art.getAgroup().getId();
			}

		}

		if (art == null) {
			art = new Article();
			art.setId(0);
		}

		String uidStr = HttpUtil.getCookie(messageSource, request, "uid_lerx");
		long uid;
		if (uidStr != null && !uidStr.trim().equals("") && StringUtil.isNumber(uidStr)) {
			uid = Long.valueOf(uidStr);
		} else {
			uid = 0L;
		}
		User user = userDaoImpl.findByID(uid);
		String mask = "";

		if (user != null && user.getRole() != null && user.getRole().getMask() != null) {
			mask = user.getRole().getMask();
		}
		EnvirSet es;
		es = envirInit(gid, id, request, null);
		GlobalTagsAnalyzeReturn gtar = HttpShowUtil.portalGlobalTagsAnalyze(es, "edit");
		es = gtar.getEs();
		String html;
		HtmlInit hi = envChk(es,gtar);
		if (!hi.isStatus()) {
			return hi.getHtml();
		}else {
			html = hi.getHtml();
		}
		
		if (id > 0 && gid > 0L) {
			if (!GroupUtil.auditMaskChk(art.getAgroup(), mask) && art.getUser().getId() - uid != 0) {
				html = TempletPortalUtil.sundriesTag(messageSource, hi.getTemplate(), "resultHtml");
				if (html == null || html.trim().equals("")) {
					html = FileUtil.readRes(messageSource, "template_result");
				}
				html = StringUtil.clear65279(html);
				html = AnalyzeUtil.replace(html, "tag", "referer", request.getHeader("Referer"));
				html = AnalyzeUtil.replace(html, "tag", "returnUrl", request.getHeader("Referer"));
				html = AnalyzeUtil.replace(html, "tag", "msg", messageSource.getMessage("fail.permission", null,
						"You have no permissions for the current operation!", null));
				return html;
			}
		}

//		html = globalTagsAnalyze(es, "edit");
		html = TempletPortalUtil.tagToEnvir(html, es);
		return html;
	}

	/*
	 * 文章页跳转
	 */
	@ResponseBody
	@RequestMapping(value = "/jump/{id}")
	public String jump(@PathVariable(value = "id", required = false) Long id, HttpServletRequest request) {
		if (id == null) {
			id = 0L;
		}
		long gid;
		Article art = articleDaoImpl.findByID(id);
		EnvirSet es;
		
		if (art == null) {
			es = envirInit(0, id, request, null);
			String html = TempletPortalUtil.tagToEnvir(FileUtil.readRes(messageSource, "template_res404"), es);
			return html;
		}else {
			gid = art.getAgroup().getId();
			es = envirInit(gid, id, request, null);
		}
		boolean jump = false;
		if (art.getJumpUrl() != null && !art.getJumpUrl().trim().equals("")) {
			jump = true;
		}
		if (jump) {
			
			art.setViews(art.getViews() + 1);
			
			GlobalTagsAnalyzeReturn gtar = HttpShowUtil.portalGlobalTagsAnalyze(es, "art");
			es = gtar.getEs();
			Portal portal = portalDaoImpl.query_update();
			VisitorsBook vbook = portal.getVbook();
			String url = request.getRequestURL().toString();
			String referer = request.getHeader("referer");
			VisitUtil.visitorRefresh(es, vbook, "101", url, referer); // 更新浏览者信息
			vbook = art.getVbook();
			VisitUtil.visitorRefresh(es, vbook, "101", url, referer); // 更新浏览者信息
			ArticleGroup group = groupDaoImpl.findByID(gid);
			vbook = group.getVbook();
			VisitUtil.visitorRefresh(es, vbook, "111", url, referer); // 更新浏览者信息
			return art.getJumpUrl();
		} else {
			return request.getContextPath() + "/show_portal/art/" + id;
		}
	}

	/*
	 * 文章页显示
	 */
	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@RequestMapping(value = "/art/{id}")
	public String art(@PathVariable(value = "id", required = false) Long id, HttpServletRequest request,HttpSession session) {
		if (id == null) {
			id = 0L;
		}
		
		Article art = articleDaoImpl.findByID(id);
		
		long gid = 0;
		if (art != null && art.getAgroup() != null) {
			gid = art.getAgroup().getId();
		}
		String html;
		EnvirSet es;
		es = envirInit(gid, id, request, null);
		if (art==null) {
			html = TempletPortalUtil.tagToEnvir(FileUtil.readRes(messageSource, "template_res404"), es);
			return html;
		}
		
		GlobalTagsAnalyzeReturn gtar = HttpShowUtil.portalGlobalTagsAnalyze(es, "art");
		HtmlInit hi = envChk(es,gtar);
		if (!hi.isStatus()) {
			return hi.getHtml();
		}else {
			html = hi.getHtml();
		}
		
		String uidStr = HttpUtil.getCookie(messageSource, request, "uid_lerx");
		long uid;
		if (uidStr != null && !uidStr.trim().equals("") && StringUtil.isNumber(uidStr)) {
			uid = Long.valueOf(uidStr);
		} else {
			uid = 0L;
		}
		User user = userDaoImpl.findByID(uid);
		boolean show=false;
		
		if (art.isStatus() && GroupUtil.statusChk(art.getAgroup())) {
			//如果文章状态已审，且栏目组级父组均状态正常
			show=true;
		}
		if (!show && LoginUtil.adminChk(messageSource, session)) {
			//如果是后台管理员
			show=true;
		}
		if (!show && user!=null && UserUtil.isadmin(user)) {
			//如果是前台管理员
			show=true;
		}
		if (!show && user!=null && user.getRole() != null && GroupUtil.auditMaskChk(art.getAgroup(), user.getRole().getMask())) {
			//如果前台用户在所属用户组有权限
			show=true;
		}
		if (!show && user!=null && art.getUser()!=null && art.getUser().getId() - user.getId()==0) {
			//如果是作者本人
			show=true;
		}
		
		if (show) {
			
			if (es.getRequest() == null) {
				System.out.println("es.getRequest() is null!");
			}
			if (es.getRequest().getContextPath() == null) {
				System.out.println("es.getRequest().getContextPath() is null!");
			}
			html = AnalyzeUtil.replace(html, "tag", "mappCurr",
					es.getRequest().getContextPath() + "/show_portal/art/" + id);
			if (art != null && art.getVbook() != null) {
				VisitorsBook vbook = art.getVbook();
				html = AnalyzeUtil.replace(html, "tag", "vbookID", "" + vbook.getId());
			} else {
				html = AnalyzeUtil.replace(html, "tag", "vbookID", "0");
			}

		} else {
			es = envirInit(0, id, request, null);
			html = FileUtil.readRes(messageSource, "template_res404");
		}
		if (art != null && art.getMediaUrl() != null && !art.getMediaUrl().trim().equals("")
				&& art.getMediaUrl().trim().length() > 3) {
			String videoHtmlTemplet = TempletPortalUtil.sundriesTag(messageSource, templetPortalMainDaoImpl.findDef(),
					"template_video");

			videoHtmlTemplet = AnalyzeUtil.replace(videoHtmlTemplet, "tag", "media", art.getMediaUrl().trim());

			html = AnalyzeUtil.replace(html, "code", "mediaPlayer", videoHtmlTemplet);

		} else {
			html = AnalyzeUtil.replace(html, "code", "mediaPlayer", "");
		}

		html = TempletPortalUtil.tagToEnvir(html, es);
		return html;
	}

	/*
	 * 文集
	 */
	@ResponseBody
	@RequestMapping(value = "/corpus/{uid}")
	public String corpus(@PathVariable(value = "uid", required = false) Long uid,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize, HttpServletRequest request) {
		EnvirSet es;
		String html;
		if (page == null) {
			page = 0;
		}
		if (pageSize == null) {
			pageSize = 0;
		}
		es = envirInit(0, 0, request, null);
		es.setPage(page);
		es.setPageSize(pageSize);
		boolean own = false;
		long currUid;
		String uidStr = HttpUtil.getCookie(messageSource, request, "uid_lerx");
		if (uidStr != null && !uidStr.trim().equals("") && StringUtil.isNumber(uidStr)) {
			currUid = Long.valueOf(uidStr);
		} else {
			currUid = 0L;
		}
		if (uid == null || uid == 0L) {

			own = true;
			uid = currUid;
			es.setPower(true);
		} else {
			if (uid - currUid == 0) {
				own = true;
				es.setPower(true);
			}
		}
		es.setUid(uid);
		
		String ownname = "null";
		User user = userDaoImpl.findByID(uid);
		if (user != null) {
			ownname = user.getUsername();
		}else {
			html = TempletPortalUtil.tagToEnvir(FileUtil.readRes(messageSource, "template_res404"), es);
			return html;
		}
		GlobalTagsAnalyzeReturn gtar = HttpShowUtil.portalGlobalTagsAnalyze(es, "corpus");
		es = gtar.getEs();
		
		HtmlInit hi = envChk(es,gtar);
		if (!hi.isStatus()) {
			return hi.getHtml();
		}else {
			html = hi.getHtml();
		}

		if (own) {
			html = AnalyzeUtil.replace(html, "tag", "ownname",
					messageSource.getMessage("title.own", null, "My ", null));
		} else {
			html = AnalyzeUtil.replace(html, "tag", "ownname", ownname);
			
		}

		html = AnalyzeUtil.replace(html, "tag", "station",
				messageSource.getMessage("title.corpus.personal", null, "Personal corpus", null));
		html = AnalyzeUtil.replace(html, "tag", "ownUid", "" + uid);
		html = AnalyzeUtil.replace(html, "tag", "mappCurr",
				es.getRequest().getContextPath() + "/show_portal/corpus/" + uid);
		return html;
	}

}
