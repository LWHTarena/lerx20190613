package com.lerx.handlers;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lerx.annotation.Token;
import com.lerx.dao.iface.IArticleDao;
import com.lerx.dao.iface.ICommentThreadDao;
import com.lerx.dao.iface.IPortalDao;
import com.lerx.dao.iface.IUserDao;
import com.lerx.dao.iface.IVisitArchivesDao;
import com.lerx.dao.iface.IVisitorCountInPeriodDao;
import com.lerx.dao.iface.IVisitorIPRecordDao;
import com.lerx.dao.iface.IVisitorsBookDao;
import com.lerx.entities.Article;
import com.lerx.entities.CommentThread;
import com.lerx.entities.Portal;
import com.lerx.entities.VisitArchives;
import com.lerx.entities.VisitorCountInPeriod;
import com.lerx.entities.VisitorIPRecord;
import com.lerx.entities.VisitorsBook;
import com.lerx.hql.entities.Rs;
import com.lerx.ip.qqwry.entities.IPLocation;
import com.lerx.ip.util.IPUtil;
import com.lerx.login.util.LoginUtil;
import com.lerx.login.val.LoginSignUnitValues;
import com.lerx.portal.obj.CommentThreadHref;
import com.lerx.portal.obj.EnvServlet;
import com.lerx.portal.obj.PortalStatInfo;
import com.lerx.portal.obj.VisitorPeriod;
import com.lerx.sys.obj.DateEl;
import com.lerx.sys.util.ConfigWriter;
import com.lerx.sys.util.FileUtil;
import com.lerx.sys.util.HttpUtil;
import com.lerx.sys.util.MavUtil;
import com.lerx.sys.util.SecurityUtil;
import com.lerx.sys.util.StringUtil;
import com.lerx.sys.util.SysUtil;
import com.lerx.sys.util.TimeUtil;
import com.lerx.v5.util.ArticleUtil;
import com.lerx.v5.util.VisitUtil;

@RequestMapping("/action_portal")
@Controller
public class PortalHandler {

	/*
	 * private static final String SUCCESS = "jsp/result/success"; private static
	 * final String FAILED = "jsp/result/failed";
	 */

	private static final String ADMINFORBID = "_admin.forbid_";

	@Autowired
	private IPortalDao portalDaoImpl;

	@Autowired
	private IArticleDao articleDaoImpl;

	@Autowired
	private IUserDao userDaoImpl;
	
	@Autowired
	private ICommentThreadDao commentThreadDaoImpl;
	
	@Autowired
	private IVisitArchivesDao visitArchivesDaoImpl;

	@Autowired
	private IVisitorCountInPeriodDao visitorCountInPeriodDaoImpl;
	
	@Autowired
	private IVisitorIPRecordDao visitorIPRecordDaoImpl;
	
	@Autowired
	private IVisitorsBookDao visitorsBookDaoImpl;

	@Autowired
	private ResourceBundleMessageSource messageSource;

	@ModelAttribute
	public void getPortal(@RequestParam(value = "id", required = false) Long id, Map<String, Object> map) {
		if (id != null && id > 0) {
			map.put("portal", portalDaoImpl.findByID(id));
		}
	}

