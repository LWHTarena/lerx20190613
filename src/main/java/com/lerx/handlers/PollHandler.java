package com.lerx.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lerx.annotation.Token;
import com.lerx.dao.iface.IPollDao;
import com.lerx.dao.iface.IPollIPRecordDao;
import com.lerx.entities.Poll;
import com.lerx.entities.PollIPRecord;
import com.lerx.hql.entities.Rs;
import com.lerx.ip.qqwry.entities.IPLocation;
import com.lerx.ip.util.IPUtil;
import com.lerx.portal.obj.PollResult;
import com.lerx.sys.util.MavUtil;
import com.lerx.sys.util.StringUtil;

@RequestMapping("/action_poll")
@Controller
public class PollHandler {

	@Autowired
	private IPollDao pollDaoImpl;
	
	@Autowired
	private IPollIPRecordDao pollIPRecordDaoImpl;
	
	@Autowired
	private ResourceBundleMessageSource messageSource;
	
	private static final String ADMINFORBID = "_admin.forbid_";
	
	@CrossOrigin(origins = "*", maxAge = 3600)
	@ResponseBody
	@RequestMapping(value = "/poll/{id}/{status}")
	public PollResult modify(@PathVariable(value = "id", required = false) Long id, @PathVariable(value = "status", required = false) Integer status,HttpServletRequest request) {
		PollResult pr = new PollResult();
		if (id==null) {
			pr.setStatus(-11);
			return pr;
		}
		if (status==null) {
			status=0;
		}
		Poll poll = pollDaoImpl.findByID(id);
		if (poll ==null) {
			pr.setStatus(-12);
			return pr;
		}
		
		if (!poll.isStatus()) {
			pr.setStatus(-5);
			return pr;
		}
		
		String ip=IPUtil.getRealRemotIP(request);
		String spanHours=messageSource.getMessage("ip.record.nosame.span.hours", null, "6", null);
		
		long datetimePoint;
		if (StringUtil.isNumber(spanHours)) {
			datetimePoint=System.currentTimeMillis() - Long.valueOf(spanHours)*1000*60*60 ;
		}else {
			datetimePoint=System.currentTimeMillis() - 6*1000*60*60 ;
		}
		
		List<PollIPRecord> viprList=pollIPRecordDaoImpl.findSameIPAfterDatetime(poll.getId(), datetimePoint, ip);
		boolean ipRecordsExists=false;
		if (viprList!=null && viprList.size()>0) {
			ipRecordsExists=true;
		}
		
		String queryString;
		if (!ipRecordsExists) {
			PollIPRecord pir = new PollIPRecord();
			pir.setIp(ip);
			pir.setPollDatetime(System.currentTimeMillis());
			pir.setPoll(poll);
			pir.setResult(status);
			
//			IPLocation ipl	=	IPUtil.queryQQWry(ip);
//			String ipfrom	=	ipl.getCountry() + " | " + ipl.getArea();
//			pir.setIpfrom(ipfrom);
			pir.setReffer(request.getHeader("Referer"));
			queryString=request.getQueryString();
			if (queryString!=null && !queryString.trim().equals("")) {
				queryString="?"+queryString;
			}else {
				queryString="";
			}
			pir.setVisitUrl(request.getRequestURI()+queryString);
			pollIPRecordDaoImpl.add(pir);
			poll=pollDaoImpl.modifyPoll(poll, status);
			pr.setAgrees(poll.getAgrees());
			pr.setAntis(poll.getAntis());
			pr.setPassbys(poll.getPassbys());
			pr.setStatus(0);
			return pr;
		}else {
			pr.setAgrees(poll.getAgrees());
			pr.setAntis(poll.getAntis());
			pr.setPassbys(poll.getPassbys());
			pr.setStatus(-8);
			return pr;
		}
	}
	
	
	/*
	 * 列表
	 */
	@RequestMapping(value = "/list/{id}")
	@Token(ajax = false, admin = true, failedPage = ADMINFORBID, msgKey = "fail.permission")
	public ModelAndView list(@PathVariable(value = "id", required = false) Long id,@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize, HttpSession session,
			Map<String, Object> map) {
		if (id==null) {
			id=0L;
		}
		Poll poll =pollDaoImpl.findByID(id);
		if (page == null) {
			page = 1;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		Rs rs =pollIPRecordDaoImpl.find(poll.getId(), page, pageSize);
		@SuppressWarnings("unchecked")
		List<PollIPRecord> list=(List<PollIPRecord>) rs.getList();
		List<PollIPRecord> listnew = new ArrayList<PollIPRecord>();
		for (PollIPRecord pipr:list) {
			IPLocation ipl	=	IPUtil.queryQQWry(pipr.getIp());
			String ipfrom	=	ipl.getCountry() + " | " + ipl.getArea();
			pipr.setIpfrom(ipfrom);
			listnew.add(pipr);
		}
		rs.setList(listnew);
		map.put("pageUrl", "/action_poll/list/"+id);
		map.put("rs", rs);
		map.put("poll", poll);

		return MavUtil.mav("jsp/poll/list", "");

	}
	
}
