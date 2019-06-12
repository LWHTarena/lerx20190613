package com.lerx.handlers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lerx.login.util.LoginUtil;


@Controller
public class PathHandler {
	
	@Autowired
	private ResourceBundleMessageSource messageSource;
	
	@RequestMapping(value="/result/{name}")
	public ModelAndView resultPath (@PathVariable String name,HttpSession session,HttpServletRequest request, Map<String, Object> map){
		long uid=LoginUtil.userChk(messageSource, session) ;
		map.put("uid", uid);
		String url=request.getScheme()+"://";   
		   url+=request.getHeader("host");
		   map.put("currReq", url);
		return new ModelAndView("result/"+name);
	}
	
	@RequestMapping(value="/jsp/{name}/{act}")
	public ModelAndView jspPath (@PathVariable String name,@PathVariable String act,HttpSession session,HttpServletRequest request, Map<String, Object> map){
		long uid=LoginUtil.userChk(messageSource, session) ;
		map.put("uid", uid);
		String url=request.getScheme()+"://";   
		   url+=request.getHeader("host");
		   map.put("currReq", url);
		return new ModelAndView("jsp/"+name+"/"+act);
	}
	
	@RequestMapping(value="/jsp/{name}/{sub}/{act}")
	public ModelAndView jspSubPath (@PathVariable String name,@PathVariable String sub,@PathVariable String act,HttpSession session,HttpServletRequest request, Map<String, Object> map){
		long uid=LoginUtil.userChk(messageSource, session) ;
		map.put("uid", uid);
		String url=request.getScheme()+"://";   
		   url+=request.getHeader("host");
		   map.put("currReq", url);
		return new ModelAndView("jsp/"+name+"/"+sub+"/"+act);
	}

}