	// 设置
	@RequestMapping("/set")
	@Token(ajax = false, log = true, mark = "portal--<update>", admin = true, token = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public ModelAndView modify(@Valid Portal portal, Errors result, Map<String, Object> map, HttpServletRequest request,
			HttpSession session) {
		portalDaoImpl.modify(portal);

		return MavUtil.mav("jsp/portal/inf",
				messageSource.getMessage("success.modify", null, "The modification is successful!", null));
	}

	@RequestMapping("/query")
	@Token(ajax = false, mark = "portal--<query>", admin = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public ModelAndView modifyAndQuery(Map<String, Object> map) {
		Portal portal = portalDaoImpl.query_update();
		map.put("portal", portal);
		map.put("vbook", portal.getVbook());
		return MavUtil.mav("jsp/portal/inf", "");
	}

	@RequestMapping("/regtest")
	@ResponseBody
	public int regtest(HttpServletRequest request) {
		Portal portal = portalDaoImpl.query_update();

		if (portal.isUserRegAllow()) {
			/*
			 * File configFile=SysUtil.getConfigFile(messageSource, "app");
			 * 
			 */
			// String ipScope = ConfigReader.getKey(configFile,"reg.ip.allow");
			String ipScope = portal.getIpRegScope();
			ipScope = StringUtil.strReplace(ipScope, "|", ",");
			String curIP = IPUtil.getRealRemotIP(request);
			if (!IPUtil.isInRange(curIP, ipScope)) {
				return -2;
			} else {
				return portal.getCodeSendMode();
			}

		} else {
			return -5;
		}

	}

	@RequestMapping("/queryName")
	@ResponseBody
	public String queryName() {
		Portal portal = portalDaoImpl.query_update();
		if (portal.getName() != null && !portal.getName().trim().equals("")) {
			return portal.getName().trim();
		} else if (portal.getFullName() != null && !portal.getFullName().trim().equals("")) {
			return portal.getFullName().trim();

		} else {
			return "Lerx CMS";
		}
	}
	
	@ResponseBody
	@RequestMapping("/reload")
	public int reload() {
		String configFile = FileUtil.classesPath() + "i18n_tmp_zh_CN.properties";
		File f = new File(configFile);
		try {
			ConfigWriter.writeKey(f, "currtmp", "" + System.currentTimeMillis());
		} catch (ConfigurationException e) {
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			e.printStackTrace();
			return -2;
		}
		return 0;
	}

	@RequestMapping("/env")
	@Token(ajax = false, admin = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public ModelAndView env(HttpServletRequest request, Map<String, Object> map) {
		EnvServlet es = new EnvServlet();
		es.setHashtable();
		map.put("es", es);
		map.put("request", request);
		map.put("currPath", FileUtil.appPath());
		
		//cpu
		map.put("processors", Runtime.getRuntime().availableProcessors());
		
		/*String os = System.getProperty("os.name");  
		if (os.toLowerCase().startsWith("win")){  
			map.put("cpuRatio", SysUtil.getCpuRatioWin()); 
		}else if (os.toLowerCase().startsWith("linu")){
			map.put("cpuRatio", SysUtil.getCpuRatioLinux());
		}*/
		

		ZoneId zone = ZoneId.systemDefault();
		map.put("zone", zone);

		String securityStr = SecurityUtil.readWords(messageSource);
		String salt = StringUtil.randomString(6);
		String md5target = StringUtil.md5(StringUtil.md5(securityStr).toLowerCase().concat(salt)).toLowerCase();

		map.put("salt", salt);
		map.put("md5target", md5target);
		map.put("host", HttpUtil.getSrvUrl(request));

		if (messageSource.getMessage("admin.monitor.login", null, "false", null).equalsIgnoreCase("true")) {
			map.put("adminLogin", true);
		} else {
			map.put("adminLogin", false);
		}

		monitorVisit(map,request);
		analyseMemory(map);
		analyseDisk(es, map);
		runtime(map);

		return MavUtil.mav("jsp/portal/env", "");
	}

	@RequestMapping(value = "/runn/{md5target}/{salt}", method = RequestMethod.GET)
	public ModelAndView running(@PathVariable("md5target") String md5target, @PathVariable("salt") String salt,
			HttpServletRequest request, HttpSession session, Map<String, Object> map) {

		String securityStr = SecurityUtil.readWords(messageSource);
		String md5targetTmp = StringUtil.md5(StringUtil.md5(securityStr).toLowerCase().concat(salt)).toLowerCase();
		if (!md5targetTmp.equalsIgnoreCase(md5target)) {
			return MavUtil.mav("jsp/result/failed", "");
		}
		if (messageSource.getMessage("admin.monitor.login", null, "false", null).equalsIgnoreCase("true")) {
			LoginSignUnitValues lsuv = LoginUtil.read(session);
			lsuv = new LoginSignUnitValues();
			lsuv.setAdminID("0");
			LoginUtil.save(session, lsuv);
			map.put("adminLogin", true);
		} else {
			map.put("adminLogin", false);
		}

		Portal portal = portalDaoImpl.query_update();

		EnvServlet es = new EnvServlet();
		es.setHashtable();
		map.put("es", es);
		map.put("request", request);
		map.put("currPath", FileUtil.appPath());

		// cpu
		map.put("processors", Runtime.getRuntime().availableProcessors());
		/*String os = System.getProperty("os.name");
		
		if (os.toLowerCase().startsWith("win")){  
			map.put("cpuRatio", SysUtil.getCpuRatioWin()); 
		}else if (os.toLowerCase().startsWith("linu")){
			map.put("cpuRatio", SysUtil.getCpuRatioLinux());
		}*/

		
		runtime(map);
		monitorVisit(map,request);
		analyseMemory(map);
		analyseDisk(es, map);
		visit(map);
		hot(map);

		// 最新发表文章
		Rs rs = articleDaoImpl.queryByGid(0, 0, 0, 20, false, 0, 0);

		map.put("newarts", rs.getList());

		map.put("portal", portal);

		return MavUtil.mav("jsp/portal/runn", "");
	}
	
	//被访页面
	private void visit(Map<String, Object> map) {
		Portal portal = portalDaoImpl.query_update();
		VisitorsBook vbook =visitorsBookDaoImpl.findByID(portal.getVbook().getId());
		Rs rs =visitorIPRecordDaoImpl.find(vbook.getId(), 0, 10);
		@SuppressWarnings("unchecked")
		List<VisitorIPRecord> list=(List<VisitorIPRecord>) rs.getList();
		List<VisitorIPRecord> listnew = new ArrayList<VisitorIPRecord>();
		for (VisitorIPRecord vipr:list) {
			IPLocation ipl	=	IPUtil.queryQQWry(vipr.getIp());
			String ipfrom	=	ipl.getCountry() + " | " + ipl.getArea();
			vipr.setIpfrom(ipfrom);
			listnew.add(vipr);
		}
		rs.setList(listnew);
		map.put("visitRs", rs);
	}
	
	//最热文章
	private void hot(Map<String, Object> map) {
		
		Rs rs= articleDaoImpl.hotByGid(0, 0, 1, 10);

		map.put("hotRs", rs.getList());

	}
	
	//运行时长
	private void runtime(Map<String, Object> map) {
		long runtime = SysUtil.srvRuntime();
		DateEl de;
		if (runtime > 0L) {
			de = TimeUtil.diff(runtime, System.currentTimeMillis());
		} else {
			de = new DateEl();
			de.setDay(-1);
			de.setHour(-1);
			de.setMinute(-1);
			de.setSecond(-1);
		}

		map.put("runtime", runtime);
		map.put("de", de);
	}

	//内存分析
	private void analyseMemory(Map<String, Object> map) {
		float fFreeMemory = (float) Runtime.getRuntime().freeMemory();
		float fTotalMemory = (float) Runtime.getRuntime().totalMemory();
		float fPercent = fFreeMemory / fTotalMemory * 100;
		map.put("totalMemory", (Float.valueOf(fTotalMemory / 1024 / 1024)).intValue());
		map.put("freeMemory", (Float.valueOf(fFreeMemory / 1024 / 1024)).intValue());
		map.put("percentMemory", (Float.valueOf(fPercent)).intValue());
	}

	//磁盘分析
	private void analyseDisk(EnvServlet es, Map<String, Object> map) {

		String appPath = FileUtil.appPath();
		int p = appPath.indexOf(":");
		String root;
		if (p > 0) {
			String[] sArray = appPath.split(":");
			root = sArray[0];
			root = StringUtil.strReplace(root, "/", "");
			root += ":";

		} else {
			root = es.queryHashtable("user.dir");
		}

		File diskPartition = new File(root);

		long totalSpace = diskPartition.getTotalSpace();

		long freePartitionSpace = diskPartition.getFreeSpace();
		long usablePatitionSpace = diskPartition.getUsableSpace();

		int dPercent = Integer.valueOf(String.valueOf((freePartitionSpace * 100) / totalSpace));

		if (freePartitionSpace > 1024 * 1024 * 1024) {
			map.put("capacityUnit", "GB");

			map.put("spaceTotal", Integer.valueOf(String.valueOf(totalSpace / (1024 * 1024 * 1024))));
			map.put("spaceFree", Integer.valueOf(String.valueOf(freePartitionSpace / (1024 * 1024 * 1024))));
			map.put("spaceUsable", Integer.valueOf(String.valueOf(usablePatitionSpace / (1024 * 1024 * 1024))));

		} else {
			map.put("capacityUnit", "MB");

			map.put("spaceTotal", Integer.valueOf(String.valueOf(totalSpace / (1024 * 1024))));
			map.put("spaceFree", Integer.valueOf(String.valueOf(freePartitionSpace / (1024 * 1024))));
			map.put("spaceUsable", Integer.valueOf(String.valueOf(usablePatitionSpace / (1024 * 1024))));
		}

		map.put("spacePercent", dPercent);
		map.put("userDir", root);
	}
	
	
	//流量监控
	private void monitorVisit(Map<String, Object> map,HttpServletRequest request) {

		Portal portal = portalDaoImpl.query_update();

		// 总访问统计
		PortalStatInfo psi = new PortalStatInfo();
		psi = articleDaoImpl.stat(psi);
		psi = userDaoImpl.stat(psi);
		psi.setViews(portal.getVbook().getViewsTotal());
		psi.setIps(portal.getVbook().getIpTotal());
		map.put("psi", psi);

		Calendar cal = Calendar.getInstance();

		int hourNow = cal.get(Calendar.HOUR_OF_DAY);

		// 今日
		VisitArchives vaToday = VisitUtil.visitQuery(portal.getVbook().getId(), 0, visitArchivesDaoImpl);
		// 昨日
		VisitArchives vaYesterday = VisitUtil.visitQuery(portal.getVbook().getId(), -1, visitArchivesDaoImpl);
		map.put("vaToday", vaToday);
		map.put("vaYesterday", vaYesterday);

		// 时间访问记录
		List<VisitorPeriod> list24HoursToday = new ArrayList<VisitorPeriod>();
		List<VisitorPeriod> list24HoursYesterday = new ArrayList<VisitorPeriod>();
		List<VisitorPeriod> list24HoursAll = new ArrayList<VisitorPeriod>();
		String vs24HourToday = "", ip24HourToday = "";
		String vs24HourYesterday = "", ip24HourYesterday = "";
		String vs24HourAvg = "", ip24HourAvg = "";
		int totalDay;
		long todalDayLong = 0L;
		for (int i = 0; i < 24; i++) {
			VisitorPeriod vp = new VisitorPeriod();
			vp.setHour(i);

			VisitorCountInPeriod vcip = visitorCountInPeriodDaoImpl.findByPeriod(vaToday.getId(), i);
			if (vcip == null) {
				vp.setCountIps(0);
				vp.setCountViews(0);
			} else {
				vp.setCountIps(vcip.getTotalIP());
				vp.setCountViews(vcip.getTotalView());
			}
			if (i == 0) {
				if (i <= hourNow) {
					vs24HourToday += String.valueOf(vp.getCountViews());
					ip24HourToday += String.valueOf(vp.getCountIps());
				}

			} else {
				if (i <= hourNow) {
					vs24HourToday += "," + String.valueOf(vp.getCountViews());
					ip24HourToday += "," + String.valueOf(vp.getCountIps());
				} else {
					vs24HourToday += ",";
					ip24HourToday += ",";
				}

			}

			list24HoursToday.add(vp);

			vcip = visitorCountInPeriodDaoImpl.findByPeriod(vaYesterday.getId(), i);
			if (vcip == null) {
				vp.setCountIps(0);
				vp.setCountViews(0);
			} else {
				vp.setCountIps(vcip.getTotalIP());
				vp.setCountViews(vcip.getTotalView());
			}

			if (i == 0) {
				vs24HourYesterday += String.valueOf(vp.getCountViews());
				ip24HourYesterday += String.valueOf(vp.getCountIps());
			} else {
				vs24HourYesterday += "," + String.valueOf(vp.getCountViews());
				ip24HourYesterday += "," + String.valueOf(vp.getCountIps());
			}
			list24HoursYesterday.add(vp);

			// 统计和计算平均值
			long tmp = visitorCountInPeriodDaoImpl.count(portal.getVbook().getId(), i);
			if (tmp > todalDayLong) {
				todalDayLong = tmp;
			}

			long sumNum = visitorCountInPeriodDaoImpl.sum(portal.getVbook().getId(), i, 1);
			vp.setCountIps(sumNum);
			sumNum = visitorCountInPeriodDaoImpl.sum(portal.getVbook().getId(), i, 0);
			vp.setCountViews(sumNum);
			list24HoursAll.add(vp);

		}

		// 取最大值为总天数
		totalDay = Integer.valueOf(String.valueOf(todalDayLong));
		if (totalDay==0) {
			totalDay=1;
		}

		for (int i = 0; i < 24; i++) {
			int tmp2 = (int) (list24HoursAll.get(i).getCountViews() / totalDay);
			int tmp3 = (int) (list24HoursAll.get(i).getCountIps() / totalDay);
			if (i == 0) {

				vs24HourAvg += String.valueOf(tmp2);
				ip24HourAvg += String.valueOf(tmp3);
			} else {
				vs24HourAvg += "," + String.valueOf(tmp2);
				ip24HourAvg += "," + String.valueOf(tmp3);
			}

		}

		// 下面输出两个数组，但没有使用
		map.put("list24HoursToday", list24HoursToday);
		map.put("list24HoursYesterday", list24HoursYesterday);

		// 输出
		map.put("vs24HourToday", vs24HourToday);
		map.put("ip24HourToday", ip24HourToday);

		map.put("vs24HourYesterday", vs24HourYesterday);
		map.put("ip24HourYesterday", ip24HourYesterday);

		map.put("vs24HourAvg", vs24HourAvg);
		map.put("ip24HourAvg", ip24HourAvg);
		
		Long comcount=commentThreadDaoImpl.countByBid(0);
		map.put("totalComments", comcount);
		
		Rs rs=commentThreadDaoImpl.queryByBid(0, 0, 20, false, 1, false);
		@SuppressWarnings("unchecked")
		List<CommentThread> listct=(List<CommentThread>) rs.getList();
		List<CommentThreadHref> listcth = new ArrayList<CommentThreadHref>();
		for (CommentThread ct:listct) {
			CommentThreadHref cth=new CommentThreadHref();
			cth.setBid(ct.getCb().getId());
			cth.setCid(ct.getId());
			cth.setOid(ct.getCb().getObjID());
			cth.setSubject(ct.getCb().getObjTitle());
			cth.setContent(ct.getContent());
			String href = null;
			
			if (ct.getCb().getObjType()==0 && ct.getCb().getObjID()>0) {
				Article article=articleDaoImpl.findByID(ct.getCb().getObjID());
				href=ArticleUtil.getHref(messageSource, request, article);
			}
			cth.setHref(href);
			cth.setAgrees(ct.getPoll().getAgrees());
			cth.setAntis(ct.getPoll().getAntis());
			cth.setPassbys(ct.getPoll().getPassbys());
			cth.setOccurDatetime(ct.getOccurDatetime());
			if (ct.getIp()!=null) {
				IPLocation ipl	=	IPUtil.queryQQWry(ct.getIp());
				String ipfrom	=	ipl.getCountry() + " | " + ipl.getArea();
				cth.setIpfrom(ipfrom);
			}else {
				cth.setIpfrom("");
			}
			
			cth.setIp(ct.getIp());
			
			listcth.add(cth);
		}
		
		map.put("listComments", listcth);
		
	}

}
