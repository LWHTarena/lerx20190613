package com.lerx.handlers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lerx.annotation.Token;
import com.lerx.aop.args.CutTags;
import com.lerx.dao.iface.IArticleAttaDao;
import com.lerx.dao.iface.IArticleDao;
import com.lerx.dao.iface.IGroupDao;
import com.lerx.dao.iface.IHtmlFileStaticDao;
import com.lerx.dao.iface.IPortalDao;
import com.lerx.dao.iface.IUserArtsCountDao;
import com.lerx.dao.iface.IUserDao;
import com.lerx.entities.Article;
import com.lerx.entities.ArticleGroup;
import com.lerx.entities.CommentBridge;
import com.lerx.entities.HtmlFileStatic;
import com.lerx.entities.Poll;
import com.lerx.entities.Portal;
import com.lerx.entities.Role;
import com.lerx.entities.User;
import com.lerx.entities.VisitorsBook;
import com.lerx.hql.entities.Rs;
import com.lerx.hql.util.RsUtil;
import com.lerx.login.util.LoginUtil;
import com.lerx.portal.obj.ArticleGroupMapCreateArgs;
import com.lerx.portal.obj.FileEl;
import com.lerx.sys.util.FileUtil;
import com.lerx.sys.util.HttpUtil;
import com.lerx.sys.util.MavUtil;
import com.lerx.sys.util.StringUtil;
import com.lerx.sys.util.TimeUtil;
import com.lerx.v5.util.ArtAttaUtil;
import com.lerx.v5.util.ArticleUtil;
import com.lerx.v5.util.GroupUtil;
import com.lerx.v5.util.UserUtil;

@RequestMapping("/action_article")
@Controller
public class ArticleHandler {
	
	/*private static final String SUCCESS = "jsp/result/success";
	private static final String FAILED = "jsp/result/failed";*/
	
	private static final String ARTICLELIST = "jsp/article/list";
	private static final String ARTICLEEDIT = "jsp/article/add";
	private static final String LOGINPAGE = "jsp/user/login";
	private static final String ADMINFORBID = "_admin.forbid_";
	private static final String FORBIDPAGE = "jsp/result/forbid";
	
	@Autowired
	private IPortalDao portalDaoImpl;
	
	@Autowired
	private IGroupDao groupDaoImpl;
	
	@Autowired
	private IArticleDao articleDaoImpl;
	
	@Autowired
	private IUserDao userDaoImpl;
	
	@Autowired
	private IUserArtsCountDao userArtsCountDaoImp;
	
	@Autowired
	private IArticleAttaDao articleAttaDaoImpl;
	
	@Autowired
	private IHtmlFileStaticDao htmlFileStaticDaoImpl;
	
	@Autowired
	private ResourceBundleMessageSource messageSource;
	
	@ModelAttribute
	public void getArticle(@RequestParam(value = "id", required = false) Long id, Map<String, Object> map) {
		if (id != null && id > 0) {
			map.put("article", articleDaoImpl.findByID(id));
		}
	}
	
