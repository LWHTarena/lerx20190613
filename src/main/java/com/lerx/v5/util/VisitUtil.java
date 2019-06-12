package com.lerx.v5.util;

import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.lerx.dao.iface.IVisitArchivesDao;
import com.lerx.entities.VisitArchives;
import com.lerx.entities.VisitorCountInPeriod;
import com.lerx.entities.VisitorIPRecord;
import com.lerx.entities.VisitorsBook;
import com.lerx.ip.util.IPUtil;
import com.lerx.portal.obj.EnvirSet;
import com.lerx.sys.util.HttpUtil;
import com.lerx.sys.util.StringUtil;
import com.lerx.sys.util.TimeUtil;

public class VisitUtil {

	/*
	 * 智能更新访问统计
	 * mask为三位数字符串 以1和0区分
	 * 百位数：总量
	 * 十位：下级总量
	 * 个位：自身
	 */
	public static boolean visitorRefresh(EnvirSet is,VisitorsBook vbook,String mask,String url,String referer) {
		boolean newip=false;
		HttpServletRequest request=is.getRequest();
		if (request==null) {
			request=HttpUtil.currRequest();
		}
		String ip=IPUtil.getRealRemotIP(request);
		String spanHours=is.getMessageSource().getMessage("ip.record.nosame.span.hours", null, "6", null);
		long datetimePoint,currWholePointTime;
		if (StringUtil.isNumber(spanHours)) {
			datetimePoint=System.currentTimeMillis() - Long.valueOf(spanHours)*1000*60*60 ;
		}else {
			datetimePoint=System.currentTimeMillis() - 6*1000*60*60 ;
		}
		
		Calendar cal = Calendar.getInstance();
		
		
		int hourNow = cal.get(Calendar.HOUR_OF_DAY);
		cal.set(Calendar.SECOND, 0);  
		cal.set(Calendar.MINUTE, 0);  
		cal.set(Calendar.MILLISECOND, 0); 
		currWholePointTime = cal.getTimeInMillis();
		
		List<VisitorIPRecord> viprList=is.getVisitorIPRecordDaoImpl().findSameIPAfterDatetime(vbook.getId(), datetimePoint, ip);
		VisitorIPRecord vipr=null;
		boolean ipRecordsExists=false;
		if (viprList!=null && viprList.size()>0) {
			ipRecordsExists=true;
			
		}
		
		//从当前整点时间开始查找ip
		List<VisitorIPRecord> viprList2=is.getVisitorIPRecordDaoImpl().findSameIPAfterDatetime(vbook.getId(), currWholePointTime, ip);
		
		
		//每日记录器
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMdd");
		
		String dayStr=formatter.format(System.currentTimeMillis());
		int dayKey=Integer.valueOf(dayStr);
		VisitArchives va=is.getVisitArchivesDaoImpl().findByDayKey(vbook.getId(), dayKey);
		if (va==null) {
			va = new VisitArchives();
			va .setDayKey(dayKey);
			va.setVbook(vbook);
			va = is.getVisitArchivesDaoImpl().add(va);
		}
		
		
		
		
		VisitorCountInPeriod vcip=is.getVisitorCountInPeriodDaoImpl().findByPeriod(va.getId(), hourNow);
		if (vcip==null){
			vcip = new VisitorCountInPeriod();
			vcip.setHour(hourNow);
			vcip.setVa(va);
			vcip=is.getVisitorCountInPeriodDaoImpl().add(vcip);
		}
		
		va.setViews(va.getViews()+1);
		vcip.setTotalView(vcip.getTotalView()+1);
		//
		boolean total=false;
		boolean sub=false;
		boolean own=false;
		String flag;
		if (mask!=null && !mask.trim().equals("")) {
			flag=mask.substring(mask.length()-1, mask.length());
//			System.out.println("oox："+flag);
			if (Integer.valueOf(flag) ==1) {
				own=true;
			}
			
			flag=mask.substring(mask.length()-2, mask.length()-1);
//			System.out.println("oxo："+flag);
			if (Integer.valueOf(flag) ==1) {
				sub=true;
			}
			
			flag=mask.substring(mask.length()-3, mask.length()-2);
//			System.out.println("xoo："+flag);
			if (Integer.valueOf(flag) ==1) {
				total=true;
			}
			
		}
		
		//如果从当前整点时间开始未有ip记录
		if (viprList2==null || viprList2.isEmpty() || viprList2.size()<=0){
			vcip.setTotalIP(vcip.getTotalIP()+1);
		}
		
		
		
		//如果从规定的前面时间段，如6小时内没有ip记录
		if (!ipRecordsExists) {
			newip=true;
			va.setIps(va.getIps()+1);
			vipr = new VisitorIPRecord();
			vipr.setTotalIn6Hours(1);
			vipr.setIp(ip);
			vipr.setVisitDatetime(System.currentTimeMillis());
			vipr.setVbook(vbook);
			
			vipr= detect(vipr,referer,url,request);
			
			
			is.getVisitorIPRecordDaoImpl().add(vipr);
			if (total) {
				vbook.setIpTotal(vbook.getIpTotal()+1);
			}
			if (sub) {
				vbook.setIpSub(vbook.getIpSub()+1);
			}
			if (own) {
				vbook.setIpOwn(vbook.getIpOwn()+1);
			}
			
			
		}else {
			vipr=viprList.get(0);
			vipr.setTotalIn6Hours(vipr.getTotalIn6Hours()+1);
			vipr= detect(vipr,referer,url,request);
			
			is.getVisitorIPRecordDaoImpl().modify(vipr);
		}
		
		if (total) {
			vbook.setViewsTotal(vbook.getViewsTotal()+1);
		}
		if (sub) {
			vbook.setViewsSub(vbook.getViewsSub()+1);
		}
		if (own) {
			vbook.setViewsOwn(vbook.getViewsOwn()+1);
		}
		is.getVisitArchivesDaoImpl().modify(va);
		is.getVisitorsBookDaoImpl().modify(vbook);
		is.getVisitorCountInPeriodDaoImpl().modify(vcip);
		return newip;
	}
	
	
	public static VisitArchives visitQuery(long vid,int dayKey,IVisitArchivesDao visitArchivesDaoImpl) {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMdd");
		String currDayStr=formatter.format(System.currentTimeMillis());
		int currDayKey=Integer.valueOf(currDayStr);
		if (dayKey==0) {			//如果=0，则于当前
			dayKey=currDayKey;
		}else if (dayKey<0){		//如果<0则，计算
			long datetime=TimeUtil.cal(System.currentTimeMillis(),3,dayKey);
			
			dayKey=Integer.valueOf(formatter.format(datetime));
		}
		VisitArchives va = visitArchivesDaoImpl.findByDayKey(vid, dayKey);
		if (va==null) {
			va=new VisitArchives();
		}
		return va;
	}
	
