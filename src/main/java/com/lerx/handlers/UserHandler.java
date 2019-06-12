package com.lerx.handlers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lerx.analyze.util.AnalyzeUtil;
import com.lerx.annotation.Token;
import com.lerx.app.util.SafeStrUtil;
import com.lerx.dao.iface.IArticleDao;
import com.lerx.dao.iface.IBaseDao;
import com.lerx.dao.iface.ICommentBridgeDao;
import com.lerx.dao.iface.ICommentThreadDao;
import com.lerx.dao.iface.ILoginConnecterDao;
import com.lerx.dao.iface.IPortalDao;
import com.lerx.dao.iface.IRoleDao;
import com.lerx.dao.iface.IUploadedFileDao;
import com.lerx.dao.iface.IUserArtsCountDao;
import com.lerx.dao.iface.IUserDao;
import com.lerx.entities.LoginConnecter;
import com.lerx.entities.Portal;
import com.lerx.entities.Role;
import com.lerx.entities.User;
import com.lerx.hql.entities.Rs;
import com.lerx.ip.util.IPUtil;
import com.lerx.login.util.LoginSafeRecUtil;
import com.lerx.login.util.LoginUtil;
import com.lerx.open.sdk.util.SignUtil;
import com.lerx.open.sdk.wechat.OauthWeChat;
import com.lerx.portal.obj.ConnecterValidateRetrun;
import com.lerx.portal.obj.LoginTest;
import com.lerx.portal.obj.LoginTestObj;
import com.lerx.portal.obj.ConnecterUserInf;
import com.lerx.portal.obj.ResponseResult;
import com.lerx.sys.obj.LoginSessionTest;
import com.lerx.sys.util.HttpUtil;
import com.lerx.sys.util.MavUtil;
import com.lerx.sys.util.StringUtil;
import com.lerx.v5.util.CaptchaUtil;
import com.lerx.v5.util.ConfigUtil;
import com.lerx.v5.util.UserUtil;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;

import weibo4j.http.Response;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

@RequestMapping("/action_user")
@Controller
public class UserHandler {

	private static final String SUCCESS = "jsp/result/success";
	private static final String FAILED = "jsp/result/failed";

	private static final String LOGINPAGE = "jsp/user/login";
	private static final String USERLIST = "jsp/user/list";
	private static final String USERADD = "jsp/user/add";
	private static final String ADMINFORBID = "_admin.forbid_";

	@Autowired
	private IBaseDao baseDaoImpl;

	@Autowired
	private ResourceBundleMessageSource messageSource;

	@Autowired
	private IPortalDao portalDaoImpl;

	@Autowired
	private IUserDao userDaoImpl;

	@Autowired
	private IArticleDao articleDaoImpl;

	@Autowired
	private IRoleDao roleDaoImpl;

	@Autowired
	private ICommentBridgeDao commentBridgeDaoImpl;

	@Autowired
	private ICommentThreadDao commentThreadDaoImpl;

	@Autowired
	private IUploadedFileDao uploadedFileDaoImpl;

	@Autowired
	private IUserArtsCountDao userArtsCountDaoImp;

	@Autowired
	private ILoginConnecterDao loginConnecterDaoImpl;

