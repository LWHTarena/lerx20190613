package com.lerx.handlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lerx.analyze.util.AnalyzeUtil;
import com.lerx.dao.iface.ITempletPortalMainDao;
import com.lerx.entities.Article;
import com.lerx.entities.TempletPortalMain;
import com.lerx.sys.obj.EnvParms;
import com.lerx.sys.util.HttpUtil;
import com.lerx.v5.util.ArticleUtil;
import com.lerx.v5.util.GroupUtil;
import com.lerx.v5.util.TempletPortalUtil;

@RequestMapping("/show_general")
@Controller
public class HttpShowGeneralHandler {
	
	@Autowired
	private ResourceBundleMessageSource messageSource;
	
	@Autowired
	private ITempletPortalMainDao templetPortalMainDaoImpl;
	
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
		TempletPortalMain template = templetPortalMainDaoImpl.findDef();
		String html = TempletPortalUtil.sundriesTag(messageSource, template, "htmlTemplet_poll");
		String imgHtmlTemplet = TempletPortalUtil.sundriesTag(messageSource, template, "htmlTemplet_img");


		html = AnalyzeUtil.replace(html, "tag", "contextPath", request.getContextPath());

		return html;
	}

}