	private static VisitorIPRecord detect (VisitorIPRecord vipr,String referer,String url,HttpServletRequest request) {
		String details=vipr.getDetails();
		if (details==null) {
			details="";
		}
		if (url==null || url.trim().equals("")) {
			String queryString=request.getQueryString();
			if (queryString!=null && !queryString.trim().equals("")) {
				queryString="?"+queryString;
			}else {
				queryString="";
			}
			vipr.setVisitUrl(request.getRequestURL()+queryString);
		}else {
			vipr.setVisitUrl(url);
		}
		String currTime=TimeUtil.coverLongToStr(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
		if (details.trim().equals("")) {
			details += currTime + " -- " + vipr.getVisitUrl();
		}else {
			details += "\n" + currTime + " -- " + vipr.getVisitUrl();
		}
		
		vipr.setDetails(details);
		
		if (referer==null || referer.trim().equals("")) {
			referer = request.getHeader("Referer");
		}
		
		boolean sametag=false;
		if (vipr.getVisitUrl()!=null && referer!=null && referer.trim().equals(vipr.getVisitUrl())) {
			sametag=true;
		}
		if (!sametag) {
			if (vipr.getReffer()==null || vipr.getReffer().trim().equals("")){
				vipr.setReffer(referer);
			}else{
				String refhost = null,curhost = null;
				java.net.URL refurl,cururl;
				if (referer!=null && !referer.trim().equals("")){
					referer = referer.trim();
					
					try {
						refurl = new java.net.URL(referer);
						refhost = refurl.getHost();// 获取主机名
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						referer=null;
						e.printStackTrace();
					}
					
				}
				
				try {
					cururl = new java.net.URL(vipr.getVisitUrl());
					curhost = cururl.getHost();// 获取主机名
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					curhost="";
					e.printStackTrace();
				}
				
				if (referer!=null && !curhost.equalsIgnoreCase(refhost)){		//如果来源host和当前host不同，才更改
					vipr.setReffer(referer);
				}
				
			}
			
		}
		
		vipr.setVisitDatetime(System.currentTimeMillis());
		
		return vipr;
	}
}