	/*
	 * 列表
	 */
	@RequestMapping("/list")
	@Token(ajax = false, admin = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public ModelAndView list(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize, HttpSession session,
			Map<String, Object> map) {
		if (page == null) {
			page = 1;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		Rs rs = userDaoImpl.find(page, pageSize);
		map.put("pageUrl", "/action_user/list");
		map.put("rs", rs);

		return MavUtil.mav(USERLIST, "");

	}

	// 查找用户名
	@RequestMapping(value = "/findname")
	@ResponseBody
	public int findname(String name) {
		if (userDaoImpl.findByUsername(name) != null) {
			return 1;
		} else {
			return -1;
		}
	}

	// 更改手机号
	@RequestMapping(value = "/chgmobile/{uid}")
	@ResponseBody
	@Token(ajax = true, log = true, loginOrAdmin = true, mark = "user--<chgmobile>")
	public int mobile(String mobile, @PathVariable("uid") Long uid, HttpServletRequest request) {
		String uidStr = HttpUtil.getCookie(messageSource, request, "uid_lerx");
		if (uidStr == null || uid == null || Long.valueOf(uidStr) - uid != 0L) {
			uid = 0L;
			return -7;
		}
		if (userDaoImpl.findByMobile(mobile, uid) != null) {
			return -10;
		} else {
			User user = userDaoImpl.findByID(uid);
			user.setMobile(mobile);
			userDaoImpl.modify(user);
			return 0;
		}
	}

	// 更改邮箱
	@RequestMapping(value = "/chgemail/{uid}")
	@ResponseBody
	@Token(ajax = true, log = true, loginOrAdmin = true, mark = "user--<chgmobile>")
	public int email(String email, @PathVariable("uid") Long uid, HttpServletRequest request) {
		String uidStr = HttpUtil.getCookie(messageSource, request, "uid_lerx");
		if (uidStr == null || uid == null || Long.valueOf(uidStr) - uid != 0L) {
			uid = 0L;
			return -7;
		}
		if (userDaoImpl.findByEmail(email, uid) != null) {
			return -10;
		} else {
			User user = userDaoImpl.findByID(uid);
			user.setEmail(email);
			;
			userDaoImpl.modify(user);
			return 0;
		}
	}

	// 查找手机号
	@RequestMapping(value = "/findmobile")
	@ResponseBody
	public int findMobile(String mobile, @RequestParam(value = "uid", required = false) Long excludeID) {
		if (excludeID == null) {
			excludeID = 0L;
		}
		if (userDaoImpl.findByMobile(mobile, excludeID) != null) {
			return 1;
		} else {
			return -1;
		}
	}

	// 查找邮箱
	@RequestMapping(value = "/findemail")
	@ResponseBody
	public int findEmail(String email, @RequestParam(value = "uid", required = false) Long excludeID) {
		if (excludeID == null) {
			excludeID = 0L;
		}
		if (userDaoImpl.findByEmail(email, excludeID) != null) {
			return 1;
		} else {
			return -1;
		}
	}

	// 注册
	@RequestMapping(value = "/reg")
	@Token(ajax = false, log = true, mark = "user--<reg>")
	public ModelAndView addByReg(@Valid User user, Errors result, String sendTarget, HttpServletRequest request,
			HttpSession session) {
		if (session.getAttribute("sendTarget") == null || session.getAttribute("targetMode") == null) {
			return MavUtil.mav(FAILED,
					messageSource.getMessage("fail.session.timeout", null, "Session timeout!", null));
		}
		String sessionTarget = (String) session.getAttribute("sendTarget");
		int sessionTargetMode = (int) session.getAttribute("targetMode");

		if (sendTarget == null || sendTarget.trim().equals("") || sessionTarget == null
				|| !sendTarget.trim().equalsIgnoreCase(sessionTarget)) {
			return MavUtil.mav(FAILED,
					messageSource.getMessage("fail.session.timeout", null, "Session timeout!", null));
		}

		if (sessionTargetMode == 0) {
			user.setEmail(sessionTarget);
		} else {
			user.setMobile(sessionTarget);
		}

		if (userDaoImpl.findByUsername(user.getUsername()) != null) {
			return MavUtil.mav(FAILED,
					messageSource.getMessage("fail.exists.username", null, "The username has already existed!", null),
					true);
		}

		String charset = messageSource.getMessage("charset", null, "UTF-8", null);
		String filterWords = ConfigUtil.configContentsByComma("filterUsernames", charset);
		if (StringUtil.findByWords(user.getUsername(), filterWords)) {
			return MavUtil.mav(FAILED, messageSource.getMessage("fail.wrongful.title", null,
					"The name is not legal or is forbidden to use!", null), true); // 发现保留字
		}
		filterWords = ConfigUtil.configContentsByComma("filterWords", charset);
		if (StringUtil.findByWords(user.getUsername(), filterWords)) {
			return MavUtil.mav(FAILED, messageSource.getMessage("fail.wrongful.title", null,
					"The name is not legal or is forbidden to use!", null), true); // 发现敏感词
		}

		if (userDaoImpl.findByEmail(user.getEmail(), 0) != null) {
			return MavUtil.mav(FAILED,
					messageSource.getMessage("fail.exists.email", null, "The email has already existed!", null), true);
		}

		if (userDaoImpl.findByMobile(user.getMobile(), 0) != null) {
			return MavUtil.mav(FAILED,
					messageSource.getMessage("fail.exists.mobile", null, "The phone code has already existed!", null));
		}

		user.setRegCodeSendTarget(sessionTarget);

		String ip = IPUtil.getRealRemotIP(request);
		user.setCreateIP(ip);

		user.setState(true);
		user.setRole(roleDaoImpl.findDef());
		user = userDaoImpl.add(user);
		if (user == null) {
			return MavUtil.mav(FAILED, messageSource.getMessage("fail.universal", null, "Failed!", null));
		} else {
			session.removeAttribute("sendTarget");
			session.removeAttribute("targetMode");
			return MavUtil.mav(SUCCESS, messageSource.getMessage("success", null, "Success!", null));
		}
	}

	// 互联登录后注册
	@ResponseBody
	@RequestMapping(value = "/regByOpenID")
	@Token(ajax = false, log = true, mark = "user--<reg>")
	public ModelAndView addByOpenID(@Valid User user, Errors result, String sendTarget, String openID, String referer,
			int targetMode, int otype, HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		ConnecterValidateRetrun cvr = new ConnecterValidateRetrun();
		if (openID == null || openID.trim().equals("") || sendTarget == null || sendTarget.trim().equals("")) {
			cvr.setStatus(-11);
			return MavUtil.mav(FAILED, cvr.getMessage());
		}

		cvr.setStatus(0);
		cvr.setOpenID(openID);
		cvr.setOtype(otype);

		if (targetMode == 0) {
			user.setEmail(sendTarget);
		} else {
			user.setMobile(sendTarget);
		}

		if (userDaoImpl.findByUsername(user.getUsername()) != null) {
			cvr.setStatus(-10);

			cvr.setMessage(
					messageSource.getMessage("fail.exists.username", null, "The username has already existed!", null));
			return MavUtil.mav(FAILED, cvr.getMessage());
		}

		String charset = messageSource.getMessage("charset", null, "UTF-8", null);
		String filterWords = ConfigUtil.configContentsByComma("filterUsernames", charset);
		if (StringUtil.findByWords(user.getUsername(), filterWords)) {
			cvr.setStatus(-17);
			cvr.setMessage(messageSource.getMessage("fail.wrongful.title", null,
					"The name is not legal or is forbidden to use!", null));// 发现保留字
			return MavUtil.mav(FAILED, cvr.getMessage());
		}
		filterWords = ConfigUtil.configContentsByComma("filterWords", charset);
		if (StringUtil.findByWords(user.getUsername(), filterWords)) {
			cvr.setStatus(-18);
			cvr.setMessage(messageSource.getMessage("fail.wrongful.title", null,
					"The name is not legal or is forbidden to use!", null));// 发现敏感词
			return MavUtil.mav(FAILED, cvr.getMessage());
		}

		if (userDaoImpl.findByEmail(user.getEmail(), 0) != null) {
			cvr.setStatus(-10);
			cvr.setMessage(messageSource.getMessage("fail.exists.email", null, "The email has already existed!", null));// 发现重复email
			return MavUtil.mav(FAILED, cvr.getMessage());
		}

		if (userDaoImpl.findByMobile(user.getMobile(), 0) != null) {
			cvr.setStatus(-10);
			cvr.setMessage(
					messageSource.getMessage("fail.exists.mobile", null, "The phone code has already existed!", null));// 发现重复手机号码
			return MavUtil.mav(FAILED, cvr.getMessage());
		}

		user.setRegCodeSendTarget(sendTarget);

		String ip = IPUtil.getRealRemotIP(request);
		user.setCreateIP(ip);

		user.setState(true);
		user.setRole(roleDaoImpl.findDef());
		user = userDaoImpl.add(user);
		if (user == null) {
			cvr.setStatus(-9);
			cvr.setMessage(messageSource.getMessage("fail.universal", null, "Failed!", null));// 数据库操作失败
			return MavUtil.mav(FAILED, cvr.getMessage());
		} else {
			cvr.setStatus(0);

			cvr.setMessage(messageSource.getMessage("success", null, "Success!", null));// 数据库操作失败

			session.removeAttribute("sendTarget");
			session.removeAttribute("targetMode");
			cvr = createConnecter(cvr, loginConnecterDaoImpl, user, otype, openID, referer, request, response, session,
					messageSource, userDaoImpl);
			return MavUtil.mav(SUCCESS, cvr.getMessage());
		}
	}

	// 增加
	@RequestMapping(value = "/addByName")
	@ResponseBody
	@Token(ajax = true, log = true, mark = "user--<addByName>", admin = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public int addByName(String name, HttpServletRequest request) {

		boolean existing = false;
		if (userDaoImpl.findByUsername(name) != null) {
			existing = true;
		}
		String charset = messageSource.getMessage("charset", null, "UTF-8", null);
		String filterWords = ConfigUtil.configContentsByComma("filterUsernames", charset);
		if (StringUtil.findByWords(name, filterWords)) {
			return -17; // 发现保留字
		}
		filterWords = ConfigUtil.configContentsByComma("filterWords", charset);
		if (StringUtil.findByWords(name, filterWords)) {
			return -18; // 发现敏感词
		}
		if (!existing) {
			String ip = IPUtil.getRealRemotIP(request);
			User user = new User();
			user.setUsername(name);
			user.setCreateTime(System.currentTimeMillis());
			user.setCreateIP(ip);

			user.setState(true);
			user.setRole(roleDaoImpl.findDef());
			user.setPassword(messageSource.getMessage("defalut.password", null, "123456", null));
			user = userDaoImpl.add(user);
			if (user == null) {
				return -9;
			} else {
				return 0;
			}
		} else {
			return -10;
		}

	}

	// 增加
	@RequestMapping("/add")
	@Token(ajax = false, log = true, mark = "user--<add>", admin = true, token = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public String add(@Valid User user, Errors result, Map<String, Object> map, HttpServletRequest request,
			HttpSession session) {
		boolean existing = false;
		boolean r = false;
		String ip = null;

		String charset = messageSource.getMessage("charset", null, "UTF-8", null);
		String filterWords = ConfigUtil.configContentsByComma("filterUsernames", charset);
		if (StringUtil.findByWords(user.getUsername(), filterWords)) {
			return FAILED; // 发现保留字
		}
		filterWords = ConfigUtil.configContentsByComma("filterWords", charset);
		if (StringUtil.findByWords(user.getUsername(), filterWords)) {
			return FAILED;
		}

		if (user.getId() == 0) {
			if (userDaoImpl.findByUsername(user.getUsername()) != null) {
				existing = true;
			}
			if (!existing) {
				ip = IPUtil.getRealRemotIP(request);
				user.setCreateTime(System.currentTimeMillis());
				user.setCreateIP(ip);

				user.setState(true);
				user.setRole(roleDaoImpl.findDef());

				if (user.getPassword() == null || user.getPassword().trim().equals("")) {
					user.setPassword(messageSource.getMessage("defalut.password", null, "123456", null));
				}

				user = userDaoImpl.add(user);
				if (user == null) {
					r = false;
				} else {
					r = true;
					user = userDaoImpl.findByID(user.getId());
					user.setPwdAtCreate(user.getPassword());
					userDaoImpl.modify(user);
				}

			} else {
				r = false;
				map.put("error", messageSource.getMessage("fail.exists.username", null,
						"The username has already existed!", null));
			}

		} else {

			if (user.getRole().getId() == 0) {
				user.setRole(null);
			}
			User userdb = userDaoImpl.findByID(user.getId());
			String regCodeSendTarget = userdb.getRegCodeSendTarget();

			userDaoImpl.modifySafely(user);
			user.setRegCodeSendTarget(regCodeSendTarget);
			userDaoImpl.modify(user);
			r = true;

		}
		if (r) {
			return SUCCESS;
		} else {
			return FAILED;
		}

	}

	// 进入修改页面
	@RequestMapping("/edit")
	@Token(ajax = false, admin = true, failedPage = ADMINFORBID, msgKey = "fail.nologin")
	public String edit(Map<String, Object> map, HttpSession session, Long id) {

		User user = userDaoImpl.findByID(id);
		user.setPassword(null);
		map.put("user", user);
		List<Role> list = roleDaoImpl.queryAll();
		map.put("roles", list);
		return USERADD;

	}

	// 网站名片
	@RequestMapping(value = "/card/{id}")
	public String card(Map<String, Object> map, HttpSession session,
			@CookieValue(value = "uid_lerx", required = false) String uid_lerx,
			@PathVariable(value = "id", required = false) Long id, HttpServletRequest request) {
		if (id == null) {
			id = 0L;
		}
		long uid;
		User loginer = null;
		if (uid_lerx == null) {
			uid = 0L;
		} else {
			uid = Long.valueOf(uid_lerx);
			loginer = userDaoImpl.findByID(uid);
		}
		
		boolean own = false;
		if (uid - id == 0) {
			own = true;
		}

		if (id == 0L) {
			id = uid;
		}

		if (id == 0) {
			return "jsp/result/failed";
		}

		User user = userDaoImpl.findByID(id);

		if (user == null) {
			return "jsp/result/failed";
		}
		boolean isadmin=UserUtil.isadmin(loginer);
		if (!own && !isadmin) {
			user.setLastLoginIP(IPUtil.ipFilter(user.getLastLoginIP(),
					messageSource.getMessage("ip.filter.mask", null, "1111", null)));
			user.setCreateIP(IPUtil.ipFilter(user.getCreateIP(),
					messageSource.getMessage("ip.filter.mask", null, "1111", null)));
		}
		
		
		LoginConnecter lc = loginConnecterDaoImpl.findByUID(user.getId());
		String bindStatus=StringUtil.countStr(3,"0");
		
		if (lc!=null) {
			if (lc.getOpenIDAtQQ()!=null && !lc.getOpenIDAtQQ().trim().equals("")) {
				bindStatus=StringUtil.switchPlase(bindStatus,1,true);
				map.put("bindQQ", true);
				map.put("unBindQQ", "");
			}else {
				map.put("bindQQ", false);
				map.put("unBindQQ", "un");
			}
			
			if (lc.getOpenIDAtWeChat()!=null && !lc.getOpenIDAtWeChat().trim().equals("")) {
				bindStatus=StringUtil.switchPlase(bindStatus,2,true);
				map.put("bindWeChat", true);
				map.put("unBindWeChat", "");
			}else {
				map.put("bindWeChat", false);
				map.put("unBindWeChat", "un");
			}
			
			if (lc.getOpenIDAtWeibo()!=null && !lc.getOpenIDAtWeibo().trim().equals("")) {
				bindStatus=StringUtil.switchPlase(bindStatus,3,true);
				map.put("bindWeibo", true);
				map.put("unBindWeibo", "");
			}else {
				map.put("bindWeibo", false);
				map.put("unBindWeibo", "un");
			}
		}else {
			map.put("unBindQQ", "un");
			map.put("unBindWeChat", "un");
			map.put("unBindWeibo", "un");
		}
		map.put("bindStatus", bindStatus);	//此处以0和1生成一个绑定状态字符串，从右侧个位开始 1qq 2微信 3微博
		map.put("isadmin", isadmin);
		map.put("own", own);
		map.put("user", user);
		map.put("avatarNull", ConfigUtil.getAvatarNullFile(messageSource, request));
		/*List<Role> list = roleDaoImpl.queryAll();
		map.put("roles", list);*/
		return "jsp/user/card";

	}

	@ResponseBody
	@RequestMapping("/pwsReset")
	@Token(ajax = true, log = true, mark = "user--<password-reset>")
	public int modifyPwsReset(long uid, String password, HttpServletRequest request, HttpSession session) {
		if (session.getAttribute("sendTarget") == null) {
			return -6;
		}
		User user = userDaoImpl.findByID(uid);
		if (user == null) {
			return -1;
		}
		userDaoImpl.modifypw(user, password);
		return 0;
	}

	@ResponseBody
	@RequestMapping("/pws")
	@Token(ajax = true, log = true, mark = "user--<password>", admin = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public int modifyPws(long uid, String password) {
		User user = userDaoImpl.findByID(uid);
		userDaoImpl.modifypw(user, password);
		return 0;
	}

	@ResponseBody
	@RequestMapping(value = "/forget")
	@Token(ajax = true, log = true, mark = "user--<forget>")
	public ResponseResult forget(String keywords, HttpServletRequest request, HttpSession session) {
		ResponseResult rr = new ResponseResult();
		Portal portal = portalDaoImpl.query_update();
		User user = userDaoImpl.findByKeywords(keywords);
		if (user == null) {
			rr.setResult(-1);
			rr.setMsg(messageSource.getMessage("fail.null", null, "null", null));
		} else {
			rr.setValueL(user.getId());
			rr.setValueS1(user.getUsername());

			rr.setResult(0);
			int r = -1;
			if (StringUtil.emailTest(keywords)) {
				r = CaptchaUtil.send(messageSource, request, session, portal.getName(), 0, keywords);
				rr.setValueS2(keywords);
				rr.setValueI(0);
			} else if (StringUtil.isNumber(keywords) && keywords.length() == 11) {
				r = CaptchaUtil.send(messageSource, request, session, portal.getName(), 1, keywords);
				rr.setValueS2(keywords);
				rr.setValueI(1);
			} else {
				if (user.getEmail() != null && StringUtil.emailTest(user.getEmail())) {
					r = CaptchaUtil.send(messageSource, request, session, portal.getName(), 0, user.getEmail());
					rr.setValueS2(user.getEmail());
					rr.setValueI(0);
				} else if (StringUtil.isNumber(user.getMobile()) && user.getMobile().length() == 11) {
					r = CaptchaUtil.send(messageSource, request, session, portal.getName(), 1, keywords);
					rr.setValueS2(user.getMobile());
					rr.setValueI(1);
				} else {
					r = -13;
				}
			}
			rr.setResult(r);
		}

		return rr;
	}

	@ResponseBody
	@RequestMapping(value = "/pws/{uid}")
	@Token(ajax = true, log = true, mark = "user--<password>", login = true, failedPage = LOGINPAGE, msgKey = "fail.permission")
	public int modifySelfPws(@PathVariable("uid") Long uid, String password, HttpServletRequest request) {
		String uidStr = HttpUtil.getCookie(messageSource, request, "uid_lerx");
		if (uidStr == null || uidStr.trim().equals("") || !StringUtil.isNumber(uidStr)) {
			return -3;
		}
		if (uid == null || uid == 0L) {
			return -11;
		}

		long currUid = Long.valueOf(uidStr);

		if (currUid - uid != 0) {
			return -2;
		}
		User user = userDaoImpl.findByID(uid);
		if (user == null) {
			return -11;
		}

		userDaoImpl.modifypw(user, password);
		return 0;
	}

	/*
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/del")
	@Token(ajax = false, log = true, mark = "user--<del>", admin0 = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public int del(Long id, HttpSession session) {

		if (articleDaoImpl.countByUid(id) > 0L) {
			return -4;
		}
		if (commentBridgeDaoImpl.countByUid(id) > 0L) {
			return -4;
		}
		if (commentThreadDaoImpl.countByUid(id) > 0L) {
			return -4;
		}
		if (uploadedFileDaoImpl.countByUid(id) > 0L) {
			return -4;
		}
		if (userArtsCountDaoImp.countByUid(id) > 0L) {
			return -4;
		}

		boolean result = false;
		result = userDaoImpl.delByID(id);
		if (result) {
			return 0;
		} else {
			return -9;
		}

	}

	/*
	 * 查找当前登录用户的uid
	 */

	/*
	 * 匹配
	 */
	@ResponseBody
	@RequestMapping(value = "/match/{uid}")
	public int match(@PathVariable("uid") Long uid, HttpServletRequest request) {

		String uidStr = HttpUtil.getCookie(messageSource, request, "uid_lerx");
		if (uidStr == null || uidStr.trim().equals("") || !StringUtil.isNumber(uidStr)) {
			return -3;
		}

		if (uid == null || uid == 0L) {
			return -11;
		}

		long currUid = Long.valueOf(uidStr);
		if (currUid - uid == 0) {
			return 0;
		} else {
			return -2;
		}

	}

	/*
	 * 更新头像
	 */
	@ResponseBody
	@RequestMapping("/avatar")
	@Token(ajax = false, log = true, mark = "user--<avatar-update>", login = true, failedPage = LOGINPAGE, msgKey = "fail.permission")
	public int avatar(@RequestParam(value = "uid", required = false) Long uid,
			@RequestParam(value = "avatar", required = false) String avatar, HttpServletRequest request) {

		if (uid == null) {
			uid = 0L;
			return -11;
		}
		User user = userDaoImpl.findByID(uid);
		if (user == null) {
			return -11;
		}

		String uidStr = HttpUtil.getCookie(messageSource, request, "uid_lerx");
		if (uidStr == null || uidStr.trim().equals("") || !StringUtil.isNumber(uidStr)) {
			return -11;
		}
		long currUid = Long.valueOf(uidStr);
		if (currUid - uid != 0L) {
			return -2;
		}

		if (avatar == null || avatar.trim().equals("")) {
			return -12;
		}

		user.setAvatarUrl(avatar);
		return 0;

	}

	@RequestMapping("/login")
	@Token(ajax = false, log = true, mark = "user--<login>", token = true, failedPage = LOGINPAGE, msgKey = "fail.login")
	public ModelAndView login(@Valid User user, Errors result,
			@RequestParam(value = "vcode", required = false) String vcode, Map<String, Object> map,
			HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		LoginTestObj lto = new LoginTestObj();
		lto.setUser(user);
		lto.setVcode(vcode);
		lto.setFromConnecter(false);
		lto.setW(true);
		lto.setMessageSource(messageSource);
		lto.setUserDaoImpl(userDaoImpl);
		lto.setRequest(request);
		lto.setResponse(response);
		lto.setSession(session);

		LoginTest lt = loginTest(lto);
		return MavUtil.mav(lt.getReturnPage(), lt.getMsg());
	}

	@ResponseBody
	@RequestMapping("/loginAjax")
	@Token(ajax = true, token = true)
	public int loginAjax(@Valid User user, Errors result, @RequestParam(value = "vcode", required = false) String vcode,
			Map<String, Object> map, HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		LoginTestObj lto = new LoginTestObj();
		lto.setUser(user);
		lto.setVcode(vcode);
		lto.setFromConnecter(false);
		lto.setW(false);
		lto.setMessageSource(messageSource);
		lto.setUserDaoImpl(userDaoImpl);
		lto.setRequest(request);
		lto.setResponse(response);
		lto.setSession(session);

		LoginTest lt = loginTest(lto);
		return lt.getResult();

	}

	@RequestMapping("/logout")
	@Token(ajax = false, log = true, mark = "user--<logout>")
	public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logout(request, response, session, true);
		long uid = LoginUtil.uid(messageSource, request);
		if (uid > 0L) {
			session.removeAttribute("vcode_user_lerx"); // 验证码需求清除
			session.removeAttribute("lsr_user"); // 登录次数Session清除
		}

		return "/jsp/user/login";

	}

	@ResponseBody
	@RequestMapping("/logoutAjax")
	@Token(ajax = true, log = true, mark = "user--<logout>")
	public int logoutAjax(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		logout(request, response, session, true);
		return 0;

	}

	private void logout(HttpServletRequest request, HttpServletResponse response, HttpSession session, boolean flag) {
		HttpUtil.clearCookie(request, response, "username_lerx");
		HttpUtil.clearCookie(request, response, "password_lerx");
		HttpUtil.clearCookie(request, response, "uid_lerx");
		HttpUtil.clearCookie(request, response, "rid_lerx");
		// HttpUtil.clearCookie(request, response, "role_mask_lerx");
		HttpUtil.clearCookie(request, response, "role0_lerx");

		HttpUtil.clearCookie(request, response, "role_name_lerx");
		session.removeAttribute("lsr"); // 清除登录次数Session
		session.removeAttribute("vcode_request_lerx"); // 清除验证码需求
	}
	
	

	@RequestMapping("/connecter/token")
	@ResponseBody
	public String token(HttpServletRequest request, HttpServletResponse response) throws Exception{
		/*String authUrl = WxConstants.AUTH_BASE_URL + "appid=" + WxConstants.APPID
                + "&redirect_uri=" + URLEncoder.encode(WxConstants.REDIRECT_URL)
                + "&response_type=code"
                + "&scope=" + WxConstants.SCOPE
                + "&state=STATE#wechat_redirect";*/
		OauthWeChat oauthWchat = new OauthWeChat();
		String authUrl = oauthWchat.authorize("code","STATE#wechat_redirect","snsapi_userinfo");


        String signature = request.getParameter("signature");/// 微信加密签名
        String timestamp = request.getParameter("timestamp");/// 时间戳
        String nonce = request.getParameter("nonce"); /// 随机数
        String echostr = request.getParameter("echostr"); // 随机字符串


        if (signature != null && timestamp != null && nonce != null && echostr != null) {
            if (SignUtil.checkSignature(signature, timestamp, nonce)) {
                return echostr;
            }
        } else {
            response.sendRedirect(authUrl);
            return null;
        }
		return echostr;

	}

	@ResponseBody
	@RequestMapping(value = "/connecter/auth/{otype}")
	public String connecterAuth(@PathVariable("otype") Integer otype, String referer, HttpServletRequest request,
			HttpServletResponse response,HttpSession session) {
		if (referer == null) {
			referer = request.getHeader("referer_lerx");
		} else {
			referer = StringUtil.escapeUrl(referer, 0);
		}
		String url;
		String stateCode=StringUtil.uuidStr();
		session.setAttribute("statecode", stateCode);
		HttpUtil.saveCookie(messageSource, response, "referer_lerx", referer);
		switch (otype) {
		case 1:
			OauthWeChat oauthWchat = new OauthWeChat();
			try {
				url = oauthWchat.authorize("code",stateCode,"snsapi_login");
				response.sendRedirect(url);
			} catch (IOException | WeiboException e) {
				e.printStackTrace();
			}
			break;
		case 2:
			weibo4j.Oauth oauth = new weibo4j.Oauth();

			try {
				url = oauth.authorize("code", "");
				response.sendRedirect(url);
			} catch (WeiboException | IOException e) {
				e.printStackTrace();
			}

			break;
		default:
			try {
				url = new Oauth().getAuthorizeURL(request);
				response.sendRedirect(url);
//				System.out.println("url:"+url);
			} catch (QQConnectException | IOException e) {
				e.printStackTrace();
			}
		}
		return "/jsp/result/error";
	}
	
	

	@ResponseBody
	@RequestMapping(value = "/connecter/login/{otype}")
	public ModelAndView connecterLogin(@CookieValue(value = "referer_lerx", required = false) String referer,
			@PathVariable("otype") Integer otype, @RequestParam(value = "code", required = false) String code, @RequestParam(value = "state", required = false) String state,
			HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		User u = null;
		String openID = null ;
		ConnecterUserInf qui = new ConnecterUserInf();
		String stateCode=(String) session.getAttribute("statecode");
		if (stateCode==null) {
			stateCode="null";
		}
		switch (otype) {
		
		case 1:
			
			OauthWeChat oauthWchat = new OauthWeChat();
			
			try {
				
				if (state==null) {
					state="error";
				}
				
				if (!stateCode.trim().equals(state)) {
					System.out.println("验证错误！");
					break;
				}
				
				Response res=oauthWchat.authorizeByCode(code);
				
				JSONObject json =res.asJSONObject();
				String openid=null;
				try {
					openid = json.getString("openid");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				/*String jsonResult = oauthWchat.getAccessTokenByCode(code).toString();
				System.out.print("jsonResult:"+jsonResult);
				JsonObject  jsonObject = (JsonObject) new JsonParser().parse(jsonResult).getAsJsonObject();
				String openid = jsonObject.get("openid").getAsString();*/
				qui.setOpenID(openid);
				u = loginConnecterDaoImpl.findUserByOpenID(openid, otype);
				
			} catch (WeiboException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			break;
		case 2:
			if (code == null) {
				return MavUtil.mav("/jsp/result/error",
						messageSource.getMessage("fail.login", null, "Login error!", null) + " (code:E10001)");
				// return "redirect:../index.jsp";
			}
			weibo4j.Oauth oauth = new weibo4j.Oauth();
			try {
				String pat="\\[(.*?)\\]";
				
				Pattern pattern = Pattern.compile(pat); 
				Matcher matcher = pattern.matcher(oauth.getAccessTokenByCode(code).toString());
				String uidstr=null;
				if (matcher.find()) {
					String tmp=matcher.group(1);
					String[] sArray = tmp.split(",");
					for (int i = 0; i < sArray.length; i++) {
						if (sArray[i]!=null && sArray[i].trim().startsWith("uid=")) {
							uidstr=sArray[i];
							break;
						}
					}
				}
				
				if (uidstr!=null) {
					String[] sArray = uidstr.split("=");
					if (sArray.length>1) {
						String uid=sArray[1];
						openID=uid;
						qui.setOpenID(uid);
					}
					
				}
				
				u = loginConnecterDaoImpl.findUserByOpenID(openID, otype);

			} catch (WeiboException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			break;
		default:
			try {
				AccessToken accessTokenObj = (new Oauth()).getAccessTokenByRequest(request);
				if (accessTokenObj.getAccessToken().equals("")) {
					// 我们的网站被CSRF攻击了或者用户取消了授权
					// 做一些数据统计工作
					System.out.println("日志输出：没有获取到响应参数");
				} else {

					String accessToken = accessTokenObj.getAccessToken();
					long tokenExpireIn = accessTokenObj.getExpireIn();
					session.setAttribute("demo_access_token", accessToken);
					session.setAttribute("demo_token_expirein", String.valueOf(tokenExpireIn));
					OpenID openIDObj = new OpenID(accessToken);
					openID = openIDObj.getUserOpenID();
					qui.setOpenID(openID);
					session.setAttribute("demo_openid", openID);
					UserInfo qzoneUserInfo = new UserInfo(accessToken, openID);
					UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
					if (userInfoBean.getRet() == 0) {
						String newname=UserUtil.findUsername(userDaoImpl, userInfoBean.getNickname());
						qui.setNickname(newname);
						qui.setGender(userInfoBean.getGender());
						qui.setLevel(userInfoBean.getLevel());
						qui.setVip(userInfoBean.isVip());
						qui.setYellowYearVip(userInfoBean.isYellowYearVip());
						qui.setAvatarURL30(userInfoBean.getAvatar().getAvatarURL30());
						qui.setAvatarURL50(userInfoBean.getAvatar().getAvatarURL50());
						qui.setAvatarURL100(userInfoBean.getAvatar().getAvatarURL100());

					} else {
						qui.setMsg(userInfoBean.getMsg());
					}

					com.qq.connect.api.weibo.UserInfo weiboUserInfo = new com.qq.connect.api.weibo.UserInfo(accessToken,
							openID);
					com.qq.connect.javabeans.weibo.UserInfoBean weiboUserInfoBean = weiboUserInfo.getUserInfo();
					if (weiboUserInfoBean.getRet() == 0) {
						qui.setWeiboAvatarURL30(weiboUserInfoBean.getAvatar().getAvatarURL30());
						qui.setWeiboAvatarURL50(weiboUserInfoBean.getAvatar().getAvatarURL50());
						qui.setWeiboAvatarURL100(weiboUserInfoBean.getAvatar().getAvatarURL100());
						qui.setWeiboBirthday(weiboUserInfoBean.getBirthday());

						qui.setWeiboCountryCode(weiboUserInfoBean.getCountryCode());
						qui.setWeiboProvinceCode(weiboUserInfoBean.getProvinceCode());
						qui.setWeiboCityCode(weiboUserInfoBean.getCityCode());
						qui.setWeiboLocation(weiboUserInfoBean.getLocation());

					} else {
						qui.setWeiboMsg(weiboUserInfoBean.getMsg());
						// out.println("很抱歉，我们没能正确获取到您的信息，原因是： " +
						// weiboUserInfoBean.getMsg());
					}

					u = loginConnecterDaoImpl.findUserByOpenID(openID, otype);

				}
			} catch (QQConnectException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return MavUtil.mav("/jsp/result/error",
						messageSource.getMessage("fail.login", null, "Login error!", null) + " (code:E10002)");
			}
		}

		if (u != null) { // 如果发现互联记录
			u = userDaoImpl.findByID(u.getId());
			if (u.isState()) {
				LoginTestObj lto = new LoginTestObj();
				lto.setUser(u);
				lto.setVcode(null);
				lto.setFromConnecter(true);
				lto.setW(true);
				lto.setMessageSource(messageSource);
				lto.setUserDaoImpl(userDaoImpl);
				lto.setRequest(request);
				lto.setResponse(response);
				lto.setSession(session);

				LoginTest lt = loginTest(lto);

				try {
					if (referer != null && !referer.trim().equals("")) {
						response.sendRedirect(referer);
					} else {
						response.sendRedirect(request.getContextPath());
					}
					return null;

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return MavUtil.mav(lt.getReturnPage(), lt.getMsg());
			}
		} else {
			User user=new User();
			user.setUsername(qui.getNickname());
			request.setAttribute("user", user);
			request.setAttribute("qui", qui);
			request.setAttribute("otype", otype);
			request.setAttribute("connectTitle", messageSource.getMessage("connect.title."+otype, null, "Open connect", null));
			return MavUtil.mav("/jsp/user/iccreate", "");
		}

		return MavUtil.mav("/jsp/result/error",
				messageSource.getMessage("fail.login", null, "Login error!", null) + " (code:E10003)");
	}

	/*
	 * 检测 绑定
	 */
	@ResponseBody
	@RequestMapping("/bindConnecter")
	public ConnecterValidateRetrun bindConnecter(String vcode, int otype, String openID, String target, int channel,
			HttpServletRequest request, @CookieValue(value = "referer_lerx", required = false) String referer,
			HttpServletResponse response, HttpSession session) throws Exception {
		ConnecterValidateRetrun cvr = new ConnecterValidateRetrun();
		if (openID == null || openID.trim().equals("") || target == null || target.trim().equals("") || vcode == null
				|| vcode.trim().equals("")) {
			cvr.setStatus(-11);
			return cvr;
		}
		int status = CaptchaUtil.valid(messageSource, vcode, request, response, session);
		cvr.setStatus(status);
		cvr.setOpenID(openID);
		cvr.setOtype(otype);
		session.setAttribute("sendTarget", target);
		session.setAttribute("targetMode", channel);

		if (status >= 0) { // 如果有此手机或邮箱，验证通过

			User u = (User) LoginUtil.find(baseDaoImpl.getHT(), target); // 按邮箱或手机查找用户

			if (u != null) {

				cvr = createConnecter(cvr, loginConnecterDaoImpl, u, otype, openID, referer, request, response, session,
						messageSource, userDaoImpl);

			} else { // 没有用户，新注册

			}

		}

		return cvr;

	}

	/*
	 * 解除绑定
	 */
	@ResponseBody
	@RequestMapping(value = "/connecter/ubind/{otype}")
	public int ubindConnecter(@PathVariable("otype") Integer otype, HttpServletRequest request) throws Exception {

		User user = UserUtil.findLoginer(messageSource, request, userDaoImpl);
		if (user != null) {
			LoginConnecter lc = loginConnecterDaoImpl.findByUID(user.getId());
			if (lc != null) {
				switch (otype) {
				case 1:
					if (lc.getOpenIDAtWeChat() != null && !lc.getOpenIDAtWeChat().trim().equals("")) {
						lc.setOpenIDAtWeChat(null);
						loginConnecterDaoImpl.modify(lc);
					} else {
						return -13;
					}
					break;
				case 2:
					if (lc.getOpenIDAtWeibo() != null && !lc.getOpenIDAtWeibo().trim().equals("")) {
						lc.setOpenIDAtWeibo(null);
						loginConnecterDaoImpl.modify(lc);
					} else {
						return -13;
					}
					break;
				default:
					if (lc.getOpenIDAtQQ() != null && !lc.getOpenIDAtQQ().trim().equals("")) {
						lc.setOpenIDAtQQ(null);
						loginConnecterDaoImpl.modify(lc);
					} else {
						return -13;
					}

				}
			}
		}

		return 0;

	}

	private static ConnecterValidateRetrun createConnecter(ConnecterValidateRetrun cvr,
			ILoginConnecterDao loginConnecterDaoImpl, User u, int otype, String openID, String referer,
			HttpServletRequest request, HttpServletResponse response, HttpSession session,
			ResourceBundleMessageSource messageSource, IUserDao userDaoImpl) {
		LoginConnecter lc = loginConnecterDaoImpl.findByUID(u.getId());
		boolean isnew = false;
		if (lc == null) {
			isnew = true;
			lc = new LoginConnecter();
			lc.setUser(u);
			lc.setCreateTimstamp(System.currentTimeMillis());
		}
		switch (otype) {
		case 1:
			lc.setOpenIDAtWeChat(openID);
			cvr.setOpenID(lc.getOpenIDAtWeChat());
			break;
		case 2:
			lc.setOpenIDAtWeibo(openID);
			cvr.setOpenID(lc.getOpenIDAtWeibo());
			break;
		default:
			lc.setOpenIDAtQQ(openID);
			cvr.setOpenID(lc.getOpenIDAtQQ());

		}

		if (isnew) {
			loginConnecterDaoImpl.add(lc, otype);
		} else {
			loginConnecterDaoImpl.modify(lc);
		}

		LoginTestObj lto = new LoginTestObj();
		lto.setUser(u);
		lto.setVcode(null);
		lto.setFromConnecter(true);
		lto.setW(true);
		lto.setMessageSource(messageSource);
		lto.setUserDaoImpl(userDaoImpl);
		lto.setRequest(request);
		lto.setResponse(response);
		lto.setSession(session);

		loginTest(lto);

		cvr.setUid(u.getId());
		cvr.setUsername(u.getUsername());

		if (referer != null && !referer.trim().equals("")) {
			cvr.setReferer(referer);
		} else {
			cvr.setReferer(request.getContextPath());
		}
		return cvr;
	}

	private static LoginTest loginTest(LoginTestObj lto) {

		User user = lto.getUser();
		String vcode = lto.getVcode();
		IUserDao userDaoImpl = lto.getUserDaoImpl();
		boolean fromConnecter = lto.isFromConnecter();
		boolean w = lto.isW();
		ResourceBundleMessageSource messageSource = lto.getMessageSource();
		HttpServletRequest request = lto.getRequest();
		HttpServletResponse response = lto.getResponse();
		HttpSession session = lto.getSession();

		LoginTest lt = new LoginTest();

		LoginSessionTest lst = LoginSafeRecUtil.interruptTest(
				messageSource.getMessage("login.upper.failed", null, "5", null),
				messageSource.getMessage("login.minutes.wait.afterFailed", null, "10", null), "user");

		if (lst != null && lst.isInterrup()) {
			String failMes = messageSource.getMessage("fail.login.time.upper", null,
					"You have overloaded the misplaced username and password within the time limit. Please try again later!",
					null);
			failMes = AnalyzeUtil.replace(failMes, "tag", "upper",
					messageSource.getMessage("login.upper.failed", null, " 5 ", null));
			failMes = AnalyzeUtil.replace(failMes, "tag", "minutes",
					messageSource.getMessage("login.minutes.wait.afterFailed", null, " 10 ", null));
			lt.setMsg(failMes);
			lt.setReturnPage(LOGINPAGE);
			lt.setResult(-5);
			session.setAttribute("vcode_user_lerx", "true"); // 验证码需求
			return lt;
		}

		if (!fromConnecter) { // 如果不是采用互联登录
			// 如果需要用验证码
			String vcodeRequest = (String) session.getAttribute("vcode_user_lerx");
			String safeSessionStr = SafeStrUtil.getstr(messageSource, request, "user");
			String vcodeSession = (String) session.getAttribute(safeSessionStr);

			if (vcodeRequest != null && !vcodeRequest.trim().equals("") && vcodeRequest.trim().equals("true")) {

				if (((vcode == null) || (!(vcode.trim().equalsIgnoreCase(vcodeSession))))) {

					String msg = messageSource.getMessage("error.verify.code", null, "Verification code error!", null);
					lt.setMsg(msg);
					lt.setReturnPage(LOGINPAGE);
					lt.setResult(-5);
					return lt;
				}

			}
		}

		if (user != null && user.getUsername() != null && !user.getUsername().trim().equals("")) {
			String passwordPlain;
			if (!fromConnecter) { // 如果不是采用互联登录
				passwordPlain = user.getPassword();
				user = userDaoImpl.login(user);
			} else {
				passwordPlain = user.getPassword();
			}

			if (user != null) { // 登录成功
				if (w) {
					String ip = IPUtil.getRealRemotIP(request);
					user.setLastLoginIP(ip);
					user.setLastLoginTime(System.currentTimeMillis());
					userDaoImpl.modify(user);
				}
				HttpUtil.saveCookie(messageSource, response, "username_lerx", user.getUsername());
				HttpUtil.saveCookie(messageSource, response, "password_lerx", passwordPlain);
				HttpUtil.saveCookie(messageSource, response, "uid_lerx", "" + user.getId());
				if (user.getRole() != null && user.getRole().getMask() != null
						&& user.getRole().getMask().trim().equals("0")) {
					HttpUtil.saveCookie(messageSource, response, "role0_lerx", "true");
				}
				session.removeAttribute("lsr_user"); // 清除登录次数Session
				session.removeAttribute("vcode_user_lerx"); // 清除验证码需求
				lt.setMsg("");
				lt.setResult(0);
				lt.setReturnPage("/jsp/user/login_success");
			} else {
				HttpUtil.clearCookie(request, response, "username_lerx");
				HttpUtil.clearCookie(request, response, "password_lerx");
				HttpUtil.clearCookie(request, response, "uid_lerx");
				HttpUtil.clearCookie(request, response, "rid_lerx");
				HttpUtil.clearCookie(request, response, "role0_lerx");
				HttpUtil.clearCookie(request, response, "role_name_lerx");
				lt.setMsg(messageSource.getMessage("fail.login", null, "Login failed!", null));
				lt.setReturnPage(LOGINPAGE);
				lt.setResult(-1);
			}
		} else {
			lt.setMsg(messageSource.getMessage("fail.login", null, "Login failed!", null));
			lt.setReturnPage(LOGINPAGE);
			lt.setResult(-11);

		}

		if (lt.getResult() < 0) {
			session.setAttribute("vcode_user_lerx", "true"); // 验证码需求
			session.setAttribute("lsr_user", lst.getLsrStr()); // 登录次数Session
		}
		return lt;

	}

}
