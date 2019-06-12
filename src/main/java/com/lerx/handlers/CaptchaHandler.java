package com.lerx.handlers;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lerx.app.util.SafeStrUtil;
import com.lerx.dao.iface.IPortalDao;
import com.lerx.dao.iface.IUserDao;
import com.lerx.entities.Portal;
import com.lerx.sys.util.RandomNumUtil;
import com.lerx.sys.util.StringUtil;
import com.lerx.v5.util.CaptchaUtil;
import com.lerx.v5.util.ConfigUtil;

/*
 * 验证码
 */
@RequestMapping("/action_captcha")
@Controller
public class CaptchaHandler {
	
	@Autowired
	private ResourceBundleMessageSource messageSource;

	@Autowired
	private IPortalDao portalDaoImpl;
	
	@Autowired
	private IUserDao userDaoImpl;
	
	
	@RequestMapping("/randomNum")
	public void randomNum(HttpServletRequest request,HttpServletResponse response,HttpSession session,String key) throws Exception{
		String captchaMode = messageSource.getMessage("captcha.mode", null, "blend", null);
		int imgMode;
		if (captchaMode.trim().equalsIgnoreCase("words")){	//
			imgMode=2;
		}else if (captchaMode.trim().equalsIgnoreCase("number")){
			imgMode=1;
		}else{
			imgMode=0;
		}
		
		RandomNumUtil randomNum = new RandomNumUtil(response,60,18,4,18,imgMode);
		String  safeSessionStr=SafeStrUtil.getstr(messageSource, request,key);
		session.setAttribute(safeSessionStr, randomNum.getRandomCode());
		
	}
	
	
	@RequestMapping("/send")
	@ResponseBody
	public int send(String target,@RequestParam(value = "mode", required = false) Integer mode,@RequestParam(value = "lookup", required = false) Integer lookup,HttpServletRequest request,HttpServletResponse response,HttpSession session) throws Exception{
		
		/*
		 * mode & sendM
		 * mode 0:email
		 * mode 1:phone
		 */
		int sendM = -1;
		Portal portal=portalDaoImpl.query_update();
		if (mode!=null) {
			sendM=mode;
		}
		if (sendM == -1) {
			
			if (portal.getCodeSendMode()==0) {	//如果验证码发向邮箱
				sendM=0;
				if (target == null || !StringUtil.emailTest(target)) {
					return -11;
				}
				if (!ConfigUtil.mailChk(messageSource, target)) {
					return -5;
				}
			}else {//如果发向手机
				sendM=1;
				if (!StringUtil.phoneTest(target.trim())) {
					return -11;
				}
			}
		}
		
		if (lookup==null) {
			lookup=0;
		}
		
		if (lookup > 0) {
			if (sendM==1) {	//查找手机
				if (userDaoImpl.findByMobile(target, 0) != null) {
					return -10;
				}
			}else {			//查找邮箱
				if (userDaoImpl.findByEmail(target, 0) != null) {
					return -10;
				}
			}
		}
		
		int r=CaptchaUtil.send(messageSource, request, session, portal.getName(), sendM, target);
		
		if (r>=0) {
			session.setAttribute("sendTarget", target);
		}
		return r;
	}
	
	
	@ResponseBody
	@RequestMapping("/validate")
	public int validate(String vcode,HttpServletRequest request,HttpServletResponse response,HttpSession session) throws Exception{
		
		return CaptchaUtil.valid(messageSource,vcode,request,response,session);
		
		
	}
	
	
	
	
	/*public static void main(String[] args) throws ParseException, Exception {
		
		System.out.println(phoneTest("15015158766"));
	}*/
	
	
		
		
}