	@RequestMapping("/beforeAdd")
	public String beforeAdd(@RequestParam(value = "gid", required = false) Long gid,Map<String, Object> map,HttpServletRequest request,HttpSession session) {
		String uidStr=HttpUtil.getCookie(messageSource, request, "uid_lerx");
		long uid;
		if (uidStr!=null  && StringUtil.isNumber(uidStr)){
			uid = Long.valueOf(uidStr);
		}else{
			uid = 0;
		}
		
		boolean free=false;
		String mask="";
		Role role;
		if (LoginUtil.adminChk(messageSource, session)) {
			free=true;
		}
		if (uid>0L) {
			User user=userDaoImpl.findByID(uid);
			role=user.getRole();
			if (role!=null) {
				if (role.getMask()!=null && (role.getMask().trim().equals("0") || role.getMask().trim().equals("a0"))) {
					free=true;
				}else {
					mask = role.getMask();
				}
			}
		}
		
		Article article = new Article();
		article.setStatus(true);
		if (gid==null) {
			gid=0L;
			
		}
		if (gid>0) {
			ArticleGroup ag=groupDaoImpl.findByID(gid);
			if (ag!=null) {
				article.setAgroup(ag);
			}
		}
		map.put("article", article);
		long currTime=System.currentTimeMillis();
		map.put("currTime", currTime);
		String rootTitle=messageSource.getMessage("root.select.title", null, "Root", null);
		ArticleGroupMapCreateArgs agmca = new ArticleGroupMapCreateArgs();
		agmca.setCurrRoleMask(mask);
		agmca.setCurrRoot(null);
		agmca.setFree(free);
		agmca.setGroupDaoImpl(groupDaoImpl);
		agmca.setMap(map);
		agmca.setRootTitle(rootTitle);
		agmca.setStatus(1);
		agmca.setMessageSource(messageSource);
		agmca.setRequest(request);
		
		GroupUtil.mapCreate(agmca);
		
		return ARTICLEEDIT;
	}
	
	@RequestMapping("/add")
	@Token(ajax=false,token=true,loginOrAdmin=true,log=true,mark="article--<add>",failedPage=LOGINPAGE)
	public ModelAndView add(Article article, String attasStr,Errors result,@CookieValue(value="uid_lerx",required=false) String uid_lerx, HttpServletRequest request,HttpSession session,Map<String, Object> map, CutTags tags) {
		Portal portal =portalDaoImpl.query_update();
		
		article=ArticleUtil.validate(messageSource, article);
		User user=null;
		Boolean admin=false;
		if (uid_lerx!=null && !uid_lerx.trim().equals("") && StringUtil.isNumber(uid_lerx)) {
			long uid=Long.valueOf(uid_lerx);
			user=userDaoImpl.findByID(uid);
			
			
			Role role=null;
			if (user!=null) {
				role=user.getRole();
			}
			
			String mask="";
			if (role!=null) {
				mask = role.getMask();
			}
			if (GroupUtil.auditMaskChk(article.getAgroup(), mask)) {
				
				admin=true;
			}
		}
		
		
		if (article.getId()==0) {
			
			CommentBridge cb=new CommentBridge();
			Poll poll=new Poll();
			poll.setStatus(portal.isPoll());
			poll.setObjTitle(article.getSubject());
			ArticleGroup agroup=article.getAgroup();
			if (agroup==null) {
				return MavUtil.mav("jsp/result/failed", messageSource.getMessage("fail.nav.null", null, "You do not have a choice of columns!", null)) ;
			}
			agroup=groupDaoImpl.findByID(article.getAgroup().getId());
			poll.setStatus(agroup.isPoll());
			cb.setStatus(agroup.isComm());
			
			if (portal.isArtPassAuto()) {
				article.setStatus(true);
			}else {
				article.setStatus(false);
			}
			
			article.setCreationTime(System.currentTimeMillis());
			article.setCb(cb);
			article.setPoll(poll);
			
			if (user!=null) {
				
				article.setUser(user);
				cb.setUser(user);
				
				if (admin) {
					article.setStatus(true);
				}
				
				UserUtil.uacUpdate(user, article, userArtsCountDaoImp, 0, 1);
				user.setArtsTotal(user.getArtsTotal() + 1);
				
				if (article.isStatus()) {
					user.setArtsPassed(user.getArtsPassed() + 1);
					UserUtil.uacUpdate(user, article, userArtsCountDaoImp, 1, 1);
				}
				
				userDaoImpl.modify(user);
				
			}
			
			VisitorsBook vbook = new VisitorsBook();
			vbook.setObjType(2);
			vbook.setObjTitle(article.getSubject());
			article.setVbook(vbook);
			
			HtmlFileStatic hfs = new HtmlFileStatic();
			article.setHfs(hfs);
			article=articleDaoImpl.add(article);
			groupDaoImpl.modifyChanged(article.getAgroup());
			FileEl fe=ArticleUtil.feBuild(messageSource, request, article,false);
			hfs=article.getHfs();
			hfs.setRealPath(fe.getRealPath());
			hfs.setUrl(fe.getUrl());
			hfs.setFilename(FileUtil.getFileFromPath(fe.getRealPath()));
			htmlFileStaticDaoImpl.modify(hfs);
			if (article.isStatus()) {
				
				boolean jump=false;
				if (article.getJumpUrl()!=null && !article.getJumpUrl().trim().equals("")) {
	  				jump=true;
	  			}
				if (!jump) {
					ArticleUtil.htmlCreate(request, messageSource, article);
					article.getHfs().setStatus(true);
					htmlFileStaticDaoImpl.modify(article.getHfs());
				}
			}
			ArtAttaUtil.refresh(article, attasStr, articleAttaDaoImpl);
			
			return MavUtil.mav("jsp/article/continue", messageSource.getMessage("success.article.add", null, "The article is published successfully!", null),article.getAgroup().getId()) ;
		}else{
			if (article.isStatus() && admin) {
				article.setStatus(true);
			}else if(!portal.isArtPassAuto()) {
				article.setStatus(false);
			}
			articleDaoImpl.modify(article);
			if (article.isStatus()) {
				ArticleUtil.htmlCreate(request, messageSource, article);
				article.getHfs().setStatus(true);
				htmlFileStaticDaoImpl.modify(article.getHfs());
			}else {
				FileUtil.delete(article.getHfs().getRealPath());
			}
			ArtAttaUtil.refresh(article, attasStr, articleAttaDaoImpl);
			return MavUtil.mav("jsp/result/success", messageSource.getMessage("success.article.add", null, "The article is published successfully!", null)) ;
		}
		
		//mapCreate(map,groupDaoImpl,group);
	}
	
	
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") Long id, Map<String, Object> map,HttpServletRequest request,HttpSession session) {
		
		
		String uidStr=HttpUtil.getCookie(messageSource, request, "uid_lerx");
		long uid;
		if (uidStr!=null  && StringUtil.isNumber(uidStr)){
			uid = Long.valueOf(uidStr);
		}else{
			uid = 0;
		}
		boolean free=false;
		String mask="";
		Role role;
		if (LoginUtil.adminChk(messageSource, session)) {
			free=true;
		}
		if (uid>0L) {
			User user=userDaoImpl.findByID(uid);
			role=user.getRole();
			if (role!=null) {
				mask = role.getMask();
				if (role.getMask()!=null && (role.getMask().trim().equals("0") || role.getMask().trim().equals("a0"))) {
					free=true;
				}
			}
		}
		
