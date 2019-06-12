package com.lerx.v5.util;

import com.lerx.analyze.util.AnalyzeUtil;
import com.lerx.entities.Portal;

public class PortalUtil {
	
	public static String fmt(String lf,Portal portal) {
		
		lf = AnalyzeUtil.replace(lf, "tag", "portalName", portal.getName());
		lf = AnalyzeUtil.replace(lf, "tag", "portalFullName", portal.getFullName());
		lf = AnalyzeUtil.replace(lf, "tag", "portalKeyWords", portal.getKeyWords());
		lf = AnalyzeUtil.replace(lf, "tag", "portalDescription", portal.getDescription());
		lf = AnalyzeUtil.replace(lf, "tag", "portalHost", portal.getHost());
		lf = AnalyzeUtil.replace(lf, "tag", "portalUrl", portal.getUrl());
		lf = AnalyzeUtil.replace(lf, "tag", "portalWelcomeStr", portal.getWelcomeStr());
		lf = AnalyzeUtil.replace(lf, "tag", "portalViews", ""+portal.getVbook().getViewsTotal());
		lf = AnalyzeUtil.replace(lf, "tag", "portalIps", ""+portal.getVbook().getIpTotal());
		
		return lf;
	}
			

}
