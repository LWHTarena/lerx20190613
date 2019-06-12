package com.lerx.handlers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lerx.sys.util.FileUtil;
import com.lerx.sys.util.StringUtil;


@RequestMapping("/action_support")
@Controller
public class SupportHandler {
	
	@Autowired
	private ResourceBundleMessageSource messageSource;
	
	private String newline(String content,String newlines) {
		String lineBreaker;
		if (newlines == null || newlines.trim().equals("")) {
			lineBreaker=FileUtil.readRes(messageSource, "support_lineBreaker");
		}else {
			lineBreaker=newlines;
		}
		content=StringUtil.strReplace(content, "\n", lineBreaker);
		content=StringUtil.strReplace(content, lineBreaker + lineBreaker, lineBreaker);
		return content;
	}
	
	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/readme", method = RequestMethod.POST)
	@ResponseBody
	public String readme(@RequestParam(value = "newlines", required = false) String newlines)  {
		String content;
		content = FileUtil.readRes(messageSource, "support_readme");
		
		return newline(content,newlines);
		
	}
	
	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/templet", method = RequestMethod.POST)
	@ResponseBody
	public String templet(@RequestParam(value = "newlines", required = false) String newlines)  {
		String content;
		content = FileUtil.readRes(messageSource, "support_templet");
		return newline(content,newlines);
		
	}
	
	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value = "/vip", method = RequestMethod.POST)
	@ResponseBody
	public String vip(@RequestParam(value = "newlines", required = false) String newlines)  {
		String content;
		content = FileUtil.readRes(messageSource, "support_vip");
		return newline(content,newlines);
		
	}

}