		Article article = articleDaoImpl.findByID(id);
		boolean admin=LoginUtil.adminChk(messageSource, session);
		
		
		if (article!=null && (admin || article.getUser().getId() - uid==0 || GroupUtil.editMaskChk(article.getAgroup(), mask))) {	//如果有权限可以编辑
			map.put("article", article);
		}else {
			return FORBIDPAGE;
		}
		
		if (admin || (mask!=null && mask.trim().equals("0"))) {
			map.put("admin", true);
		}
		
		
		long currTime=System.currentTimeMillis();
		map.put("currTime", currTime);
		String rootTitle=messageSource.getMessage("root.title", null, "Root", null);
		
		ArticleGroupMapCreateArgs agmca = new ArticleGroupMapCreateArgs();
		agmca.setCurrRoleMask(mask);
		agmca.setCurrRoot(null);
		agmca.setFree(free);
		agmca.setGroupDaoImpl(groupDaoImpl);
		agmca.setMap(map);
		agmca.setRootTitle(rootTitle);
		agmca.setStatus(1);
		agmca.setMessageSource(messageSource);
		agmca.setRequest(request);
		
		GroupUtil.mapCreate(agmca);
		return ARTICLEEDIT;
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/delete/{id}")
	@Token(ajax=true,log=true,mark="article--<delete>")
	public int delOnFg(@PathVariable(value = "id", required = true) Long id,HttpSession session,@CookieValue(value="uid_lerx",required=false) String uid_lerx) {
		boolean result;
		
		Article article=articleDaoImpl.findByID(id);
		User loger;
		boolean admin=false;
		if (uid_lerx!=null && !uid_lerx.trim().equals("") && StringUtil.isNumber(uid_lerx)) {
			long uid=Long.valueOf(uid_lerx);
			loger=userDaoImpl.findByID(uid);
			
			if (loger==null) {
				return -3;
			}
			
			Role role=null;
			if (loger!=null) {
				role=loger.getRole();
			}
			
			String mask="";
			if (role!=null) {
				mask = role.getMask();
			}
			if (GroupUtil.auditMaskChk(article.getAgroup(), mask)) {
				
				admin=true;
			}
		}else {
			return -3;
		}
		if (!admin) {
			return -2;
		}
		
		ArticleGroup g = article.getAgroup();
		User user=article.getUser();
		if (user!=null) {
			
			UserUtil.uacUpdate(user, article, userArtsCountDaoImp, 0, -1);
			if (user.getArtsTotal()>0) {
				user.setArtsTotal(user.getArtsTotal() - 1);
			}
			if (article.isStatus()) {
				if (user.getArtsPassed()>0) {
					user.setArtsPassed(user.getArtsPassed() - 1);
				}
				UserUtil.uacUpdate(user, article, userArtsCountDaoImp, 1, -1);
			}
			userDaoImpl.modify(user);
		}
		
		result=articleDaoImpl.delByID(id);
		if (result) {
			
			groupDaoImpl.modifyChanged(g);
			return 0;
		}else {
			return -1;
		}
		
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/del/{id}")
	@Token(ajax=true,log=true,mark="article--<del>",admin=true,failedPage=LOGINPAGE)
	public int del(@PathVariable(value = "id", required = true) Long id,HttpSession session) {
		boolean result;
		
		Article article=articleDaoImpl.findByID(id);
		
		ArticleGroup g = article.getAgroup();
		User user=article.getUser();
		if (user!=null) {
			
			UserUtil.uacUpdate(user, article, userArtsCountDaoImp, 0, -1);
			if (user.getArtsTotal()>0) {
				user.setArtsTotal(user.getArtsTotal() - 1);
			}
			if (article.isStatus()) {
				if (user.getArtsPassed()>0) {
					user.setArtsPassed(user.getArtsPassed() - 1);
				}
				UserUtil.uacUpdate(user, article, userArtsCountDaoImp, 1, -1);
			}
			userDaoImpl.modify(user);
		}
		
		result=articleDaoImpl.delByID(id);
		if (result) {
			
			groupDaoImpl.modifyChanged(g);
			return 0;
		}else {
			return -1;
		}
		
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/addtime/{id}")
	@Token(ajax=true,log=true,mark="article--<addtime>",admin=true,failedPage=LOGINPAGE)
	public int modifyAddTime(@PathVariable(value = "id", required = true) Long id,String addtimeStr, HttpServletRequest request) {
		if (addtimeStr==null || addtimeStr.trim().equals("") || !TimeUtil.isValidDatetime(addtimeStr)) {
			return -11;
		}
		
		Article article=articleDaoImpl.findByID(id);
		if (article==null) {
			return -13;
		}
		
		boolean htmlBuild=false;
		
		
		
		HtmlFileStatic hfs;
		article.setCreationTime(TimeUtil.coverStrToLong(addtimeStr));
		FileEl fe=ArticleUtil.feBuild(messageSource, request, article,true);
		hfs=article.getHfs();
		htmlBuild=hfs.isStatus();
		FileUtil.delete(hfs.getRealPath());
		
		hfs.setRealPath(fe.getRealPath());
		hfs.setUrl(fe.getUrl());
		hfs.setFilename(FileUtil.getFileFromPath(fe.getRealPath()));
		htmlFileStaticDaoImpl.modify(hfs);
		
		if (article.isStatus() && htmlBuild) {
			ArticleUtil.htmlCreate(request, messageSource, article);
		}
		
		articleDaoImpl.modify(article);
		
		return 0;
	}
		
	
	
	@ResponseBody
	@RequestMapping(value = "/top/{id}/{status}")
	@Token(ajax=true,log=true,mark="article--<top>",role0=true,failedPage=LOGINPAGE)
	public int modifyTopOne(@PathVariable(value = "id", required = true) Long id,@PathVariable(value = "status", required = true) Integer status) {
		
		Article article=articleDaoImpl.findByID(id);
		boolean sta;
		if (status==0) {
			sta=false;
		}else {
			sta=true;
		}
		if (article!=null) {
			articleDaoImpl.modifyTopOne(id, sta);
		}else {
			return -13;
		}
		
		return 0;
		
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/pass/{id}/{status}")
	@Token(ajax=true,log=true,mark="article--<pass>",loginOrAdmin=true,failedPage=LOGINPAGE)
	public int modifyPass(@PathVariable(value = "id", required = true) Long id,@PathVariable(value = "status", required = true) Integer status,HttpServletRequest request,HttpSession session) {
		boolean admin=LoginUtil.adminChk(messageSource, session);
		boolean con=false;
		if (admin) {
			con=true;
		}
		Article art = articleDaoImpl.findByID(id);
		String uidStr=HttpUtil.getCookie(messageSource, request, "uid_lerx");
		long uid=0L;
		if (uidStr!=null && !uidStr.trim().equals("") && StringUtil.isNumber(uidStr)) {
			uid = Long.valueOf(uidStr);
		}
		
		User user=userDaoImpl.findByID(uid);
		Role role=null;
		if (user!=null) {
			role=user.getRole();
		}
		
		String mask="";
		if (role!=null) {
			mask = role.getMask();
		}
		if (!admin && GroupUtil.auditMaskChk(art.getAgroup(), mask)) {
			con=true;
		}
		
		if (con) {
			if (status==1) {
				art.setStatus(true);
			}else {
				art.setStatus(false);
			}
			articleDaoImpl.modify(art);
			
			if (art.isStatus() && GroupUtil.openChk(art.getAgroup())) {
				boolean jump=false;
				if (art.getJumpUrl()!=null && !art.getJumpUrl().trim().equals("")) {
	  				jump=true;
	  			}
				if (!jump) {
					ArticleUtil.htmlCreate(request, messageSource, art);
					art.getHfs().setStatus(true);
				}
				if (art.getUser()!=null) {
					UserUtil.uacUpdate(art.getUser(), art, userArtsCountDaoImp, 1, 1);
					art.getUser().setArtsPassed(art.getUser().getArtsPassed()+1);
					userDaoImpl.modify(art.getUser());
				}
				
			}else {
				
				
				if (art.getUser()!=null) {
					UserUtil.uacUpdate(art.getUser(), art, userArtsCountDaoImp, 1, -1);
					if (art.getUser().getArtsPassed()>0) {
						art.getUser().setArtsPassed(art.getUser().getArtsPassed() - 1);
						userDaoImpl.modify(art.getUser());
					}
				}
				
				FileUtil.delete(art.getHfs().getRealPath());
				art.getHfs().setStatus(false);
				
			}
			//更新目录的状态
			groupDaoImpl.modifyChanged(art.getAgroup());
			htmlFileStaticDaoImpl.modify(art.getHfs());
			return 0;
		}
		
		return -2;
		
	}
	
	@RequestMapping("/inventory")
	@Token(ajax=false)
	public String inventory(@RequestParam(value = "gid", required = false) Long gid,@RequestParam(value = "status", required = false) Integer status,@RequestParam(value = "page", required = false) Integer page,@RequestParam(value = "pageSize", required = false) Integer pageSize,HttpServletRequest request,Map<String, Object> map) {
		Portal portal=portalDaoImpl.query_update();
		boolean asc=false;
		if (gid==null) {
			gid=0L;
		}
		if (page == null) {
			page = 1;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		
		String uidStr=HttpUtil.getCookie(messageSource, request, "uid_lerx");
		long uid=0L;
		if (uidStr!=null && !uidStr.trim().equals("") && StringUtil.isNumber(uidStr)) {
			uid = Long.valueOf(uidStr);
		}
		
		User user=userDaoImpl.findByID(uid);
		Role role=null;
		if (status==null) {
			status=1;
		}
		boolean admin=false;
		ArticleGroup agroup=groupDaoImpl.findByID(gid);
		if (user!=null) {
			role=user.getRole();
			if (role!=null && role.getMask()!=null) {
				if (GroupUtil.auditMaskChk(groupDaoImpl.findByID(gid), role.getMask())) {
					admin=true;
				}
			}
		}
		Rs rs;
		if (!admin) {
			switch (status) {
			case 1:
				rs=articleDaoImpl.queryByGid(gid,0, page, pageSize, asc,1,0);
				break;
			case 0:
				rs=articleDaoImpl.queryByGid(gid,0, page, pageSize, asc,1,0);
				break;
			default:
				rs=RsUtil.init(page, pageSize, 0);
				rs.setList(null);
			}
			
		}else {
			rs=articleDaoImpl.queryByGid(gid,0, page, pageSize, asc,status,0);
		}
		map.put("rs", rs);
		map.put("pageUrl", "/action_article/inventory?gid="+gid+"&status="+status);
		map.put("portal", portal);
		map.put("agroup", agroup);
		map.put("status", status);
		
		return "jsp/article/inventory";
	}
	
	@RequestMapping("/list")
	@Token(ajax=false,admin=true,failedPage=ADMINFORBID)
	public String list(@RequestParam(value = "gid", required = false) Long gid,@RequestParam(value = "page", required = false) Integer page,@RequestParam(value = "pageSize", required = false) Integer pageSize,HttpServletRequest request,Map<String, Object> map) {
		boolean asc=false;
		if (gid==null) {
			gid=0L;
		}
		if (page == null) {
			page = 1;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		ArticleGroup agCur;
		if (gid>0L) {
			agCur=groupDaoImpl.findByID(gid);
		}else {
			agCur=null;
			
		}
		
		if (agCur==null) {
			gid=0L;
		}
		
		map.put("gid", gid);
		
		String rootTitle=messageSource.getMessage("nav.all.select.title", null, "All navs", null);
		ArticleGroupMapCreateArgs agmca = new ArticleGroupMapCreateArgs();
		agmca.setCurrRoleMask("0");
		agmca.setCurrRoot(agCur);
		agmca.setFree(true);
		agmca.setGroupDaoImpl(groupDaoImpl);
		agmca.setMap(map);
		agmca.setRootTitle(rootTitle);
		agmca.setStatus(0);
		agmca.setMessageSource(messageSource);
		agmca.setRequest(request);
		
		GroupUtil.mapCreate(agmca);
		
		Rs rs=articleDaoImpl.queryByGid(gid,0, page, pageSize, asc,0,0);
		map.put("rs", rs);
		map.put("pageUrl", "action_article/list?gid="+gid);
		map.put("rootUrl", "action_article/list");
		
		return ARTICLELIST;
	}
	
	@RequestMapping("/search")
	public String search(String keywords,@RequestParam(value = "page", required = false) Integer page,@RequestParam(value = "pageSize", required = false) Integer pageSize,Map<String, Object> map) {
		Portal portal =portalDaoImpl.query_update();
		map.put("portal", portal);
		if (keywords!=null) {
			keywords=StringUtil.htmlFilter(keywords);
		}
		if (keywords==null || keywords.trim().equals("")) {
			map.put("keywords", null);
			return "jsp/article/search";
		}
		if (page == null) {
			page = 1;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		Rs rs  = articleDaoImpl.search(0, keywords, true, 0, page, pageSize);
		map.put("keywords", keywords);
		map.put("rs", rs);
		map.put("pageUrl", "/action_article/search?keywords="+keywords);
		
		return "jsp/article/search";
	}
	
}
